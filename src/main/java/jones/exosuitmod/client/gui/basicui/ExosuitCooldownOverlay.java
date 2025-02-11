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
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber
public class ExosuitCooldownOverlay extends Gui
{
    private static final ResourceLocation EXOSUITUI = new ResourceLocation(ExosuitMod.MODID + ":textures/client/gui/exosuitui.png");

    public static final ExosuitCooldownOverlay INSTANCE = new ExosuitCooldownOverlay();

    public static int cooldown_height = 59;
    public static int cooldown_bar_length = 40;

	@SideOnly(Side.CLIENT)
	public void renderHUD(ScaledResolution resolution, EntityPlayer player) 
    {

        Entity tmp = player.getRidingEntity();
        if (!(tmp instanceof AbstractExosuit)) 
            return;

        int left_align = resolution.getScaledWidth() / 2 - 91;
        int right_align = resolution.getScaledWidth() / 2 + 91;

        Minecraft.getMinecraft().getTextureManager().bindTexture(EXOSUITUI);
        GlStateManager.enableBlend();
        AbstractExosuit mount = (AbstractExosuit)tmp;

        drawTexturedModalRect(left_align, resolution.getScaledHeight() - cooldown_height, mount.leftClickCooldown == 0 ? cooldown_bar_length * 2 : 0, 9, cooldown_bar_length, 9);
        if(mount.leftClickCooldown != 0)
        {
            int fillValue = cooldown_bar_length - Math.round(((float)mount.leftClickCooldown / (float)mount.getMaxLeftClickCooldown()) * cooldown_bar_length);
            drawTexturedModalRect(left_align, resolution.getScaledHeight() - cooldown_height, cooldown_bar_length, 9, fillValue, 9);
        }

        drawTexturedModalRect(right_align, resolution.getScaledHeight() - cooldown_height + 9, mount.rightClickCooldown == 0 ? cooldown_bar_length * 3 : cooldown_bar_length, 18, -cooldown_bar_length, -9);
        if(mount.rightClickCooldown != 0)
        {
            int fillValue = cooldown_bar_length - Math.round(((float)mount.rightClickCooldown / (float)mount.getMaxRightClickCooldown()) * cooldown_bar_length);
            drawTexturedModalRect(right_align, resolution.getScaledHeight() - cooldown_height + 9, cooldown_bar_length * 2, 18, -fillValue, -9);
        }

        GlStateManager.disableBlend();
    }
}
