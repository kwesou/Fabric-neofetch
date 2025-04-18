package org.kwesou.mcNeofetch;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class sendOutput implements CommandExecutor {
    StringBuilder neofetchOutput = new StringBuilder();
    Process neofetchProcess;

    public String executeNeoFetchCommand() {
        try {
            try {
                neofetchProcess = new ProcessBuilder(
                        "fastfetch",
                        "--pipe",
                        "--logo", "none",
                        "--structure", "title:separator:os:kernel:uptime:terminal:cpu:gpu:memory"
                ).start();
            } catch (IOException e) {
                try {
                    neofetchProcess = new ProcessBuilder(
                            "neofetch",
                            "--stdout",
                            "--disable", "packages", "shell", "theme", "icons"
                    ).start();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(neofetchProcess.getInputStream()));
            neofetchProcess.waitFor();
            String line;
            while ((line = reader.readLine()) != null) {
                neofetchOutput.append(line).append("\n");
            }
        } catch (InterruptedException | IOException exception) {
            neofetchOutput = new StringBuilder("neofetch failed");
        }
        return String.valueOf(neofetchOutput);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String @NotNull [] strings) {
        executeNeoFetchCommand();
        commandSender.sendMessage(String.valueOf(neofetchOutput));
        return false;
    }
}
