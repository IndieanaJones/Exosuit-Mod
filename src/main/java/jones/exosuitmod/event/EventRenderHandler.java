package jones.exosuitmod.event;

import jones.exosuitmod.entity.EntityMessagerChicken;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class EventRenderHandler 
{
    //Don't render the player if they're riding in an exosuit.
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onPlayerRender(RenderPlayerEvent.Pre event)
    {
        EntityPlayer player = event.getEntityPlayer();
        if (!player.isRiding() || !(player.getRidingEntity() instanceof EntityMessagerChicken))
            return;
        event.setCanceled(true);
    }

    //Don't render an exosuit the player is riding in if they're in first person.
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onEntityRender(RenderLivingEvent.Pre<EntityMessagerChicken> event)
    {
        EntityLivingBase exosuit = event.getEntity();
        if(!exosuit.isBeingRidden() || exosuit.getControllingPassenger() != Minecraft.getMinecraft().player)
            return;
        if(Minecraft.getMinecraft().gameSettings.thirdPersonView == 0)
            event.setCanceled(true);
    }

    //Don't render the hotbar if we're in an exosuit.
    @SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void RenderGameOverlay(RenderGameOverlayEvent event)
	{
		if(event.getType() != RenderGameOverlayEvent.ElementType.HOTBAR)
            return;
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (!player.isRiding() || !(player.getRidingEntity() instanceof EntityMessagerChicken))
            return;
        event.setCanceled(true);
	}

    //Don't render the player's hand in first person if in an exosuit.
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void RenderHandFirstPerson(RenderHandEvent event)
    {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (!player.isRiding() || !(player.getRidingEntity() instanceof EntityMessagerChicken))
            return;
        event.setCanceled(true);
    }
}
