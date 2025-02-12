package jones.exosuitmod.network;

import jones.exosuitmod.network.packets.PacketChickenFlapClient;
import jones.exosuitmod.network.packets.PacketSendClick;
import jones.exosuitmod.network.packets.PacketSendExosuitCooldown;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketInit
{
    public static final SimpleNetworkWrapper PACKET_HANDLER_INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel("exosuitmod");
    
    public static void registerMessages(FMLPreInitializationEvent event) 
    {
        PACKET_HANDLER_INSTANCE.registerMessage(PacketSendClick.SendClickHandler.class, PacketSendClick.class, 0, Side.SERVER);
        PACKET_HANDLER_INSTANCE.registerMessage(PacketSendExosuitCooldown.SendExosuitCooldownHandler.class, PacketSendExosuitCooldown.class, 1, Side.CLIENT);
        PACKET_HANDLER_INSTANCE.registerMessage(PacketChickenFlapClient.ChickenFlapClientHandler.class, PacketChickenFlapClient.class, 2, Side.SERVER);
    }
}
