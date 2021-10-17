package it.multicoredev.sti.twitch.streamlabs;


import it.multicoredev.sti.ScTwitch;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.function.Consumer;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Copyright © 2020 by Lorenzo Magni
 * This file is part of MCLib.
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
    private StreamlabsEventHandler handler;
    private boolean connected = false;

    public StreamlabsSocket(String token, StreamlabsEventHandler handler) throws IOException {
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
        JSONObject eventJson = (JSONObject) args[0];
        JSONArray msgs = extractMessages(eventJson);
        if (msgs == null) return;

        String eventType = extractFrom(eventJson, "type", String.class, null);
        String eventFor = extractFrom(eventJson, "for", String.class, "streamlabs");
        String eventAccount = eventFor.replace("_account", "");

        forEach(msgs, message -> {
            if(ScTwitch.DEBUG) System.out.println(eventType + ": " + message);
            StreamlabsEvent event = new StreamlabsEvent(eventType, eventAccount);

            String nickname = extractFrom(message, "display_name", String.class, null);
            if(nickname != null) event.setNickname(nickname);
            else event.setNickname(extractFrom(message, "name", String.class, null));

            String comment = extractFrom(message, "comment", String.class, null);
            if(comment != null) event.setMsg(comment);
            else event.setMsg(extractFrom(message, "message", String.class, null));

            String formattedAmount = extractFrom(message, "displayString", String.class, null);
            if(formattedAmount != null) event.setFormattedAmount(formattedAmount);
            else event.setFormattedAmount(extractFrom(message, "formatted_amount", String.class, null));

            String gifter = extractFrom(message, "gifter_display_name", String.class, null);
            if(gifter != null) event.setGifter(gifter);
            else event.setGifter(extractFrom(message, "gifter", String.class, null));

            event.setDonationAmount(extractNumberFrom(message, "amount", 0.0).doubleValue());
            event.setDonationCurrency(extractFrom(message, "currency", String.class, null));
            event.setSubscriptionMonths(extractNumberFrom(message, "months", 0).intValue());
            event.setRaiderCount(extractNumberFrom(message, "raiders", 0).intValue());
            event.setViewerCount(extractNumberFrom(message, "viewers", 0).intValue());
            event.setSubscriptionTier(extractTier(message, "sub_plan"));
            event.setSubscriptionStreakMonths(extractNumberFrom(message, "streak_months", 0).intValue());
            event.setGifted(extractFrom(message, "gifter", String.class, null) != null);
            event.setResubbed(extractFrom(message, "repeat", Boolean.class, null));

            try {
                handler.handleStramlabsEvent(event);
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

    private JSONArray extractMessages(JSONObject event) {
        try {
            Object messageField = event.get("message");

            if (messageField instanceof JSONArray)
                return extractFrom(event, "message", JSONArray.class, new JSONArray());
            else if (messageField instanceof JSONObject) return new JSONArray().put(messageField);
            return null;
        } catch (JSONException e) {
            return null;
        }
    }

    private int extractTier(JSONObject message, String tierFieldName) {
        String tierString = extractFrom(message, tierFieldName, String.class, null);

        if (tierString == null) return -1;
        if (tierString.equalsIgnoreCase("Prime")) return 0; //Prime
        if (tierString.equalsIgnoreCase("1000")) return 1;
        if (tierString.equalsIgnoreCase("2000")) return 2;
        if (tierString.equalsIgnoreCase("3000")) return 3;
        return -1;
    }

    private Number extractNumberFrom(JSONObject json, String key, Number defaultValue) {
        Object obj;

        try {
            obj = json.get(key);
            if (obj instanceof String) return Double.parseDouble((String) obj);
            return (Number) obj;
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private <T> T extractFrom(JSONObject json, String key, Class<T> type, T defaultValue) {
        try {
            Object obj = json.get(key);
            return type.cast(obj);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    private void forEach(JSONArray array, Consumer<JSONObject> consumer) {
        for (int i = 0; i < array.length(); i++) {
            try {
                JSONObject json = array.getJSONObject(i);
                consumer.accept(json);
            } catch (JSONException e) {
                throw new InternalError("Error performing JSONArray forEachMessage.");
            }
        }
    }
}
