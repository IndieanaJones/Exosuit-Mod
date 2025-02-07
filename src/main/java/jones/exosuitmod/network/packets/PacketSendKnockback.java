package jones.exosuitmod.network.packets;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketSendKnockback implements IMessage
{
    public int entityID;
    public int entityInID;
    public float strength;
    public double xRatio;
    public double zRatio;
    
    public PacketSendKnockback() {}
    
    public PacketSendKnockback(EntityLivingBase entity, Entity entityIn, float str, double xRat, double zRat) 
    {
        this.entityID = entity.getEntityId();
        this.entityInID = entityIn.getEntityId();
        this.strength = str;
        this.xRatio = xRat;
        this.zRatio = zRat;
    }
    
    public void fromBytes(final ByteBuf buf) 
    {
        this.entityID = buf.readInt();
        this.entityInID = buf.readInt();
        this.strength = buf.readFloat();
        this.xRatio = buf.readDouble();
        this.zRatio = buf.readDouble();
    }
    
    public void toBytes(final ByteBuf buf) 
    {
        buf.writeInt(this.entityID);
        buf.writeInt(this.entityInID);
        buf.writeFloat(this.strength);
        buf.writeDouble(this.xRatio);
        buf.writeDouble(this.zRatio);
    }
    
    public static class SendKnockbackHandler implements IMessageHandler<PacketSendKnockback, IMessage>
    {
        public IMessage onMessage(final PacketSendKnockback message, final MessageContext ctx) 
        {
            EntityLivingBase entity = (EntityLivingBase)Minecraft.getMinecraft().world.getEntityByID(message.entityID);
            if (entity == null)
                return null;
            EntityLivingBase entityIn = (EntityLivingBase)Minecraft.getMinecraft().world.getEntityByID(message.entityInID);
            entity.knockBack(entityIn, message.strength, message.xRatio, message.zRatio);
            entity.motionY = 50;
            return null;
        }
    }
}