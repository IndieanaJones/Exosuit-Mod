package jones.exosuitmod.network.packets;

import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import io.netty.buffer.ByteBuf;
import jones.exosuitmod.entity.AbstractExosuit;
import net.minecraft.init.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketChickenFlapClient implements IMessage
{
    public int entityID;
    
    public PacketChickenFlapClient() {}
    
    public PacketChickenFlapClient(AbstractExosuit exosuit) 
    {
        this.entityID = exosuit.getEntityId();
    }
    
    public void fromBytes(final ByteBuf buf) 
    {
        this.entityID = buf.readInt();
    }
    
    public void toBytes(final ByteBuf buf) 
    {
        buf.writeInt(this.entityID);
    }
    
    public static class ChickenFlapClientHandler implements IMessageHandler<PacketChickenFlapClient, IMessage>
    {
        public IMessage onMessage(PacketChickenFlapClient message, MessageContext ctx) 
        {
            World server = ctx.getServerHandler().player.world;
            AbstractExosuit exosuit = (AbstractExosuit)server.getEntityByID(message.entityID);
            if (exosuit == null || exosuit.world.isRemote)
                return null;

            exosuit.playSound(SoundEvents.ENTITY_BAT_TAKEOFF, 0.75F, 1);
            
            return null;
        }
    }
}