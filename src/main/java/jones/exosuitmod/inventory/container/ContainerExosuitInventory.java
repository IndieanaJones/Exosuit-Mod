package jones.exosuitmod.inventory.container;

import jones.exosuitmod.entity.AbstractExosuit;
import jones.exosuitmod.inventory.slot.RestrictedExosuitSlot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerExosuitInventory extends Container 
{
    public final AbstractExosuit exosuit;
    public int genericSlots = 0;

    public ContainerExosuitInventory(EntityPlayer player, AbstractExosuit mob) 
    {
        this.exosuit = mob;

        for (int i = 1; i <genericSlots; i++) 
        {
            this.addSlotToContainer(new Slot(exosuit.inventory, i, 80 + (i * 18), 18));
        }

        // Add player's inventory slots
        for (int row = 0; row < 3; row++) 
        {
            for (int col = 0; col < 9; col++) 
            {
                this.addSlotToContainer(new Slot(player.inventory, col + row * 9 + 9, 8 + col * 18, 126 + row * 18));
            }
        }

        // Add player's hotbar slots
        for (int i = 0; i < 9; i++) 
        {
            this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 184));
        }
    }

    public boolean canInteractWith(EntityPlayer playerIn) 
    {
        return this.exosuit.isEntityAlive() && this.exosuit.getDistance(playerIn) < 8.0F;
    }

    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) 
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) 
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < exosuit.inventory.getSizeInventory()) 
            {
                if (!this.mergeItemStack(itemstack1, exosuit.inventory.getSizeInventory(), this.inventorySlots.size(), true)) 
                {
                    return ItemStack.EMPTY;
                }
            } 
            else if (!this.mergeItemStack(itemstack1, 0, exosuit.inventory.getSizeInventory(), false)) 
            {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) 
            {
                slot.putStack(ItemStack.EMPTY);
            } 
            else 
            {
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }

    public boolean canMergeSlot(ItemStack stack, Slot slotIn)
    {
        if (slotIn instanceof RestrictedExosuitSlot) 
        {
            RestrictedExosuitSlot exosuitSlot = (RestrictedExosuitSlot) slotIn;
            // Check if the item matches the restriction for the slot
            return exosuitSlot.isItemValid(stack);
        }
        return super.canMergeSlot(stack, slotIn);
    }
}