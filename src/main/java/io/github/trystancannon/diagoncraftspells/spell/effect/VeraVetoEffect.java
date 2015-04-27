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
import io.github.trystancannon.diagoncraftspells.spell.VeraVeto;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;

/**
 * Transforms any non-monster mob into a pot for 20 seconds.
 * 
 * @author Trystan Cannon (tccannon@live.com)
 */
public class VeraVetoEffect extends SpellEffect {

    public static final int LIFE_SPAN = 400;
    
    private Location flowerPotLocation;
    
    private int removalTaskId = -1;
    
    public VeraVetoEffect(Plugin plugin, LivingEntity entityAffected, Wizard caster) {
        super(VeraVeto.NAME, plugin, entityAffected, caster);
    }

    @Override
    public void start() {
        super.start();
        removalTaskId = Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), this, LIFE_SPAN);
        
        getEntity().getLocation().getBlock().setType(Material.FLOWER_POT);
        flowerPotLocation = getEntity().getLocation();
        
        getEntity().remove();
        Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), this, LIFE_SPAN);
    }
    
    @Override
    public void run() {
        if (!isRemoved()) {
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
        
        // This isn't perfect: The entity won't retain any of its original
        // characterstics beyond its type. For example, a pink sheep will spawn
        // back in as a white one.
        if (hasStarted() && flowerPotLocation.getBlock().getType() == Material.FLOWER_POT) {
            flowerPotLocation.getWorld().spawnEntity(flowerPotLocation, getEntity().getType());
            flowerPotLocation.getBlock().setType(Material.AIR);
        }
    }
    
}
