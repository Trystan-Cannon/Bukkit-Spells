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
package io.github.trystancannon.diagoncraftspells.spell;

import io.github.trystancannon.diagoncraftspells.player.Wizard;
import io.github.trystancannon.diagoncraftspells.spell.effect.SpellEffect;

import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

/**
 * Removes all <code>SpellEffect</code>s from the caster.
 * 
 * @author Trystan Cannon
 */
public class FiniteIncantatum extends Spell {

    public static final String NAME = "Finite Incantatum";
    
    public static final List<String> DESCRIPTION = Arrays.asList("Remves all spell effects from", "the caster.");
    
    public FiniteIncantatum(Plugin plugin) {
        super(NAME, DESCRIPTION, plugin);
    }
    
    @Override
    public boolean execute(Wizard caster, Block targetBlock, BlockFace targetFace, Entity target, String[] args) {
        // Cannot use a for-each loop here because effect.remove also removes itself
        // from the wizard's effect list. Therefore, it would cause some funny business with
        // the way for-each loops get the elements over which they iterate.
        for (int index = 0; index < caster.getEffects().size(); index++) {
            SpellEffect effect = caster.getEffects().get(index);
            
            if (effect != null) {
                effect.remove();
            }
        }
        
        caster.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + getName() + "!");
        return true;
    }
    
}
