# Handle

<br>

## Introduction

Handle is a Minecraft plugin suite written for the Bukkit API, and it contains a number of miscellaneous server tools.

I plan for it to be the project hub of the majority of my custom plugins. Many will end up being implemented on my small Minecraft server, [Vality](https://playvality.com).

Currently I'm only supporting Minecraft version 1.19 or higher.

<br>

## Installation

1. First, download or clone the repository:

```
$ git clone https://github.com/iamvalion/handle-bukkit.git
```

2. Build the JAR files using Maven.

3. Drop the JARs into your server's `plugins` folder, restart the server, and you're good to go!

<br>

## Roadmap / To-Do Items
_Note: These are in no particular order._

### HandleCore

* Clean up all code, and add (better) commenting to all files. Ensure that configurations are uniform too, and that all configs use the proper setting for copying defaults.
* Add a debugger, and replace the abundance of console logging messages with debugger messages that can be turned on or off. The debug mode will most likely be enabled/disabled by command and config option.
* The current way of mapping player usernames to UUIDs (`player-map.yml`) may be completely unnecessary. Find out if there's a better way to do this with either the Mojang or Spigot API.
* Add the ability to set other players as the target of actions, as opposed to the target player always having to be the one who triggered the action.
* Fix the issue where `MsgToConsoleAction` color codes don't work properly unless a plugin like Essentials is installed and handles the colors.
* Fix `MsgAsPlayerAction`, as it throws errors when called and may just need to be removed. The player gets kicked with the message "Illegal characters in chat".
* Tweak the `FilingCabinet` to allow for handling directories (and all files contained) instead of just individual files.

### HandleLocations

* The handling of capitalization of usernames in `player-map.yml` and with the `/loc` and `/locations` commands needs to be tightened up.

### HandleTriggers

* Fix the issue of some PlaceholderAPI placeholders not being set if an action using them is run before the PlaceholderAPI expansions are loaded. The PAPI expansions sometimes take several seconds after server start to load, so things like `JOIN` trigger actions sometimes don't have the placeholders set before the actions are run (e.g. a player joining immediately upon restart).
* Add `aliases`, `description`, and `allow-console` properties to `COMMAND` trigger types. In general, there needs to be handling of the console sending of commands so errors aren't being thrown.
* Ensure everything is being done properly to unregister `COMMAND` triggers during the unload stage of reloading triggers. Removed command triggers still show up when a user tab-completes.
* Add `MESSAGE` trigger type that triggers actions when a user's chat message matches or contains a specific pattern.
* Overhaul default configurations to show many more potential use cases.

<br>

## Contributing

I appreciate any contribution you wish to make, whether it is related to a bug or another type of suggestion! Pull requests, therefore, are welcome.

For major changes, please [open an issue](https://github.com/iamvalion/handle-bukkit/issues/new) first to discuss what you would like to change.

<br>

## License

Handle is licensed under the [GNU GPLv3 License](https://choosealicense.com/licenses/gpl-3.0).
