package it.multicoredev.sti.twitch.streamlabs;

import it.multicoredev.sti.scarpet.ScarpetTwitchEvents;

public class TwitchEventHandler {
    public final String nickname;

    public TwitchEventHandler(String nickname) {
        this.nickname = nickname;
    }

    public void onStreamlabsConnect() {
    }

    public void onStreamlabsDisconnect() {
    }

    public void handleTwitchEvent(TwitchEvent event) {
        if (event.getType().equals("follow")) {
            ScarpetTwitchEvents.TWITCH_FOLLOW.onTwitchEvent(nickname, event);
        } else if (event.getType().equals("subscription") || event.getType().equals("resub")) {
            ScarpetTwitchEvents.TWITCH_SUBSCRIPTION.onTwitchEvent(nickname, event);
        } else if (event.getType().equals("donation")) {
            ScarpetTwitchEvents.TWITCH_DONATION.onTwitchEvent(nickname, event);
        } else if (event.getType().equals("host")) {
            ScarpetTwitchEvents.TWITCH_HOST.onTwitchEvent(nickname, event);
        } else if (event.getType().equals("bits")) {
            ScarpetTwitchEvents.TWITCH_BITS.onTwitchEvent(nickname, event);
        } else if (event.getType().equals("raid")) {
            ScarpetTwitchEvents.TWITCH_RAID.onTwitchEvent(nickname, event);
        } else if (event.getType().equals("subMysteryGift")) {
            ScarpetTwitchEvents.TWITCH_SUBSCRIPTION_GIFT.onTwitchEvent(nickname, event);
        }
    }

}
