package jones.exosuitmod.event;

import jones.exosuitmod.entity.EntityMessagerChicken;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class EventInteractionHandler 
{
    @SubscribeEvent
    public static void onEntitySpecificInteractEvent(PlayerInteractEvent.EntityInteractSpecific event)
    {
        EntityPlayer player = event.getEntityPlayer();
        if (!player.isRiding() || !(player.getRidingEntity() instanceof EntityMessagerChicken))
            return;
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onEntityInteractEvent(PlayerInteractEvent.EntityInteract event)
    {
        EntityPlayer player = event.getEntityPlayer();
        if (!player.isRiding() || !(player.getRidingEntity() instanceof EntityMessagerChicken))
            return;
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onRightClickBlock (PlayerInteractEvent.RightClickBlock event)
    {
        EntityPlayer player = event.getEntityPlayer();
        if (!player.isRiding() || !(player.getRidingEntity() instanceof EntityMessagerChicken))
            return;
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onRightClickItemEvent (PlayerInteractEvent.RightClickItem event)
    {
        EntityPlayer player = event.getEntityPlayer();
        if (!player.isRiding() || !(player.getRidingEntity() instanceof EntityMessagerChicken))
            return;
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onLeftClickBlock (PlayerInteractEvent.LeftClickBlock event)
    {
        EntityPlayer player = event.getEntityPlayer();
        if (!player.isRiding() || !(player.getRidingEntity() instanceof EntityMessagerChicken))
            return;
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onPlayerAttack (AttackEntityEvent event)
    {
        EntityPlayer player = event.getEntityPlayer();
        if (!player.isRiding() || !(player.getRidingEntity() instanceof EntityMessagerChicken))
            return;
        event.setCanceled(true);
    }
}