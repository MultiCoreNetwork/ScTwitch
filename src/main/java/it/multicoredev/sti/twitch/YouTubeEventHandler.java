package it.multicoredev.sti.twitch;

import it.multicoredev.sti.scarpet.ScarpetYouTubeEvents;
import it.multicoredev.sti.twitch.streamlabs.StreamlabsEvent;

public class YouTubeEventHandler {
    public final String nickname;

    public YouTubeEventHandler(String nickname) {
        this.nickname = nickname;
    }

    public void handleYouTubeEvent(StreamlabsEvent event) {
        if (event.getType().equals("follow")) {
            ScarpetYouTubeEvents.YOUTUBE_FOLLOW.onYouTubeEvent(nickname, event);
        } else if (event.getType().equals("subscription")) {
            ScarpetYouTubeEvents.YOUTUBE_SUBSCRIPTION.onYouTubeEvent(nickname, event);
        } else if (event.getType().equals("superchat")) {
            ScarpetYouTubeEvents.YOUTUBE_SUPERCHAT.onYouTubeEvent(nickname, event);
        }
    }

}
