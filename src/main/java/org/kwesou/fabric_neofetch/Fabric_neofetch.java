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
    StringBuilder neofetchOutput;
    public static final Logger LOGGER = LoggerFactory.getLogger("org.kwesou.fabric_neofetch");

    public String executeNeoFetchCommand() {
        try {
            neofetchOutput = new StringBuilder();
            Process neofetchProcess = new ProcessBuilder("neofetch", "--stdout", "--disable", "packages", "shell", "theme", "icons").start();
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
        return neofetchOutput.toString();
    }

    @Override
    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(literal("neofetch")
                .executes(context -> {
                    context.getSource().sendFeedback(() -> Text.literal(executeNeoFetchCommand()), false);
                    return 1;
                })));
    }
}
