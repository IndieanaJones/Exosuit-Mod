package jones.exosuitmod.event;

import jones.exosuitmod.client.gui.container.GuiMessagerChickenInventory;
import jones.exosuitmod.client.gui.container.GuiPatriotExosuitInventory;
import jones.exosuitmod.entity.EntityMessagerChicken;
import jones.exosuitmod.entity.EntityPatriotExosuit;
import jones.exosuitmod.inventory.container.ContainerMessagerChickenInventory;
import jones.exosuitmod.inventory.container.ContainerPatriotExosuitInventory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler 
{
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
    {
        if (ID == 1) 
        {
            Entity entity = world.getEntityByID(x);
            if (entity instanceof EntityMessagerChicken) 
            {
                return new ContainerMessagerChickenInventory(player, (EntityMessagerChicken)entity);
            }
            if (entity instanceof EntityPatriotExosuit) 
            {
                return new ContainerPatriotExosuitInventory(player, (EntityPatriotExosuit)entity);
            }
        }
        return null;
    }

    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) 
    {
        if (ID == 1) 
        {
            Entity entity = world.getEntityByID(x);
            if (entity instanceof EntityMessagerChicken) 
            {
                return new GuiMessagerChickenInventory(player, (EntityMessagerChicken)entity);
            }
            if (entity instanceof EntityPatriotExosuit) 
            {
                return new GuiPatriotExosuitInventory(player, (EntityPatriotExosuit)entity);
            }
        }
        return null;
    }
}