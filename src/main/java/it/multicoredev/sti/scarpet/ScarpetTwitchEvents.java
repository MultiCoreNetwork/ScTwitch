package it.multicoredev.sti.scarpet;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Arrays;

import carpet.CarpetServer;
import carpet.script.CarpetEventServer.Event;
import carpet.script.value.NumericValue;
import carpet.script.value.StringValue;
import it.multicoredev.sti.twitch.streamlabs.TwitchEvent;

public class ScarpetTwitchEvents extends Event {

    public static ScarpetTwitchEvents TWITCH_SUBSCRIPTION = new ScarpetTwitchEvents("twitch_subscription", 8, false) {
        @Override
        public void onTwitchEvent(String playerName, TwitchEvent event) {
            handler.call(
                    () -> Arrays.asList(
                            ((c, t) -> new StringValue(playerName)),
                            ((c, t) -> new StringValue(event.getNickname())),
                            ((c, t) -> new StringValue(event.getMsg())),
                            ((c, t) -> new NumericValue(event.getSubscriptionTier())),
                            ((c, t) -> new NumericValue(event.getSubscriptionMonths())),
                            ((c, t) -> new NumericValue(event.isResubbed())),
                            ((c, t) -> new NumericValue(event.getSubscriptionStreakMonths())),
                            ((c, t) -> new NumericValue(event.isGifted()))
                    ),
                    () -> {
                        ServerPlayerEntity player = CarpetServer.minecraft_server.getPlayerManager().getPlayer(playerName);
                        if (player != null) {
                            return player.getCommandSource();
                        } else {
                            return CarpetServer.minecraft_server.getCommandSource();
                        }
                    }
            );
        }
    };
    public static ScarpetTwitchEvents TWITCH_DONATION = new ScarpetTwitchEvents("twitch_donation", 6, false) {
        @Override
        public void onTwitchEvent(String playerName, TwitchEvent event) {
            handler.call(
                    () -> Arrays.asList(
                            ((c, t) -> new StringValue(playerName)),
                            ((c, t) -> new StringValue(event.getNickname())),
                            ((c, t) -> new StringValue(event.getMsg())),
                            ((c, t) -> new NumericValue(event.getDonationAmount())),
                            ((c, t) -> new StringValue(event.getFormattedAmount())),
                            ((c, t) -> new StringValue(event.getDonationCurrency()))
                    ),
                    () -> {
                        ServerPlayerEntity player = CarpetServer.minecraft_server.getPlayerManager().getPlayer(playerName);
                        if (player != null) {
                            return player.getCommandSource();
                        } else {
                            return CarpetServer.minecraft_server.getCommandSource();
                        }
                    }
            );
        }
    };
    public static ScarpetTwitchEvents TWITCH_FOLLOW = new ScarpetTwitchEvents("twitch_follow", 2, false) {
        @Override
        public void onTwitchEvent(String playerName, TwitchEvent event) {
            handler.call(
                    () -> Arrays.asList(
                            ((c, t) -> new StringValue(playerName)),
                            ((c, t) -> new StringValue(event.getNickname()))
                    ),
                    () -> {
                        ServerPlayerEntity player = CarpetServer.minecraft_server.getPlayerManager().getPlayer(playerName);
                        if (player != null) {
                            return player.getCommandSource();
                        } else {
                            return CarpetServer.minecraft_server.getCommandSource();
                        }
                    }
            );
        }
    };
    public static ScarpetTwitchEvents TWITCH_BITS = new ScarpetTwitchEvents("twitch_bits", 4, false) {
        @Override
        public void onTwitchEvent(String playerName, TwitchEvent event) {
            handler.call(
                    () -> Arrays.asList(
                            ((c, t) -> new StringValue(playerName)),
                            ((c, t) -> new StringValue(event.getNickname())),
                            ((c, t) -> new StringValue(event.getMsg())),
                            ((c, t) -> new NumericValue(event.getDonationAmount()))
                    ),
                    () -> {
                        ServerPlayerEntity player = CarpetServer.minecraft_server.getPlayerManager().getPlayer(playerName);
                        if (player != null) {
                            return player.getCommandSource();
                        } else {
                            return CarpetServer.minecraft_server.getCommandSource();
                        }
                    }
            );
        }
    };
    public static ScarpetTwitchEvents TWITCH_RAID = new ScarpetTwitchEvents("twitch_raid", 3, false) {
        @Override
        public void onTwitchEvent(String playerName, TwitchEvent event) {
            handler.call(
                    () -> Arrays.asList(
                            ((c, t) -> new StringValue(playerName)),
                            ((c, t) -> new StringValue(event.getNickname())),
                            ((c, t) -> new NumericValue(event.getRaiderCount()))
                    ),
                    () -> {
                        ServerPlayerEntity player = CarpetServer.minecraft_server.getPlayerManager().getPlayer(playerName);
                        if (player != null) {
                            return player.getCommandSource();
                        } else {
                            return CarpetServer.minecraft_server.getCommandSource();
                        }
                    }
            );
        }
    };
    public static ScarpetTwitchEvents TWITCH_HOST = new ScarpetTwitchEvents("twitch_host", 3, false) {
        @Override
        public void onTwitchEvent(String playerName, TwitchEvent event) {
            handler.call(
                    () -> Arrays.asList(
                            ((c, t) -> new StringValue(playerName)),
                            ((c, t) -> new StringValue(event.getNickname())),
                            ((c, t) -> new NumericValue(event.getViewerCount()))
                    ),
                    () -> {
                        ServerPlayerEntity player = CarpetServer.minecraft_server.getPlayerManager().getPlayer(playerName);
                        if (player != null) {
                            return player.getCommandSource();
                        } else {
                            return CarpetServer.minecraft_server.getCommandSource();
                        }
                    }
            );
        }
    };
    public static ScarpetTwitchEvents TWITCH_SUBSCRIPTION_GIFT = new ScarpetTwitchEvents("twitch_subscription_gift", 3, false) {
        @Override
        public void onTwitchEvent(String playerName, TwitchEvent event) {
            handler.call(
                    () -> Arrays.asList(
                            ((c, t) -> new StringValue(playerName)),
                            ((c, t) -> new StringValue(event.getNickname())),
                            ((c, t) -> new NumericValue(event.getDonationAmount()))
                    ),
                    () -> {
                        ServerPlayerEntity player = CarpetServer.minecraft_server.getPlayerManager().getPlayer(playerName);
                        if (player != null) {
                            return player.getCommandSource();
                        } else {
                            return CarpetServer.minecraft_server.getCommandSource();
                        }
                    }
            );
        }
    };


    public ScarpetTwitchEvents(String name, int reqArgs, boolean isGlobalOnly) {
        super(name, reqArgs, isGlobalOnly);
    }

    public void onTwitchEvent(String playerName, TwitchEvent event) {
    }
}
