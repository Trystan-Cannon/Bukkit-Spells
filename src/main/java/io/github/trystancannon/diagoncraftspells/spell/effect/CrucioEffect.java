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
import io.github.trystancannon.diagoncraftspells.spell.Crucio;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;

/**
 * Damages the affected living entity once per second for 4 seconds, causing
 * 4 hearts of damage each interval.
 * 
 * @author Trystan Cannon
 */
public class CrucioEffect extends SpellEffect {

    /**
     * The number of server ticks for which this effect is active.
     */
    public static final int LIFE_SPAN = 80;
    
    /**
     * The number of ticks between each execution of the effect's <code>run</code> method.
     * 
     * Begins at negative 20 so, after the initial execution, the effect will
     * have lived for 0 ticks.
     */
    public static final int INTERVAL_LENGTH = -20;
    
    /**
     * The number of ticks for which this effect has been active.
     */
    private int ticksLived = 0;
    
    public CrucioEffect(Plugin plugin, LivingEntity entityAffected, Wizard caster) {
        super(Crucio.NAME, plugin, entityAffected, caster);
    }
    
    @Override
    public void start() {
        super.start();
        run();
    }
    
    @Override
    public void run() {
        if (ticksLived < LIFE_SPAN) {
            getEntity().damage(4);
            ticksLived += 20;
            
            Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), this, 20);
        }
        
        if (!isRemoved()) {
            remove();
        }
    }
    
}
