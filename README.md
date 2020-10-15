# RedstoneProbe

A Spigot/Paper plugin for analyzing redstone circuits. Users can set probes on redstone components and recieve chat messages when the component turns on or off.

## How to use
There are two ways to set or remove probes:

 - Right clicking on a block with the probe tool (use `/probe` to get one)
 - Using `/probe add <x> <y> <z>` or `/probe remove <x> <y> <z>`

Once a probe is set, you will recieve chat messages whenever the component turns on or off. Almost all redstone components will work, however solid blocks will not. You can hide those messages temporarily using `/probe hide` and show them again using `/probe show`, or use `/probe clear` to remove all of your probes.

The command `/probectl` is also available, designed for server admins. This can be used to clear the probes of other players (`/probectl clear <player>`) or dump a list of probes to console (`/probectl dump`). The storage of probes is optimised using hash maps and hash sets to reduce lag.

## Planned additions
 - Implement permissions
 - Refactoring and organization
 - Naming probes, delete by name
 - List probes

## Possible additions
 - Persistent particles
 - Signal strength
 - Further optimisations
