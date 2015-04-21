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
import io.github.trystancannon.diagoncraftspells.spell.Imperio;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

/**
 * Allows the caster to take control of the player struck, meaning that they
 * may move the player around by changing the direction in which they are looking.
 * 
 * @author Trystan Cannon
 */
public class ImperioEffect extends SpellEffect implements Listener {
    
    /**
     * Whether or not the victim was allowed flight when the effect began.
     */
    private boolean wasAllowedFlight;
    
    /**
     * Whether or not the victim was flying when the effect began.
     */
    private boolean wasFlying;
    
    /**
     * The flight speed of the victim when the effect began.
     */
    private float initialSpeed;
    
    public ImperioEffect(Plugin plugin, Player victim, Wizard caster) {
        super(Imperio.NAME, plugin, victim, caster);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    @Override
    public void start() {
        super.start();
        Player victim = getVictim();
        
        wasAllowedFlight = victim.getAllowFlight();
        wasFlying = victim.isFlying();
        initialSpeed = victim.getFlySpeed();
        
        getVictim().setAllowFlight(true);
        getVictim().setFlying(true);
        getVictim().setFlySpeed(0);
    }
    
    @Override
    public void remove() {
        super.remove();
        Player victim = getVictim();
        
        victim.setAllowFlight(wasAllowedFlight);
        victim.setFlying(wasFlying);
        victim.setFlySpeed(initialSpeed);
    }
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent move) {
        Wizard wizard = DiagonCraftSpellsPlugin.getWizard(move.getPlayer().getUniqueId());
        
        if (!isRemoved() && wizard != null && wizard.getUniqueId().equals(getCaster().getUniqueId()) && !getEntity().isDead()) {
            Player caster = move.getPlayer();
            Player victim = getVictim();
            
            Location location = caster.getLocation();
            location.add(location.getDirection().multiply(3));
            location.setDirection(getEntity().getLocation().getDirection());
            
            victim.teleport(location);
            victim.setFallDistance(0);
        }
    }
    
    /**
     * @return
     *          The player being controlled by this effect.
     */
    public Player getVictim() {
        return (Player) getEntity();
    }
    
    @Override
    public void run() {
    }
    
}
