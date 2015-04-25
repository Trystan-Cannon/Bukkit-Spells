/*
 * The MIT License
 *
 * Copyright 2015 Trystan Cannon.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package io.github.trystancannon.diagoncraftspells.core;

import io.github.trystancannon.diagoncraftspells.event.KnownSpellInventoryListener;
import io.github.trystancannon.diagoncraftspells.event.SpellCastListener;
import io.github.trystancannon.diagoncraftspells.event.SpellLearnedEvent;
import io.github.trystancannon.diagoncraftspells.file.Loader;
import io.github.trystancannon.diagoncraftspells.file.Saver;
import io.github.trystancannon.diagoncraftspells.item.SpellBook;
import io.github.trystancannon.diagoncraftspells.item.SpellBookActivateListener;
import io.github.trystancannon.diagoncraftspells.player.Wizard;
import io.github.trystancannon.diagoncraftspells.spell.Spell;
import io.github.trystancannon.diagoncraftspells.event.SpellCastEventGenerator;
import io.github.trystancannon.diagoncraftspells.spell.SpellManager;
import io.github.trystancannon.diagoncraftspells.wand.Wand;
import io.github.trystancannon.diagoncraftspells.wand.Wand.WandType;

import pgDev.bukkit.DisguiseCraft.api.DisguiseCraftAPI;
import pgDev.bukkit.DisguiseCraft.DisguiseCraft;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

/*
TODO:
    - Spells remaining:
        + Sectumsempra
        + Serpensortia
        + Silencio
        + Stupefy
        + Vipera Evanesca
        + 15 other spells mentioned in a PM

    - Add all of the spells.
    - Add SpellProjectileCollide event to protect from spells.
    - Rewrite all deprecated spells to use the new spell system.
    - Cleanup code.
*/

/**
 * Implementation of the main <code>JavaPlugin</code>. Handles actions related
 * to the enabling, disabling, and event/command registering for the plugin.
 * 
 * Basic code outline as of 10 April 2015:
 *  - Players can use stick <code>ItemStack</code>s which have special meta data
 *    which designate them as "Wands."
 *      - When players cause a <code>PlayerInteractEvent</code> or <code>PlayerInteractEntityEvent</code>
 *        with a wand, the current "spell" bound to the wand is activated.
 *      - Players can bind different spells to their wand using the command /spells [spell name]. However,
 *        the spell will only bind if the player knows this spell.
 *  - Players learn spells by interacting with special books. The spell is saved into a special save file for the
 *    player. Once they've learned the spell, they may bind it to a wand at any time using /spells [spell name].
 *      - The player's save file is a text file whose name is the player's <code>UUID</code>. Each spell has a unique
 *        name. The name of each spell that a player knows is saved in the file on its own line. Therefore, one may
 *        add spells manually to the file given a player's unique id.
 *  - Players can view the spells that they currently know by using /spells list.
 * 
 * @author Trystan Cannon
 */
public class DiagonCraftSpellsPlugin extends JavaPlugin implements Listener {
    
    /**
     * The name of this plugin.
     */
    public static final String PLUGIN_NAME = "DiagonCraftSpells";
    
    /**
     * List of all <code>Player</code>s represented as <code>Wizard</code>s. This
     * is loaded from the plugin's data folder.
     * 
     * NOTE:
     *      It may be better to load each wizard's save file upon joining the server,
     *      but this may cause performance issues when many, many players join at
     *      the same time with lots of spells known.
     * 
     *      However, a similar danger appears when loading all of the wizards on
     *      enable: Memory usage. If there are lots of wizard save files, then it
     *      will take a little while to load all of them, and all of the save data
     *      will be floating around in this hash map, taking up space until a player
     *      shows up.
     */
    private static final HashMap<UUID, Wizard> wizards = new HashMap<>();
    
    /**
     * The Spell Manager for the plugin. Allows for registration and referencing of
     * spells given a spell name.
     */
    private SpellManager spellManager;
    
    /**
     * The object which generates all <code>SpellCastEvent</code>s.
     * 
     * When a wizard interacts with their wand, this object will spawn the event
     * that allows the spell to manifest an effect in the world.
     */
    private static final SpellCastEventGenerator spellCastEventGenerator = new SpellCastEventGenerator();
    
    /**
     * The object which catches and handles all <code>SpellCastEvent</code>events.
     */
    private static final SpellCastListener spellCastListener = new SpellCastListener();
    
