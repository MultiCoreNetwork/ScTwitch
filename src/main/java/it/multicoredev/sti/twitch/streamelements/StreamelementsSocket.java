package it.multicoredev.sti.twitch.streamelements;


import com.google.gson.JsonParser;
import io.socket.client.IO;
import io.socket.client.Socket;
import it.multicoredev.sti.ScTwitch;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.function.Consumer;

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

public class StreamelementsSocket {
    private static final String API = "https://realtime.streamelements.com";

    private Thread thread;
    private final String token;
    private Socket socket;
    private StreamelementsEventHandler handler;
    private boolean connected = false;

    public StreamelementsSocket(String token, StreamelementsEventHandler handler) throws IOException {
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
        try {
            JSONObject authArguments = new JSONObject();
            authArguments.put("method", "jwt");
            authArguments.put("token", token);
            socket.emit("authenticate", authArguments);

        } catch (JSONException ignored) {}

        socket.on("authenticated", foo -> {
            connected = true;
            for (Object o : foo) {
                System.out.println(o);
            }
        });
        handler.onStreamelementsConnect();
    }

    private void onDisconnect(Object... args) {
        connected = false;
        handler.onStreamelementsDisconnect();
    }

    private void onLiveEvent(Object... args) {
        for(Object arg : args) {
            if (ScTwitch.DEBUG) System.out.println(arg);
            JSONObject eventJson = (JSONObject) arg;
            if (!eventJson.has("data") || eventJson.optJSONObject("data") == null) return;

            String eventType = extractFrom(eventJson, "type", String.class, null);
            String eventAccount = extractFrom(eventJson, "provider", String.class, "streamelements");
            JSONObject eventData = extractFrom(eventJson, "data", JSONObject.class, new JSONObject());

            if (ScTwitch.DEBUG) System.out.println(eventType + ": " + eventData);
            StreamelementsEvent event = new StreamelementsEvent(eventType, eventAccount);

            event.setNickname(extractFrom(eventData, "username", String.class, null));

            event.setMsg(extractFrom(eventData, "message", String.class, null));

            int donationAmount = extractNumberFrom(eventData, "amount", 0.0).intValue();
            String donationCurrency = extractFrom(eventData, "currency", String.class, null);
            if(donationCurrency != null)
                event.setFormattedAmount(switch(donationCurrency) {
                    case "USD" -> String.format("$%d.%02d", donationAmount / 100, donationAmount % 100);
                    case "EUR" -> String.format("€%d.%02d", donationAmount / 100, donationAmount % 100);
                    default -> String.format("%s %d.%02d", donationCurrency, donationAmount / 100, donationAmount % 100);
                });
            event.setDonationAmount(donationAmount);
            event.setDonationCurrency(donationCurrency);

            try {
                handler.handleStramelementsEvent(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private IO.Options createOptions() {
        IO.Options options = new IO.Options();
        options.forceNew = true;
        options.reconnection = false;
        options.transports = new String[]{"websocket"};

        return options;
    }

    private JSONArray extractData(JSONObject event) {
        try {
            Object messageField = event.get("data");

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

    private boolean extractBooleanFrom(JSONObject json, String key, boolean defaultValue) {
        try {
            return json.getBoolean(key);
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
