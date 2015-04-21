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
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;

/**
 * Creates a weak explosion in the ground. The explosion
 * occurs at the location at which the spell projectile lands.
 * 
 * @author Trystan Cannon
 */
public class Bombarda extends ProjectileSpell {

    public static final String NAME = "Bombarda";
    
    public static final List<String> DESCRIPTION = Arrays.asList("Creates a small exploision", "upon impact.");
    
    /**
     * Power of the explosion created when the spell's projectile strikes the ground.
     */
    public static final float EXPLOSIVENESS = 1F;
    
    public Bombarda(Plugin plugin) {
        super(NAME, DESCRIPTION, plugin);
    }
    
    /**
     * Creates an explosion of power <code>EXPLOSIVENESS</code> at the given
     * location.
     * 
     * @param projectileLocation
     *          Location at which the explosion will occur (where the projectile hits).
     */
    private void explodeProjectile(Location projectileLocation) {
        projectileLocation.getWorld().createExplosion(projectileLocation, EXPLOSIVENESS);
    }
    
    @Override
    public void onProjectileStrikeLivingEntity(LivingEntity entity, SpellProjectileCollideEntityEvent collisionEvent, Wizard caster) {
        explodeProjectile(entity.getLocation());
    }

    @Override
    public void onProjectileStrikeGround(Wizard caster, Block blockStruck) {
        explodeProjectile(blockStruck.getLocation());
    }
    
}
