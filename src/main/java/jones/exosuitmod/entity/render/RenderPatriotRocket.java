package jones.exosuitmod.entity.render;

import jones.exosuitmod.ExosuitMod;
import jones.exosuitmod.entity.EntityPatriotRocket;
import jones.exosuitmod.entity.model.ModelPatriotRocket;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPatriotRocket extends Render<EntityPatriotRocket> 
{
   private static final ResourceLocation ROCKET_TEXTURES = new ResourceLocation(ExosuitMod.MODID + ":textures/entity/patriot_rocket/patriot_rocket.png");
   private final ModelPatriotRocket rocketModel = new ModelPatriotRocket();

   public RenderPatriotRocket(RenderManager renderManager) 
   {
      super(renderManager);
   }

    @Override
    public void doRender(EntityPatriotRocket entity, double x, double y, double z, float entityYaw, float partialTicks) 
    {
        GlStateManager.pushMatrix();

        GlStateManager.translate(x, y, z);
        GlStateManager.translate(0.0F, -0.75F, 0.0F);

        double dx = entity.motionX;
        double dy = entity.motionY;
        double dz = entity.motionZ;

        float yaw = (float)(Math.atan2(dz, dx) * (180D / Math.PI)) - 90F;
        float pitch = (float)(-(Math.atan2(dy, Math.sqrt(dx * dx + dz * dz)) * (180D / Math.PI)));

        GlStateManager.rotate(-yaw, 0F, 1F, 0F);
        GlStateManager.rotate(pitch, 1F, 0F, 0F);
        GlStateManager.rotate(180F, 0F, 1F, 0F);

        bindEntityTexture(entity);
        rocketModel.render(entity, 0, 0, 0, 0, 0, 0.05f);

        GlStateManager.popMatrix();
    }

   protected ResourceLocation getEntityTexture(EntityPatriotRocket p_110775_1_) 
    {
        return ROCKET_TEXTURES;
    }
}