package it.multicoredev.sti.twitch.streamlabs;

import it.multicoredev.sti.scarpet.ScarpetStreamlabsEvent;
import it.multicoredev.sti.scarpet.ScarpetYouTubeEvents;
import it.multicoredev.sti.twitch.TwitchEventHandler;
import it.multicoredev.sti.twitch.YouTubeEventHandler;
import it.multicoredev.sti.twitch.streamlabs.StreamlabsEvent;

import javax.swing.*;

public class StreamlabsEventHandler {
    public final String nickname;
    private TwitchEventHandler twitchEventHandler;
    private YouTubeEventHandler youTubeEventHandler;

    public StreamlabsEventHandler(String nickname) {
        this.nickname = nickname;
        twitchEventHandler = new TwitchEventHandler(nickname);
        youTubeEventHandler = new YouTubeEventHandler(nickname);
    }

    public void onStreamlabsConnect() {
    }

    public void onStreamlabsDisconnect() {
    }

    public void handleStramlabsEvent(StreamlabsEvent event) {
        if (event.getAccount().equals("twitch")) twitchEventHandler.handleTwitchEvent(event);
        if (event.getAccount().equals("youtube")) youTubeEventHandler.handleYouTubeEvent(event);
        if (event.getType().equals("donation")) {
            ScarpetStreamlabsEvent.STREAMLABS_DONATION.onStreamlabsEvent(nickname, event);
        }
    }

}
