package com.example.clientmod;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(
    modid = ClientMod.MODID,
    name = ClientMod.NAME,
    version = ClientMod.VERSION,
    clientSideOnly = true,
    acceptedMinecraftVersions = "[1.12.2]"
)
public class ClientMod {

    public static final String MODID = "clientmod";
    public static final String NAME = "Client Utilities";
    public static final String VERSION = "1.0.0";

    @Mod.Instance(MODID)
    public static ClientMod instance;

    @SidedProxy(
        clientSide = "com.example.clientmod.ClientProxy",
        serverSide = "com.example.clientmod.CommonProxy"
    )
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }
}