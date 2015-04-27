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
import io.github.trystancannon.diagoncraftspells.spell.Orchideous;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.material.Tree;
import org.bukkit.plugin.Plugin;

/**
 * Spawns a temporary block of spruce leaves for 50 seconds.
 *
 * These leaves will not decay.
 *
 * @author Trystan Cannon (tccannon@live.com)
 */
public class OrchideousEffect extends LocationEffect implements Listener {

    public static final int LIFE_SPAN = 1000;

    private int removalTaskId = -1;

    public OrchideousEffect(Plugin plugin, Location blockLocation, Wizard caster) {
        super(Orchideous.NAME, plugin, blockLocation, caster);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void start() {
        super.start();
        removalTaskId = Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), this, LIFE_SPAN);

        getBlock().setType(Material.LEAVES);
        BlockState state = getBlock().getState();

        Tree leaf = (Tree) state.getData();
        leaf.setSpecies(TreeSpecies.REDWOOD);

        state.setData(leaf);
        state.update(true);
    }

    @EventHandler
    public void onLeafDecay(LeavesDecayEvent decay) {
        if (decay.getBlock().equals(getBlock())) {
            decay.setCancelled(true);
        }
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

        if (hasStarted() && getBlock().getType() == Material.LEAVES) {
            getBlock().setType(Material.AIR);
        }
        
        if (removalTaskId != -1) {
            Bukkit.getScheduler().cancelTask(removalTaskId);
            removalTaskId = -1;
        }
    }

}
