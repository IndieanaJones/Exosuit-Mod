package jones.exosuitmod.event;

import jones.exosuitmod.client.gui.basicui.ExosuitArmorOverlay;
import jones.exosuitmod.client.gui.basicui.ExosuitCooldownOverlay;
import jones.exosuitmod.client.gui.basicui.ExosuitHealthbarOverlay;
import jones.exosuitmod.entity.AbstractExosuit;
import jones.exosuitmod.entity.render.AdvancedEntityTextureHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.client.event.RenderHandEvent;
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
        if (!player.isRiding() || !(player.getRidingEntity() instanceof AbstractExosuit))
            return;
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
        if (!player.isRiding() || !(player.getRidingEntity() instanceof AbstractExosuit))
            return;
        event.setCanceled(true);
	}

    //Don't render the player's hand in first person if in an exosuit.
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void RenderHandFirstPerson(RenderHandEvent event)
    {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (!player.isRiding() || !(player.getRidingEntity() instanceof AbstractExosuit))
            return;
        event.setCanceled(true);
        if(Minecraft.getMinecraft().gameSettings.thirdPersonView == 0)
        {
            AbstractExosuit exosuit = (AbstractExosuit)player.getRidingEntity();
            exosuit.renderMobHands(event.getPartialTicks());
        }
    }

    //Render custom exosuit health bar
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
	public static void onRenderHealthBar(RenderGameOverlayEvent.Pre event)
	{
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (!player.isRiding() || !(player.getRidingEntity() instanceof AbstractExosuit))
        return;
		if(event.getType() == ElementType.HEALTHMOUNT)
		{
            event.setCanceled(true);
		}
        else if(event.getType() == ElementType.HEALTH)
        {
            event.setCanceled(true);
        }
        else if(event.getType() == ElementType.HOTBAR)
        {
            ExosuitHealthbarOverlay.INSTANCE.renderHUD(event.getResolution(), player);
            ExosuitCooldownOverlay.INSTANCE.renderHUD(event.getResolution(), player);
            ExosuitArmorOverlay.INSTANCE.renderHUD(event.getResolution(), player);
        }
	}

    //Clear the custom skin cache when we leave a world
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void onWorldUnload(WorldEvent.Unload event)
    {
        AdvancedEntityTextureHandler.INSTANCE.close();
    }

    //Zooms out the third person camera further away, since we're in a big hulking mech.
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public static void adjustCamera(EntityViewRenderEvent.CameraSetup event)
    {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if (!player.isRiding() || !(player.getRidingEntity() instanceof AbstractExosuit))
            return;
        AbstractExosuit exosuit = (AbstractExosuit)player.getRidingEntity();
        if(exosuit.getExtraThirdPersonZoom() == 0)
            return;

        Vec3d cameraPos = player.getPositionEyes((float)event.getRenderPartialTicks());
        Vec3d lookDirection = player.getLook(1.0F);
        double zoomDistance = exosuit.getExtraThirdPersonZoom() + 4;
        float targetDirection = 1;
        if(Minecraft.getMinecraft().gameSettings.thirdPersonView == 1)
            targetDirection = -1;
        Vec3d targetPos = cameraPos.add(new Vec3d(lookDirection.x * zoomDistance * targetDirection, lookDirection.y * zoomDistance * targetDirection, lookDirection.z * zoomDistance * targetDirection));
        RayTraceResult rayTraceResult = player.world.rayTraceBlocks(cameraPos, targetPos);
        if (rayTraceResult != null && rayTraceResult.typeOfHit == RayTraceResult.Type.BLOCK) 
        {
            // Adjust the camera to just in front of the hit block
            targetPos = new Vec3d(rayTraceResult.hitVec.x, rayTraceResult.hitVec.y, rayTraceResult.hitVec.z);
        }
        double finalAlteration = targetPos.distanceTo(cameraPos);
        if(finalAlteration < 4.1)
            return;
        finalAlteration -= 4.1;
        
        Minecraft.getMinecraft().getRenderViewEntity().getPositionVector();
        if(Minecraft.getMinecraft().gameSettings.thirdPersonView > 0)
        {
            if(Minecraft.getMinecraft().gameSettings.thirdPersonView == 1)
                finalAlteration *= -1;
            GlStateManager.translate(0.0F, 0.0F, (float)finalAlteration);
        }
    }
}
