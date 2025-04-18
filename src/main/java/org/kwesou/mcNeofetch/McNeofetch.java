package org.kwesou.mcNeofetch;

import com.mojang.brigadier.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import org.kwesou.mcNeofetch.sendOutput;

public final class McNeofetch extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("neofetch").setExecutor(new sendOutput());
    }
}

