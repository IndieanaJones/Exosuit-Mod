package jones.exosuitmod.entity.render;

import jones.exosuitmod.entity.EntityPatriotExosuit;
import jones.exosuitmod.entity.model.ModelPatriotExosuit;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPatriotExosuit extends RenderLiving<EntityPatriotExosuit>
{
    public RenderPatriotExosuit(RenderManager p_i47211_1_)
    {
        super(p_i47211_1_, new ModelPatriotExosuit(), 1.5F);
    }

    protected ResourceLocation getEntityTexture(EntityPatriotExosuit entity)
    {
        return AdvancedEntityTextureHandler.INSTANCE.getTextureLocation(entity);
    }
}
