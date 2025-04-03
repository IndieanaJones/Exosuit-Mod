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
import jones.exosuitmod.entity.render.RenderPatriotExosuit;
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
        registerEntity("messager_chicken", (Class<? extends Entity>)EntityMessagerChicken.class, 848, 128, 1, false, 5582119, 13079892);
        registerEntity("exosuit_explosive_egg", (Class<? extends Entity>)EntityExosuitExplosiveEgg.class, 849, 128, 10, true);
        registerEntity("patriot_exosuit", (Class<? extends Entity>)EntityPatriotExosuit.class, 850, 128, 1, false, 5582119, 13079892);
        registerEntity("patriot_bullet", (Class<? extends Entity>)EntityPatriotBullet.class, 851, 128, 10, true);
        registerEntity("patriot_rocket", (Class<? extends Entity>)EntityPatriotRocket.class, 852, 128, 10, true);
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

        RenderingRegistry.registerEntityRenderingHandler(EntityPatriotExosuit.class, new IRenderFactory<EntityPatriotExosuit>() 
        {
            public Render<? super EntityPatriotExosuit> createRenderFor(final RenderManager manager) 
            {
                return (Render<? super EntityPatriotExosuit>)new RenderPatriotExosuit(manager);
            }
        });

        RenderingRegistry.registerEntityRenderingHandler(EntityPatriotBullet.class, new IRenderFactory<EntityPatriotBullet>() 
        {
            @SuppressWarnings({ "rawtypes", "unchecked" })
            public Render<? super EntityPatriotBullet> createRenderFor(final RenderManager manager) 
            {
                return (Render<? super EntityPatriotBullet>)new RenderSnowball(manager, ItemInit.PATRIOT_BULLET_ITEM, Minecraft.getMinecraft().getRenderItem());
            }
        });

        RenderingRegistry.registerEntityRenderingHandler(EntityPatriotRocket.class, new IRenderFactory<EntityPatriotRocket>() 
        {
            @SuppressWarnings({ "rawtypes", "unchecked" })
            public Render<? super EntityPatriotRocket> createRenderFor(final RenderManager manager) 
            {
                return (Render<? super EntityPatriotRocket>)new RenderSnowball(manager, ItemInit.PATRIOT_ROCKET_ITEM, Minecraft.getMinecraft().getRenderItem());
            }
        });
    }
}
