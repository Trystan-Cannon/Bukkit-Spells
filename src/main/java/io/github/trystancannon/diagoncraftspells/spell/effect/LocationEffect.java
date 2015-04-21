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

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

/**
 * A type of spell effect which affects a block at a specified location as opposed
 * to a <code>LivingEntity</code>.
 * 
 * @author Trystan Cannon
 */
public abstract class LocationEffect extends SpellEffect {
    
    /**
     * The location of the block which is to be affected.
     */
    private final Location blockLocation;
    
    public LocationEffect(String name, Plugin plugin, Location blockLocation, Wizard caster) {
        super(name, plugin, null, caster);
        this.blockLocation = blockLocation;
    }
    
    /**
     * @return
     *          The block being affected.
     */
    public Block getBlock() {
        return blockLocation.getBlock();
    }
    
    /**
     * @return
     *          The location of the block being affected.
     */
    public Location getBlockLocation() {
        return blockLocation;
    }
    
}
