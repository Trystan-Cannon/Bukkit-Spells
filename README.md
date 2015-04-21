# Bukkit-Spells
A bukkit plugin which introduces spells into the game. Players can learn and wield spells for PVP, survival, or entertainment purposes!

# Installation
For now, all you have to do is download the plugin from the "target" folder and drop it into your plugins folder.

All profiles are stored within the plugin's data folder. Each profile is a text file whose name is the unique id of the player to whom it belongs. The contents of the profile is simply the list of all of the spell names that the player knows. Therefore, their profile can be edited outside of the game.

# Planned Features
- Disguise spells: Spells that make use of the ProtocolLib dependency to manipulate outgoing packets to players, causing clients to see "disguised" players/creatures as other entities.
- Make wands craftable, probably.
- Make current spell effects listable by each affected player.

# Commands
- /spells -- The base command for the plugin. Simply using this gives you the usage of all commands available.
- /spells reload -- Reloads all of the wizard profiles and reports the time it took to do so.
- /spells book [spell name] -- Gives the player the learnable spell book for the spell with the given name. Nothing is given if the spell provided does not exist.
- /spells wands -- Gives the player all available wands.
- /spells list -- Lists all of the registered spells in the version of the plugin being used.
- /spells fixfly -- Resets the fly speed for the player if they are in creative or spectator mode. Because the plugin manipulates fly speed and permissions for players in order to achieve particular spell effects, reloads and crashes can cause the chagnes to be persistent even when they should have disappeared. Therefore, if a player experiences the inability to fly when they should, they should use this command.

# In Game Use
- Players are given enchanted books with have a custom display name and lore, identifying the book as a spell book. The name is of the format: "Spell Book: [spell name]." The lore contains the description of the spell's effects.
- With a spell book in hand, the player may right click, teaching them the spell and removing the spell book from their inventory. If they already know the spell for the given book, they are simply reminded of this and nothing else happens.
- Once a player knows a spell, they can access an inventory GUI by right clicking on their wand. Wands are distributed by server operators for right now.
- In the Known Spells GUI, the player can view and select any of the spells they currently know. Upon clicking the book for the knonw spell in the Known Spells GUI, the inventory will close, and they will receive a message informing them that the spell was selected.
- Three non-spell options exist in the bottom right corner of the inventory GUI: Previous, Next, and Remove. The first of these options allow the player to navigate the inventory if they know more than one page of spells. The remove option, howver, simply deselects the player's currently selected spell, closing the inventory.
- With a spell selected, the player simply left-clicks with the wand in hand to activate the spell. Upon activation, they will be informed of the cast.
- Spells that apply effects inform the affected player of their presence upon reception.
- Once the effect for a spell has worn off, the player will be informed of this occurrence.
 
# How The System Works
- Coming soon along with a JavaDoc...
