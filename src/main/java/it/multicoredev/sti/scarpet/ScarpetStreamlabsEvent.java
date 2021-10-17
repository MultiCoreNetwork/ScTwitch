package it.multicoredev.sti.scarpet;

import carpet.CarpetServer;
import carpet.script.CarpetEventServer.Event;
import carpet.script.value.*;
import it.multicoredev.sti.twitch.streamlabs.StreamlabsEvent;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ScarpetStreamlabsEvent extends Event {
    public static ScarpetStreamlabsEvent STREAMLABS_DONATION = new ScarpetStreamlabsEvent("streamlabs_donation", 6, false) {
        @Override
        public void onStreamlabsEvent(String playerName, StreamlabsEvent event) {
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

    public ScarpetStreamlabsEvent(String name, int reqArgs, boolean isGlobalOnly) {
        super(name, reqArgs, isGlobalOnly);
    }

    public void onStreamlabsEvent(String playerName, StreamlabsEvent event) {
    }
    public static void noop() {}
}
