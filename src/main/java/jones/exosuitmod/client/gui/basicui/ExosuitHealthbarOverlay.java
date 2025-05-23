package jones.exosuitmod.client.gui.basicui;

import jones.exosuitmod.ExosuitMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ExosuitHealthbarOverlay extends Gui
{
    private static final ResourceLocation EXOSUITUI = new ResourceLocation(ExosuitMod.MODID + ":textures/client/gui/exosuitui.png");

    public static final ExosuitHealthbarOverlay INSTANCE = new ExosuitHealthbarOverlay();

	@SideOnly(Side.CLIENT)
	public void renderHUD(ScaledResolution resolution, EntityPlayer player) 
    {
        int healthbar_height = 39;

        Entity tmp = player.getRidingEntity();
        if (!(tmp instanceof EntityLivingBase)) return;

        int left_align = resolution.getScaledWidth() / 2 - 91;

        Minecraft.getMinecraft().getTextureManager().bindTexture(EXOSUITUI);
        GlStateManager.enableBlend();
        EntityLivingBase mount = (EntityLivingBase)tmp;
        int health = (int)Math.ceil((double)mount.getHealth());
        float healthMax = mount.getMaxHealth();
        float healthPercentage = health / healthMax;
        int hearts = 10;

        int top = resolution.getScaledHeight() - healthbar_height;
        for (int i = 0; i < hearts; i++)
        {
            int x = left_align + i * 8;
            drawTexturedModalRect(x, top, 0, 0, 9, 9);

            if ((i + 1) * 0.1 <= healthPercentage)
            {
                drawTexturedModalRect(x, top, 9, 0, 9, 9);
            }
            else if ((i * 0.1) + 0.05 <= healthPercentage)
            {
                drawTexturedModalRect(x, top, 18, 0, 9, 9);
            }
        }
        GlStateManager.disableBlend();
    }
}
