__on_twitch_follow(player, actor) -> (
    print('');
    print('__on_twitch_follow(player, actor)');
    print('  - player: ' + player);
    print('  - actor: ' + actor)
);

__on_twitch_subscription(player, actor, message, tier, months, resubbed, streak, gifted) -> (
    print('');
    print('__on_twitch_subscription(player, actor, message, tier, months, resubbed, streak, gifted)');
    print('  - player: ' + player);
    print('  - actor: ' + actor);
    print('  - message: ' + message);
    print('  - tier: ' + tier);
    print('  - months: ' + months);
    print('  - resubbed: ' + if(resubbed,'true','false'));
    print('  - streak: ' + streak);
    print('  - gifted: ' + if(gifted,'true','false'))
);

__on_twitch_subscription_gift(player, actor, tier, amount) -> (
    print('');
    print('__on_twitch_subscription_gift(player, actor, message, amount)');
    print('  - player: ' + player);
    print('  - actor: ' + actor);
    print('  - tier: ' + tier);
    print('  - amount: ' + amount)
);

__on_twitch_donation(player, actor, message, amount, formattedAmount, currency) -> (
    print('');
    print('__on_twitch_donation(player, actor, message, amount, formattedAmount, currency)');
    print('  - player: ' + player);
    print('  - actor: ' + actor);
    print('  - message: ' + message);
    print('  - amount: ' + amount);
    print('  - formattedAmount: ' + formattedAmount);
    print('  - currency: ' + currency)
);

__on_twitch_bits(player, actor, message, amount) -> (
    print('');
    print('__on_twitch_bits(player, actor, message, amount)');
    print('  - player: ' + player);
    print('  - actor: ' + actor);
    print('  - message: ' + message);
    print('  - amount: ' + amount)
);

__on_twitch_raid(player, actor, viewers) -> (
    print('');
    print('__on_twitch_raid(player, actor, viewers)');
    print('  - player: ' + player);
    print('  - actor: ' + actor);
    print('  - viewers: ' + viewers)
);

__on_twitch_host(player, actor, viewers) -> (
    print('');
    print('__on_twitch_host(player, actor, viewers)');
    print('  - player: ' + player);
    print('  - actor: ' + actor);
    print('  - viewers: ' + viewers)
);

__on_twitch_chat_message(player, actor, message, badges, subscriptionMonths) -> (
    print('');
    print('__on_twitch_chat_message(player, actor, message, badges, subscriptionMonths)');
    print('  - player: ' + player);
    print('  - actor: ' + actor);
    print('  - message: ' + message);
    print('  - badges: ' + badges);
    print('  - subscriptionMonths: ' + subscriptionMonths)
)
