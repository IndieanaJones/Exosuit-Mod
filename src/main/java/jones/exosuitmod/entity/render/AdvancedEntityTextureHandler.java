package jones.exosuitmod.entity.render;

import jones.exosuitmod.entity.AbstractExosuit;

import com.google.common.collect.Maps;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class AdvancedEntityTextureHandler implements AutoCloseable
{
    public static AdvancedEntityTextureHandler INSTANCE = new AdvancedEntityTextureHandler();
    private final Map<Integer, AdvancedEntityTextureHandler.Instance> loadedEntities = Maps.newHashMap();

    public ResourceLocation getTextureLocation(AbstractExosuit entity)
    {
        return this.getEntityInstance(entity).getResourceLocation();
    }

    private AdvancedEntityTextureHandler.Instance getEntityInstance(AbstractExosuit entity) 
    {
        AdvancedEntityTextureHandler.Instance wantedInstance = this.loadedEntities.get(entity.getEntityId());
       if (wantedInstance == null) 
       {
            wantedInstance = new AdvancedEntityTextureHandler.Instance(entity);
            this.loadedEntities.put(entity.getEntityId(), wantedInstance);
       }
       return wantedInstance;
    }

    public void updateExosuitTexture(AbstractExosuit entity)
    {
        this.getEntityInstance(entity).updateEntityTexture();
    }

    public void close() 
    {
        this.loadedEntities.clear();
    }

    @SideOnly(Side.CLIENT)
    class Instance
    {
        private final int entityId;
        private DynamicTexture entityTexture;
        private ResourceLocation location;

        private Instance(AbstractExosuit exosuit)
        {
            this.entityId = exosuit.getEntityId();
            this.updateEntityTexture();
        }

        public void updateEntityTexture()
        {
            AbstractExosuit entity = (AbstractExosuit)Minecraft.getMinecraft().world.getEntityByID(this.entityId);
            BufferedImage baseImage = this.loadTextureFile(0);
            BufferedImage output = baseImage;

            if (entity.getTotalTextureLayers() > 1)
            {
                for (int i = 1; i < entity.getTotalTextureLayers(); i++)
                {
                    BufferedImage layer = loadTextureFile(i);
                    if(layer != null)
                        output = this.combineImages(output, layer);
                }
            }
            this.entityTexture = new DynamicTexture(output);
            this.location = Minecraft.getMinecraft().getTextureManager().getDynamicTextureLocation("exosuitmod/entity/" + entity.getExosuitIdentifier(), this.entityTexture);
        }

        public DynamicTexture getEntityTexture()
        {
            return this.entityTexture;
        }

	   /**
	    * Loads a texture from a specified file (path defined based off layer id in species class).
	    */
        private BufferedImage loadTextureFile(int id)
        {
            AbstractExosuit entity = (AbstractExosuit)Minecraft.getMinecraft().world.getEntityByID(this.entityId);
		 	BufferedImage img = new BufferedImage(entity.getTextureLength(), entity.getTextureHeight(), BufferedImage.TYPE_INT_ARGB);

            ResourceLocation filelocation = entity.getTextureResource(id);
            if(filelocation == null)
                return null;
            InputStream inputstream = null;
            try 
            {
                inputstream = Minecraft.getMinecraft().getResourceManager().getResource(filelocation).getInputStream();
            } 
            catch (IOException e1) 
            {
                e1.printStackTrace();
            }
            if (inputstream != null)
            {
                try 
                {
                    img = ImageIO.read(inputstream);
                } catch (IOException e) 
                {
                    e.printStackTrace();
                }
            }
			return img;
        }

	   /**
	    * Combines the two inputted images, with the overlay on top
	    */
        private BufferedImage combineImages(BufferedImage base, BufferedImage overlay)
        {
            AbstractExosuit entity = (AbstractExosuit)Minecraft.getMinecraft().world.getEntityByID(this.entityId);
            BufferedImage combined = new BufferedImage(entity.getTextureLength(), entity.getTextureHeight(), BufferedImage.TYPE_INT_ARGB);

            Graphics2D g = combined.createGraphics();
            g.drawImage(base, 0, 0, null);
            g.setComposite(AlphaComposite.SrcAtop);
            g.drawImage(overlay, 0, 0, null);
            g.dispose();
            return combined;
        }
	   
	   public ResourceLocation getResourceLocation()
	   {
		   return this.location;
	   }
    }
}
