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

import io.github.trystancannon.diagoncraftspells.event.SpellProjectileTouchBlockEvent;
import io.github.trystancannon.diagoncraftspells.spell.effect.GlaciusEffect;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

/**
 * Freezes a single block of water for 10 seconds.
 * 
 * @author Trystan Cannon
 */
public class Glacius extends ProjectileSpell {
    
    public static final String NAME = "Glacius";
    
    public static final List<String> DESCRIPTION = Arrays.asList("Freezes a single block", "of water for 10 seconds.");
    
    public Glacius(Plugin plugin) {
        super(NAME, DESCRIPTION, plugin);
        
        addFilterBlock(Material.WATER);
        addFilterBlock(Material.STATIONARY_WATER);
    }

    @Override
    public void onProjectileTouchFilterBlock(SpellProjectileTouchBlockEvent touch) {
        new GlaciusEffect(getPlugin(), touch.getTouchedBlockLocation(), touch.getProjectile().getCaster()).start();
        touch.getProjectile().remove();
    }
    
}
