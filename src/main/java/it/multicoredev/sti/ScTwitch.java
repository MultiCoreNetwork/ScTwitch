package it.multicoredev.sti;

import carpet.CarpetExtension;
import carpet.CarpetServer;
import carpet.script.CarpetExpression;
import carpet.script.CarpetScriptServer;
import carpet.script.Module;
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
import java.util.*;

public class ScTwitch implements CarpetExtension, ModInitializer {
    public static final String MOD_ID = "sctwitch";
    public static final String MOD_NAME = "ScTwitch";
    public static final String MOD_VERSION = "1.4.69";
    public static final boolean DEBUG = true;

    static {
        CarpetServer.manageExtension(new ScTwitch());
        CarpetScriptServer.registerBuiltInApp(Module.fromJarPath("assets/sctwitch/scripts/","sctwitch_event_test", false));
        CarpetScriptServer.registerSettingsApp(Module.fromJarPath("assets/sctwitch/scripts/","twitchspawn", false));
    }

    public static List<StreamlabsSocket> streamlabsSockets = new ArrayList<>();
    public static List<StreamelementsSocket> streamelementsSockets = new ArrayList<>();
    public static List<TwitchChatSocket> twitchChatSockets = new ArrayList<>();


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
                streamelementsSockets.add(new StreamelementsSocket(s.STREAMELEMENTS_SECRET_TOKEN, new StreamelementsEventHandler(s.MINECRAFT_ACCOUNT)));
                streamlabsSockets.add(new StreamlabsSocket(s.STREAMLABS_SECRET_TOKEN, new StreamlabsEventHandler(s.MINECRAFT_ACCOUNT)));
                twitchChatSockets.add(new TwitchChatSocket(s.TWITCH_ACCOUNT, s.TWITCH_CHAT_TOKEN, new TwitchEventHandler(s.MINECRAFT_ACCOUNT)));
                System.out.println("Collegamento a " + s.TWITCH_ACCOUNT + " avvenuto con successo.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void startSockets() {
        streamelementsSockets.forEach(StreamelementsSocket::start);
        streamlabsSockets.forEach(StreamlabsSocket::start);
        twitchChatSockets.forEach(TwitchChatSocket::start);
    }

    public void stopSockets() {
        streamelementsSockets.forEach(StreamelementsSocket::stop);
        streamlabsSockets.forEach(StreamlabsSocket::stop);
        twitchChatSockets.forEach(TwitchChatSocket::stop);
    }

    @Override
    public void onServerLoadedWorlds(MinecraftServer server) {
        onReload(server);
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
