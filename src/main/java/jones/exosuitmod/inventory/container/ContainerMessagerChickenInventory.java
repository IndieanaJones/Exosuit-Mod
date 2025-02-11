package jones.exosuitmod.inventory.container;

import jones.exosuitmod.entity.AbstractExosuit;
import jones.exosuitmod.inventory.slot.RestrictedExosuitSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;

public class ContainerMessagerChickenInventory extends ContainerExosuitInventory 
{
    public ContainerMessagerChickenInventory(EntityPlayer player, AbstractExosuit mob) 
    {
        super(player, mob);
        this.addSlotToContainer(new RestrictedExosuitSlot(exosuit.inventory, 0, 80, 18, Items.DIAMOND, 1));
    }
}