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
package io.github.trystancannon.diagoncraftspells.event;

import io.github.trystancannon.diagoncraftspells.player.Wizard;
import io.github.trystancannon.diagoncraftspells.spell.ProjectileSpell;
import io.github.trystancannon.diagoncraftspells.spell.SpellProjectile;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a <code>SpellProjectile</code> collides with a living entity.
 * 
 * @author Trystan Cannon
 */
public class SpellProjectileCollideEntityEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    
    /**
     * The living entity struck by the projectile.
     */
    private final LivingEntity entity;
    
    /**
     * The wizard who cast the spell which fired this projectile.
     */
    private final Wizard caster;
    
    /**
     * The projectile which collided with the entity.
     */
    private final SpellProjectile projectile;
    
    /**
     * The spell which fired the projectile.
     */
    private final ProjectileSpell spell;
    
    private boolean isCancelled = false;
    
    public SpellProjectileCollideEntityEvent(LivingEntity entityStruck, SpellProjectile projectile, ProjectileSpell spell, Wizard caster) {
        this.entity = entityStruck;
        this.caster = caster;
        this.projectile = projectile;
        this.spell = spell;
    }
    
    /**
     * @return
     *          The projectile which collided with the entity.
     */
    public SpellProjectile getProjectile() {
        return projectile;
    }
    
    /**
     * @return
     *          The spell which fired the projectile.
     */
    public ProjectileSpell getSpell() {
        return spell;
    }
    
    /**
     * @return
     *          The entity struck by the spell's projectile.
     */
    public LivingEntity getEntity() {
        return entity;
    }
    
    /**
     * @return
     *          The wizard who cast the spell which fired this projectile.
     */
    public Wizard getCaster() {
        return caster;
    }
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    /**
     * Stops the spell from affecting the entity struck.
     * 
     * @param cancel 
     */
    @Override
    public void setCancelled(boolean cancel) {
        isCancelled = cancel;
    }
    
}
