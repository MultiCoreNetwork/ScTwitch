package it.multicoredev.sti;

import carpet.settings.Rule;

import static carpet.settings.RuleCategory.EXPERIMENTAL;

public class ScTwitchSettings {
    @Rule(
            desc = "Adds twitchspawn file compatibility.",
            appSource = "twitchspawn",
            category = {EXPERIMENTAL}
    )
    public static boolean twitchSpawn = false;
}
