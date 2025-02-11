package jones.exosuitmod.network.packets;

import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import io.netty.buffer.ByteBuf;
import jones.exosuitmod.entity.AbstractExosuit;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketSendExosuitCooldown implements IMessage
{
    public int entityID;
    public int cooldownTime;
    public int cooldownType;
    
    public PacketSendExosuitCooldown() {}
    
    public PacketSendExosuitCooldown(AbstractExosuit exosuit, int cooldown, int type) 
    {
        this.entityID = exosuit.getEntityId();
        this.cooldownTime = cooldown;
        this.cooldownType = type;
    }
    
    public void fromBytes(final ByteBuf buf) 
    {
        this.entityID = buf.readInt();
        this.cooldownTime = buf.readInt();
        this.cooldownType = buf.readInt();
    }
    
    public void toBytes(final ByteBuf buf) 
    {
        buf.writeInt(this.entityID);
        buf.writeInt(this.cooldownTime);
        buf.writeInt(this.cooldownType);
    }
    
    public static class SendExosuitCooldownHandler implements IMessageHandler<PacketSendExosuitCooldown, IMessage>
    {
        public IMessage onMessage(PacketSendExosuitCooldown message, MessageContext ctx) 
        {
            AbstractExosuit exosuit = (AbstractExosuit)Minecraft.getMinecraft().world.getEntityByID(message.entityID);
            if (exosuit == null || !exosuit.world.isRemote)
                return null;

            switch(message.cooldownType)
            {
                case 0:
                {
                    exosuit.leftClickCooldown = message.cooldownTime;
                    break;
                }
                case 1:
                {
                    exosuit.rightClickCooldown = message.cooldownTime;
                    break;
                }
                default:
                {
                    exosuit.leftClickCooldown = message.cooldownTime;
                    break;
                }
            }
            
            return null;
        }
    }
}