/*
 * The MIT License
 *
 * Copyright 2015 Trystan Cannon (tccannon@live.com).
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
import io.github.trystancannon.diagoncraftspells.spell.Silencio;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;

/**
 * Prevents the struck player from using chat for 30 seconds.
 * 
 * @author Trystan Cannon (tccannon@live.com)
 */
public class SilencioEffect extends SpellEffect implements Listener {
    
    /**
     * The number of server ticks for which this effect is active (~30 seconds).
     */
    public static final int LIFE_SPAN = 600;

    /**
     * The task id assigned by the schedule for this event's removal task.
     */
    private int removalTaskId = -1;
    
    public SilencioEffect(Plugin plugin, Player playerAffected, Wizard caster) {
        super(Silencio.NAME, plugin, playerAffected, caster);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent chat) {
        if (hasStarted() && !isRemoved() && chat.getPlayer().getUniqueId().equals(((Player) getEntity()).getUniqueId())) {
            chat.setCancelled(true);
        }
    }
    
    @Override
    public void start() {
        super.start();
        removalTaskId = Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), this, LIFE_SPAN);
    }
    
    @Override
    public void run() {
        if (!isRemoved()) {
            removalTaskId = -1;
            remove();
        }
    }
    
    @Override
    public void remove() {
        super.remove();
        
        if (removalTaskId != -1) {
            Bukkit.getScheduler().cancelTask(removalTaskId);
            removalTaskId = -1;
        }
    }
    
}
