package jones.exosuitmod.block;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class BlockInit 
{
    public static final List<Block> BLOCKS = new ArrayList<Block>();

    public static final ExosuitModBlockBase MECHANICAL_SCRAP_BLOCK = new ExosuitModBlockBase("mechanical_scrap_block");

	@SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) 
    {
        event.getRegistry().registerAll(BlockInit.BLOCKS.toArray(new Block[0]));
    }

	@SubscribeEvent
    public static void registerItemModels(ModelRegistryEvent event) 
    {
        for(Block block : BlockInit.BLOCKS)
        {
            if(block instanceof ExosuitModBlockBase)
                ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
        }
    }
}
