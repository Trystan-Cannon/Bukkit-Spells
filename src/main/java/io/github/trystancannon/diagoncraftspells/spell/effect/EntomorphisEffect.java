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
import io.github.trystancannon.diagoncraftspells.spell.Entomorphis;

import pgDev.bukkit.DisguiseCraft.disguise.DisguiseType;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

/**
 * Transforms the player into a spider for 1 minute. This effect is not visible
 * to the caster themselves. However, other players will be able to see the
 * disguise.
 * 
 * @author Trystan Cannon
 */
public class EntomorphisEffect extends DisguiseEffect {

    /**
     * The number of server ticks for which this effect is active.
     */
    public static final int LIFE_SPAN = 1200;
    
    /**
     * The task id for the scheduled removal task.
     */
    private int removalTaskId = -1;
    
    public EntomorphisEffect(Plugin plugin, Wizard caster) {
        super(Entomorphis.NAME, plugin, caster.getPlayer(), caster);
    }

    @Override
    public void start() {
        super.start();
        
        disguisePlayer(DisguiseType.Spider);
        removalTaskId = Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), this, LIFE_SPAN);
    }
    
    @Override
    public void run() {
        // The removal taks runs:
        if (!isRemoved()) {
            removalTaskId = -1;
            remove();
        }
    }
    
    @Override
    public void remove() {
        if (removalTaskId != -1) {
            Bukkit.getScheduler().cancelTask(removalTaskId);
        }
        
        super.remove();
    }
    
}
