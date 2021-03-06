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
package io.github.trystancannon.diagoncraftspells.spell;

import io.github.trystancannon.diagoncraftspells.event.SpellProjectileTouchBlockEvent;
import io.github.trystancannon.diagoncraftspells.spell.effect.GlaciusAreaEffect;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

/**
 * Behaves exactly like <code>Glacius</code>, but with an area effect of radius 2.
 * 
 * @author Trystan Cannon (tccannon@live.com)
 */
public class GlaciusDuo extends ProjectileSpell {
    
    public static final int EFFECT_RADIUS = 2;
    
    public static final String NAME = "Glacius Duo";
    
    public static final List<String> DESCRIPTION = Arrays.asList("Freezes a square area of radius", "2 blocks for 10 seconds.");
    
    public GlaciusDuo(Plugin plugin) {
        super(NAME, DESCRIPTION, plugin);
        
        addFilterBlock(Material.WATER);
        addFilterBlock(Material.STATIONARY_WATER);
    }

    @Override
    public void onProjectileTouchFilterBlock(SpellProjectileTouchBlockEvent touch) {
        new GlaciusAreaEffect(getPlugin(), touch.getTouchedBlockLocation(), touch.getProjectile().getCaster(), EFFECT_RADIUS).start();
        touch.getProjectile().remove();
    }
    
}
