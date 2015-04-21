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

import io.github.trystancannon.diagoncraftspells.event.SpellProjectileDeathEvent;
import io.github.trystancannon.diagoncraftspells.event.SpellProjectileStrikeGroundEvent;
import io.github.trystancannon.diagoncraftspells.event.SpellProjectileTouchBlockEvent;
import io.github.trystancannon.diagoncraftspells.player.Wizard;

import java.util.HashMap;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

/**
 * A spell projectile launches is an arrow which removes itself upon impact,
 * allowing a spell to recognize via <code>EntityDamageByEntityEvent</code> which
 * entity was struck by its projectile, imparting any desired effects.
 * 
 * The spell projectile maintains its launch launchVelocity until collision.
 *
 * @author Trystan Cannon
 */
public class SpellProjectile implements Runnable {

    /**
     * The maximum number of server ticks before the arrow will be removed,
     * for its velocity will never cause it to land.
     */
    public static final int LIFE_SPAN = 200;
    
    private final Arrow arrow;
    
    private final Plugin plugin;
    
    private final Wizard caster;
    
    private final Vector launchVelocity;
    
    private boolean isRemoved = false;
    
    /**
     * A list of all blocks that cause the projectile to call a <code>SpellProjectileTouchBlockEvent</code>.
     */
    private final HashMap<Material, Boolean> blockFilter;
    
    public SpellProjectile(Arrow arrow, Plugin plugin, Wizard caster) {
        this.arrow = arrow;
        this.plugin = plugin;
        this.caster = caster;
        this.launchVelocity = arrow.getVelocity();
        this.blockFilter = new HashMap<>();
    }
    
    /**
     * @return
     *          The UUID of the <code>Arrow</code> entity by which this projectile
     *          is embodied.
     */
    public UUID getUniqueId() {
        return arrow.getUniqueId();
    }
    
    public Wizard getCaster() {
        return caster;
    }
    
    public Arrow getArrow() {
        return arrow;
    }
    
    public void addFilterBlock(Material blockType) {
        blockFilter.put(blockType, true);
    }
    
    public void removeFilterBlock(Material blockType) {
        blockFilter.remove(blockType);
    }
    
    public boolean hasFilterBlock(Material blockType) {
        return blockFilter.get(blockType) != null;
    }
    
    public HashMap<Material, Boolean> getFilterBlocks() {
        return blockFilter;
    }
    
    public void remove() {
        isRemoved = true;
        
        arrow.remove();
        plugin.getServer().getPluginManager().callEvent(new SpellProjectileDeathEvent(this));
    }
    
    public boolean isRemoved() {
        return isRemoved;
    }
    
    @Override
    public void run() {
        if (isRemoved()) {
            return;
        }
        
        if (arrow.getTicksLived() > LIFE_SPAN) {
            remove();
        // Throw a SpellProjectileTouchBlock event if the arrow is currently
        // inside of a filter block type.
        } else if (blockFilter.get(arrow.getLocation().getBlock().getType()) != null) {
            plugin.getServer().getPluginManager().callEvent(new SpellProjectileTouchBlockEvent(this));
        } else if (!arrow.isOnGround() && !arrow.isDead()) {
            arrow.getWorld().playEffect(arrow.getLocation(), Effect.SMOKE, 50);
            arrow.setVelocity(launchVelocity);
            
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, this, 1L);
        } else {
            if (arrow.isOnGround()) {
                Location arrowStrikeLocation = arrow.getLocation();
                Vector direction = launchVelocity.normalize();
                double reach = 0.01;
                
                while (arrowStrikeLocation.getBlock().getType() == Material.AIR) {
                    arrowStrikeLocation.add(direction.multiply(reach));
                    reach += 0.01;
                }
                
                plugin.getServer().getPluginManager().callEvent(new SpellProjectileStrikeGroundEvent(this, arrowStrikeLocation));
            }
            
            remove();
        }
    }
    
}
