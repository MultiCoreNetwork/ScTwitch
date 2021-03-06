package it.multicoredev.sti.scarpet;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import carpet.CarpetServer;
import carpet.script.CarpetEventServer.Event;
import carpet.script.value.EntityValue;
import carpet.script.value.ListValue;
import carpet.script.value.NumericValue;
import carpet.script.value.StringValue;
import it.multicoredev.sti.twitch.TwitchEvent;

public class ScarpetTwitchEvents extends Event {
    public static ScarpetTwitchEvents TWITCH_SUBSCRIPTION = new ScarpetTwitchEvents("twitch_subscription", 8, false) {
        @Override
        public void onTwitchEvent(String playerName, TwitchEvent event) {
            ServerPlayerEntity player = CarpetServer.minecraft_server.getPlayerManager().getPlayer(playerName);
            handler.call(
                    () -> Arrays.asList(
                            player != null ? new EntityValue(player) : new StringValue(playerName),
                            new StringValue(event.getNickname()),
                            new StringValue(event.getMsg()),
                            new NumericValue(event.getSubscriptionTier()),
                            new NumericValue(event.getSubscriptionMonths()),
                            new NumericValue(event.isResubbed()),
                            new NumericValue(event.getSubscriptionStreakMonths()),
                            new NumericValue(event.isGifted())
                    ),
                    () -> player != null ? player.getCommandSource() : CarpetServer.minecraft_server.getCommandSource()
            );
        }
    };
    public static ScarpetTwitchEvents TWITCH_DONATION = new ScarpetTwitchEvents("twitch_donation", 6, false) {
        @Override
        public void onTwitchEvent(String playerName, TwitchEvent event) {
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
    public static ScarpetTwitchEvents TWITCH_FOLLOW = new ScarpetTwitchEvents("twitch_follow", 2, false) {
        @Override
        public void onTwitchEvent(String playerName, TwitchEvent event) {
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
        public void onTwitchEvent(String playerName, TwitchEvent event) {
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
        public void onTwitchEvent(String playerName, TwitchEvent event) {
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
        public void onTwitchEvent(String playerName, TwitchEvent event) {
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
    public static ScarpetTwitchEvents TWITCH_SUBSCRIPTION_GIFT = new ScarpetTwitchEvents("twitch_subscription_gift", 4, false) {
        @Override
        public void onTwitchEvent(String playerName, TwitchEvent event) {
            ServerPlayerEntity player = CarpetServer.minecraft_server.getPlayerManager().getPlayer(playerName);
            handler.call(
                    () -> Arrays.asList(
                            player != null ? new EntityValue(player) : new StringValue(playerName),
                            new StringValue(event.getNickname()),
                            new NumericValue(event.getSubscriptionTier()),
                            new NumericValue(event.getDonationAmount())
                    ),
                    () -> player != null ? player.getCommandSource() : CarpetServer.minecraft_server.getCommandSource()
            );
        }
    };
    public static ScarpetTwitchEvents TWITCH_CHAT_MESSAGE = new ScarpetTwitchEvents("twitch_chat_message", 5, false) {
        @Override
        public void onTwitchEvent(String playerName, TwitchEvent event) {
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

        private Set<StringValue> getBadges(TwitchEvent event) {
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

    public void onTwitchEvent(String playerName, TwitchEvent event) {
    }
    public static void noop() {}
}
