package jones.exosuitmod.inventory.container;

import jones.exosuitmod.entity.AbstractExosuit;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerPatriotExosuitInventory extends ContainerExosuitInventory 
{
    public ContainerPatriotExosuitInventory(EntityPlayer player, AbstractExosuit mob) 
    {
        super(player, mob);
    }
}