package jones.exosuitmod.entity.render;

import jones.exosuitmod.ExosuitMod;
import jones.exosuitmod.entity.EntityPatriotBullet;
import jones.exosuitmod.entity.model.ModelPatriotBullet;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPatriotBullet extends Render<EntityPatriotBullet> 
{
   private static final ResourceLocation BULLET_TEXTURES = new ResourceLocation(ExosuitMod.MODID + ":textures/entity/patriot_bullet/patriot_bullet.png");
   private final ModelPatriotBullet bulletModel = new ModelPatriotBullet();

   public RenderPatriotBullet(RenderManager renderManager) 
   {
      super(renderManager);
   }

   public void doRender(EntityPatriotBullet entity, double x, double y, double z, float entityYaw, float partialTicks) 
   {
        this.bindTexture(BULLET_TEXTURES);

        // Set up position
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x, (float) y - 0.5F, (float) z);

        // Apply the rotation based on the bullet's yaw and pitch
        GlStateManager.rotate(entity.rotationYaw, 0.0F, 1.0F, 0.0F); // Rotate around Y-axis (yaw)
        GlStateManager.rotate(entity.rotationPitch, 1.0F, 0.0F, 0.0F); // Rotate around X-axis (pitch)

        // Rotate and render the bullet
        this.bulletModel.render(entity, 0, 0, entityYaw, 0, 0, 0.05F);

        // Pop the matrix
        GlStateManager.popMatrix();
   }

   protected ResourceLocation getEntityTexture(EntityPatriotBullet p_110775_1_) 
    {
        return BULLET_TEXTURES;
    }
}