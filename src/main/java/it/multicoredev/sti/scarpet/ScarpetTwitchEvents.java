package it.multicoredev.sti.scarpet;

import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import carpet.CarpetServer;
import carpet.script.CarpetEventServer.Event;
import carpet.script.value.ListValue;
import carpet.script.value.NumericValue;
import carpet.script.value.StringValue;
import it.multicoredev.sti.twitch.TwitchEvent;

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
    public static ScarpetTwitchEvents TWITCH_SUBSCRIPTION_GIFT = new ScarpetTwitchEvents("twitch_subscription_gift", 4, false) {
        @Override
        public void onTwitchEvent(String playerName, TwitchEvent event) {
            handler.call(
                    () -> Arrays.asList(
                            ((c, t) -> new StringValue(playerName)),
                            ((c, t) -> new StringValue(event.getNickname())),
                            ((c, t) -> new NumericValue(event.getSubscriptionTier())),
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
    public static ScarpetTwitchEvents TWITCH_CHAT_MESSAGE = new ScarpetTwitchEvents("twitch_chat_message", 5, false) {
        @Override
        public void onTwitchEvent(String playerName, TwitchEvent event) {
            handler.call(
                    () -> Arrays.asList(
                            ((c, t) -> new StringValue(playerName)),
                            ((c, t) -> new StringValue(event.getNickname())),
                            ((c, t) -> new StringValue(event.getMsg())),
                            ((c, t) -> new ListValue(getBadges(event))),
                            ((c, t) -> new NumericValue(event.getSubscriptionMonths()))
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
}
