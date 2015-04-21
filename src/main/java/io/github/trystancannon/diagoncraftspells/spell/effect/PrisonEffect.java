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

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

/**
 * Abstraction layer between SpellEffect and Spells that have effects such as
 * that of Duro, Crustulam, and Impedimentio. These spell surround an entity
 * with a particular material.
 * 
 * Therefore, all prison spells surround a living entity with the block of
 * a given type for a set amount of time.
 * 
 * @author Trystan Cannon
 */
public abstract class PrisonEffect extends SpellEffect {
    
    /**
     * The material out of which the prison is made.
     */
    private final Material prisonMaterial;
    
    /**
     * The number of server ticks for which the spell should be in effect.
     */
    private final int lifeSpan;
    
    /**
     * The blocks representing the stone case in which the entity is stuck.
     */
    private final Block[][][] prison;
    
    /**
     * The block which caps the top of the prison. This exists because I'm bad
     * at programming.
     */
    private Block prisonCap;
    
    /**
     * The location at which the entity was when they were struck with the spell.
     * This is set when the <code>start</code> method is called.
     */
    private Location initialLocation;
    
    public PrisonEffect(String name, Plugin plugin, LivingEntity entityAffected, Wizard caster, Material material, int lifeSpan) {
        super(name, plugin, entityAffected, caster);
        
        this.prisonMaterial = material;
        this.lifeSpan = lifeSpan;
        this.prison = new Block[3][(int) entityAffected.getEyeHeight() + 2][3];
    }

    /**
     * Starts the effect by creating the prison around the entity affected and
     * beginning the removal scheduled task @see <code>run</code>.
     */
    @Override
    public void start() {
        super.start();
        
        LivingEntity entityAffected = getEntity();
        initialLocation = entityAffected.getLocation();
        
        
        // Make sure the entity starts in the location around which the
        // prison is constructed, ensuring that the entity does not phase
        // through the blocks upon creation.
        entityAffected.teleport(initialLocation);
        entityAffected.setVelocity(new Vector(0, 0, 0));

        for (int y = -1; y < (int) entityAffected.getEyeHeight() + 1; y++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    Block block = entityAffected.getWorld().getBlockAt(initialLocation.getBlockX() + x, initialLocation.getBlockY() + y + 1, initialLocation.getBlockZ() + z);
                    
                    if (!isEntityInBlock(entityAffected, block) && (block.getType() == Material.AIR || block.isLiquid())) {
                        prison[x + 1][y + 1][z + 1] = block;
                        block.setType(prisonMaterial);
                    }
                }
            }
        }
        
        
        // Put a cap on the prison because my code is bad. I'm, uh, not very
        // good at programming.
        prisonCap = entityAffected.getWorld().getBlockAt(initialLocation.getBlockX(),
                (int) Math.round(initialLocation.getBlockY() + entityAffected.getEyeHeight()),
                initialLocation.getBlockZ());
        prisonCap.setType(prisonMaterial);
        
        // Schedule the taks which will remove the prison when the effect's life
        // span ends.
        Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), this, lifeSpan);
    }
    
    private static boolean isEntityInBlock(LivingEntity entity, Block block) {
        Location location = entity.getLocation();
        
        return block.getX() == location.getBlockX() && block.getZ() == location.getBlockZ() &&
               block.getY() >= location.getBlockY() && block.getY() < location.getBlockY() + entity.getEyeHeight();
    }
    
    /**
     * Removes the prison blocks which have gone undisturbed from around the
     * entity affected.
     * 
     * Also removes the spell effect from the effected entity.
     */
    @Override
    public void run() {
        // Remove the stone prison.
        for (Block[][] layer : prison) {
            for (Block[] section : layer) {
                for (Block block : section) {
                    if (block != null && block.getType() == prisonMaterial) {
                        block.setType(Material.AIR);
                    }
                }
            }
        }
        
        if (prisonCap.getType() == prisonMaterial) {
            prisonCap.setType(Material.AIR);
        }
        
        // Remove the effect from the entity if it has not yet been forcibly
        // removed.
        if (!isRemoved()) {
            remove();
        }
    }

    @Override
    public void remove() {
        super.remove();
        run();
    }
    
    /**
     * @return The blocks representing the stone case in which the entity is
     * stuck.
     */
    public Block[][][] getPrison() {
        return prison;
    }
    
    /**
     * @return
     *          The location at which the affected entity is placed when the
     *          effect is started. May be <code>null</code> if the effect has
     *          not yet been started.
     */
    public Location getLocation() {
        return initialLocation;
    }
    
    /**
     * @return
     *          The material out of which the prison is made.
     */
    public Material getMaterial() {
        return prisonMaterial;
    }
    
    /**
     * @return
     *          The number of server ticks for which the spell is in effect once it starts.
     */
    public int getLifeSpan() {
        return lifeSpan;
    }
    
}
