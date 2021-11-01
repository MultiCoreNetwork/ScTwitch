package it.multicoredev.sti.scarpet;

import carpet.script.CarpetContext;
import carpet.script.Expression;
import carpet.script.LazyValue;
import carpet.script.exception.InternalExpressionException;
import carpet.script.value.NumericValue;
import carpet.script.value.Value;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import it.multicoredev.sti.ScTwitch;
import it.multicoredev.sti.config.Config;
import it.multicoredev.sti.config.StreamerConfig;
import it.multicoredev.sti.twitch.chat.TwitchChatSocket;
import net.minecraft.entity.player.PlayerEntity;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class ScarpetTwitchFunctions {
    public static void apply(Expression expression) {
        // twitch_send_message(message)
        // twitch_send_message(message, channel)
        expression.addLazyFunction("twitch_send_message", -1, (c, t, lv) -> {
            if (lv.size() > 2 || lv.size() < 1) throw new InternalExpressionException("'twitch_send_message' requires one or two parameters");
            CarpetContext cc = (CarpetContext) c;
            List<Value> params = lv.stream().map(a -> a.evalValue(cc)).collect(toList());
            String message = params.get(0).getString();
            List<StreamerConfig> channels;
            if(lv.size() == 2){
                channels = Config.getInstance().STREAMERS.stream().filter(
                        streamerConfig -> streamerConfig.TWITCH_ACCOUNT.equals(params.get(1).getString())
                ).collect(toList());
            } else {
                try {
                    PlayerEntity player = cc.s.getPlayer();
                    channels = Config.getInstance().STREAMERS.stream().filter(
                            streamerConfig -> streamerConfig.MINECRAFT_ACCOUNT.equals(player.getEntityName())
                    ).collect(toList());
                } catch (CommandSyntaxException e) {
                    throw new InternalExpressionException("'twitch_send_message' requires a linked channel as second parameter");
                }
            }
            channels.forEach(
                    streamerConfig -> {
                        try{
                            TwitchChatSocket tcs = ScTwitch.twitchChatSockets.get(streamerConfig.STREAMLABS_ACCOUNT);
                            tcs.sendMessage(message);
                        } catch (Exception e){
                            if(ScTwitch.DEBUG) e.printStackTrace();
                        }
                    }
            );

            return (ccc, tt) -> new NumericValue(channels.size());
        });
    }

}
