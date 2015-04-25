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
import io.github.trystancannon.diagoncraftspells.spell.Avifors;

import pgDev.bukkit.DisguiseCraft.disguise.DisguiseType;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Transforms the caster into a bat for 1 minute, allowing flight for this
 * duration.
 * 
 * @author Trystan Cannon
 */
public class AviforsEffect extends DisguiseEffect {

    public static final int LIFE_SPAN = 1200;
    
    private boolean wasAllowedFlight = false;
    
    private boolean wasFlying = false;
    
    private float flySpeed = 0.1F;
    
    private int removalTaskId = -1;
    
    public AviforsEffect(Plugin plugin, Wizard caster) {
        super(Avifors.NAME, plugin, caster.getPlayer(), caster);
    }

    @Override
    public void start() {
        super.start();
        Player playerAffected = getPlayerAffected();
        
        wasAllowedFlight = playerAffected.getAllowFlight();
        wasFlying = playerAffected.isFlying();
        flySpeed = playerAffected.getFlySpeed();
        
        playerAffected.setAllowFlight(true);
        playerAffected.setFlying(true);
        playerAffected.setFlySpeed(0.1F);
        
        disguisePlayer(DisguiseType.Bat);
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
        Player playerAffected = getPlayerAffected();
        
        playerAffected.setAllowFlight(wasAllowedFlight);
        playerAffected.setFlying(wasFlying);
        playerAffected.setFlySpeed(flySpeed);
        
        playerAffected.setFallDistance(0F);
        
        if (removalTaskId != -1) {
            Bukkit.getScheduler().cancelTask(removalTaskId);
            removalTaskId = -1;
        }
    }
    
}
