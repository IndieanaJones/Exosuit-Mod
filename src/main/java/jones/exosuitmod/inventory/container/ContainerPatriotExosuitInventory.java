package jones.exosuitmod.inventory.container;

import java.util.Arrays;

import jones.exosuitmod.entity.AbstractExosuit;
import jones.exosuitmod.inventory.slot.RestrictedExosuitSlot;
import jones.exosuitmod.item.ItemInit;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerPatriotExosuitInventory extends ContainerExosuitInventory 
{
    public ContainerPatriotExosuitInventory(EntityPlayer player, AbstractExosuit mob) 
    {
        super(player, mob);
        this.addSlotToContainer(new RestrictedExosuitSlot(exosuit.inventory, 0, 9, 69, Arrays.asList(ItemInit.EXOSUIT_NIGHTIVISON_UPGRADE), 1));
        this.addSlotToContainer(new RestrictedExosuitSlot(exosuit.inventory, 1, 27, 69, Arrays.asList(ItemInit.EXOSUIT_ENERGY_REGENERATION_MK1, ItemInit.EXOSUIT_ENERGY_REGENERATION_MK2, ItemInit.EXOSUIT_ENERGY_REGENERATION_MK3), 1));
    }
}