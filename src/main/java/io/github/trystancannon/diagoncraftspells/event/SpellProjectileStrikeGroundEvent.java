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
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * Called when a <code>SpellProjectile</code> strikes the ground.
 * 
 * It is important to know that, once the arrow representing a <code>SpellProjectile</code>
 * has been marked "dead" by <code>isDead()</code>, it is removed from the world,
 * so using the arrow entity of the projectile will not allow the programmer to extract
 * information regarding the world around the projectile.
 * 
 * @author Trystan Cannon
 */
public final class SpellProjectileStrikeGroundEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    
    private final SpellProjectile projectile;
    
    private final Location location;
    
    public SpellProjectileStrikeGroundEvent(SpellProjectile projectile, Location locationHit) {
        this.projectile = projectile;
        this.location = locationHit;
    }
    
    /**
     * @return
     *          The projectile which struck the ground, calling this event.
     */
    public SpellProjectile getProjectile() {
        return projectile;
    }
    
    /**
     * @return
     *          The location at which the projectile struck.
     */
    public Location getLocation() {
        return location;
    }
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
}
