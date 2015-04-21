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
package io.github.trystancannon.diagoncraftspells.inventory;

import io.github.trystancannon.diagoncraftspells.player.Wizard;
import io.github.trystancannon.diagoncraftspells.spell.Spell;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * The Known Spells GUI appears when a player right clicks their wand. It shows
 * a chest full of books which represent the spells that the player currently
 * knows. Once the player has clicked a spell, their currently selected spell
 * changes to the clicked spell.
 * 
 * @author Trystan Cannon
 */
public class KnownSpellsGUI {
    
    /**
     * Number of slots in the <code>KnownSpellsGUI</code> inventory. This is
     * the size of a double chest.
     * 
     * Since there is currently a bug (11 April 2015) regarding the size of
     * inventories, we'll settle for just 45 slots.
     * 
     * The bug above occurs when clicking a slot bigger than index 45. It causes
     * Minecraft to throw an error, but the plugin doesn't crash. There is no
     * reference to this plugin, only Spigot.
     */
    public static final int INVENTORY_SIZE = 36;
    
    /**
     * The maximum number of slots that can be occupied by spells on a single
     * page of the interface.
     */
    public static final int MAX_SPELLS_PER_PAGE = INVENTORY_SIZE - 3;
    
    /**
     * The title of the inventory which appears above the GUI when it appears
     * for the player.
     */
    public static final String INVENTORY_TITLE = ChatColor.DARK_RED.toString() + ChatColor.BOLD + "Known Spells";
    
    /**
     * The material which represents the stack option to remove the currently selected spell.
     */
    public static final Material REMOVE_SELECTED_SPELL_MATERIAL = Material.BLAZE_ROD;
    
    /**
     * The title for the stack option which allows the player to remove their currently
     * selected spell.
     */
    public static final String REMOVE_SELECTED_SPELL_TITLE = ChatColor.GREEN + "Remove Selected Spell";
    
    /**
     * The description for the stack option which allows the player to remove their
     * currently selected spell.
     */
    public static final List<String> REMOVE_SELECTED_SPELL_DESCRIPTION = Arrays.asList("Wipes your wand clean;", "you will have no spell", "currently selected.");
    
    /**
     * The material which represents the stack option to move on to the next page in the Known Spells GUI.
     */
    public static final Material NEXT_PAGE_MATERIAL = Material.ARROW;
    
    /**
     * The title for the stack option which allows the player to move to the next page of
     * known spells.
     */
    public static final String NEXT_PAGE_TITLE = ChatColor.AQUA + "Next Page";
    
    /**
     * The material which represents the stack option to move back to the previous page in the Known Spells GUI.
     */
    public static final Material PREVIOUS_PAGE_MATERIAL = Material.ARROW;
    
    /**
     * The title for the stack option which allows the player to move to the previous page
     * of known spells.
     */
    public static final String PREVIOUS_PAGE_TITLE = ChatColor.AQUA + "Previous Page";
    
    /**
     * Opens the inventory to the given wizard.
     * 
     * @param wizard
     *          <code>Wizard</code> to which the GUI will be opened.
     * 
     * @param page
     *          The page in the wizard's Known Spells GUI.
     * 
     * @return
     *          The inventory created or <code>null</code> if none.
     */
    public static Inventory openInventory(Wizard wizard, int page) {
        // Ensure the player is online:
        if (wizard.getPlayer() == null) {
            return null;
        }
        
        Inventory knownSpellsInventory = Bukkit.createInventory(null, INVENTORY_SIZE, INVENTORY_TITLE);
        
        for (int index = (wizard.getCurrentPageIndex() - 1) * MAX_SPELLS_PER_PAGE;
            index < Math.min(MAX_SPELLS_PER_PAGE * wizard.getCurrentPageIndex(), wizard.getKnownSpells().size());
            index++)
        {
            Spell spell = wizard.getKnownSpells().get(index);
            
            ItemStack spellStack = new ItemStack(Material.ENCHANTED_BOOK);
            ItemMeta stackMeta = spellStack.getItemMeta();
            
            stackMeta.setDisplayName(ChatColor.LIGHT_PURPLE + spell.getName());
            stackMeta.setLore(spell.getDescription());
            spellStack.setItemMeta(stackMeta);
            
            knownSpellsInventory.addItem(spellStack);
        }
        
        // Put options to change pages even if there is not more than 1 page to be displayed.
        ItemStack nextPageStack = new ItemStack(NEXT_PAGE_MATERIAL, 1);
        ItemMeta nextPageStackMeta = nextPageStack.getItemMeta();
        
        ItemStack previousPageStack = new ItemStack(PREVIOUS_PAGE_MATERIAL, 1);
        ItemMeta previousPageStackMeta = previousPageStack.getItemMeta();
        
        // The lore the next and previous page items reflect the current
        // position of the player in the Known Spells GUI.
        List<String> pagePositionLore = Arrays.asList("Page " + wizard.getCurrentPageIndex() + "/" + wizard.getMaxPageNumber());
        
        nextPageStackMeta.setDisplayName(NEXT_PAGE_TITLE);
        nextPageStackMeta.setLore(pagePositionLore);
        nextPageStack.setItemMeta(nextPageStackMeta);
        
        previousPageStackMeta.setDisplayName(PREVIOUS_PAGE_TITLE);
        previousPageStackMeta.setLore(pagePositionLore);
        previousPageStack.setItemMeta(previousPageStackMeta);
        
        
        // Put an option to remove any currently selected spell in the bottom right
        // hand corner.
        ItemStack removeSpellStack = new ItemStack(REMOVE_SELECTED_SPELL_MATERIAL, 1);
        ItemMeta removeSpellStackMeta = removeSpellStack.getItemMeta();
        
        removeSpellStackMeta.setDisplayName(REMOVE_SELECTED_SPELL_TITLE);
        removeSpellStackMeta.setLore(REMOVE_SELECTED_SPELL_DESCRIPTION);
        removeSpellStack.setItemMeta(removeSpellStackMeta);
        
        knownSpellsInventory.setItem(knownSpellsInventory.getSize() - 3, previousPageStack);
        knownSpellsInventory.setItem(knownSpellsInventory.getSize() - 2, nextPageStack);
        knownSpellsInventory.setItem(knownSpellsInventory.getSize() - 1, removeSpellStack);
        
        wizard.getPlayer().openInventory(knownSpellsInventory);
        return knownSpellsInventory;
    }
    
}
