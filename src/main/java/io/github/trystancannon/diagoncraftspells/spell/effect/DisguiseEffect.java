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
import io.github.trystancannon.diagoncraftspells.player.Wizard;

import pgDev.bukkit.DisguiseCraft.api.PlayerUndisguiseEvent;
import pgDev.bukkit.DisguiseCraft.disguise.Disguise;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import pgDev.bukkit.DisguiseCraft.api.DisguiseCraftAPI;
import pgDev.bukkit.DisguiseCraft.disguise.DisguiseType;

/**
 * Abstract implementation of a <code>SpellEffect</code> which deals with
 * disguises.
 * 
 * DisguiseEffect's have super methods which handle <code>UndisguiseEvent</code>,
 * removing the effect upon undisguise.
 * 
 * @author Trystan Cannon
 */
public abstract class DisguiseEffect extends SpellEffect implements Listener {

    public DisguiseEffect(String name, Plugin plugin, Player playerAffected, Wizard caster) {
        super(name, plugin, playerAffected, caster);
    }
    
    /**
     * Registers the <code>DisguiseEffect</code> for events.
     */
    @Override
    public void start() {
        super.start();
        getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());
    }
    
    /**
     * Removes the disguise from the player affected.
     */
    @Override
    public void remove() {
        super.remove();
        
        // Remove the disguise if the player is disguised, and the effect has been started.
        if (hasStarted() && DiagonCraftSpellsPlugin.getDisguiseAPI().isDisguised(getPlayerAffected())) {
            DiagonCraftSpellsPlugin.getDisguiseAPI().undisguisePlayer(getPlayerAffected());
        }
    }
    
    @EventHandler
    public void onPlayerUndisguise(PlayerUndisguiseEvent undisguise) {
        if (hasStarted() && !isRemoved() && undisguise.getPlayer().getUniqueId().equals(getPlayerAffected().getUniqueId())) {
            remove();
        }
    }
    
    /**
     * @return
     *          The player disguised by this effect.
     */
    public Player getPlayerAffected() {
        return (Player) getEntity();
    }
    
    /**
     * @return
     *          The disguise of the player affected.
     */
    public Disguise getDisguise() {
        return DiagonCraftSpellsPlugin.getDisguiseAPI().getDisguise(getPlayerAffected());
    }
    
    /**
     * Disguises the effect's affected player.
     * 
     * @param type
     *          The type of disguise.
     * @return
     *          The entity id for the disguise entity;
     */
    public int disguisePlayer(DisguiseType type) {
        if (type != null) {
            DisguiseCraftAPI dcApi = DiagonCraftSpellsPlugin.getDisguiseAPI();
            int entityId = dcApi.newEntityID();
            
            dcApi.disguisePlayer(getCaster().getPlayer(), new Disguise(entityId, type));
            
            return entityId;
        }
        
        return -1;
    }
    
}