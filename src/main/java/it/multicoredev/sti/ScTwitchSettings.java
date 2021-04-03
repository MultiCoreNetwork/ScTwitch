package it.multicoredev.sti;

import carpet.settings.Rule;

import static carpet.settings.RuleCategory.EXPERIMENTAL;

public class ScTwitchSettings {
    @Rule(
            desc = "Ogni volta che un utente lascerà un sub/follow, ti verrà dato un albero.",
            appSource = "twitch_spawn",
            category = {EXPERIMENTAL}
    )
    public static boolean twitch_spawn = false;
}
