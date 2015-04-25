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
import io.github.trystancannon.diagoncraftspells.player.Wizard;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

/**
 * Launches the struck creature or nearby object (on ground) towards the
 * caster.
 * 
 * As the distance between the two increases, the more likely it is that
 * the launched entity or object will overshoot the caster.
 * 
 * @author Trystan Cannon
 */
public class Accio extends ProjectileSpell {
    
    public static final String NAME = "Accio";
    
    public static final List<String> DESCRIPTION = Arrays.asList("Pulls the struck creature or object", "towards the caster.");
    
    public Accio(Plugin plugin) {
        super(NAME, DESCRIPTION, plugin);
    }

    @Override
    public void onProjectileStrikeGround(Wizard caster, Block blockStruck) {
        Location strikeLocation = blockStruck.getLocation();
        
        for (Item itemOnGround : strikeLocation.getWorld().getEntitiesByClass(Item.class)) {
            if (itemOnGround.getLocation().distanceSquared(strikeLocation) <= 3) {
                itemOnGround.setVelocity(getRelativeVelocityToPlayer(caster.getPlayer().getLocation(), itemOnGround.getLocation()));
                return;
            }
        }
    }

    @Override
    public void onProjectileStrikeLivingEntity(LivingEntity entity, SpellProjectileCollideEntityEvent collisionEvent, Wizard caster) {
        if (!collisionEvent.isCancelled()) {
            entity.setVelocity(getRelativeVelocityToPlayer(caster.getPlayer().getLocation(), entity.getLocation()));
        }
    }
    
    /**
     * @return
     *          A <code>Vector</code> which acts as an entity's velocity, pulling
     *          the entity towards the player at an appropriate speed to cover
     *          a relevant distance.
     * 
     *          If an item on the ground is only 5 blocks away, the velocity should
     *          be of a small magnitude. However, if it was 100 blocks away, the magnitude
     *          of the <code>Vector</code> returned would be much larger.
     */
    private Vector getRelativeVelocityToPlayer(Location playerLocation, Location entityLocation) {
        // Flatten the y values for the locations, so there is minimal upward movement.
        playerLocation.setY(Math.round(playerLocation.getY()));
        entityLocation.setY(Math.round(entityLocation.getY()));
        
        // This is the direction from the entity to the player.
        Vector toPlayerDirection = playerLocation.getDirection().multiply(-1);
        // Compute a magnitude for the velocity of the entity so it reaches the player.
        // Magnitude minimum is 0.5
        double magnitude = Math.max(playerLocation.distanceSquared(entityLocation)/ 50, 0.5);
        
        return toPlayerDirection.multiply(magnitude);
    }
    
}
