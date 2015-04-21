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
package io.github.trystancannon.diagoncraftspells.spell;

import io.github.trystancannon.diagoncraftspells.player.Wizard;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

/**
 * Creates a single block of water on the block where the wizard clicks for
 * 5 seconds.
 * 
 * @deprecated Uses the old spell system.
 * @author Trystan Cannon
 */
public class Aquamenti extends Spell {

    /**
     * The name for this spell: "Aquamenti".
     */
    public static final String NAME = "Aquamenti";
    
    /**
     * The description of this spell's effects.
     */
    private static final List<String> DESCRIPTION = Arrays.asList("Creates a single block", "of water for 5 seconds.");
    
    public Aquamenti(Plugin plugin) {
        super(NAME, DESCRIPTION, plugin);
    }
    
    /**
     * Creates a block of water where the player clicks, either adjacent to
     * a targeted block or one block ahead of the player's eye location. This
     * block of water lasts for 5 seconds, then disappears.
     * 
     * @param caster
     *          The <code>Wizard</code> casting this spell.
     * @param targetBlock
     *          The block next to which the water will spawn.
     *          If <code>null</code>, then the water appears one block directly
     *          front of the player's eye location.
     * @param targetFace
     *          The face against which the water will spawn. If <code>null</code>,
     *          the water is spawned one block directly in front of the player's
     *          eye location.
     * @param target
     *          <code>null</code>.
     * @param args
     *          <code>null</code>.
     * 
     * @return
     *          <code>true</code> if the spell is cast successfully. This is
     *          always <code>true</code>.
     */
    @Override
    public boolean execute(Wizard caster, Block targetBlock, BlockFace targetFace, Entity target, String[] args) {
        // No target block? Cast it right in front of the wizard.
        if (targetBlock == null) {
            targetBlock = caster.getPlayer().getWorld().getBlockAt(caster.getPlayer().getEyeLocation().add(caster.getPlayer().getEyeLocation().getDirection()));
            targetFace = BlockFace.SELF;
        }
        
        AquamentiTask task = new AquamentiTask(targetBlock, targetFace);
        task.run();
        
        caster.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE.toString() + ChatColor.ITALIC + NAME + "!");
        return true;
    }
    
    /**
     * <code>Runnable</code> task which spawns and keeps track of the water
     * block. If the block hasn't been replaced or destroyed, then it is removed
     * after 5 seconds.
     * 
     * Each <code>AquamentiTask</code> reschedules itself as long as the water
     * block is still on track to be removed after its life span ends. Once
     * destroyed/replaced or the block is no longer to be removed, the task
     * stops rescheduling itself and dies out.
     */
    private class AquamentiTask implements Runnable {
        
        /**
         * Number of server ticks before the water block is removed.
         */
        private static final long LIFE_SPAN = 100L;
        
        /**
         * Actual <code>Block</code> object in the world which represents
         * the water spawned.
         */
        private final Block waterBlock;
        
        /**
         * Number of server ticks the block has been alive.
         */
        private int ticksLived = 0;
        
        public AquamentiTask(Block blockClicked, BlockFace faceClicked) {
            //blockLocation.subtract(directionToPlayer.toLocation(casterWorld));
            this.waterBlock = blockClicked.getRelative(faceClicked);
            waterBlock.setType(Material.WATER);
        }
        
        /**
         * Checks every tick if the block is still water and has not finished
         * its life span.
         * 
         * Once either of these conditions turns up false, then block is removed
         * if it hasn't been already, and the task stops rescheduling itself.
         */
        @Override
        public void run() {
            // Only reschedule or manipulate the block if it is still water.
            if (waterBlock.getType() == Material.WATER || waterBlock.getType() == Material.STATIONARY_WATER) {
                // Reschedule the check as long as the spell hasn't reached the end
                // of its life span.
                if (ticksLived < LIFE_SPAN) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), this, 1L);
                // Remove the water at the end of its life span.
                } else {
                    waterBlock.setType(Material.AIR);
                }
            }
            
            ticksLived++;
        }
        
    }
}
