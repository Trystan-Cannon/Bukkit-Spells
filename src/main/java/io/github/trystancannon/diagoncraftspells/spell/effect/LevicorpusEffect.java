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

import io.github.trystancannon.diagoncraftspells.player.Wizard;
import io.github.trystancannon.diagoncraftspells.spell.Levicorpus;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

/**
 * Launches the caster into the air and keeps them there for 1 minute.
 * 
 * Teleports the player back to their float location if they try to move.
 * 
 * @author Trystan Cannon
 */
public final class LevicorpusEffect extends SpellEffect implements Listener {

    /**
     * The number of server ticks for which the effect lasts.
     */
    public static final int LIFE_SPAN = 1200;
    
    /**
     * The number of server ticks before the caster is frozen where they
     * reached after launch.
     */
    public static final int RUN_DELAY = 3;
    
    /**
     * The location at which the caster will stay once the effect engages.
     */
    private Location floatLocation = null;
    
    /**
     * Because the fly speed is overridden, this keeps track of the initial speed
     * for reset purposes.
     */
    private float initialFlySpeed;
    
    /**
     * Because the player's flight state is altered, this keeps track of whether
     * or not the player was flying when they were frozen for reset purposes.
     */
    private boolean wasFlying;
    
    /**
     * The original truth of whether or not the player was allowed flight, for the
     * flight state is changed, so this is needed for reset purposes.
     */
    private boolean isAllowedFlight;
    
    /**
     * The task id for the task which, if not manually removed, will remove
     * the effect from the player. This is needed to cancel the event properly.
     */
    private int removalTaskId = -1;
    
    public LevicorpusEffect(Plugin plugin, Wizard caster) {
        super(Levicorpus.NAME, plugin, caster.getPlayer(), caster);
    }

    @Override
    public void start() {
        super.start();
        
        // Launch the caster into the air.
        getEntity().setVelocity(new Vector(0, 5, 0));
        // Stick them there.
        Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), this, RUN_DELAY);
        getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());
    }
    
    /**
     * Prevents players from getting out of flight while they are affected by
     * Levicorpus.
     * 
     * @param event 
     */
    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event) {
        if (hasStarted() && !isRemoved() && event.getPlayer().getUniqueId().equals(getCaster().getPlayer().getUniqueId())) {
            event.setCancelled(true);
            event.getPlayer().teleport(getCaster().getPlayer().getLocation());
        }
    }
    
    /**
     * Holds the caster at their current position, only allowing them to fall
     * after the effect's life span has been reached.
     */
    @Override
    public void run() {
        // Initialize the location at which the player will stay if this is
        // the first run.
        if (floatLocation == null) {
            floatLocation = getEntity().getLocation();
            
            initialFlySpeed = getPlayer().getFlySpeed();
            wasFlying = getPlayer().isFlying();
            isAllowedFlight = getPlayer().getAllowFlight();
            
            // Freeze the player.
            getPlayer().setAllowFlight(true);
            getPlayer().setFlying(true);
            getPlayer().setFlySpeed(0);
            
            // Setup the task to remove the effect.
            removalTaskId = Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), this, LIFE_SPAN);
            return;
        }
        
        // Effect will have reached the end of its life span at this point.
        remove();
    }
    
    @Override
    public void remove() {
        resetPlayer();
        super.remove();
        
        if (removalTaskId != -1) {
            Bukkit.getScheduler().cancelTask(removalTaskId);
        }
    }
    
    /**
     * Resets all player fields which are modified by this effect.
     */
    private void resetPlayer() {
        getPlayer().setFlying(wasFlying);
        getPlayer().setFlySpeed(initialFlySpeed);
        getPlayer().setAllowFlight(isAllowedFlight);
    }
    
    /**
     * @return
     *          The location at which the player is floating.
     *          <code>null</code> if the player is not yet floating.
     */
    public Location getFloatLocation() {
        return floatLocation;
    }
    
    /**
     * @return
     *          The player being affected.
     */
    public Player getPlayer() {
        return (Player) getEntity();
    }
    
}
