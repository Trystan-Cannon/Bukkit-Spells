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
import io.github.trystancannon.diagoncraftspells.spell.effect.LevicorpusEffect;

import java.util.Arrays;
import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

/**
 * Launches the caster into the air, freezing them in place for 1 minute after
 * they have been flying upwards for 1 second.
 * 
 * @author Trystan Cannon
 */
public class Levicorpus extends Spell {
    
    public static final String NAME = "Levicorpus";
    
    public static final List<String> DESCRIPTION = Arrays.asList("Launches the caster into the", "air and keeps them there", "for 1 minute.");
    
    public Levicorpus(Plugin plugin) {
        super(NAME, DESCRIPTION, plugin);
    }

    @Override
    public boolean execute(Wizard caster, Block targetBlock, BlockFace targetFace, Entity target, String[] args) {
        if (!caster.hasEffect(getName())) {
            new LevicorpusEffect(getPlugin(), caster).start();
        // Remove the effect if the caster already has it.
        } else {
            for (SpellEffect levicorpus : caster.getEffect(getName())) {
                levicorpus.remove();
            }
        }
        
        alertCast(caster);
        return true;
    }
    
}
