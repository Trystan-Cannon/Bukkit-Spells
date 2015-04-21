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

import io.github.trystancannon.diagoncraftspells.core.DiagonCraftSpellsPlugin;
import io.github.trystancannon.diagoncraftspells.event.SpellEffectAddEvent;
import io.github.trystancannon.diagoncraftspells.event.SpellEffectEndEvent;
import io.github.trystancannon.diagoncraftspells.player.Wizard;

import org.bukkit.ChatColor;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Abstract implementation of all effects caused by spells. Spell effects allow
 * the plugin and other developers to identify which effects currently affecting
 * a player are caused by spells. In knowing this, effects may be canceled or
 * amplified.
 * 
 * Each effect has its own unique name, allowing it to be identified.
 * 
 * All effects must be explicitly started before an event or scheduled task
 * is created.
 * 
 * @author Trystan Cannon
 */
public abstract class SpellEffect implements Runnable {
    
    /**
     * The unique name for this effect.
     */
    private final String name;
    
    /**
     * The entity affected by this spell. Can be <code>null</code>.
     */
    private final LivingEntity entityAffected;
    
    /**
     * The wizard casting the spell which created this effect.
     */
    private final Wizard caster;
    
    /**
     * The plugin instance used for scheduling tasks, registering for events, etc.
     */
    private final Plugin plugin;
    
    /**
     * Whether or not the effect has been removed.
     */
    private boolean isRemoved = false;
    
    public SpellEffect(String name, Plugin plugin, LivingEntity entityAffected, Wizard caster) {
        this.name = name;
        this.plugin = plugin;
        this.entityAffected = entityAffected;
        this.caster = caster;
    }
    
    /**
     * @return
     *          The unique name for this spell effect.
     */
    public String getName() {
        return name;
    }
    
    /**
     * @return
     *          The plugin instance used for scheduling tasks, registering for events, etc.
     */
    public Plugin getPlugin() {
        return plugin;
    }
    
    /**
     * @return
     *          The entity currently affected by this effect. Can be <code>null</code>
     *          if no entity is affected.
     */
    public LivingEntity getEntity() {
        return entityAffected;
    }
    
    /**
     * @return
     *          The wizard who casted the spell which created this effect.
     */
    public Wizard getCaster() {
        return caster;
    }
    
    /**
     * @return
     *          Whether or not this effect has been removed.
     */
    public boolean isRemoved() {
        return isRemoved;
    }
    
    /**
     * Carries out the effect.
     */
    @Override
    public abstract void run();
    
    /**
     * Starts the effect, producing an add event and adding the effect to
     * the affected wizard's effect list, if the entity affected is a wizard.
     * 
     * This method does NOT run the <code>run</code> method.
     */
    public void start() {
        if (getEntity() instanceof Player) {
            getEntity().sendMessage(ChatColor.ITALIC + "You have been affected by " + ChatColor.LIGHT_PURPLE + getName() + "!");
            Wizard wizard = DiagonCraftSpellsPlugin.getWizard(((Player) getEntity()).getUniqueId());
            
            if (wizard != null) {
                wizard.addEffect(this);
            }
        }
        
        produceAddEvent();
    }
    
    /**
     * Removes the spell effect from the afflicted entity, removing the effect
     * from an affected wizard's effect list, if any.
     */
    public void remove() {
        if (getEntity() instanceof Player) {
            getEntity().sendMessage(ChatColor.ITALIC + "You are now free from the effects of " + ChatColor.LIGHT_PURPLE + getName() + "!");
            Wizard wizard = DiagonCraftSpellsPlugin.getWizard(((Player) getEntity()).getUniqueId());
            
            if (wizard != null) {
                wizard.removeEffectFromList(this);
            }
        }
        
        isRemoved = true;
        produceEndEvent();
    }
    
    /**
     * Sets whether or not this effect has been removed from the affected entity.
     * 
     * @param isRemoved
     *      Whether or not this effect has been removed from the affected entity.
     */
    protected void setRemoved(boolean isRemoved) {
        this.isRemoved = isRemoved;
    }
    
    /**
     * Calls this spell effect as a <code>SpellEffectAddEvent</code>.
     */
    public void produceAddEvent() {
        getPlugin().getServer().getPluginManager().callEvent(new SpellEffectAddEvent(this, entityAffected, caster));
    }
    
    /**
     * Calls this spell effect as a <code>SpellEffectEndEvent</code>.
     */
    public void produceEndEvent() {
        getPlugin().getServer().getPluginManager().callEvent(new SpellEffectEndEvent(this, entityAffected, caster));
    }
}
