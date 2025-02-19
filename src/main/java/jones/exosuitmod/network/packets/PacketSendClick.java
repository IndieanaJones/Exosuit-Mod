package jones.exosuitmod.network.packets;

import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import io.netty.buffer.ByteBuf;
import jones.exosuitmod.entity.AbstractExosuit;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketSendClick implements IMessage
{
    public int entityID;
    public int clickType;
    public boolean setPressed;
    
    public PacketSendClick() {}
    
    public PacketSendClick(EntityPlayer player, int click, Boolean pressed) 
    {
        this.entityID = player.getEntityId();
        this.clickType = click;
        this.setPressed = pressed;
    }
    
    public void fromBytes(final ByteBuf buf) 
    {
        this.entityID = buf.readInt();
        this.clickType = buf.readInt();
        this.setPressed = buf.readBoolean();
    }
    
    public void toBytes(final ByteBuf buf) 
    {
        buf.writeInt(this.entityID);
        buf.writeInt(this.clickType);
        buf.writeBoolean(this.setPressed);
    }
    
    public static class SendClickHandler implements IMessageHandler<PacketSendClick, IMessage>
    {
        public IMessage onMessage(PacketSendClick message, MessageContext ctx) 
        {
            World server = ctx.getServerHandler().player.world;
            EntityPlayer player = (EntityPlayer)server.getEntityByID(message.entityID);
            if (player == null || player.world.isRemote)
                return null;
            if (!player.isRiding() || !(player.getRidingEntity() instanceof AbstractExosuit))
                return null;

            AbstractExosuit exosuit = (AbstractExosuit) player.getRidingEntity();

            if(message.clickType == 0)
            {
                exosuit.leftClickPressed = message.setPressed;
                exosuit.onLeftClickPressed(message.setPressed);
            }
            else
            {
                exosuit.rightClickPressed = message.setPressed;
                exosuit.onRightClickPressed(message.setPressed);
            }
            return null;
        }
    }
}