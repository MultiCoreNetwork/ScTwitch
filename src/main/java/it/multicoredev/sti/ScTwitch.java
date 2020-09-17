package it.multicoredev.sti;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.MinecraftServer;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.script.CarpetScriptServer;
import carpet.script.bundled.BundledModule;
import it.multicoredev.sti.config.Config;
import it.multicoredev.sti.config.StreamerConfig;
import it.multicoredev.sti.twitch.TwitchEventHandler;
import it.multicoredev.sti.twitch.chat.TwitchChatSocket;
import it.multicoredev.sti.twitch.streamlabs.StreamlabsSocket;

public class ScTwitch implements CarpetExtension {
    public static final String MOD_ID = "sctwitch";
    public static final String MOD_NAME = "ScTwitch";
    public static final String MOD_VERSION = "1.0.1";

    private Map<String, StreamlabsSocket> streamlabsSockets = new HashMap<>();
    private Map<String, TwitchChatSocket> twitchChatSockets = new HashMap<>();

    static {
        CarpetServer.manageExtension(new ScTwitch());
        CarpetScriptServer.registerBuiltInScript(sctwitchDefaultScript("sctwitch_event_test", false));
    }

    public static BundledModule sctwitchDefaultScript(String scriptName, boolean isLibrary) {
        BundledModule module = new BundledModule(scriptName.toLowerCase(Locale.ROOT), null, false);
        try {
            module = new BundledModule(scriptName.toLowerCase(Locale.ROOT),
                    IOUtils.toString(
                            BundledModule.class.getClassLoader().getResourceAsStream("assets/sctwitch/scripts/" + scriptName + (isLibrary ? ".scl" : ".sc")),
                            StandardCharsets.UTF_8
                    ), isLibrary);
        } catch (NullPointerException | IOException e) {
        }
        return module;
    }

    public void createSockets() {
        try {
            streamlabsSockets.clear();
            Path configDir = FabricLoader.getInstance().getConfigDir().normalize();
            Files.createDirectories(configDir);
            Path configFile = configDir.resolve("sctwitch.json").normalize();
            System.out.println("File di config loaded from " + configFile);
            Config.load(configFile.toFile());
            for (StreamerConfig s : Config.getInstance().STREAMERS) {
                streamlabsSockets.put(s.TWITCH_ACCOUNT, new StreamlabsSocket(s.STREAMLABS_SECRET_TOKEN, new TwitchEventHandler(s.MINECRAFT_ACCOUNT)));
                twitchChatSockets.put(s.TWITCH_ACCOUNT, new TwitchChatSocket(s.TWITCH_ACCOUNT, s.TWITCH_CHAT_TOKEN, new TwitchEventHandler(s.MINECRAFT_ACCOUNT)));
                System.out.println("Collegamento a " + s.TWITCH_ACCOUNT + " avvenuto con successo.");
            }
            Config.getInstance().toFile(configFile.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startSockets() {
        streamlabsSockets.forEach((streamer, socket) -> socket.start());
        twitchChatSockets.forEach((streamer, socket) -> socket.start());
    }

    public void stopSockets() {
        streamlabsSockets.forEach((streamer, socket) -> socket.stop());
        twitchChatSockets.forEach((streamer, socket) -> socket.stop());
    }

    @Override
    public void onServerLoaded(MinecraftServer server) {
        createSockets();
        startSockets();
    }

    public static void noop() {
    }

    @Override
    public void onReload(MinecraftServer server) {
        stopSockets();
        createSockets();
        startSockets();
    }

    @Override
    public void onServerClosed(MinecraftServer server) {
        stopSockets();
    }
}