package it.multicoredev.sti.twitch.chat;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Copyright © 2020 by Anılcan Metinyurt
 * This file is part of TwitchSpawn.
 * TwitchSpawn is under "Eclipse Public License - v 1.0", you can find a copy <a href="https://www.eclipse.org/org/documents/epl-v10.html">here</a>.
 */

public class TwitchChatMessage {

    public static final Pattern TWITCH_CHAT_PATTERN = Pattern.compile("^@?(?<tags>.*?) ?(:(?<user>.*?)!.*?\\.tmi\\.twitch\\.tv) PRIVMSG #(?<channel>.*?) :(?<msg>.*)$");

    private String raw;

    public String username;
    public String message;
    public Set<String> badges; // admin, bits, broadcaster, global_mod, moderator, subscriber, staff, turbo, vip, glhf-pledge
    public int subscriptionMonths;

    public TwitchChatMessage(String raw) {
        this.raw = raw;
        this.badges = new HashSet<>();

        Matcher matcher = TWITCH_CHAT_PATTERN.matcher(raw);

        if (matcher.matches()) {
            Map<String, String> tags = parseTags(matcher.group("tags"));

            String displayName = tags.getOrDefault("display-name", "");
            this.username = displayName.isEmpty() ? matcher.group("user") : displayName;

            Stream.of(tags.getOrDefault("badges", "").split(",")).forEach(badgeRaw -> {
                if (badgeRaw.isEmpty()) return;
                String[] parts = badgeRaw.split("/", 2);
                String badgeName = parts[0];
                String badgeVersion = parts[1];
                badges.add(badgeName);
            });

            Stream.of(tags.getOrDefault("badge-info", "").split(",")).forEach(infoRaw -> {
                if (infoRaw.isEmpty()) return;
                String[] parts = infoRaw.split("/", 2);
                String infoName = parts[0];
                String infoValue = parts[1];
                if (infoName.equals("subscriber"))
                    subscriptionMonths = Integer.parseInt(infoValue);
            });

            this.message = matcher.group("msg");
        }
    }

    public static Map<String, String> parseTags(String tagsRaw) {
        Map<String, String> tags = new HashMap<>();

        for (String tagPairRaw : tagsRaw.split(";")) {
            String[] tagPair = tagPairRaw.split("=", 2);
            tags.put(tagPair[0], tagPair[1]);
        }

        return tags;
    }

    public static boolean matches(String raw) {
        return TWITCH_CHAT_PATTERN.matcher(raw).matches();
    }

}