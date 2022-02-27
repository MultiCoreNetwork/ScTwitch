package it.multicoredev.sti.twitch.streamelements;

import it.multicoredev.sti.scarpet.ScarpetStreamelementsEvent;
import it.multicoredev.sti.scarpet.ScarpetStreamelementsEvent;
import it.multicoredev.sti.twitch.TwitchEventHandler;
import it.multicoredev.sti.twitch.YouTubeEventHandler;
import it.multicoredev.sti.twitch.streamelements.StreamelementsEvent;

public class StreamelementsEventHandler {
    public final String nickname;

    public StreamelementsEventHandler(String nickname) {
        this.nickname = nickname;
    }

    public void onStreamelementsConnect() {
    }

    public void onStreamelementsDisconnect() {
    }

    public void handleStramelementsEvent(StreamelementsEvent event) {
        if (event.getType().equals("tip")) {
            ScarpetStreamelementsEvent.STREAMELEMENTS_DONATION.onStreamelementsEvent(nickname, event);
        }
    }

}
