from re import compile as _compile

# maybe only strip whitespace?
_sub = _compile(r"([^\w]|_)").sub
def sanitize(x):
    return _sub("", x).strip().lower()
