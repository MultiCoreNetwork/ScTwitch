package it.multicoredev.sti;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.script.CarpetScriptServer;
import carpet.script.bundled.BundledModule;
import it.multicoredev.sti.twitch.streamlabs.StreamlabsSocket;
import it.multicoredev.sti.twitch.streamlabs.TwitchEventHandler;

public class ScTwitch implements CarpetExtension {
    public static final String MOD_ID = "sctwitch";
    public static final String MOD_NAME = "ScTwitch";
    public static final String MOD_VERSION = "1.0.0";

    private Map<String, StreamlabsSocket> sockets = new HashMap<>();

    static {
        CarpetServer.manageExtension(new ScTwitch());
        CarpetScriptServer.registerBuiltInScript(sctwitchDefaultScript("sctwitch_event_test", false));
    }

    public static BundledModule sctwitchDefaultScript(String scriptName, boolean isLibrary) {
        BundledModule module = new BundledModule(scriptName.toLowerCase(Locale.ROOT),null,false);
        try {
            module = new BundledModule(scriptName.toLowerCase(Locale.ROOT),
                    IOUtils.toString(
                            BundledModule.class.getClassLoader().getResourceAsStream("assets/sctwitch/scripts/" + scriptName + (isLibrary ? ".scl" : ".sc")),
                            StandardCharsets.UTF_8
                    ), isLibrary);
        } catch (NullPointerException | IOException e) { }
        return module;
    }

    public void createStreamlabsSockets() {
        try {
            sockets.clear();
            Path configDir = FabricLoader.getInstance().getConfigDir().normalize();
            Files.createDirectories(configDir);
            Path configFile = configDir.resolve("sctwitch.properties").normalize();
            System.out.println("File di config loaded from " + configFile);
            try {
                Files.createFile(configFile);
            } catch (FileAlreadyExistsException e) {
            }
            Properties properties = new Properties();
            properties.load(Files.newBufferedReader(configFile));

            for (String nickname : properties.stringPropertyNames()) {
                String token = properties.getProperty(nickname);
                sockets.put(nickname, new StreamlabsSocket(token, new TwitchEventHandler(nickname)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startStreamlabsSockets() {
        sockets.forEach((nick, socket) -> socket.start());
    }

    public void stopStreamlabsSockets() {
        sockets.forEach((nick, socket) -> socket.stop());
    }

    @Override
    public void onServerLoaded(MinecraftServer server) {
        createStreamlabsSockets();
        startStreamlabsSockets();
    }

    @Override
    public String version() {
        return MOD_ID;
    }

    public static void noop() {
    }

    @Override
    public void onReload(MinecraftServer server) {
        stopStreamlabsSockets();
        createStreamlabsSockets();
        startStreamlabsSockets();
    }

    @Override
    public void onServerClosed(MinecraftServer server) {
        stopStreamlabsSockets();
    }
}