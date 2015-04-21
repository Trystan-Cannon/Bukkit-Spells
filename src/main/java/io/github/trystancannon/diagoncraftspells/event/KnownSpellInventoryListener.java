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
package io.github.trystancannon.diagoncraftspells.event;

import io.github.trystancannon.diagoncraftspells.core.DiagonCraftSpellsPlugin;
import io.github.trystancannon.diagoncraftspells.inventory.KnownSpellsGUI;
import io.github.trystancannon.diagoncraftspells.player.Wizard;
import io.github.trystancannon.diagoncraftspells.spell.Spell;
import io.github.trystancannon.diagoncraftspells.spell.SpellManager;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Handles the following events:
 *      - <code>KnownSpellInventoryOpenEvent</code> for opening the inventory
 *        from the player's wand.
 * 
 *      - <code>InventoryClickEvent</code> for clicking on a spell book in the
 *        known spell inventory.
 *
 * Each <code>KnownSpellInventoryListener</code> instance needs to be
 * explicitly registered for events because it does not do this itself.
 * 
 * @author Trystan Cannon
 */
public class KnownSpellInventoryListener implements Listener {
    
    /**
     * Opens the Known Spells Inventory for the wizard who right clicked on their
     * wand.
     * 
     * @param open
     */
    @EventHandler
    public void onKnownSpellInventoryOpen(KnownSpellInventoryOpenEvent open) {
        KnownSpellsGUI.openInventory(open.getWizard(), 1);
    }
    
    /**
     * Sets the selected spell for the wizard who clicked a known spell in their
     * Known Spells Inventory.
     * 
     * @param click 
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent click) {
        // Prevent this logic from affecting inventory's other than the KnownSpellsGUI.
        if (!click.getInventory().getName().equals(KnownSpellsGUI.INVENTORY_TITLE)) {
            return;
        }
        
        ItemStack clickedStack = click.getCurrentItem();
        
        // Cancel the action if a spell book wasn't clicked.
        if (clickedStack == null || !clickedStack.hasItemMeta() ||
           (clickedStack.getType() != KnownSpellsGUI.NEXT_PAGE_MATERIAL && clickedStack.getType() != KnownSpellsGUI.PREVIOUS_PAGE_MATERIAL &&
            clickedStack.getType() != KnownSpellsGUI.REMOVE_SELECTED_SPELL_MATERIAL && clickedStack.getType() != Material.ENCHANTED_BOOK))
        {
            click.setCancelled(true);
            return;
        }
        
        Wizard clicker = DiagonCraftSpellsPlugin.getWizard(click.getWhoClicked().getUniqueId());
        String stackName = clickedStack.getItemMeta().getDisplayName();
        
        // Check if the player clicked the remove spell option. If so,
        // remove their currently selected spell.
        if (stackName.equals(KnownSpellsGUI.REMOVE_SELECTED_SPELL_TITLE)) {
            clicker.setSelectedSpell(null);
            clicker.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + "Removed the currently selected spell!");
        } else if (stackName.equals(KnownSpellsGUI.NEXT_PAGE_TITLE) || stackName.equals(KnownSpellsGUI.PREVIOUS_PAGE_TITLE)) {
            clicker.setCurrentPageIndex(clicker.getCurrentPageIndex() + (stackName.equals(KnownSpellsGUI.NEXT_PAGE_TITLE) ? 1 : -1));
            click.getInventory().setContents(KnownSpellsGUI.openInventory(clicker, clicker.getCurrentPageIndex()).getContents());
            
            click.setCancelled(true);
            return;
        } else {
            Spell spell = SpellManager.getSpell(ChatColor.stripColor(clickedStack.getItemMeta().getDisplayName()));
        
            if (spell == null || clicker == null) {
                return;
            }

            clicker.setSelectedSpell(spell);
            clicker.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + spell.getName() + ChatColor.RESET + " selected!");
        }
        
        click.getWhoClicked().closeInventory();
    }
}
