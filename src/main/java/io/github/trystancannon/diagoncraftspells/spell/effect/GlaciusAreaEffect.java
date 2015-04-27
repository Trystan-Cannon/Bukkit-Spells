/*
 * The MIT License
 *
 * Copyright 2015 Trystan Cannon (tccannon@live.com).
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

import io.github.trystancannon.diagoncraftspells.player.Wizard;
import io.github.trystancannon.diagoncraftspells.spell.Glacius;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

/**
 * Performs the <code>GlaciusEffect</code> across an area of water.
 * 
 * @author Trystan Cannon (tccannon@live.com)
 */
public class GlaciusAreaEffect extends AreaEffect {
    
    private final List<GlaciusEffect> effects;
    
    public GlaciusAreaEffect(Plugin plugin, Location blockLocation, Wizard caster, int radius) {
        super(Glacius.NAME + ". r = " + radius, plugin, blockLocation, caster, radius);
        effects = new ArrayList<>();
    }

    @Override
    public void start() {
        super.start();
        
        for (Block affectedBlock : getAffectedBlocks()) {
            GlaciusEffect effect = new GlaciusEffect(getPlugin(), affectedBlock.getLocation(), getCaster());
            effect.start();
            
            effects.add(effect);
        }
    }
    
    @Override
    public void run() {
    }
    
    @Override
    public void remove() {
        super.remove();
        
        if (hasStarted()) {
            for (GlaciusEffect effect : effects) {
                if (effect.hasStarted() && !effect.isRemoved()) {
                    effect.remove();
                }
            }
        }
    }
    
}
