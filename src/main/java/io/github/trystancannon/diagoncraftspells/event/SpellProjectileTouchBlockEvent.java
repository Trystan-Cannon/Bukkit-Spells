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

import io.github.trystancannon.diagoncraftspells.spell.SpellProjectile;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Occurs when a <code>SpellProjectile</code>'s arrow enters a location whose
 * block is of a type stored in its <code>blockFilter</code> <code>HashMap</code>.
 * 
 * @author Trystan Cannon
 */
public final class SpellProjectileTouchBlockEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    
    /**
     * The projectile which touched a block in its <code>blockFilter</code>.
     */
    private final SpellProjectile projectile;
    
    /**
     * The location of the block which the projectile touched.
     */
    private final Location touchedBlockLocation;
    
    /**
     * The material of the block which is included in the projectile's
     * <code>blockFilter</code>.
     */
    private final Material touchedMaterial;
    
    public SpellProjectileTouchBlockEvent(SpellProjectile projectile) {
        this.projectile = projectile;
        this.touchedBlockLocation = projectile.getArrow().getLocation();
        this.touchedMaterial = touchedBlockLocation.getBlock().getType();
    }
    
    /**
     * @return
     *          The projectile which touched a block in its <code>blockFilter</code>.
     */
    public SpellProjectile getProjectile() {
        return projectile;
    }
    
    /**
     * @return
     *          The location of the block which the projectile touched.
     */
    public Location getTouchedBlockLocation() {
        return touchedBlockLocation;
    }
    
    /**
     * @return
     *          The material of the block which is included in the projectile's
     *          <code>blockFilter</code>.
     */
    public Material getTouchedMaterial() {
        return touchedMaterial;
    }
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
}
