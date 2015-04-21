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
package io.github.trystancannon.diagoncraftspells.item;

import io.github.trystancannon.diagoncraftspells.core.DiagonCraftSpellsPlugin;
import io.github.trystancannon.diagoncraftspells.event.SpellLearnedEvent;
import io.github.trystancannon.diagoncraftspells.player.Wizard;
import io.github.trystancannon.diagoncraftspells.spell.Spell;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * The Spell Book Activate Listener handles the case in which a wizard right
 * clicks with a spell book in hand, teaching them the spell if the case may be.
 * 
 * If the wizard already knows the spell, they are simply reminded of this,
 * and nothing more occurs.
 * 
 * Otherwise, the wizard is taught the spell, their profile is updated, and
 * the book is removed from their hand.
 * 
 * Each <code>SpellBookActivateListener</code> MUST be explicitly registered for
 * events because it does not do this on its own.
 * 
 * Generates the following events:
 *      - <code>SpellLearnedEvent</code>
 * 
 * @author Trystan Cannon
 */
public final class SpellBookActivateListener implements Listener {
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent interaction) {
        if (!interaction.hasItem() || !SpellBook.isItemSpellBook(interaction.getItem())) {
            return;
        }
        
        Spell spell = SpellBook.fromStack(interaction.getItem()).getSpell();
        Wizard wizard = DiagonCraftSpellsPlugin.getWizard(interaction.getPlayer().getUniqueId());
        
        if (wizard == null) {
            return;
        }
        
        if (wizard.knowsSpell(spell)) {
            wizard.getPlayer().sendMessage(ChatColor.RED + "You already know the spell " + ChatColor.LIGHT_PURPLE + spell.getName() + "!");
            return;
        }
        
        wizard.getPlayer().getInventory().setItemInHand(null);
        wizard.addSpell(spell);
        wizard.getPlayer().sendMessage("Learned " + ChatColor.LIGHT_PURPLE + spell.getName() + "!");
        
        // Now that the player has learned a new spell, we should update
        // the player's wizard profile so their new knowledge is saved across
        // reloads.
        Bukkit.getPluginManager().callEvent(new SpellLearnedEvent(wizard, spell));
    }
    
}
