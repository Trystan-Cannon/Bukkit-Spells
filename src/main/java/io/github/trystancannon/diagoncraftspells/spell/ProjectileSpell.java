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

import io.github.trystancannon.diagoncraftspells.event.SpellProjectileCollideEntityEvent;
import io.github.trystancannon.diagoncraftspells.event.SpellProjectileDeathEvent;
import io.github.trystancannon.diagoncraftspells.event.SpellProjectileStrikeGroundEvent;
import io.github.trystancannon.diagoncraftspells.event.SpellProjectileTouchBlockEvent;
import io.github.trystancannon.diagoncraftspells.player.Wizard;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;

/**
 * An abstraction layer for spells. All Projectile Spells fire a projectile and
 * therefore must keep track of their fired projectiles.
 * 
 * Each <code>ProjectileSpell</code> registers itself for events.
 * 
 * @author Trystan Cannon
 */
public abstract class ProjectileSpell extends Spell implements Listener {
    
    /**
     * All projectiles for this spell that have not been removed from the world
     * i.e. they have not struck an entity or landed.
     */
    private final HashMap<UUID, SpellProjectile> projectiles;
    
    /**
     * A list of all blocks that cause a projectile from this spell to call a
     * <code>SpellProjectileTouchBlockEvent</code>.
     */
    private final HashMap<Material, Boolean> blockFilter;
    
    public ProjectileSpell(String name, List<String> description, Plugin plugin) {
        super(name, description, plugin);
        getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());
        
        this.projectiles = new HashMap<>();
        this.blockFilter = new HashMap<>();
    }
    
    /**
     * Removes the given entity from the projectiles list if it is a registered
     * projectile.
     * 
     * @param projectile 
     */
    public void removeProjectile(Entity projectile) {
        if (projectiles.get(projectile.getUniqueId()) != null) {
            projectiles.remove(projectile.getUniqueId());
        }
    }
    
    /**
     * Adds a projectile to the fired projectile list.
     * 
     * @param projectile 
     */
    public void addProjectile(SpellProjectile projectile) {
        projectiles.put(projectile.getUniqueId(), projectile);
    }
    
    /**
     * Fires a projectile and adds it to the projectiles list without
     * the overhead.
     * 
     * @param caster
     *          The <code>Wizard</code> casting the spell that fires this projectile.
     */
    public void fireProjectile(Wizard caster) {
        SpellProjectile projectile = fireSpellProjectileFromCaster(caster);
        addProjectile(projectile);
        
        // Add all of the filter blocks.
        for (Material filterType : blockFilter.keySet()) {
            projectile.addFilterBlock(filterType);
        }
        
        projectile.run();
    }
    
    /**
     * @return
     *          All projectiles for this spell that have not been removed from the
     *          world i.e. they have not struck an entity or landed.
     */
    public HashMap<UUID, SpellProjectile> getProjectiles() {
        return projectiles;
    }
    
    /**
     * Adds the given block type to the <code>blockFilter</code> for all
     * projectiles spawned by this spell.
     * 
     * @param blockType
     *          The type of block to add.
     */
    public void addFilterBlock(Material blockType) {
        blockFilter.put(blockType, true);
    }
    
    /**
     * Removes the given block type to the <code>blockFilter</code> for all
     * projectiles spawned by this spell.
     * 
     * @param blockType
     *          The type of block to remove.
     */
    public void removeFilterBlock(Material blockType) {
        blockFilter.remove(blockType);
    }
    
    
    /**
     * Checks if the <code>blockFilter</code> for all projectiles spawned by
     * this spell has the given type of block.
     * 
     * @param blockType
     *          Type of block to check.
     * @return
     *          Whether or not the type exists in the <code>blockFilter</code>.
     */
    public boolean hasFilterBlock(Material blockType) {
        return blockFilter.get(blockType) != null;
    }
    
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent damage) {
        if (projectiles.get(damage.getDamager().getUniqueId()) != null) {
            damage.setCancelled(true);
            damage.getDamager().remove();
            
            if (damage.getEntity() instanceof LivingEntity) {
                SpellProjectile projectile = projectiles.get(damage.getDamager().getUniqueId());
                LivingEntity entity = (LivingEntity) damage.getEntity();
                SpellProjectileCollideEntityEvent event = new SpellProjectileCollideEntityEvent(entity, projectile, this, projectile.getCaster());
                
                getPlugin().getServer().getPluginManager().callEvent(event);
                onProjectileStrikeLivingEntity(entity, event, projectile.getCaster());
            }
            
            projectiles.remove(damage.getDamager().getUniqueId());
        }
    }
    
    @EventHandler
    public void onSpellProjectileStrikeGround(SpellProjectileStrikeGroundEvent strike) {
        if (projectiles.get(strike.getProjectile().getUniqueId()) != null) {
            onProjectileStrikeGround(strike.getProjectile().getCaster(), strike.getLocation().getBlock());
        }
    }
    
    @EventHandler
    public void onSpellProjectileDeath(SpellProjectileDeathEvent death) {
        if (projectiles.get(death.getProjectile().getUniqueId()) != null) {
            onProjectileDeath(death.getProjectile().getCaster(), death.getProjectile());
        }
    }
    
    @EventHandler
    public void onSpellProjectileTouchBlock(SpellProjectileTouchBlockEvent touch) {
        if (projectiles.get(touch.getProjectile().getUniqueId()) != null) {
            onProjectileTouchFilterBlock(touch);
        }
    }
    
    /**
     * Handles the case in which one of the projectile's spawned by this spell
     * comes into contact with a filter block.
     * 
     * @param touch
     *          The event called by the projectile which touched the filter block.
     */
    public void onProjectileTouchFilterBlock(SpellProjectileTouchBlockEvent touch) {
    }
    
    /**
     * Handles the case in which one of this spell's projectiles has struck a
     * living entity.
     * 
     * @param entity
     *          The entity struck by this spell's projectile.
     * @param collisionEvent
     *          The event called due to this collision. Used to check for cancellation.
     * @param caster
     *          The wizard who cast this spell.
     */
    public void onProjectileStrikeLivingEntity(LivingEntity entity, SpellProjectileCollideEntityEvent collisionEvent, Wizard caster) {
    }
    
    /**
     * Handles the case in which the projectile has landed in the ground.
     * 
     * @param caster
     *          The wizard who cast this spell.
     * @param blockStruck
     *          The block struck by the projectile.
     */
    public void onProjectileStrikeGround(Wizard caster, Block blockStruck) {
    }
    
    /**
     * Handles the case in which arrow representing the spell projectile
     * has been marked as dead by <code>isDead</code>.
     * 
     * @param caster
     *          The wizard who caster the spell.
     * @param projectile
     *          The projectile which has died.
     */
    public void onProjectileDeath(Wizard caster, SpellProjectile projectile) {
    }
    
    /**
     * Handles the case in which one of this spell's proctiles is marked as
     * "dead" by Bukkit. This occurs when the projectile is now longer moving i.e.
     * has hit a <code>LivingEntity</code> or is stuck in the ground.
     * 
     * @param caster
     *          The wizard who cast this spell.
     * @param projectile
     *          The spell projectile which has died.
     */
    /*public void onProjectileDeath(Wizard caster, SpellProjectile projectile) {
    }*/
    
    @Override
    public boolean execute(Wizard caster, Block targetBlock, BlockFace targetFace, Entity target, String[] args) {
        fireProjectile(caster);
        alertCast(caster);
        
        return true;
    }
    
}
