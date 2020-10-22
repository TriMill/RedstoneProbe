# RedstoneProbe

A Spigot/Paper plugin for analyzing redstone circuits. Users can set probes on redstone components and recieve chat messages when the component turns on or off.

## How to use
There are two ways to set or remove probes:

 - Right clicking on a block with the probe tool (use `/probe` to get one)
 - Using `/probe add <x> <y> <z>` or `/probe remove <x> <y> <z>`

Once a probe is set, you will recieve chat messages whenever the component turns on or off. Almost all redstone components will work, however solid blocks will not. You can hide those messages temporarily using `/probe hide` and show them again using `/probe show`, or use `/probe clear` to remove all of your probes.

The command `/probectl` is also available, designed for server admins. This can be used to clear the probes of other players (`/probectl clear <player>`) or dump a list of probes to console (`/probectl dump`). The storage of probes is optimised using hash maps and hash sets to reduce lag.

## Commands

| Command | Aliases | Usage |
| --- | --- | --- |
| `/probe` | `/pr` | Give yourself the probe item |
| `/probe clear` | `/pr cl` | Clear all probes |
| `/probe list` | `/pr ls` | List all probes |
| `/probe hide` | `/pr h` | Temporary hide probe messages |
| `/probe show` | `/pr s` | Stop hiding messages |11
| `/probe add <location>` | `/pr a <location>` | Set a probe at a location |
| `/probe remove <location>` | `/pr r <location>` | Remove a probe from a location |
| `/probe help` | `/pr ?` | Show help message |
| `/probectl clear` | `/prctl cl` | Clear probes for all players |
| `/probectl clear <player>` | `/prctl cl <player>` | Clear probes for a specific player |
| `/probectl dump` | `/prctl dump` | Dump all probe data to the server console |
| `/probectl help` | `/prctl ?` | Show help message |


## Planned additions
 - Refactoring and organization
 - Naming probes, delete by name

## Possible additions
 - Persistent particles
 - Signal strength
 - More timing tools
 - Further optimisations
