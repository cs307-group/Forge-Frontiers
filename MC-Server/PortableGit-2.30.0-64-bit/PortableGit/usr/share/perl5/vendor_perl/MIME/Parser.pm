package MIME::Parser;


=head1 NAME

MIME::Parser - experimental class for parsing MIME streams


=head1 SYNOPSIS

Before reading further, you should see L<MIME::Tools> to make sure that
you understand where this module fits into the grand scheme of things.
Go on, do it now.  I'll wait.

Ready?  Ok...

=head2 Basic usage examples

    ### Create a new parser object:
    my $parser = new MIME::Parser;

    ### Tell it where to put things:
    $parser->output_under("/tmp");

    ### Parse an input filehandle:
    $entity = $parser->parse(\*STDIN);

    ### Congratulations: you now have a (possibly multipart) MIME entity!
    $entity->dump_skeleton;          # for debugging


=head2 Examples of input

    ### Parse from filehandles:
    $entity = $parser->parse(\*STDIN);
    $entity = $parser->parse(IO::File->new("some command|");

    ### Parse from any object that supports getline() and read():
    $entity = $parser->parse($myHandle);

    ### Parse an in-core MIME message:
    $entity = $parser->parse_data($message);

    ### Parse an MIME message in a file:
    $entity = $parser->parse_open("/some/file.msg");

    ### Parse an MIME message out of a pipeline:
    $entity = $parser->parse_open("gunzip - < file.msg.gz |");

    ### Parse already-split input (as "deliver" would give it to you):
    $entity = $parser->parse_two("msg.head", "msg.body");


=head2 Examples of output control

    ### Keep parsed message bodies in core (default outputs to disk):
    $parser->output_to_core(1);

    ### Output each message body to a one-per-message directory:
    $parser->output_under("/tmp");

    ### Output each message body to the same directory:
    $parser->output_dir("/tmp");

    ### Change how nameless message-component files are named:
    $parser->output_prefix("msg");

    ### Put temporary files somewhere else
    $parser->tmp_dir("/var/tmp/mytmpdir");

=head2 Examples of error recovery

    ### Normal mechanism:
    eval { $entity = $parser->parse(\*STDIN) };
    if ($@) {
	$results  = $parser->results;
	$decapitated = $parser->last_head;  ### get last top-level head
    }

    ### Ultra-tolerant mechanism:
    $parser->ignore_errors(1);
    $entity = eval { $parser->parse(\*STDIN) };
    $error = ($@ || $parser->last_error);

    ### Cleanup all files created by the parse:
    eval { $entity = $parser->parse(\*STDIN) };
    ...
    $parser->filer->purge;


=head2 Examples of parser options

    ### Automatically attempt to RFC 2047-decode the MIME headers?
    $parser->decode_headers(1);             ### default is false

    ### Parse contained "message/rfc822" objects as nested MIME streams?
    $parser->extract_nested_messages(0);    ### default is true

    ### Look for uuencode in "text" messages, and extract it?
    $parser->extract_uuencode(1);           ### default is false

    ### Should we forgive normally-fatal errors?
    $parser->ignore_errors(0);              ### default is true


=head2 Miscellaneous examples

    ### Convert a Mail::Internet object to a MIME::Entity:
    my $data = join('', (@{$mail->header}, "\n", @{$mail->body}));
    $entity = $parser->parse_data(\$data);



=head1 DESCRIPTION

You can inherit from this class to create your own subclasses
that parse MIME streams into MIME::Entity objects.


=head1 PUBLIC INTERFACE

=cut

#------------------------------

require 5.004;

### Pragmas:
use strict;
use vars (qw($VERSION $CAT $CRLF));

### core Perl modules
use IO::File;
use File::Spec;
use File::Path;
use Config qw(%Config);
use Carp;

### Kit modules:
use MIME::Tools qw(:config :utils :msgtypes usage tmpopen );
use MIME::Head;
use MIME::Body;
use MIME::Entity;
use MIME::Decoder;
use MIME::Parser::Reader;
use MIME::Parser::Filer;
use MIME::Parser::Results;

#------------------------------
#
# Globals
#
#------------------------------

### The package version, both in 1.23 style *and* usable by MakeMaker:
$VERSION = "5.509";

### How to catenate:
$CAT = '/bin/cat';

### The CRLF sequence:
$CRLF = "\015\012";

### Who am I?
my $ME = 'MIME::Parser';



#------------------------------------------------------------

=head2 Construction

=over 4

=cut

#------------------------------

=item new ARGS...

I<Class method.>
Create a new parser object.
Once you do this, you can then set up various parameters
before doing the actual parsing.  For example:

    my $parser = new MIME::Parser;
    $parser->output_dir("/tmp");
    $parser->output_prefix("msg1");
    my $entity = $parser->parse(\*STDIN);

Any arguments are passed into C<init()>.
Don't override this in your subclasses; override init() instead.

=cut

sub new {
    my $self = bless {}, shift;
    $self->init(@_);
}

#------------------------------

=item init ARGS...

I<Instance method.>
Initiallize a new MIME::Parser object.
This is automatically sent to a new object; you may want to override it.
If you override this, be sure to invoke the inherited method.

=cut

sub init {
    my $self = shift;

    $self->{MP5_DecodeHeaders}   = 0;
    $self->{MP5_DecodeBodies}    = 1;
    $self->{MP5_Interface}       = {};
    $self->{MP5_ParseNested}     = 'NEST';
    $self->{MP5_TmpToCore}       = 0;
    $self->{MP5_IgnoreErrors}    = 1;
    $self->{MP5_UUDecode}        = 0;
    $self->{MP5_MaxParts}        = -1;
    $self->{MP5_TmpDir}          = undef;

    $self->interface(ENTITY_CLASS => 'MIME::Entity');
    $self->interface(HEAD_CLASS   => 'MIME::Head');

    $self->output_dir(".");

    $self;
}

#------------------------------

=item init_parse

I<Instance method.>
Invoked automatically whenever one of the top-level parse() methods
is called, to reset the parser to a "ready" state.

=cut

sub init_parse {
    my $self = shift;

    $self->{MP5_Results} = new MIME::Parser::Results;

    $self->{MP5_Filer}->results($self->{MP5_Results});
    $self->{MP5_Filer}->purgeable([]);
    $self->{MP5_Filer}->init_parse();
    $self->{MP5_NumParts} = 0;
    1;
}

=back

=cut





#------------------------------------------------------------

=head2 Altering how messages are parsed

=over 4

=cut

#------------------------------

=item decode_headers [YESNO]

I<Instance method.>
Controls whether the parser will attempt to decode all the MIME headers
(as per RFC 2047) the moment it sees them.  B<This is not advisable
for two very important reasons:>

=over

=item *

B<It screws up the extraction of information from MIME fields.>
If you fully decode the headers into bytes, you can inadvertently
transform a parseable MIME header like this:

    Content-type: text/plain; filename="=?ISO-8859-1?Q?Hi=22Ho?="

into unparseable gobbledygook; in this case:

    Content-type: text/plain; filename="Hi"Ho"

=item *

B<It is information-lossy.>  An encoded string which contains
both Latin-1 and Cyrillic characters will be turned into a binary
mishmosh which simply can't be rendered.

=back

B<History.>
This method was once the only out-of-the-box way to deal with attachments
whose filenames had non-ASCII characters.  However, since MIME-tools 5.4xx
this is no longer necessary.

B<Parameters.>
If YESNO is true, decoding is done.  However, you will get a warning
unless you use one of the special "true" values:

   "I_NEED_TO_FIX_THIS"
	  Just shut up and do it.  Not recommended.
	  Provided only for those who need to keep old scripts functioning.

   "I_KNOW_WHAT_I_AM_DOING"
	  Just shut up and do it.  Not recommended.
	  Provided for those who REALLY know what they are doing.

If YESNO is false (the default), no attempt at decoding will be done.
With no argument, just returns the current setting.
B<Remember:> you can always decode the headers I<after> the parsing
has completed (see L<MIME::Head::decode()|MIME::Head/decode>), or
decode the words on demand (see L<MIME::Words>).

=cut

sub decode_headers {
    my ($self, $yesno) = @_;
    if (@_ > 1) {
	$self->{MP5_DecodeHeaders} = $yesno;
	if ($yesno) {
	    if (($yesno eq "I_KNOW_WHAT_I_AM_DOING") ||
		($yesno eq "I_NEED_TO_FIX_THIS")) {
		### ok
	    }
	    else {
		$self->whine("as of 5.4xx, decode_headers() should NOT be ".
			     "set true... if you are doing this to make sure ".
			     "that non-ASCII filenames are translated, ".
			     "that's now done automatically; for all else, ".
			     "use MIME::Words.");
	    }
	}
    }
    $self->{MP5_DecodeHeaders};
}

#------------------------------

=item extract_nested_messages OPTION

I<Instance method.>
Some MIME messages will contain a part of type C<message/rfc822>
,C<message/partial> or C<message/external-body>:
literally, the text of an embedded mail/news/whatever message.
This option controls whether (and how) we parse that embedded message.

If the OPTION is false, we treat such a message just as if it were a
C<text/plain> document, without attempting to decode its contents.

If the OPTION is true (the default), the body of the C<message/rfc822>
or C<message/partial> part is parsed by this parser, creating an
entity object.  What happens then is determined by the actual OPTION:

=over 4

=item NEST or 1

The default setting.
The contained message becomes the sole "part" of the C<message/rfc822>
entity (as if the containing message were a special kind of
"multipart" message).
You can recover the sub-entity by invoking the L<parts()|MIME::Entity/parts>
method on the C<message/rfc822> entity.

=item REPLACE

The contained message replaces the C<message/rfc822> entity, as though
the C<message/rfc822> "container" never existed.

B<Warning:> notice that, with this option, all the header information
in the C<message/rfc822> header is lost.  This might seriously bother
you if you're dealing with a top-level message, and you've just lost
the sender's address and the subject line.  C<:-/>.

=back

I<Thanks to Andreas Koenig for suggesting this method.>

=cut

sub extract_nested_messages {
    my ($self, $option) = @_;
    $self->{MP5_ParseNested} = $option if (@_ > 1);
    $self->{MP5_ParseNested};
}

sub parse_nested_messages {
    usage "parse_nested_messages() is now extract_nested_messages()";
    shift->extract_nested_messages(@_);
}

#------------------------------

=item extract_uuencode [YESNO]

I<Instance method.>
If set true, then whenever we are confronted with a message
whose effective content-type is "text/plain" and whose encoding
is 7bit/8bit/binary, we scan the encoded body to see if it contains
uuencoded data (generally given away by a "begin XXX" line).

If it does, we explode the uuencoded message into a multipart,
where the text before the first "begin XXX" becomes the first part,
and all "begin...end" sections following become the subsequent parts.
The filename (if given) is accessible through the normal means.

=cut

sub extract_uuencode {
    my ($self, $yesno) = @_;
    $self->{MP5_UUDecode} = $yesno if @_ > 1;
    $self->{MP5_UUDecode};
}

#------------------------------

=item ignore_errors [YESNO]

I<Instance method.>
Controls whether the parser will attempt to ignore normally-fatal
errors, treating them as warnings and continuing with the parse.

If YESNO is true (the default), many syntax errors are tolerated.
If YESNO is false, fatal errors throw exceptions.
With no argument, just returns the current setting.

=cut

sub ignore_errors {
    my ($self, $yesno) = @_;
    $self->{MP5_IgnoreErrors} = $yesno if (@_ > 1);
    $self->{MP5_IgnoreErrors};
}


#------------------------------

=item decode_bodies [YESNO]

I<Instance method.>
Controls whether the parser should decode entity bodies or not.
If this is set to a false value (default is true), all entity bodies
will be kept as-is in the original content-transfer encoding.

To prevent double encoding on the output side MIME::Body->is_encoded
is set, which tells MIME::Body not to encode the data again, if encoded
data was requested. This is in particular useful, when it's important that
the content B<must not> be modified, e.g. if you want to calculate
OpenPGP signatures from it.

B<WARNING>: the semantics change significantly if you parse MIME
messages with this option set, because MIME::Entity resp. MIME::Body
*always* see encoded data now, while the default behaviour is
working with *decoded* data (and encoding it only if you request it).
You need to decode the data yourself, if you want to have it decoded.

So use this option only if you exactly know, what you're doing, and
that you're sure, that you really need it.

=cut

sub decode_bodies {
    my ($self, $yesno) = @_;
    $self->{MP5_DecodeBodies} = $yesno if (@_ > 1);
    $self->{MP5_DecodeBodies};
}

#------------------------------
#
# MESSAGES...
#

#------------------------------
#
# debug MESSAGE...
#
sub debug {
    my $self = shift;
    if (MIME::Tools->debugging()) {
	    if (my $r = $self->{MP5_Results}) {
		    unshift @_, $r->indent;
		    $r->msg($M_DEBUG, @_);
	    }
	    MIME::Tools::debug(@_);
    }
}

#------------------------------
#
# whine PROBLEM...
#
sub whine {
    my $self = shift;
    if (my $r = $self->{MP5_Results}) {
	unshift @_, $r->indent;
	$r->msg($M_WARNING, @_);
    }
    &MIME::Tools::whine(@_);
}

#------------------------------
#
# error PROBLEM...
#
# Possibly-forgivable parse error occurred.
# Raises a fatal exception unless we are ignoring errors.
#
sub error {
    my $self = shift;
    if (my $r = $self->{MP5_Results}) {
	unshift @_, $r->indent;
	$r->msg($M_ERROR, @_);
    }
    &MIME::Tools::error(@_);
    $self->{MP5_IgnoreErrors} ? return undef : die @_;
}




#------------------------------
#
# PARSING...
#

#------------------------------
#
# process_preamble IN, READER, ENTITY
#
# I<Instance method.>
# Dispose of a multipart message's preamble.
#
sub process_preamble {
    my ($self, $in, $rdr, $ent) = @_;

    ### Sanity:
    ($rdr->depth > 0) or die "$ME: internal logic error";

    ### Parse preamble:
    my @saved;
    my $data = '';
    open(my $fh, '>', \$data) or die $!;
    $rdr->read_chunk($in, $fh, 1);
    close $fh;

    # Ugh.  Horrible.  If the preamble consists only of CRLF, squash it down
    # to the empty string.  Else, remove the trailing CRLF.
    if( $data =~ m/^[\r\n]\z/ ) {
	@saved = ('');
    } els