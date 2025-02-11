package jones.exosuitmod.client.gui.container;

import jones.exosuitmod.ExosuitMod;
import jones.exosuitmod.entity.AbstractExosuit;
import jones.exosuitmod.entity.EntityMessagerChicken;
import jones.exosuitmod.inventory.container.ContainerExosuitInventory;
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
    private static final ResourceLocation EXOSUIT_INVENTORY_UI = new ResourceLocation(ExosuitMod.MODID + ":textures/client/gui/inventory/exosuit.png");

    public GuiMessagerChickenInventory(EntityPlayer player, EntityMessagerChicken mob) 
    {
        super(new ContainerExosuitInventory(player, mob));
        this.name = mob.getName();
        exosuit = mob;
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) 
    {
        mc.getTextureManager().bindTexture(EXOSUIT_INVENTORY_UI);
        int xPos = (this.width - this.xSize) / 2;
        int yPos = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(xPos, yPos, 0, 0, this.xSize, this.ySize);
        GuiInventory.drawEntityOnScreen(xPos + 51, yPos + 60, 40, (float)(xPos + 51) - this.mousePosX, (float)(yPos + 25) - this.mousePosY, this.exosuit);
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
