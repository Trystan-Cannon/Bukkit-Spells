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
package io.github.trystancannon.diagoncraftspells.spell.effect;

import io.github.trystancannon.diagoncraftspells.core.DiagonCraftSpellsPlugin;
import io.github.trystancannon.diagoncraftspells.player.Wizard;
import io.github.trystancannon.diagoncraftspells.spell.Obliviate;
import io.github.trystancannon.diagoncraftspells.spell.Spell;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Makes the targeted Wizard forget all spells for 15 seconds.
 * 
 * @author Trystan Cannon
 */
public class ObliviateEffect extends SpellEffect {

    /**
     * The number of server ticks before this effect's <code>run</code> method
     * is executed.
     */
    public static final int RUN_DELAY_INTERVAL = 300;
    
    /**
     * All of the spells that the wizard knows at the time that this effect is
     * started.
     */
    private List<Spell> knownSpells;
    
    /**
     * The spell the wizard had currently selected when this effect began.
     */
    private Spell selectedSpell;
    
    public ObliviateEffect(Plugin plugin, Wizard wizardAffected, Wizard caster) {
        super(Obliviate.NAME, plugin, wizardAffected.getPlayer(), caster);
    }

    @Override
    public void start() {
        super.start();
        knownSpells = new ArrayList<>();
        selectedSpell = getAffectedWizard().getSelectedSpell();
        
        // Make a copy of all the spells this wizard knows.
        for (Spell knownSpell : getAffectedWizard().getKnownSpells()) {
            knownSpells.add(knownSpell);
        }
        
        // Remove all spells they know for 15 seconds, including the currently
        // selected spell.
        getAffectedWizard().getKnownSpells().clear();
        getAffectedWizard().setSelectedSpell(null);
        Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), this, RUN_DELAY_INTERVAL);
    }
    
    @Override
    public void run() {
        if (!isRemoved()) {
            remove();
        }
    }
    
    @Override
    public void remove() {
        super.remove();
        
        // Restore all spells that the player knows.
        getAffectedWizard().getKnownSpells().addAll(knownSpells);
        getAffectedWizard().setSelectedSpell(selectedSpell);
    }
    
    public Wizard getAffectedWizard() {
        return DiagonCraftSpellsPlugin.getWizard(((Player) getEntity()).getUniqueId());
    }
}
