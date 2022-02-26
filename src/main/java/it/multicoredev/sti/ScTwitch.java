package it.multicoredev.sti;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.script.CarpetExpression;
import carpet.script.CarpetScriptServer;
import carpet.script.bundled.BundledModule;
import it.multicoredev.sti.config.Config;
import it.multicoredev.sti.config.StreamerConfig;
import it.multicoredev.sti.scarpet.ScarpetTwitchEvents;
import it.multicoredev.sti.scarpet.ScarpetTwitchFunctions;
import it.multicoredev.sti.scarpet.ScarpetYouTubeEvents;
import it.multicoredev.sti.twitch.TwitchEventHandler;
import it.multicoredev.sti.twitch.chat.TwitchChatSocket;
import it.multicoredev.sti.twitch.streamelements.StreamelementsEventHandler;
import it.multicoredev.sti.twitch.streamelements.StreamelementsSocket;
import it.multicoredev.sti.twitch.streamlabs.StreamlabsEventHandler;
import it.multicoredev.sti.twitch.streamlabs.StreamlabsSocket;
import net.fabricmc.api.ModInitializer;
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

public class ScTwitch implements CarpetExtension, ModInitializer {
    public static final String MOD_ID = "sctwitch";
    public static final String MOD_NAME = "ScTwitch";
    public static final String MOD_VERSION = "1.4.57";
    public static final boolean DEBUG = true;

    static {
        CarpetServer.manageExtension(new ScTwitch());
        CarpetScriptServer.registerBuiltInScript(sctwitchDefaultScript("sctwitch_event_test", false));
        CarpetScriptServer.registerBuiltInScript(sctwitchDefaultScript("chat_message_event_test", false));
        CarpetScriptServer.registerSettingsApp(sctwitchDefaultScript("sapling", false));
        CarpetScriptServer.registerSettingsApp(sctwitchDefaultScript("snowman", false));
    }

    public static Map<String, StreamlabsSocket> streamlabsSockets = new HashMap<>();
    public static Map<String, StreamelementsSocket> streamelementsSockets = new HashMap<>();
    public static Map<String, TwitchChatSocket> twitchChatSockets = new HashMap<>();

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
            streamelementsSockets.clear();
            streamlabsSockets.clear();
            twitchChatSockets.clear();
            Path configDir = FabricLoader.getInstance().getConfigDir().normalize();
            Files.createDirectories(configDir);
            Path configFile = configDir.resolve("sctwitch.json").normalize();
            Config.load(configFile.toFile());
            System.out.println("File di config loaded from " + configFile);
            Config.getInstance().toFile(configFile.toFile());
            for (StreamerConfig s : Config.getInstance().STREAMERS) {
                streamelementsSockets.put(s.STREAMLABS_ACCOUNT, new StreamelementsSocket(s.STREAMELEMENTS_SECRET_TOKEN, new StreamelementsEventHandler(s.MINECRAFT_ACCOUNT)));
                streamlabsSockets.put(s.STREAMLABS_ACCOUNT, new StreamlabsSocket(s.STREAMLABS_SECRET_TOKEN, new StreamlabsEventHandler(s.MINECRAFT_ACCOUNT)));
                twitchChatSockets.put(s.STREAMLABS_ACCOUNT, new TwitchChatSocket(s.TWITCH_ACCOUNT, s.TWITCH_CHAT_TOKEN, new TwitchEventHandler(s.MINECRAFT_ACCOUNT)));
                System.out.println("Collegamento a " + s.STREAMLABS_ACCOUNT + " avvenuto con successo.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startSockets() {
        streamelementsSockets.forEach((streamer, socket) -> socket.start());
        streamlabsSockets.forEach((streamer, socket) -> socket.start());
        twitchChatSockets.forEach((streamer, socket) -> socket.start());
    }

    public void stopSockets() {
        streamelementsSockets.forEach((streamer, socket) -> socket.stop());
        streamlabsSockets.forEach((streamer, socket) -> socket.stop());
        twitchChatSockets.forEach((streamer, socket) -> socket.stop());
    }

    @Override
    public void onGameStarted() {
        createSockets();
        startSockets();
        CarpetServer.settingsManager.parseSettingsClass(ScTwitchSettings.class);
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

    @Override
    public void onInitialize() {
        ScarpetTwitchEvents.noop();
        ScarpetYouTubeEvents.noop();
    }

    @Override
    public void scarpetApi(CarpetExpression expression) {
        ScarpetTwitchFunctions.apply(expression.getExpr());
    }
}
