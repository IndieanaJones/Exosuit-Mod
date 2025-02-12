package jones.exosuitmod.client.gui.basicui;

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

public class ExosuitArmorOverlay extends Gui
{
    private static final ResourceLocation ICONS = new ResourceLocation("textures/gui/icons.png");

    public static final ExosuitArmorOverlay INSTANCE = new ExosuitArmorOverlay();

    public static int armorbar_height = 39;

	@SideOnly(Side.CLIENT)
	public void renderHUD(ScaledResolution resolution, EntityPlayer player) 
    {
        Entity tmp = player.getRidingEntity();
        if (!(tmp instanceof EntityLivingBase)) return;

        int right_align = resolution.getScaledWidth() / 2;
        int top = resolution.getScaledHeight() - armorbar_height;

        Minecraft.getMinecraft().getTextureManager().bindTexture(ICONS);
        GlStateManager.enableBlend();
        EntityLivingBase mount = (EntityLivingBase)tmp;

        int level = mount.getTotalArmorValue();
        for (int i = 1; level > 0 && i < 20; i += 2)
        {
            if (i < level)
            {
                drawTexturedModalRect(right_align, top, 34, 9, 9, 9);
            }
            else if (i == level)
            {
                drawTexturedModalRect(right_align, top, 25, 9, 9, 9);
            }
            else if (i > level)
            {
                drawTexturedModalRect(right_align, top, 16, 9, 9, 9);
            }
            right_align += 8;
        }

        GlStateManager.disableBlend();
    }
}
