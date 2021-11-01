package it.multicoredev.sti.twitch.chat;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.regex.Pattern;

import it.multicoredev.sti.ScTwitch;
import it.multicoredev.sti.twitch.streamlabs.StreamlabsEvent;
import it.multicoredev.sti.twitch.TwitchEventHandler;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Copyright © 2020 by Lorenzo Magni
 * This file is part of ScTwitch.
 * MultiCoreNetworkWeb is under "The 3-Clause BSD License", you can find a copy <a href="https://opensource.org/licenses/BSD-3-Clause">here</a>.
 * <p>
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * <p>
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING,
 * BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */
public class TwitchChatSocket {
    private static final Pattern USER_PATTERN = Pattern.compile("(?<=^:)\\w+(?=!)");
    private static final String MSG_PATTERN = "^:\\w+!\\w+@\\w+\\.tmi\\.twitch\\.tv PRIVMSG #\\w+ :";
    private static final String SOCKET_ADDRESS = "irc.chat.twitch.tv";
    private static final int SOCKET_PORT = 6667;

    public String getChannel() {
        return channel;
    }

    private final String channel;

    private TwitchEventHandler handler;

    private String botName;
    private String botOauth;
    private Thread thread;
    private Socket socket;
    private BufferedReader in;
    private DataOutputStream out;
    private boolean connected = false;

    public TwitchChatSocket(String channel, String token, TwitchEventHandler handler) {
        this.channel = channel;

        if (!token.equals("")) {
            this.botName = channel;
            this.botOauth = token;
        } else {
            this.botName = "justinfan421";
            this.botOauth = null;
        }

        this.handler = handler;
    }

    public void start() {
        thread = new Thread(this::connect);
        thread.start();
    }

    public void stop() {
        connected = false;
        //System.out.println("IRC Disconnect");

        try {
            if (socket != null) socket.close();
        } catch (IOException ignored) {
        }
        if (thread == null) return;
        if (!thread.isInterrupted()) thread.interrupt();
        thread = null;
    }

    public boolean isConnected() {
        return connected;
    }

    public void sendMessage(String msg) {
        try {
            out.write(("PRIVMSG #" + channel.toLowerCase() + " :" + msg + "\n\r").getBytes(UTF_8));
        } catch (IOException ignored) {
        }
    }

    private void connect() {
        try {
            //System.out.println("&3Connecting to " + channel + " chat...");

            socket = new Socket(SOCKET_ADDRESS, SOCKET_PORT);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), UTF_8));
            out = new DataOutputStream(socket.getOutputStream());


            if (botOauth != null) out.write(("PASS " + botOauth + "\n\r").getBytes(UTF_8));
            out.write(("NICK " + botName + "\n\r").getBytes(UTF_8));
            if (botOauth != null) out.write(("USER " + botName + " \n\r").getBytes(UTF_8));
            out.write("CAP REQ : twitch.tv/commands twitch.tv/membership twitch.tv/tags \n\r".getBytes(UTF_8));
            out.write(String.format("JOIN #%s\n\r", channel.toLowerCase()).getBytes(UTF_8));

            connected = true;
            System.out.println("Connected to " + channel + " chat...");

            String response;
            while ((response = in.readLine()) != null) {
                processResponse(response);
            }
        } catch (Exception ignored) {
        } finally {
            System.out.println("&3Disconnected from " + channel + " chat...");
            stop();
        }
    }

    private void processResponse(String response) {
        //System.out.println(response);
        if (response.equals("PING :tmi.twitch.tv")) {
            try {
                out.write("PONG :tmi.twitch.tv\n\r".getBytes(UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            if (TwitchCustomReward.matchesCustomReward(response)) {
                if (ScTwitch.DEBUG) System.out.println("customReward: " + response);
                TwitchCustomReward twitchCustomReward = new TwitchCustomReward(response);
                StreamlabsEvent event = new StreamlabsEvent("customReward", channel);
                event.setMsg(twitchCustomReward.message);
                event.setNickname(twitchCustomReward.username);
                event.setBadges(twitchCustomReward.badges);
                event.setCustomRewardId(twitchCustomReward.customRewardId);
                event.setSubscriptionMonths(twitchCustomReward.subscriptionMonths);
                handler.handleTwitchEvent(event);
            } // else -> Esclusive "customReward" event
            if (TwitchChatMessage.matchesMessage(response)) {
                if (ScTwitch.DEBUG) System.out.println("chatMessage: " + response);
                TwitchChatMessage twitchChatMessage = new TwitchChatMessage(response);
                StreamlabsEvent event = new StreamlabsEvent("chatMessage", channel);
                event.setMsg(twitchChatMessage.message);
                event.setNickname(twitchChatMessage.username);
                event.setBadges(twitchChatMessage.badges);
                event.setSubscriptionMonths(twitchChatMessage.subscriptionMonths);
                handler.handleTwitchEvent(event);
            }
        }
    }
}