__on_twitch_follow(playerNick, actor) -> (
    print('');
    print('__on_twitch_follow(playerNick, actor)');
    print('  - playerNick: '+playerNick);
	print('  - actor: '+actor)
);

__on_twitch_subscription(playerNick, actor, message, tier, months, resubbed, streak, gifted) -> (
    print('');
    print('__on_twitch_subscription(playerNick, actor, message, tier, months, resubbed, streak, gifted)');
    print('  - playerNick: '+playerNick);
	print('  - actor: '+actor);
	print('  - message: '+message);
	print('  - tier: '+tier);
	print('  - months: '+months);
	print('  - resubbed: '+if(resubbed,'true','false'));
	print('  - streak: '+streak);
    print('  - gifted: '+if(gifted,'true','false'))
);

__on_twitch_subscription(playerNick, actor, message, tier, months, resubbed, streak, gifted) -> (
    print('');
    print('__on_twitch_subscription(playerNick, actor, message, tier, months, resubbed, streak, gifted)');
    print('  - playerNick: '+playerNick);
	print('  - actor: '+actor);
	print('  - message: '+message);
	print('  - tier: '+tier);
	print('  - months: '+months);
	print('  - resubbed: '+if(resubbed,'true','false'));
	print('  - streak: '+streak);
    print('  - gifted: '+if(gifted,'true','false'))
);

__on_twitch_donation(playerNick, actor, message, amount, formattedAmount, currency) -> (
    print('');
    print('__on_twitch_donation(playerNick, actor, message, amount, formattedAmount, currency)');
    print('  - playerNick: '+playerNick);
	print('  - actor: '+actor);
	print('  - message: '+message);
	print('  - amount: '+amount);
	print('  - formattedAmount: '+formattedAmount);
	print('  - currency: '+currency)
);

__on_twitch_bits(playerNick, actor, message, amount) -> (
    print('');
    print('__on_twitch_bits(playerNick, actor, message, amount)');
    print('  - playerNick: '+playerNick);
	print('  - actor: '+actor);
	print('  - message: '+message);
	print('  - amount: '+amount)
);

__on_twitch_raid(playerNick, actor, viewers) -> (
    print('');
    print('__on_twitch_raid(playerNick, actor, viewers)');
    print('  - playerNick: '+playerNick);
	print('  - actor: '+actor);
	print('  - viewers: '+viewers)
);

__on_twitch_host(playerNick, actor, viewers) -> (
    print('');
    print('__on_twitch_raid(playerNick, actor, viewers)');
    print('  - playerNick: '+playerNick);
	print('  - actor: '+actor);
	print('  - viewers: '+viewers)
)
