package it.multicoredev.sti;

import carpet.settings.Rule;

import static carpet.settings.RuleCategory.EXPERIMENTAL;

public class ScTwitchSettings {
    @Rule(
            desc = "Ogni volta che un utente lascerà un sub/follow, ti verrà dato un albero.",
            appSource = "sapling",
            category = {EXPERIMENTAL}
    )
    public static boolean sctwitchSapling = false;
    @Rule(
            desc = "Ogni volta che un utente lascerà un follow, verrà evocato un golem di neve.",
            appSource = "snowman",
            category = {EXPERIMENTAL}
    )
    public static boolean sctwitchSnowman = false;
}
