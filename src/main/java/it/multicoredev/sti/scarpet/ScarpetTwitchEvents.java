package it.multicoredev.sti.scarpet;

import carpet.script.value.*;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import carpet.CarpetServer;
import carpet.script.CarpetEventServer.Event;
import it.multicoredev.sti.twitch.streamlabs.StreamlabsEvent;

public class ScarpetTwitchEvents extends Event {
    public static ScarpetTwitchEvents TWITCH_SUBSCRIPTION = new ScarpetTwitchEvents("twitch_subscription", 9, false) {
        @Override
        public void onTwitchEvent(String playerName, StreamlabsEvent event) {
            ServerPlayerEntity player = CarpetServer.minecraft_server.getPlayerManager().getPlayer(playerName);
            handler.call(
                    () -> Arrays.asList(
                            player != null ? new EntityValue(player) : new StringValue(playerName),
                            new StringValue(event.getNickname()),
                            new StringValue(event.getMsg()),
                            new NumericValue(event.getSubscriptionTier()),
                            new NumericValue(event.getSubscriptionMonths()),
                            BooleanValue.of(event.isResubbed()),
                            new NumericValue(event.getSubscriptionStreakMonths()),
                            BooleanValue.of(event.isGifted()),
                            new StringValue(event.getGifter())
                    ),
                    () -> player != null ? player.getCommandSource() : CarpetServer.minecraft_server.getCommandSource()
            );
        }
    };

    public static ScarpetTwitchEvents TWITCH_FOLLOW = new ScarpetTwitchEvents("twitch_follow", 2, false) {
        @Override
        public void onTwitchEvent(String playerName, StreamlabsEvent event) {
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
    public static ScarpetTwitchEvents TWITCH_BITS = new ScarpetTwitchEvents("twitch_bits", 4, false) {
        @Override
        public void onTwitchEvent(String playerName, StreamlabsEvent event) {
            ServerPlayerEntity player = CarpetServer.minecraft_server.getPlayerManager().getPlayer(playerName);
            handler.call(
                    () -> Arrays.asList(
                            player != null ? new EntityValue(player) : new StringValue(playerName),
                            new StringValue(event.getNickname()),
                            new StringValue(event.getMsg()),
                            new NumericValue(event.getDonationAmount())
                    ),
                    () -> player != null ? player.getCommandSource() : CarpetServer.minecraft_server.getCommandSource()
            );
        }
    };
    public static ScarpetTwitchEvents TWITCH_RAID = new ScarpetTwitchEvents("twitch_raid", 3, false) {
        @Override
        public void onTwitchEvent(String playerName, StreamlabsEvent event) {
            ServerPlayerEntity player = CarpetServer.minecraft_server.getPlayerManager().getPlayer(playerName);
            handler.call(
                    () -> Arrays.asList(
                            player != null ? new EntityValue(player) : new StringValue(playerName),
                            new StringValue(event.getNickname()),
                            new NumericValue(event.getRaiderCount())
                    ),
                    () -> player != null ? player.getCommandSource() : CarpetServer.minecraft_server.getCommandSource()
            );
        }
    };
    public static ScarpetTwitchEvents TWITCH_HOST = new ScarpetTwitchEvents("twitch_host", 3, false) {
        @Override
        public void onTwitchEvent(String playerName, StreamlabsEvent event) {
            ServerPlayerEntity player = CarpetServer.minecraft_server.getPlayerManager().getPlayer(playerName);
            handler.call(
                    () -> Arrays.asList(
                            player != null ? new EntityValue(player) : new StringValue(playerName),
                            new StringValue(event.getNickname()),
                            new NumericValue(event.getViewerCount())
                    ),
                    () -> player != null ? player.getCommandSource() : CarpetServer.minecraft_server.getCommandSource()
            );
        }
    };
    public static ScarpetTwitchEvents TWITCH_CHAT_MESSAGE = new ScarpetTwitchEvents("twitch_chat_message", 5, false) {
        @Override
        public void onTwitchEvent(String playerName, StreamlabsEvent event) {
            ServerPlayerEntity player = CarpetServer.minecraft_server.getPlayerManager().getPlayer(playerName);
            handler.call(
                    () -> Arrays.asList(
                            player != null ? new EntityValue(player) : new StringValue(playerName),
                            new StringValue(event.getNickname()),
                            new StringValue(event.getMsg()),
                            new ListValue(getBadges(event)),
                            new NumericValue(event.getSubscriptionMonths())
                    ),
                    () -> player != null ? player.getCommandSource() : CarpetServer.minecraft_server.getCommandSource()
            );
        }

        private Set<StringValue> getBadges(StreamlabsEvent event) {
            Set<StringValue> badges = new HashSet<>();
            for (String b : event.getBadges()) {
                badges.add(new StringValue(b));
            }
            return badges;
        }
    };
    public static ScarpetTwitchEvents TWITCH_CUSTOM_REWARD = new ScarpetTwitchEvents("twitch_custom_reward", 6, false) {
        @Override
        public void onTwitchEvent(String playerName, StreamlabsEvent event) {
            ServerPlayerEntity player = CarpetServer.minecraft_server.getPlayerManager().getPlayer(playerName);
            handler.call(
                    () -> Arrays.asList(
                            player != null ? new EntityValue(player) : new StringValue(playerName),
                            new StringValue(event.getNickname()),
                            new StringValue(event.getMsg()),
                            new ListValue(getBadges(event)),
                            new NumericValue(event.getSubscriptionMonths()),
                            new StringValue(event.getCustomRewardId())
                    ),
                    () -> player != null ? player.getCommandSource() : CarpetServer.minecraft_server.getCommandSource()
            );
        }

        private Set<StringValue> getBadges(StreamlabsEvent event) {
            Set<StringValue> badges = new HashSet<>();
            for (String b : event.getBadges()) {
                badges.add(new StringValue(b));
            }
            return badges;
        }
    };

    public ScarpetTwitchEvents(String name, int reqArgs, boolean isGlobalOnly) {
        super(name, reqArgs, isGlobalOnly);
    }

    public void onTwitchEvent(String playerName, StreamlabsEvent event) {
    }
    public static void noop() {}
}
