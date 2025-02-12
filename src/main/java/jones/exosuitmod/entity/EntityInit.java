package jones.exosuitmod.entity;

import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;
import jones.exosuitmod.ExosuitMod;
import jones.exosuitmod.entity.render.RenderMessagerChicken;
import jones.exosuitmod.entity.render.RenderMorphDragon;
import jones.exosuitmod.item.ItemInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;

@Mod.EventBusSubscriber
public class EntityInit
{
	@SubscribeEvent
    public static void registerEntities(final RegistryEvent.Register<EntityEntry> event) 
    {
        registerEntity("morph_dragon", (Class<? extends Entity>)EntityMorphDragon.class, 847, 100, 1, false, 5582119, 13079892);
        registerEntity("messager_chicken", (Class<? extends Entity>)EntityMessagerChicken.class, 848, 100, 1, false, 5582119, 13079892);
        registerEntity("exosuit_explosive_egg", (Class<? extends Entity>)EntityExosuitExplosiveEgg.class, 849, 128, 10, true);
    }
    
    private static void registerEntity(final String name, final Class<? extends Entity> entity, final int id, final int range, int frequency, Boolean sendVelocity) 
    {
        EntityRegistry.registerModEntity(new ResourceLocation(ExosuitMod.MODID + ":" + name), entity, name, id, ExosuitMod.instance, range, frequency, sendVelocity);
    }

    private static void registerEntity(final String name, final Class<? extends Entity> entity, final int id, final int range, int frequency, Boolean sendVelocity, final int color1, final int color2) 
    {
        EntityRegistry.registerModEntity(new ResourceLocation(ExosuitMod.MODID + ":" + name), entity, name, id, ExosuitMod.instance, range, frequency, sendVelocity, color1, color2);
    }
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
	public static void registerEntityRenders(final ModelRegistryEvent event)
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityMorphDragon.class, new IRenderFactory<EntityMorphDragon>() 
        {
            public Render<? super EntityMorphDragon> createRenderFor(final RenderManager manager) 
            {
                return (Render<? super EntityMorphDragon>)new RenderMorphDragon(manager, 1.0f);
            }
        });

        RenderingRegistry.registerEntityRenderingHandler(EntityMessagerChicken.class, new IRenderFactory<EntityMessagerChicken>() 
        {
            public Render<? super EntityMessagerChicken> createRenderFor(final RenderManager manager) 
            {
                return (Render<? super EntityMessagerChicken>)new RenderMessagerChicken(manager);
            }
        });

        RenderingRegistry.registerEntityRenderingHandler(EntityExosuitExplosiveEgg.class, new IRenderFactory<EntityExosuitExplosiveEgg>() 
        {
            @SuppressWarnings({ "rawtypes", "unchecked" })
            public Render<? super EntityExosuitExplosiveEgg> createRenderFor(final RenderManager manager) 
            {
                return (Render<? super EntityExosuitExplosiveEgg>)new RenderSnowball(manager, ItemInit.EXOSUIT_EXPLOSIVE_EGG, Minecraft.getMinecraft().getRenderItem());
            }
        });
    }
}
