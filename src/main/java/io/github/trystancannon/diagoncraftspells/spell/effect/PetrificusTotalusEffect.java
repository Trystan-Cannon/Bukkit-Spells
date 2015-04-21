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
import io.github.trystancannon.diagoncraftspells.spell.PetrificusTotalus;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;

/**
 * Stops a player from moving for 15 seconds.
 * 
 * @author Trystan Cannon
 */
public class PetrificusTotalusEffect extends SpellEffect implements Listener {
    
    /**
     * The number of server ticks for which this effect is active.
     */
    public static final int LIFE_SPAN = 300;

    public PetrificusTotalusEffect(Plugin plugin, Player entityAffected, Wizard caster) {
        super(PetrificusTotalus.NAME, plugin, entityAffected, caster);
        getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent move) {
        // Stop the player from moving but allow their direction to change.
        if (!isRemoved() && move.getPlayer().getUniqueId().equals(getPlayerAffected().getUniqueId())) {
            move.getPlayer().teleport(move.getFrom().setDirection(move.getTo().getDirection()));
        }
    }
    
    @Override
    public void start() {
        super.start();
        Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), this, 300);
    }
    
    @Override
    public void run() {
        if (!isRemoved()) {
            remove();
        }
    }
 
    private Player getPlayerAffected() {
        return (Player) getEntity();
    }
    
}
