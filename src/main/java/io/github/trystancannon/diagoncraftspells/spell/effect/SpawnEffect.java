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

import io.github.trystancannon.diagoncraftspells.event.SpellEffectAddEvent;
import io.github.trystancannon.diagoncraftspells.event.SpellEffectEndEvent;
import io.github.trystancannon.diagoncraftspells.player.Wizard;

import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

/**
 * An abstraction of a <code>SpellEffect</code> which spawns an entity into the
 * world as opposed to affecting a <code>LivingEntity</code>.
 * 
 * Spawn effects are added to the caster's player entity as opposed to some
 * afflicted entity.
 * 
 * @author Trystan Cannon
 */
public abstract class SpawnEffect extends SpellEffect {

    /**
     * The entity spawned by this effect.
     */
    private Entity entitySpawned;
    
    public SpawnEffect(String name, Plugin plugin, Wizard caster) {
        super(name, plugin, null, caster);
    }
    
    @Override
    public void start() {
        getCaster().addEffect(this);
        produceAddEvent();
    }
    
    @Override
    public void remove() {
        getCaster().removeEffectFromList(this);
        produceEndEvent();
    }
    
    /**
     * Calls this spell effect as a <code>SpellEffectAddEvent</code>.
     */
    @Override
    public void produceAddEvent() {
        getPlugin().getServer().getPluginManager().callEvent(new SpellEffectAddEvent(this, getCaster().getPlayer(), getCaster()));
    }
    
    /**
     * Calls this spell effect as a <code>SpellEffectEndEvent</code>.
     */
    @Override
    public void produceEndEvent() {
        getPlugin().getServer().getPluginManager().callEvent(new SpellEffectEndEvent(this, getCaster().getPlayer(), getCaster()));
    }
    
    /**
     * Spawns the entity for the effect.
     */
    protected abstract void spawnEntity();
    
    /**
     * @return
     *          The entity spawned by this effect.
     *          This may be <code>null</code> if the effect has not yet been
     *          started or the child implementation has not made use of the protected
     *          method <code>setSpawnedEntity</code>.
     */
    public Entity getSpawnedEntity() {
        return entitySpawned;
    }
    
    /**
     * Sets the entity spawned by this effect to the given entity.
     * 
     * @param entity
     *          The entity to designate that which is spawned by this effect.
     */
    protected void setSpawnedEntity(Entity entity) {
        this.entitySpawned = entity;
    }
    
}
