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
import io.github.trystancannon.diagoncraftspells.spell.Avis;
import org.bukkit.Bukkit;

import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;

/**
 * Spawns a bat in front of the caster that lasts for 10 seconds.
 * 
 * @author Trystan Cannon
 */
public class AvisEffect extends SpawnEffect {

    /**
     * The number of server ticks for which this effect will be active after starting.
     */
    public static int LIFE_SPAN = 200;
    
    public AvisEffect(Plugin plugin, Wizard caster) {
        super(Avis.NAME, plugin, caster);
    }

    @Override
    protected void spawnEntity() {
        setSpawnedEntity(getCaster().getPlayer().getWorld().spawnEntity(getCaster().getPlayer().getLocation(), EntityType.BAT));
    }
    
    @Override
    public void start() {
        super.start();
        spawnEntity();
        
        Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), this, LIFE_SPAN);
    }

    @Override
    public void run() {
        if (!isRemoved()) {
            remove();
        }
    }
    
    @Override
    public void remove() {
        if (getSpawnedEntity() != null) {
            getSpawnedEntity().remove();
        }
        
        super.remove();
    }
    
}
