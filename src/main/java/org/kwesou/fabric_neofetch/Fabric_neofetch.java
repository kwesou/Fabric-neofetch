package org.kwesou.fabric_neofetch;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.text.Text;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static net.minecraft.server.command.CommandManager.*;

public class Fabric_neofetch implements ModInitializer {
    StringBuilder neofetchOutput = new StringBuilder();
    Process neofetchProcess;
    public static final Logger LOGGER = LoggerFactory.getLogger("org.kwesou.fabric_neofetch");

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
            LOGGER.warn(exception.getMessage());
        }
        return String.valueOf(neofetchOutput);
    }

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("neofetch")
                .executes(context -> {
                    context.getSource().sendFeedback(() -> Text.literal(executeNeoFetchCommand()), false);
                    neofetchOutput.setLength(0);
                    return 1;
                })));
    }
}
