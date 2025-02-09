package jones.exosuitmod.entity.render;

import jones.exosuitmod.ExosuitMod;
import jones.exosuitmod.entity.EntityMessagerChicken;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMessagerChicken extends RenderLiving<EntityMessagerChicken>
{
    private static final ResourceLocation CHICKEN_TEXTURES = new ResourceLocation(ExosuitMod.MODID + ":textures/entity/messager_chicken.png");

    public RenderMessagerChicken(RenderManager p_i47211_1_)
    {
        super(p_i47211_1_, new ModelChicken(), 0.3F);
    }

    protected ResourceLocation getEntityTexture(EntityMessagerChicken entity)
    {
        return CHICKEN_TEXTURES;
    }

    protected float handleRotationFloat(EntityMessagerChicken livingBase, float partialTicks)
    {
        float f = livingBase.oFlap + (livingBase.wingRotation - livingBase.oFlap) * partialTicks;
        float f1 = livingBase.oFlapSpeed + (livingBase.destPos - livingBase.oFlapSpeed) * partialTicks;
        return (MathHelper.sin(f) + 1.0F) * f1;
    }
}
