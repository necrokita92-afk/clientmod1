package com.example.clientmod;

import com.example.clientmod.config.ConfigManager;
import com.example.clientmod.event.EventManager;
import com.example.clientmod.modules.ModuleManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        ModuleManager.init();
        ConfigManager.load();
    }

    @Override
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(new EventManager());
    }
}