package jones.exosuitmod.client.gui.basicui;

import jones.exosuitmod.ExosuitMod;
import jones.exosuitmod.entity.AbstractExosuit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ExosuitEnergyOverlay extends Gui
{
    private static final ResourceLocation EXOSUITUI = new ResourceLocation(ExosuitMod.MODID + ":textures/client/gui/exosuitui.png");

    public static final ExosuitEnergyOverlay INSTANCE = new ExosuitEnergyOverlay();

    public static int armorbar_height = 39;

	@SideOnly(Side.CLIENT)
	public void renderHUD(ScaledResolution resolution, EntityPlayer player) 
    {
        Entity tmp = player.getRidingEntity();
        if (!(tmp instanceof AbstractExosuit)) return;

        int right_align = resolution.getScaledWidth() / 2 + 10;
        int top = resolution.getScaledHeight() - armorbar_height;

        Minecraft.getMinecraft().getTextureManager().bindTexture(EXOSUITUI);
        GlStateManager.enableBlend();
        AbstractExosuit mount = (AbstractExosuit)tmp;

        int energy = (int)Math.ceil((double)mount.getCurrentEnergy());
        float energyMax = mount.getMaxEnergy();
        float energyPercentage = energy / energyMax;
        int energyBits = 10;

        for (int i = 0; i < energyBits; i++)
        {
            int x = right_align + i * 8;
            drawTexturedModalRect(x, top, 27, 0, 9, 9);

            if ((i + 1) * 0.1 <= energyPercentage)
            {
                drawTexturedModalRect(x, top, 45, 0, 9, 9);
            }
            else if ((i * 0.1) + 0.05 <= energyPercentage)
            {
                drawTexturedModalRect(x, top, 36, 0, 9, 9);
            }
        }

        GlStateManager.disableBlend();
    }
}
