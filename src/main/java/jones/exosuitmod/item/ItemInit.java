package jones.exosuitmod.item;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ItemInit 
{
    public static final List<Item> ITEMS = new ArrayList<Item>();

    public static final ExosuitModItemBase EXOSUIT_EGG_UPGRADE_MK1 = new ExosuitModItemBase("exosuit_egg_upgrade_mk1");

	@SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) 
    {
        event.getRegistry().registerAll(ItemInit.ITEMS.toArray(new Item[0]));
    }

	@SubscribeEvent
    public static void registerItemModels(ModelRegistryEvent event) 
    {
        for(Item item : ItemInit.ITEMS)
        {
            if(item instanceof ExosuitModItemBase)
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), ((ExosuitModItemBase)item).variantIn));
        }
    }
}
