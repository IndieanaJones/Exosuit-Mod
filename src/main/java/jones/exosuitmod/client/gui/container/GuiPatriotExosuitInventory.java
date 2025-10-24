package jones.exosuitmod.client.gui.container;

import jones.exosuitmod.ExosuitMod;
import jones.exosuitmod.entity.AbstractExosuit;
import jones.exosuitmod.entity.EntityPatriotExosuit;
import jones.exosuitmod.inventory.container.ContainerPatriotExosuitInventory;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiPatriotExosuitInventory extends GuiContainer 
{
    private String name;
    private AbstractExosuit exosuit;
    private float mousePosX;
    private float mousePosY;

    private int guiSizeX = 176;
    private int guiSizeY = 250;
    private static final ResourceLocation EXOSUIT_INVENTORY_UI = new ResourceLocation(ExosuitMod.MODID + ":textures/client/gui/inventory/exosuitnew.png");

    public GuiPatriotExosuitInventory(EntityPlayer player, EntityPatriotExosuit mob) 
    {
        super(new ContainerPatriotExosuitInventory(player, mob));
        this.name = mob.getName();
        exosuit = mob;
    }

    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) 
    {
        mc.getTextureManager().bindTexture(EXOSUIT_INVENTORY_UI);
        int xPos = (this.width - this.guiSizeX) / 2;
        int yPos = (this.height - this.guiSizeY) / 2;
        this.drawTexturedModalRect(xPos, yPos, 0, 0, this.guiSizeX, this.guiSizeY);
        //Night vision upgrade slot
        this.drawTexturedModalRect(xPos + 8, yPos + 110, 176, 18, 18, 18);
        GuiInventory.drawEntityOnScreen(xPos + 87, yPos + 70, 10, (float)(xPos + 87) - this.mousePosX, (float)(yPos + 70) - this.mousePosY, this.exosuit);
    }

    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) 
    {
        this.fontRenderer.drawString(name, this.guiSizeX / 2 - this.fontRenderer.getStringWidth(name) / 2, -35, 000000);
        this.fontRenderer.drawString(String.valueOf(exosuit.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue() / 2), (this.guiSizeX / 2) - 19, 59, 000000);
        this.fontRenderer.drawString(String.valueOf(exosuit.getEntityAttribute(SharedMonsterAttributes.ARMOR).getBaseValue() / 2), (this.guiSizeX / 2) + 30, 59, 000000);
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
