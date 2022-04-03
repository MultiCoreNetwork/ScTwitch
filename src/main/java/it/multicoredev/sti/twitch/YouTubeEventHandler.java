package it.multicoredev.sti.twitch;

import it.multicoredev.sti.scarpet.ScarpetYouTubeEvents;
import it.multicoredev.sti.twitch.streamlabs.StreamlabsEvent;

public record YouTubeEventHandler(String nickname) {

    public void handleYouTubeEvent(StreamlabsEvent event) {
        switch (event.getType()) {
            case "follow" -> ScarpetYouTubeEvents.YOUTUBE_FOLLOW.onYouTubeEvent(nickname, event);
            case "subscription" -> ScarpetYouTubeEvents.YOUTUBE_SUBSCRIPTION.onYouTubeEvent(nickname, event);
            case "superchat" -> ScarpetYouTubeEvents.YOUTUBE_SUPERCHAT.onYouTubeEvent(nickname, event);
        }
    }

}
