package it.multicoredev.sti.twitch;

import it.multicoredev.sti.scarpet.ScarpetTwitchEvents;
import it.multicoredev.sti.twitch.streamlabs.StreamlabsEvent;

public record TwitchEventHandler(String nickname) {

    public void handleTwitchEvent(StreamlabsEvent event) {
        switch (event.getType()) {
            case "follow" -> ScarpetTwitchEvents.TWITCH_FOLLOW.onTwitchEvent(nickname, event);
            case "subscription" -> ScarpetTwitchEvents.TWITCH_SUBSCRIPTION.onTwitchEvent(nickname, event);
            case "host" -> ScarpetTwitchEvents.TWITCH_HOST.onTwitchEvent(nickname, event);
            case "bits" -> ScarpetTwitchEvents.TWITCH_BITS.onTwitchEvent(nickname, event);
            case "raid" -> ScarpetTwitchEvents.TWITCH_RAID.onTwitchEvent(nickname, event);
            case "chatMessage" -> ScarpetTwitchEvents.TWITCH_CHAT_MESSAGE.onTwitchEvent(nickname, event);
            case "customReward" -> ScarpetTwitchEvents.TWITCH_CUSTOM_REWARD.onTwitchEvent(nickname, event);
        }
    }

}
