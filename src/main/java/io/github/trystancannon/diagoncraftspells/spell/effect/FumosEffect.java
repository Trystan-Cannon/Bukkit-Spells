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

import io.github.trystancannon.diagoncraftspells.player.Wizard;
import io.github.trystancannon.diagoncraftspells.spell.Fumos;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

/**
 * Creates a cloud of <code>Effect.SMOKE</code> around a <code>Player</code>.
 * 
 * @author Trystan Cannon
 */
public class FumosEffect extends SpellEffect {

    public FumosEffect(Plugin plugin, LivingEntity entityAffected, Wizard caster) {
        super(Fumos.NAME, plugin, entityAffected, caster);
    }
    
    @Override
    public void run() {
        // Create the cloud of smoke as long as the effect hasn't yet been removed.
        if (!isRemoved()) {
            // The effects must be shifted +1 on the z-axis so the smoke flows from its
            // origin and appears over the player.
            getEntity().getWorld().playEffect(getEntity().getLocation().add(new Vector(0, 0, 1)), Effect.SMOKE, 100);
            getEntity().getWorld().playEffect(getEntity().getEyeLocation().add(new Vector(0, 0, 1)), Effect.SMOKE, 100);
            
            Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), this, 1L);
        }
    }
    
    @Override
    public void start() {
        super.start();
        run();
    }
}
