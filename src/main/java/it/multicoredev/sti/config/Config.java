package it.multicoredev.sti.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

/**
 * Copyright © 2013 by Frank Roth
 * Java JSON-Config-File is a simple GSON based implementation to save Objects or primitive values like in a property .ini file.
 * You can find the original source <a href="https://github.com/frankred/json-config-file">here</a>.
 */

public class Config {

    // Hier schreibst du deine Attribute hin
    @SerializedName("streamers")
    public ArrayList<StreamerConfig> STREAMERS;

    public Config() {
        // Hier die Standardwerte falls das jeweiligen Attribut nicht in der
        // config.json enthalten ist.
        this.STREAMERS = new ArrayList<>();
        this.STREAMERS.add(
                new StreamerConfig(
                        "Your Twitch Name",
                        "Your YouTube Name",
                        "Your Minecraft Nickname",
                        "Your Streamlabs Socket API Token - https://streamlabs.com/dashboard#/settings/api-settings",
                        "Your Streamelements JWT Token - https://streamelements.com/dashboard/account/channels",
                        "Your Twitch Chat Token - https://twitchapps.com/tmi/")
        );
    }

    // DON'T TOUCH THE FOLLOWING CODE
    private static Config instance;

    public static Config getInstance() {
        if (instance == null) {
            instance = fromDefaults();
        }
        return instance;
    }

    public static void load(File file) {
        instance = fromFile(file);

        // no config file found
        if (instance == null) {
            instance = fromDefaults();
        }
    }

    public static void load(String file) {
        load(new File(file));
    }

    private static Config fromDefaults() {
        return new Config();
    }

    public void toFile(String file) {
        toFile(new File(file));
    }

    public void toFile(File file) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String jsonConfig = gson.toJson(this);
        FileWriter writer;
        try {
            writer = new FileWriter(file);
            writer.write(jsonConfig);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Config fromFile(File configFile) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(configFile)));
            return gson.fromJson(reader, Config.class);
        } catch (FileNotFoundException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }
}