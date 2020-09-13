package it.multicoredev.sti.twitch.streamlabs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.function.Consumer;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Copyright © 2020 by Lorenzo Magni
 * This file is part of ScTwitch.
 * MCLib is under "The 3-Clause BSD License", you can find a copy <a href="https://opensource.org/licenses/BSD-3-Clause">here</a>.
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

public class StreamlabsSocket {
    private static final String API = "https://sockets.streamlabs.com";

    private Thread thread;
    private final String token;
    private Socket socket;
    private TwitchEventHandler handler;
    private boolean connected = false;

    public StreamlabsSocket(String token, TwitchEventHandler handler) throws IOException {
        this.token = token;
        this.handler = handler;

        IO.Options options = createOptions();

        try {
            socket = IO.socket(API, options);

            socket.on(Socket.EVENT_CONNECT, this::onConnect);
            socket.on(Socket.EVENT_DISCONNECT, this::onDisconnect);
            socket.on("event", this::onLiveEvent);
        } catch (URISyntaxException e) {
            throw new IOException(e.getMessage(), e);
        }
    }

    public void start() {
        thread = new Thread(() -> socket.connect());
        thread.setUncaughtExceptionHandler((t, e) -> {
        });
        thread.start();
    }

    public void stop() {
        try {
            try {
                socket.disconnect();
            } catch (Exception ignored) {
            }

            socket = null;
            thread.interrupt();
            thread = null;
            if (connected) connected = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        return connected;
    }

    private void onConnect(Object... args) {
        connected = true;
        handler.onStreamlabsConnect();
    }

    private void onDisconnect(Object... args) {
        connected = false;
        handler.onStreamlabsDisconnect();
    }

    private void onLiveEvent(Object... args) {
        JsonObject eventJson = (JsonObject) args[0];
        JsonArray msgs = extractMessages(eventJson);
        if (msgs == null) return;

        String eventType = extractFrom(eventJson, "type", String.class, null);
        String eventFor = extractFrom(eventJson, "for", String.class, "streamlabs");
        String eventAccount = eventFor.replace("_account", "");

        forEach(msgs, message -> {
            System.out.println(eventType + ": " + message);
            TwitchEvent event = new TwitchEvent(eventType, eventAccount);
            event.setNickname(extractFrom(message, "name", String.class, null));
            event.setMsg(extractFrom(message, "message", String.class, null));
            event.setDonationAmount(extractNumberFrom(message, "amount", 0.0).doubleValue());
            event.setFormattedAmount(extractFrom(message, "formatted_amount", String.class, null));
            event.setDonationCurrency(extractFrom(message, "currency", String.class, null));
            event.setSubscriptionMonths(extractNumberFrom(message, "months", 0).intValue());
            event.setRaiderCount(extractNumberFrom(message, "raiders", 0).intValue());
            event.setViewerCount(extractNumberFrom(message, "viewers", 0).intValue());
            event.setSubscriptionTier(extractTier(message, "sub_plan"));
            event.setSubscriptionStreakMonths(extractNumberFrom(message, "streak_months", 0).intValue());
            event.setGifted(extractFrom(message, "gifter_twitch_id", String.class, null) != null);
            event.setResubbed(eventType.equals("resub"));


            try {
                handler.handleTwitchEvent(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private IO.Options createOptions() {
        IO.Options options = new IO.Options();
        options.forceNew = true;
        options.reconnection = false;
        options.transports = new String[]{"websocket"};
        options.query = "token=" + token;

        return options;
    }

    private JsonArray extractMessages(JsonObject event) {
        JsonElement messageField = event.get("message");

        if (messageField instanceof JsonArray)
            return extractFrom(event, "message", JsonArray.class, new JsonArray());
        else if (messageField instanceof JsonObject)  {
            JsonArray r = new JsonArray();
            r.add(messageField);
            return r;
        }
        return null;
    }

    private int extractTier(JsonObject message, String tierFieldName) {
        String tierString = extractFrom(message, tierFieldName, String.class, null);

        if (tierString == null) return -1;
        if (tierString.equalsIgnoreCase("Prime")) return 0; //Prime
        if (tierString.equalsIgnoreCase("1000")) return 1;
        if (tierString.equalsIgnoreCase("2000")) return 2;
        if (tierString.equalsIgnoreCase("3000")) return 3;
        return -1;
    }

    private Number extractNumberFrom(JsonObject json, String key, Number defaultValue) {
        Object obj;

        try {
            obj = json.get(key);
            if (obj instanceof String) return Double.parseDouble((String) obj);
            return (Number) obj;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private <T> T extractFrom(JsonObject json, String key, Class<T> type, T defaultValue) {
        try {
            Object obj = json.get(key);
            return type.cast(obj);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private void forEach(JsonArray array, Consumer<JsonObject> consumer) {
        for (int i = 0; i < array.size(); i++) {
            JsonObject json = array.get(i).getAsJsonObject();
            consumer.accept(json);
        }
    }
}
