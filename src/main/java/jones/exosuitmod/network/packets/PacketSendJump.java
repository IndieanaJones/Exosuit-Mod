package jones.exosuitmod.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import io.netty.buffer.ByteBuf;
import jones.exosuitmod.entity.AbstractExosuit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketSendJump implements IMessage
{
    public int entityID;
    public boolean setJumping;
    
    public PacketSendJump() {}
    
    public PacketSendJump(final EntityPlayer player, final Boolean setJump) 
    {
        this.entityID = player.getEntityId();
        this.setJumping = setJump;
    }
    
    public void fromBytes(final ByteBuf buf) 
    {
        this.entityID = buf.readInt();
        this.setJumping = buf.readBoolean();
    }
    
    public void toBytes(final ByteBuf buf) 
    {
        buf.writeInt(this.entityID);
        buf.writeBoolean(this.setJumping);
    }
    
    public static class SendJumpHandler implements IMessageHandler<PacketSendJump, IMessage>
    {
        public IMessage onMessage(final PacketSendJump message, final MessageContext ctx) 
        {
            final EntityPlayer player = (EntityPlayer)Minecraft.getMinecraft().world.getEntityByID(message.entityID);
            if (player == null)
                return null;
            
            if (!player.isRiding() || !(player.getRidingEntity() instanceof AbstractExosuit))
                return null;

            AbstractExosuit exosuit = (AbstractExosuit) player.getRidingEntity();

            exosuit.setJumping(message.setJumping);
            
            return null;
        }
    }
}