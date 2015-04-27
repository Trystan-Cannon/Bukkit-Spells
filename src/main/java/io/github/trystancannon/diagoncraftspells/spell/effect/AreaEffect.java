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

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

/**
 * Similar to a <code>LocationEffect</code>, except this type of effect
 * affects an area in a square radius of blocks.
 * 
 * @author Trystan Cannon (tccannon@live.com)
 */
public abstract class AreaEffect extends LocationEffect {
    
    /**
     * The radius, in blocks from the struck block, that this effect affects.
     * This radius extends only on the x and z axes.
     */
    private final int radius;
    
    /**
     * All blocks affected by this effect. The block at index 0 is the
     * center block.
     */
    private final List<Block> affectedBlocks;

    public AreaEffect(String name, Plugin plugin, Location blockLocation, Wizard caster, final int radius) {
        super(name, plugin, blockLocation, caster);
        
        this.radius = radius;
        this.affectedBlocks = new ArrayList<>();
        
        updateAffectedBlocks();
    }
    
    /**
     * Changes the block this effect is affecting, and this method
     * updates all affected blocks given the provided block as the origin.
     * 
     * @param block
     *           The new block to affect.
     */
    @Override
    protected void setBlock(Block block) {
        super.setBlock(block);
        updateAffectedBlocks();
    }
    
    /**
     * Using the current block returned by <code>getBlock</code>,
     * this method updates the <code>affectedBlocks</code> list with all
     * blocks surrounding the original within the radius of <code>getRadius</code>.
     */
    private void updateAffectedBlocks() {
        Location blockLocation = getBlock().getLocation();
        
        affectedBlocks.clear();
        
        for (int x = -(radius / 2); x <= radius / 2; x++) {
            for (int z = -(radius / 2); z <= radius / 2; z++) {
                affectedBlocks.add(blockLocation.getWorld().getBlockAt(blockLocation.getBlockX() + x, blockLocation.getBlockY(), blockLocation.getBlockZ() + z));
            }
        }
    }
    
    /**
     * @return
     *          The radius, in blocks from the struck block, that this effect
     *          affects.
     */
    public int getRadius() {
        return radius;
    }
    
    /**
     * @return
     *          All blocks affected by this effect.
     */
    public List<Block> getAffectedBlocks() {
        return affectedBlocks;
    }
    
}
