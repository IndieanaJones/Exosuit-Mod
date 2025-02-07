package jones.exosuitmod.event;

import jones.exosuitmod.entity.EntityMessagerChicken;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.InputUpdateEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class EventHandler 
{
    //Lets you jump while riding an exosuit.
	@SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGH)
	public static void onUpdateInput(InputUpdateEvent event)
    {
        EntityPlayer player = event.getEntityPlayer();
        if (!player.isRiding() || !(player.getRidingEntity() instanceof EntityMessagerChicken))
            return;

        boolean isJumping = event.getMovementInput().jump;
        EntityMessagerChicken messagerChicken = (EntityMessagerChicken) player.getRidingEntity();
        messagerChicken.setJumping(isJumping);
    }

    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.NORMAL, receiveCanceled = true)
    public static void onInputEvent(InputEvent event)
    {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (!player.isRiding() || !(player.getRidingEntity() instanceof EntityMessagerChicken))
            return;
    }

    //Don't pick up items while in an exosuit.
    @SubscribeEvent
    public static void pickupItem(EntityItemPickupEvent event)
    {
        EntityPlayer player = event.getEntityPlayer();
        if (!player.isRiding() || !(player.getRidingEntity() instanceof EntityMessagerChicken))
            return;
        event.setCanceled(true);
    }

    //Pass whatever damage we would normally take while in the exosuit to the exosuit.
    //If the attacker is a mob, direct them to target the exosuit instead.
    @SubscribeEvent
    public static void onAttackedEvent(LivingAttackEvent event)
    {
        if(!(event.getEntity() instanceof EntityPlayer))
            return;
        EntityPlayer player = (EntityPlayer)event.getEntity();
        if (!player.isRiding() || !(player.getRidingEntity() instanceof EntityMessagerChicken))
            return;
        event.setCanceled(true);
    }

    //Remove the player's collision box and set the eye height of the player to that of the exosuit while inside it.
    @SubscribeEvent
    public static void onPlayerTickEvent(PlayerTickEvent event)
    {
        if (!event.player.isRiding() || !(event.player.getRidingEntity() instanceof EntityMessagerChicken))
            return;
        event.player.noClip = true;
        event.player.setSize(0.0F, 0.0F);
        event.player.eyeHeight = event.player.getRidingEntity().getEyeHeight();
    }

    //If a mob tries to target the player while they are inside an exosuit, target the exosuit instead.
    @SubscribeEvent
    public static void onLivingSetAttackTarget(LivingSetAttackTargetEvent event) 
    {
        if (!(event.getTarget() instanceof EntityPlayer))
        	return;
        EntityPlayer player = (EntityPlayer)event.getTarget();
        if (!player.isRiding() || !(player.getRidingEntity() instanceof EntityMessagerChicken))
            return;
        if(!(event.getEntity() instanceof EntityLiving))
            return;
        EntityLiving mobEntity = (EntityLiving)event.getEntityLiving();
        mobEntity.setAttackTarget((EntityLivingBase)player.getRidingEntity());
    }
}