    /**
     * The object which catches and handles all attempts to open and interact
     * with the <code>KnownSpellsGUI</code>.
     */
    private static final KnownSpellInventoryListener knownSpellInventoryListener = new KnownSpellInventoryListener();
    
    /**
     * The object which catches and handles all interactions with Spell Books.
     * 
     * For example, this object will add a spell to a wizard's known spell
     * list if they right click on a spell book for a spell they do not currently know.
     */
    private static final SpellBookActivateListener spellBookActivateListener = new SpellBookActivateListener();
    
    /**
     * The object through which disguises are controlled. This is used for disguise
     * related spells.
     */
    private static DisguiseCraftAPI disguiseApi;
    
    /**
     * Loads all of the wizard profiles from the plugin's data folder, storing
     * them into the <code>wizards</code> <code>HashMap</code>.
     * 
     * Registers the plugin instance for events.
     */
    @Override
    public void onEnable() {
        // Initialize the spell manager.
        spellManager = new SpellManager(this);
        
        // Create the plugin's data folder if it doesn't already exist.
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }
        
        // Load apis.
        disguiseApi = DisguiseCraft.getAPI();
        getLogger().log(Level.INFO, "DisguiseCraft was{0}fround.", (disguiseApi != null ? " " : " not "));
        
        // Load wizard profiles.
        loadWizardProfiles();
        getLogger().log(Level.INFO, "Loaded {0} wizard profiles.", wizards.size());
        
