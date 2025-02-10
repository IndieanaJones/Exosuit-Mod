package jones.exosuitmod.client.gui.container;

import jones.exosuitmod.entity.AbstractExosuit;
import jones.exosuitmod.entity.EntityMessagerChicken;
import jones.exosuitmod.inventory.ContainerMessagerChickenInventory;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiMessagerChickenInventory extends GuiContainer 
{
    private String name;
    private AbstractExosuit exosuit;
    private float mousePosX;
    private float mousePosY;
    private static final ResourceLocation HORSE_GUI_TEXTURES = new ResourceLocation("textures/gui/container/horse.png");

    public GuiMessagerChickenInventory(EntityPlayer player, EntityMessagerChicken mob) 
    {
        super(new ContainerMessagerChickenInventory(player, mob));
        this.name = mob.getName();
        exosuit = mob;
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) 
    {
        mc.getTextureManager().bindTexture(HORSE_GUI_TEXTURES);
        int lvt_4_1_ = (this.width - this.xSize) / 2;
        int lvt_5_1_ = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(lvt_4_1_, lvt_5_1_, 0, 0, this.xSize, this.ySize);
        GuiInventory.drawEntityOnScreen(lvt_4_1_ + 51, lvt_5_1_ + 60, 40, (float)(lvt_4_1_ + 51) - this.mousePosX, (float)(lvt_5_1_ + 75 - 50) - this.mousePosY, this.exosuit);
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) 
    {
        this.fontRenderer.drawString(name, this.xSize / 2 - this.fontRenderer.getStringWidth(name) / 2, 6, 000000);
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) 
    {
        this.drawDefaultBackground();
        this.mousePosX = (float)mouseX;
        this.mousePosY = (float)mouseY;
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
     }
}
