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
import io.github.trystancannon.diagoncraftspells.spell.Glacius;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

/**
 * Freezes a block of water for 10 seconds, then reverts it to its original state.
 * 
 * @author Trystan Cannon
 */
public class GlaciusEffect extends LocationEffect {
    
        /**
         * The number of server ticks for which each <code>GlaciusEffect</code>
         * will be in action. After this life span has ended, the effect
         * will dissolve and the water will return to its normal, unfrozen state.
         */
        public static final int LIFE_SPAN = 200;
        
        /**
         * Whether or not the water block replaced is of material type
         * STATIONARY_WATER. This is used for resetting purposes.
         */
        private final boolean isStationary;
    
        public GlaciusEffect(Plugin plugin, Location blockLocation, Wizard caster) {
            super(Glacius.NAME, plugin, blockLocation, caster);
            this.isStationary = blockLocation.getBlock().getType() == Material.STATIONARY_WATER;
        }
        
        /**
         * Starts the scheduled task for the effect, initializing/satisfying any fields
         * or preconditions necessary.
         */
        @Override
        public void start() {
            // Ensure that the blocks are water, of course.
            if (getBlock().getType() == Material.WATER || getBlock().getType() == Material.STATIONARY_WATER) {
                // Freeze the block and begin the scheduled checks.
                getBlock().setType(Material.ICE);
                Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), this, LIFE_SPAN);

                super.start();
            }
        }
        
        /**
         * Resets the water to its unfrozen, natural state once the time limit
         * has been reached or the effect has been removed.
         */
        @Override
        public void run() {
            resetBlock();
            
            if (!isRemoved()) {
                remove();
            }
        }
        
        @Override
        public void remove() {
            super.remove();
            resetBlock();
        }
        
        /**
         * Resets the water block to its original state if it is currently ice.
         */
        private void resetBlock() {
            if (getBlock().getType() == Material.ICE) {
                getBlock().setType(isStationary ? Material.STATIONARY_WATER : Material.WATER);
            }
        }
    
}
