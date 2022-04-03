# ScTwitch
Twitch Integration for Gnembon's Scarpet

## Installation
- Install [Fabric](https://fabricmc.net/use/)
- Download [Carpet Mod](https://github.com/gnembon/carpet-mod/releases)
- Download [ScTwitch](https://github.com/multicorenetwork/ScTwitch/releases)

## Configuration
- Add the following to your `sctwitch.json` file:
    ```json
    {
      "streamers": [
        {
          "twitch_account": "Your Twitch Name",
          "youtube_account": "Your YouTube Name",
          "minecraft_account": "Your Minecraft Nickname",
          "streamlabs_secret_token": "Your Streamlabs Socket API Token",
          "streamelements_secret_token": "Your Streamelements JWT Token",
          "twitch_chat_token": "Your Twitch Chat Token"
        }
      ]
    }
    ```
- You can get your Streamlabs `Soket API Token` by creating an account at [Streamlabs](https://streamlabs.com/) and visiting the [API settings](https://streamlabs.com/dashboard#/settings/api-settings) page
- You can get your Streamelements `JWT Token` by creating an account at [Streamelements](https://streamelements.com/) and visiting the [Account](https://streamelements.com/dashboard/account/channels) page
- You can get your Twitch Chat Token by logging into your Twitch account and visiting the following URL:
    https://twitchapps.com/tmi/
  
## Scarpet events
### STREAMLABS
#### `__on_streamlabs_donation(player, actor, message, amount, formattedAmount, currency)`
When a StreamLabs donation is made

### STREAMELEMENTS
#### `__on_streamelements_donation(player, actor, message, amount, formattedAmount, currency)`
When a StreamElements donation is made

### TWITCH
#### `__on_twitch_follow(player, actor, message)`
When an user start following you on Twitch 
#### `__on_twitch_subscription(player, actor, message, tier, months, resubbed, streak, gifted, gifter)`
When an user start subscribing to you on Twitch
#### `__on_twitch_host(player, actor, message)`
When an user start hosting you on Twitch
#### `__on_twitch_bits(player, actor, message, amount)`
When an user gives you bits on Twitch
#### `__on_twitch_raid(player, actor, message, viewers)`
When an user start raiding you on Twitch
#### `__on_twitch_chat_message(player, actor, message, badges, subscriptionMonths)` 
When an user sends a message in your chat on Twitch
#### `__on_twitch_custom_reward(player, actor, message, badges, subscriptionMonths, customRewardId)`
When an user gives you a custom reward on Twitch

### YOUTUBE
#### `__on_youtube_follow(player, actor)`
When an user start following you on YouTube
#### `__on_youtube_subscription(player, actor, months)`
When an user subscribe to your YouTube channel
#### `__on_youtube_superchat(player, actor, message, amount, formattedAmount, currency)`
When an user donate you on YouTube with Superchat

