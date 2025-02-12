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

    public static int healthbar_height = 39;

	@SideOnly(Side.CLIENT)
	public void renderHUD(ScaledResolution resolution, EntityPlayer player) 
    {
        healthbar_height = 39;

        Entity tmp = player.getRidingEntity();
        if (!(tmp instanceof EntityLivingBase)) return;

        int left_align = resolution.getScaledWidth() / 2 - 91;

        Minecraft.getMinecraft().getTextureManager().bindTexture(EXOSUITUI);
        GlStateManager.enableBlend();
        EntityLivingBase mount = (EntityLivingBase)tmp;
        int health = (int)Math.ceil((double)mount.getHealth());
        float healthMax = mount.getMaxHealth();
        int hearts = (int)(healthMax + 0.5F) / 2;

        if (hearts > 30) hearts = 30;

        for (int heart = 0; hearts > 0; heart += 20)
        {
            int top = resolution.getScaledHeight() - healthbar_height;

            int rowCount = Math.min(hearts, 10);
            hearts -= rowCount;

            for (int i = 0; i < rowCount; ++i)
            {
                int x = left_align + i * 8;
                drawTexturedModalRect(x, top, 0, 0, 9, 9);

                if (i * 2 + 1 + heart < health)
                {
                    drawTexturedModalRect(x, top, 9, 0, 9, 9);
                }
                else if (i * 2 + 1 + heart == health)
                {
                    drawTexturedModalRect(x, top, 18, 0, 9, 9);
                }
            }

            healthbar_height += 10;
        }
        GlStateManager.disableBlend();
    }
}
