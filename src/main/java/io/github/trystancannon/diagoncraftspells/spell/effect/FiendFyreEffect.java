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
import io.github.trystancannon.diagoncraftspells.spell.FiendFyre;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;

/**
 * Catches the given <code>LivingEntity</code> on fire until their fire ticks
 * are zero. This can occur via spell or natural occurrence.
 * 
 * This effect must be started.
 * 
 * @author Trystan Cannon
 */
public final class FiendFyreEffect extends SpellEffect {
    
    public FiendFyreEffect(Plugin plugin, LivingEntity entityAffected, Wizard caster) {
        super(FiendFyre.NAME, plugin, entityAffected, caster);
    }

    @Override
    public void start() {
        // The entity will be on fire forever unless they quench the flame.
        getEntity().setFireTicks(Integer.MAX_VALUE);
        super.start();
        
        run();
    }
    
    /**
     * Checks every tick if the entity is still on fire. If it is, then the
     * effect continues.
     * 
     * Because this could very well cause lag, it should only be invoked
     * when the entity affected is a wizard.
     */
    @Override
    public void run() {
        if (getEntity().getFireTicks() > 0 && !getEntity().isDead()) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), this, 1L);
        // If the event hasn't been manually removed, then remove it from
        // the entity.
        } else if (!isRemoved()) {
            remove();
        }
    }
    
    /**
     * Removes the fire from the affected entity and produces the proper
     * <code>SpellEffectEndEvent</code> as well as removing the effect from
     * the affected wizard, if any.
     */
    @Override
    public void remove() {
        super.remove();
        getEntity().setFireTicks(0);
    }
    
}
