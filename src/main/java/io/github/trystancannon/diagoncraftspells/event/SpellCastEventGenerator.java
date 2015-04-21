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
import io.github.trystancannon.diagoncraftspells.player.Wizard;
import io.github.trystancannon.diagoncraftspells.spell.Spell;
import io.github.trystancannon.diagoncraftspells.wand.Wand;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

/**
 * The Spell Cast Event Generator spawn Spell Cast events when a player spawns
 * an interact event with a Wand in hand.
 * 
 * Each instance of <code>SpellCastEventGenerator</code> MUST be registered
 * explicitly for events; it does not do this itself.
 * 
 * @author Trystan Cannon
 */
public final class SpellCastEventGenerator implements Listener {
    
    /**
     * Generates events for the following situations:
     *      - A spell is cast on a block (block targeted when the player left clicks with their wand).
     *      - A spell is cast into the air (no block targeted when the player casts).
     * 
     * These situations spawn the following events, respectively:
     *      - <code>SpellCastOnBlockEvent</code>
     *      - <code>SpellCastOnAirEvent</code>
     * 
     * @param interaction 
     */
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent interaction) {
        if (Wand.isItemWand(interaction.getItem())) {
            Action action = interaction.getAction();
            Wizard caster = DiagonCraftSpellsPlugin.getWizard(interaction.getPlayer().getUniqueId());
            Spell selectedSpell = caster.getSelectedSpell();
            
            // Spell cast on a particular block.
            if (action == Action.LEFT_CLICK_BLOCK) {
                Bukkit.getPluginManager().callEvent(new SpellCastOnBlockEvent(caster, selectedSpell, interaction.getClickedBlock(), interaction.getBlockFace()));
            // Spell cast without a block in reach.
            } else if (action == Action.LEFT_CLICK_AIR) {
                Bukkit.getPluginManager().callEvent(new SpellCastOnAirEvent(caster, selectedSpell));
            // Player wants to open their spell inventory.
            } else if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                Bukkit.getPluginManager().callEvent(new KnownSpellInventoryOpenEvent(caster));
            }
        }
    }
    
}
