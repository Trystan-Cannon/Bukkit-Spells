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

import io.github.trystancannon.diagoncraftspells.player.Wizard;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

/**
 * Abstract implementation of a spell as represented in the plugin. Each spell
 * has its own execution method given some basic information about the call. Also,
 * each spell has a unique name that allows it to be identified.
 * 
 * @author Trystan Cannon
 */
public abstract class Spell {
    
    /**
     * The common speed at which all spell projectiles travel.
     */
    public static final float SPELL_PROJECTILE_SPEED = 5F;
    
    /**
     * The unique name for this spell.
     */
    private final String name;
    
    /**
     * The unique description for this spell.
     */
    private final List<String> description;
    
    /**
     * The plugin this spell uses to schedule tasks, register for events, and the like.
     */
    private final Plugin plugin;
    
    public Spell(String name, List<String> description, Plugin plugin) {
        this.name = name;
        this.description = description;
        this.plugin = plugin;
    }
    
    /**
     * Executes the spell given basic information about the caster and his surroundings.
     * Some of this information may be passed as <code>null</code>. However, the handling
     * of these <code>null</code> cases is up to the specific implementation.
     * 
     * @param caster
     *          Wizard casting the spell.
     * @param targetBlock
     *          Block on which the spell is targeted.
     * @param targetFace
     *          Block face on which the spell is targeted.
     * @param target
     *          The entity being targeted by the spell. This may be a player.
     * @param args
     *          Other arguments which may help the method carry out the desired effects of this spell.
     * 
     * @return
     *          <code>true</code> if the cast was successful.
     */
    public abstract boolean execute(Wizard caster, Block targetBlock, BlockFace targetFace, Entity target, String[] args);
    
    /**
     * @return
     *          The unique name of this spell.
     */
    public String getName() {
        return name;
    }
    
    /**
     * @return
     *          The description of this spell.
     */
    public List<String> getDescription() {
        return description;
    }
    
    /**
     * @return
     *          The plugin this spell uses for task scheduling and the like.
     */
    public Plugin getPlugin() {
        return plugin;
    }
    
    /**
     * Compares two spells by their unique names (case sensitive).
     * 
     * @param other
     *          Spell to compare to this.
     * 
     * @return
     *          <code>true</code> if the spells have the same name.
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Spell)) {
            return false;
        }
        
        Spell otherSpell = (Spell) other;
        return otherSpell.getName().equals(this.getName());
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }
    
    /**
     * Fires a spell projectile from this spell.
     * 
     * @param caster
     * @return
     *          The projectile fired.
     */
    public SpellProjectile fireSpellProjectileFromCaster(Wizard caster) {
        return new SpellProjectile(fireArrowFromCaster(caster, SPELL_PROJECTILE_SPEED), getPlugin(), caster);
    }
    
    /**
     * Fires an arrow from the caster's eye location in the direction they
     * they are looking.
     * 
     * @param caster
     *          <code>Wizard</code> from which the arrow will be fired.
     * @param speed
     *          The speed at which the arrow will initially travel.
     * 
     * @return
     *          The <code>Arrow</code> entity fired.
     */
    public static Arrow fireArrowFromCaster(Wizard caster, float speed) {
        Player player = caster.getPlayer();
        Location arrowLocation = player.getEyeLocation();
        Vector direction = arrowLocation.getDirection().multiply(2);
        
        // Make sure the arrow spawns a little in front of the caster.
        arrowLocation.add(arrowLocation.getDirection().multiply(2));
        Arrow arrowFired = player.getWorld().spawnArrow(arrowLocation, direction, speed, 0F);
        
        return arrowFired;
    }
    
    /**
     * Tells the caster that this spell has been cast with a message.
     * 
     * @param caster
     */
    protected void alertCast(Wizard caster) {
        caster.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + getName() + "!");
    }
    
}
