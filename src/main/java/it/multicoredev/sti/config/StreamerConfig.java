package it.multicoredev.sti.config;

import com.google.gson.annotations.SerializedName;

public class StreamerConfig {
    @SerializedName("twitch_account")
    public final String TWITCH_ACCOUNT;
    @SerializedName("minecraft_account")
    public final String MINECRAFT_ACCOUNT;
    @SerializedName("streamlabs_secret_token")
    public final String STREAMLABS_SECRET_TOKEN;
    @SerializedName("twitch_chat_token")
    public final String TWITCH_CHAT_TOKEN;

    public StreamerConfig(String twitch_account, String minecraft_account, String streamlabs_secret_token, String twitch_chat_token) {
        TWITCH_ACCOUNT = twitch_account;
        MINECRAFT_ACCOUNT = minecraft_account;
        STREAMLABS_SECRET_TOKEN = streamlabs_secret_token;
        TWITCH_CHAT_TOKEN = twitch_chat_token;
    }
}
