// STREAMLABS //
__on_streamlabs_donation(player, actor, message, amount, formattedAmount, currency) -> (
    print('');
    print('__on_streamlabs_donation(player, actor, message, amount, formattedAmount, currency)');
    print('  - player: ' + player);
    print('  - actor: ' + actor);
    print('  - message: ' + message);
    print('  - amount: ' + amount);
    print('  - formattedAmount: ' + formattedAmount);
    print('  - currency: ' + currency)
);

// STREAMELEMENTS //
__on_streamelements_donation(player, actor, message, amount, formattedAmount, currency) -> (
    print('');
    print('__on_streamelements_donation(player, actor, message, amount, formattedAmount, currency)');
    print('  - player: ' + player);
    print('  - actor: ' + actor);
    print('  - message: ' + message);
    print('  - amount: ' + amount);
    print('  - formattedAmount: ' + formattedAmount);
    print('  - currency: ' + currency)
);

// TWITCH //
__on_twitch_follow(player, actor) -> (
    print('');
    print('__on_twitch_follow(player, actor)');
    print('  - player: ' + player);
    print('  - actor: ' + actor)
);

__on_twitch_subscription(player, actor, message, tier, months, resubbed, streak, gifted, gifter) -> (
    print('');
    print('__on_twitch_subscription(player, actor, message, tier, months, resubbed, streak, gifted, gifter)');
    print('  - player: ' + player);
    print('  - actor: ' + actor);
    print('  - message: ' + message);
    print('  - tier: ' + tier);
    print('  - months: ' + months);
    print('  - resubbed: ' + if(resubbed,'true','false'));
    print('  - streak: ' + streak);
    print('  - gifted: ' + if(gifted,'true','false'));
    print('  - gifter: ' + gifter)
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
);

__on_twitch_custom_reward(player, actor, message, badges, subscriptionMonths, customRewardId) -> (
    print('');
    print('__on_twitch_custom_reward(player, actor, message, badges, subscriptionMonths, customRewardId)');
    print('  - player: ' + player);
    print('  - actor: ' + actor);
    print('  - message: ' + message);
    print('  - badges: ' + badges);
    print('  - subscriptionMonths: ' + subscriptionMonths);
    print('  - customRewardId: ' + customRewardId)
);

// YOUTUBE //
__on_youtube_follow(player, actor) -> (
    print('');
    print('__on_youtube_follow(player, actor)');
    print('  - player: ' + player);
    print('  - actor: ' + actor)
);

__on_youtube_subscription(player, actor, months) -> (
    print('');
    print('__on_youtube_subscription(player, actor, months)');
    print('  - player: ' + player);
    print('  - actor: ' + actor);
    print('  - months: ' + months);
);

__on_youtube_superchat(player, actor, message, amount, formattedAmount, currency) -> (
    print('');
    print('__on_youtube_superchat(player, actor, message, amount, formattedAmount, currency)');
    print('  - player: ' + player);
    print('  - actor: ' + actor);
    print('  - message: ' + message);
    print('  - amount: ' + amount);
    print('  - formattedAmount: ' + formattedAmount);
    print('  - currency: ' + currency)
);
