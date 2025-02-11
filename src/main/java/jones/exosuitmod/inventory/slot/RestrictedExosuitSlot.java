package jones.exosuitmod.inventory.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RestrictedExosuitSlot extends Slot 
{
    private final Item itemRestriction;
    private final int itemLimit;
    
    public RestrictedExosuitSlot(IInventory inventoryIn, int index, int xPosition, int yPosition, Item item, int limit) 
    {
        super(inventoryIn, index, xPosition, yPosition);
        this.itemRestriction = item;
        this.itemLimit = limit;
    }

    public boolean isItemValid(ItemStack stack) 
    {
        return stack.getItem() == itemRestriction;
    }

    public int getSlotStackLimit()
    {
        return itemLimit;
    }
}