        // Register the plugin and its event generators for events.
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(spellCastEventGenerator, this);
        getServer().getPluginManager().registerEvents(spellCastListener, this);
        getServer().getPluginManager().registerEvents(knownSpellInventoryListener, this);
        getServer().getPluginManager().registerEvents(spellBookActivateListener, this);
    }
    
    /**
     * Creates a new <code>Wizard</code> object for the player who just joined if
     * there is not one already loaded for them.
     * 
     * If there is not a <code>Wizard</code> object already loaded for a player, it
     * means that there is not a save file for their personal data for this plugin.
     * Therefore, one is created for them upon joining.
     * 
     * @param playerJoin 
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent playerJoin) {
        UUID playerId = playerJoin.getPlayer().getUniqueId();
        
        // No wizard object for this player:
        if (wizards.get(playerId) == null) {
            Wizard wizardProfile = new Wizard(playerId);
            // Create the save file for the wizard.
            Saver.saveWizardProfile(getDataFolder().getAbsolutePath(), wizardProfile);
            
            wizards.put(playerId, wizardProfile);
            playerJoin.getPlayer().sendMessage("Wizard profile created for you.");
        }
    }
    
    /**
     * Causes the plugin to update the save file for the wizard who just learned
     * a spell.
     * 
     * @param learn
     */
    @EventHandler
    public void onSpellLearned(SpellLearnedEvent learn) {
        Saver.saveWizardProfile(getDataFolder().getAbsolutePath(), learn.getWizard());
    }
    
    /**
     * @return
     *          The object through which disguises are controlled.
     */
    public static DisguiseCraftAPI getDisguiseAPI() {
        return disguiseApi;
    }
    
    /**
     * @param playerId
     *              Unique ID of the player represented by the desired <code>Wizard</code> object.
     * 
     * @return
     *          The <code>Wizard</code> object representing the given player's UUID or <code>null</code>
     *          if none such object exists.
     */
    public static Wizard getWizard(UUID playerId) {
        if (playerId == null) {
            return null;
        }
        
        return wizards.get(playerId);
    }
    
    /**
     * @return
     *          The <code>SpellManager</code> for the plugin, containing all registered spells.
     */
    public SpellManager getSpellManager() {
        return spellManager;
    }
    
    /**
     * Reloads all wizard profiles contained in the plugin's data folder, filling
     * the <code>wizards</code> <code>HashMap</code>.
     */
    private void loadWizardProfiles() {
        for (File wizardProfile : getDataFolder().listFiles()) {
            UUID playerId = UUID.fromString(wizardProfile.getName().replace(".txt", ""));
            Wizard wizard = Loader.loadWizardProfile(playerId, wizardProfile.getAbsolutePath());
            
            if (wizard != null) {
                wizards.put(wizard.getUniqueId(), wizard);
            }
        }
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("diagoncraftspells.spells") && sender instanceof Player && command.getName().equalsIgnoreCase("spells") && args.length > 0) {
            Player player = (Player) sender;
            String commandName = args[0];
            
            // /spells wands -> Give all wands.
            if (commandName.equalsIgnoreCase("wands") && player.hasPermission("diagoncraftspells.wand")) {
                for (WandType type : Wand.WandType.values()) {
                    player.getInventory().addItem(Wand.getWand(type));
                }
            // /spells book [spell name] -> Give a spell book.
            } else if (commandName.equalsIgnoreCase("book") && player.hasPermission("diagoncraftspells.book")) {
                // Not enough arguments for a book.
                if (args.length < 2) {
                    player.sendMessage(ChatColor.RED + "Usage: /spells book [spell name]");
                    return true;
                }
                
                String spellName = args[1];
                
                if (args.length > 2) {
                    for (String word : Arrays.copyOfRange(args, 2, args.length)) {
                        spellName = spellName + " " + word;
                    }
                }
                
                Spell spell = SpellManager.getSpell(spellName);
                
                if (spell == null) {
                    player.sendMessage(ChatColor.LIGHT_PURPLE + spellName + ChatColor.RED + " is not a valid spell name.");
                    return true;
                }
                
                player.getInventory().addItem(new SpellBook(spell).getStack());
            // /spells list -> List all spells.
            } else if (commandName.equalsIgnoreCase("list") && player.hasPermission("diagoncraftspells.list")) {
                player.sendMessage(ChatColor.ITALIC + "All registered spells on this server in version " + ChatColor.BOLD + getDescription().getVersion() + ChatColor.RESET + ChatColor.ITALIC + ":");
                
                for (Spell spell : SpellManager.getRegisteredSpells().values()) {
                    player.sendMessage(ChatColor.LIGHT_PURPLE + spell.getName());
                }
            // /spells reload -> Reload all wizard profiles.
            } else if (commandName.equalsIgnoreCase("reload") && player.hasPermission("diagoncraftspells.reload")) {
                long startTime = System.nanoTime();
                player.sendMessage("Reloading all wizard profiles...");
                
                loadWizardProfiles();
                
                long endTime = System.nanoTime();
                player.sendMessage("Done in " + (endTime - startTime) / 1E9 + " seconds.");
            // /spells fixfly -> Set player fly speed back to default.
            } else if (commandName.equalsIgnoreCase("fixfly") && player.hasPermission("diagoncraftspells.fixly")) {
                Wizard wizard = getWizard(player.getUniqueId());
                
                // Make sure the player isn't actually just tring to avoid an effect.
                if (wizard != null && !wizard.getEffects().isEmpty()) {
                    player.sendMessage(ChatColor.RED + "You must not be under the influence of any spells to use this command!");
                    return true;
                }
                
                player.setWalkSpeed(0.2F);
                player.setFlySpeed(0.1F);
                player.setAllowFlight(player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR);
                player.sendMessage(ChatColor.ITALIC + "Updated fly speed to 0.1F and walk speed to 0.2F.");
            // Send usage.
            } else {
                sendUsage(sender);
            }
            
            return true;
        }
        
        sendUsage(sender);
        return false;
    }
    
    /**
     * Sends the plugin's command usage to the receiver.
     * 
     * @param receiver 
     */
    public static void sendUsage(CommandSender receiver) {
        receiver.sendMessage(ChatColor.LIGHT_PURPLE + "------ " + ChatColor.GOLD + "Diagon Craft Spells" + ChatColor.LIGHT_PURPLE + " ------");
        receiver.sendMessage(applyCommandUsageColor("/spells reload", "Reloads all of the wizard profiles."));
        receiver.sendMessage(applyCommandUsageColor("/spells [sub command] [args]", "The base command for the plugin. This is used to access all other commands."));
        receiver.sendMessage(applyCommandUsageColor("/spells wands", "Gives the player all available wands."));
        receiver.sendMessage(applyCommandUsageColor("/spells book [spell name]", "Gives the player the spell book for the spell with the given name."));
        receiver.sendMessage(applyCommandUsageColor("/spells list", "Lists all of the registered spells for this version of the plugin."));
        receiver.sendMessage(applyCommandUsageColor("/spells fixfly", "Resets the player's fly speed to the default. Use if you are unable to move in flight after to a server reload or crash."));
    }
    
    /**
     * Applies the standardized colors for each command usage message.
     * 
     * @param command
     *          The command.
     * @param usage
     *          The usage description for this command.
     */
    private static String applyCommandUsageColor(String command, String usage) {
        return ChatColor.LIGHT_PURPLE + command + ChatColor.WHITE + " - " + ChatColor.AQUA + usage;
    }
    
}
