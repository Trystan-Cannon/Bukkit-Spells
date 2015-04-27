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
import io.github.trystancannon.diagoncraftspells.spell.Alohomora;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

/**
 * Transforms an iron door into a oak wooden door for 10 seconds.
 * 
 * @author Trystan Cannon (tccannon@live.com)
 */
public class AlohomoraEffect extends LocationEffect {
    
    public static final int LIFE_SPAN = 200;
    
    private int removalTaskId = -1;
    
    public AlohomoraEffect(Plugin plugin, Location blockLocation, Wizard caster) {
        super(Alohomora.NAME, plugin, blockLocation, caster);
    }
    
    @Override
    public void start() {
        super.start();
        removalTaskId = Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), this, LIFE_SPAN);
        
        // Make sure the block struck was the top door block.
        // If we try to use the bottom of the door, it will be destroyed
        // when we change its type to a wooden door.
        Block topBlock = getBlock().getLocation().add(0, 1, 0).getBlock();
        if (topBlock.getType() != Material.IRON_DOOR_BLOCK) {
            setBlock(getBlock().getLocation().add(0, -1, 0).getBlock());
        }
        
        // Deprecated method usage: What else can we do?
        getBlock().setTypeIdAndData(64, (byte) 0, false);
        getBlock().getLocation().add(0, 1, 0).getBlock().setTypeIdAndData(64, (byte) 8, true);
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
        
        // Make sure the block is still a door.
        if (getBlock().getType() == Material.WOODEN_DOOR) {
            getBlock().setTypeIdAndData(71, (byte) 0, false);
            getBlock().getLocation().add(0, 1, 0).getBlock().setTypeIdAndData(71, (byte) 8, true);
        }
        
        if (removalTaskId != -1) {
            Bukkit.getScheduler().cancelTask(removalTaskId);
            removalTaskId = -1;
        }
    }
    
}
