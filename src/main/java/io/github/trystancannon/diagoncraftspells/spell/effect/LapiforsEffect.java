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
import io.github.trystancannon.diagoncraftspells.spell.Lapifors;
import org.bukkit.Bukkit;

import org.bukkit.plugin.Plugin;
import pgDev.bukkit.DisguiseCraft.disguise.DisguiseType;

/**
 * Transforms the caster into a silverfish for 1 minute.
 * 
 * @author Trystan Cannon
 */
public class LapiforsEffect extends DisguiseEffect {

    public static final int LIFE_SPAN = 1200;
    
    private int removalTaskId = -1;
    
    public LapiforsEffect(Plugin plugin, Wizard caster) {
        super(Lapifors.NAME, plugin, caster.getPlayer(), caster);
    }

    @Override
    public void start() {
        super.start();
        
        disguisePlayer(DisguiseType.Silverfish);
        removalTaskId = Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), this, LIFE_SPAN);
    }
    
    @Override
    public void run() {
        if (hasStarted() && !isRemoved()) {
            removalTaskId = -1;
            remove();
        }
    }
    
    @Override
    public void remove() {
        super.remove();
        
        if (removalTaskId != -1) {
            Bukkit.getScheduler().cancelTask(removalTaskId);
            removalTaskId = -1;
        }
    }
    
}
