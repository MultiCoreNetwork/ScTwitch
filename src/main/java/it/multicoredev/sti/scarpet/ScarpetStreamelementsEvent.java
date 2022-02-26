package it.multicoredev.sti.scarpet;

import carpet.CarpetServer;
import carpet.script.CarpetEventServer.Event;
import carpet.script.value.EntityValue;
import carpet.script.value.NumericValue;
import carpet.script.value.StringValue;
import it.multicoredev.sti.twitch.streamelements.StreamelementsEvent;
import it.multicoredev.sti.twitch.streamlabs.StreamlabsEvent;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Arrays;

public class ScarpetStreamelementsEvent extends Event {
    public static ScarpetStreamelementsEvent STREAMELEMENTS_DONATION = new ScarpetStreamelementsEvent("streamelements_donation", 6, false) {
        @Override
        public void onStreamelementsEvent(String playerName, StreamelementsEvent event) {
            ServerPlayerEntity player = CarpetServer.minecraft_server.getPlayerManager().getPlayer(playerName);
            handler.call(
                    () -> Arrays.asList(
                            player != null ? new EntityValue(player) : new StringValue(playerName),
                            new StringValue(event.getNickname()),
                            new StringValue(event.getMsg()),
                            new NumericValue(event.getDonationAmount()),
                            new StringValue(event.getFormattedAmount()),
                            new StringValue(event.getDonationCurrency())
                    ),
                    () -> player != null ? player.getCommandSource() : CarpetServer.minecraft_server.getCommandSource()
            );
        }
    };

    public ScarpetStreamelementsEvent(String name, int reqArgs, boolean isGlobalOnly) {
        super(name, reqArgs, isGlobalOnly);
    }

    public void onStreamelementsEvent(String playerName, StreamelementsEvent event) {
    }
    public static void noop() {}
}
