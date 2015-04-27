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
import io.github.trystancannon.diagoncraftspells.spell.Firestorm;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;

/**
 * Creates a shroud of mob spawner fire particles around the player in 4 block
 * radius.
 * 
 * @author Trystan Cannon (tccannon@live.com)
 */
public class FirestormEffect extends SpellEffect {

    public static final int EFFECT_RADIUS = 4;
    
    public FirestormEffect(Plugin plugin, Wizard caster) {
        super(Firestorm.NAME, plugin, caster.getPlayer(), caster);
    }

    @Override
    public void start() {
        super.start();
        Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), this, 1L);
    }
    
    @Override
    public void run() {
        // Stop if the effect has been removed.
        if (isRemoved()) {
            return;
        }
        
        World playerWorld = getEntity().getWorld();
        Location playerLocation = getEntity().getLocation().add(0, 0, 1);
        
        // Play fire effects around the player.
        for (int x = -(EFFECT_RADIUS / 2); x < EFFECT_RADIUS / 2; x++) {
            for (int z = -(EFFECT_RADIUS / 2); z < EFFECT_RADIUS / 2; z++) {
                for (int y = 0; y < EFFECT_RADIUS / 2; y++) {
                    playerWorld.playEffect(new Location(playerWorld, playerLocation.getBlockX() + x, playerLocation.getBlockY() + y, playerLocation.getBlockZ() + z),
                                          Effect.MOBSPAWNER_FLAMES, 100);
                }
            }
        }
        
        // Reschedule this method to run every half second.
        Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), this, 10L);
    }
    
}
