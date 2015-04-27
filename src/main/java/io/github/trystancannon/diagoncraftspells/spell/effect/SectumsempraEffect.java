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
import io.github.trystancannon.diagoncraftspells.spell.Sectumsempra;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;

/**
 * Damages a <code>LivingEntity</code> over 10 seconds, dealing a total of 4.5
 * hearts of damage.
 * 
 * @author Trystan Cannon (tccannon@live.com)
 */
public class SectumsempraEffect extends SpellEffect {
    
    /**
     * The number of server ticks for which this effect is active: 10 seconds.
     */
    public static final int LIFE_SPAN = 200;
    
    /**
     * The amount of damage done every second (20 ticks).
     */
    public static double DAMAGE_INTERVAL = 4.5 / 10;
    
    /**
     * The number of ticks for which this effect has been active.
     */
    private int ticksLived = 0;
    
    /**
     * The id of the task which will inflict damage on the player.
     * 
     * This is used for stopping any scheduled damaging if the effect
     * is canceled before the task executes but after it is scheduled.
     */
    private int damageTaskId = -1;
    
    public SectumsempraEffect(Plugin plugin, LivingEntity entityAffected, Wizard caster) {
        super(Sectumsempra.NAME, plugin, entityAffected, caster);
    }
    
    @Override
    public void start() {
        super.start();
        
        getEntity().damage(DAMAGE_INTERVAL);
        damageTaskId = Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), this, 20);
    }
    
    @Override
    public void run() {
        if (isRemoved()) {
            return;
        }
        
        ticksLived += 20;
        
        if (ticksLived > LIFE_SPAN) {
            remove();
            damageTaskId = -1;
            
            return;
        }
        
        getEntity().damage(DAMAGE_INTERVAL);
        Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), this, 20);
    }
    
    @Override
    public void remove() {
        super.remove();
        
        if (damageTaskId != -1) {
            Bukkit.getScheduler().cancelTask(damageTaskId);
            damageTaskId = -1;
        }
    }
    
}
