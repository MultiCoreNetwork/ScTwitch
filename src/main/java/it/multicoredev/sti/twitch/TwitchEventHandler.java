package it.multicoredev.sti.twitch;

import it.multicoredev.sti.scarpet.ScarpetTwitchEvents;
import it.multicoredev.sti.twitch.streamlabs.StreamlabsEvent;

public class TwitchEventHandler {
    public final String nickname;

    public TwitchEventHandler(String nickname) {
        this.nickname = nickname;
    }

    public void handleTwitchEvent(StreamlabsEvent event) {
        if (event.getType().equals("follow")) {
            ScarpetTwitchEvents.TWITCH_FOLLOW.onTwitchEvent(nickname, event);
        } else if (event.getType().equals("subscription")) {
            ScarpetTwitchEvents.TWITCH_SUBSCRIPTION.onTwitchEvent(nickname, event);
        } else if (event.getType().equals("host")) {
            ScarpetTwitchEvents.TWITCH_HOST.onTwitchEvent(nickname, event);
        } else if (event.getType().equals("bits")) {
            ScarpetTwitchEvents.TWITCH_BITS.onTwitchEvent(nickname, event);
        } else if (event.getType().equals("raid")) {
            ScarpetTwitchEvents.TWITCH_RAID.onTwitchEvent(nickname, event);
        } else if (event.getType().equals("chatMessage")) {
            ScarpetTwitchEvents.TWITCH_CHAT_MESSAGE.onTwitchEvent(nickname, event);
        }
    }

}
