package it.multicoredev.sti.scarpet;

import carpet.CarpetServer;
import carpet.script.CarpetEventServer.Event;
import carpet.script.value.*;
import it.multicoredev.sti.twitch.streamlabs.StreamlabsEvent;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ScarpetYouTubeEvents extends Event {
    public static ScarpetYouTubeEvents YOUTUBE_SUBSCRIPTION = new ScarpetYouTubeEvents("youtube_subscription", 3, false) {
        @Override
        public void onYouTubeEvent(String playerName, StreamlabsEvent event) {
            ServerPlayerEntity player = CarpetServer.minecraft_server.getPlayerManager().getPlayer(playerName);
            handler.call(
                    () -> Arrays.asList(
                            player != null ? new EntityValue(player) : new StringValue(playerName),
                            new StringValue(event.getNickname()),
                            new NumericValue(event.getSubscriptionMonths())
                    ),
                    () -> player != null ? player.getCommandSource() : CarpetServer.minecraft_server.getCommandSource()
            );
        }
    };

    public static ScarpetYouTubeEvents YOUTUBE_FOLLOW = new ScarpetYouTubeEvents("youtube_follow", 2, false) {
        @Override
        public void onYouTubeEvent(String playerName, StreamlabsEvent event) {
            ServerPlayerEntity player = CarpetServer.minecraft_server.getPlayerManager().getPlayer(playerName);
            handler.call(
                    () -> Arrays.asList(
                            player != null ? new EntityValue(player) : new StringValue(playerName),
                            new StringValue(event.getNickname())
                    ),
                    () -> player != null ? player.getCommandSource() : CarpetServer.minecraft_server.getCommandSource()
            );
        }
    };
    public static ScarpetYouTubeEvents YOUTUBE_SUPERCHAT = new ScarpetYouTubeEvents("youtube_superchat", 6, false) {
        @Override
        public void onYouTubeEvent(String playerName, StreamlabsEvent event) {
            ServerPlayerEntity player = CarpetServer.minecraft_server.getPlayerManager().getPlayer(playerName);
            handler.call(
                    () -> Arrays.asList(
                            player != null ? new EntityValue(player) : new StringValue(playerName),
                            new StringValue(event.getNickname()),
                            new StringValue(event.getMsg()),
                            new NumericValue(event.getDonationAmount()),
                            new StringValue(event.getFormattedAmount()),
                            new StringValue(event.getDonationCurrency())
                    ),
                    () -> player != null ? player.getCommandSource() : CarpetServer.minecraft_server.getCommandSource()
            );
        }
    };

    public ScarpetYouTubeEvents(String name, int reqArgs, boolean isGlobalOnly) {
        super(name, reqArgs, isGlobalOnly);
    }

    public void onYouTubeEvent(String playerName, StreamlabsEvent event) {
    }
    public static void noop() {}
}
