package net.minecraft.network;

import com.mojang.logging.LogUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import java.io.IOException;
import net.minecraft.network.protocol.EnumProtocolDirection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.util.profiling.jfr.JvmProfiler;
import org.slf4j.Logger;

public class PacketEncoder extends MessageToByteEncoder<Packet<?>> {

    private static final Logger LOGGER = LogUtils.getLogger();
    private final EnumProtocolDirection flow;

    public PacketEncoder(EnumProtocolDirection enumprotocoldirection) {
        this.flow = enumprotocoldirection;
    }

    protected void encode(ChannelHandlerContext channelhandlercontext, Packet<?> packet, ByteBuf bytebuf) throws Exception {
        EnumProtocol enumprotocol = (EnumProtocol) channelhandlercontext.channel().attr(NetworkManager.ATTRIBUTE_PROTOCOL).get();

        if (enumprotocol == null) {
            throw new RuntimeException("ConnectionProtocol unknown: " + packet);
        } else {
            Integer integer = enumprotocol.getPacketId(this.flow, packet);

            if (PacketEncoder.LOGGER.isDebugEnabled()) {
                PacketEncoder.LOGGER.debug(NetworkManager.PACKET_SENT_MARKER, "OUT: [{}:{}] {}", new Object[]{channelhandlercontext.channel().attr(NetworkManager.ATTRIBUTE_PROTOCOL).get(), integer, packet.getClass().getName()});
            }

            if (integer == null) {
                throw new IOException("Can't serialize unregistered packet");
            } else {
                PacketDataSerializer packetdataserializer = new PacketDataSerializer(bytebuf);

                packetdataserializer.writeVarInt(integer);

                try {
                    int i = packetdataserializer.writerIndex();

                    packet.write(packetdataserializer);
                    int j = packetdataserializer.writerIndex() - i;

                    if (j > 8388608) {
                        throw new IllegalArgumentException("Packet too big (is " + j + ", should be less than 8388608): " + packet);
                    } else {
                        int k = ((EnumProtocol) channelhandlercontext.channel().attr(NetworkManager.ATTRIBUTE_PROTOCOL).get()).getId();

                        JvmProfiler.INSTANCE.onPacketSent(k, integer, channelhandlercontext.channel().remoteAddress(), j);
                    }
                } catch (Throwable throwable) {
                    PacketEncoder.LOGGER.error("Error receiving packet {}", integer, throwable);
                    if (packet.isSkippable()) {
                        throw new SkipEncodeException(throwable);
                    } else {
                        throw throwable;
                    }
                }
            }
        }
    }
}
