package jones.exosuitmod.entity.render;

import jones.exosuitmod.ExosuitMod;
import jones.exosuitmod.entity.EntityMessagerChicken;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMessagerChicken extends RenderLiving<EntityMessagerChicken>
{
    private static final ResourceLocation MESSAGER_CHICKEN_TEXTURES = new ResourceLocation(ExosuitMod.MODID + ":textures/entity/messager_chicken/messager_chicken.png");
    private static final ResourceLocation JUMP_1_TEXTURES = new ResourceLocation(ExosuitMod.MODID + ":textures/entity/messager_chicken/messager_chicken_jump_1.png");

    public RenderMessagerChicken(RenderManager p_i47211_1_)
    {
        super(p_i47211_1_, new ModelChicken(), 0.3F);
    }

    protected ResourceLocation getEntityTexture(EntityMessagerChicken entity)
    {
        return MESSAGER_CHICKEN_TEXTURES;
    }

    @Override
    protected void renderModel(EntityMessagerChicken entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor)
    {
        super.renderModel(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);

        boolean flag = this.isVisible(entitylivingbaseIn);
        boolean flag1 = !flag && !entitylivingbaseIn.isInvisibleToPlayer(Minecraft.getMinecraft().player);

        if (!flag && !flag1)
            return;

        if (flag1)
            GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
        this.bindTexture(JUMP_1_TEXTURES);
        this.mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
        if (flag1)
            GlStateManager.disableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
    }

    protected float handleRotationFloat(EntityMessagerChicken livingBase, float partialTicks)
    {
        float f = livingBase.oFlap + (livingBase.wingRotation - livingBase.oFlap) * partialTicks;
        float f1 = livingBase.oFlapSpeed + (livingBase.destPos - livingBase.oFlapSpeed) * partialTicks;
        return (MathHelper.sin(f) + 1.0F) * f1;
    }
}
