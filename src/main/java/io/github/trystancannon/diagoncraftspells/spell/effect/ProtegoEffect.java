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

import io.github.trystancannon.diagoncraftspells.event.SpellProjectileCollideEntityEvent;
import io.github.trystancannon.diagoncraftspells.player.Wizard;
import io.github.trystancannon.diagoncraftspells.spell.Protego;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

/**
 * Protects the caster from all spells by canceling any <code>SpellProjectileCollideEntityEvent</code>s which
 * involve the caster as the entity struck.
 * 
 * @author Trystan Cannon
 */
public final class ProtegoEffect extends SpellEffect implements Listener {

    public ProtegoEffect(Plugin plugin, Wizard caster) {
        super(Protego.NAME, plugin, caster.getPlayer(), caster);
        getPlugin().getServer().getPluginManager().registerEvents(this, getPlugin());
    }

    /**
     * @deprecated Does nothing.
     */
    @Override
    public void run() {
    }
    
    
    /**
     * Cancels any spell projectile collisions which strike the wizard who casted
     * "Protego," preventing them from harming the wizard.
     * 
     * @param collision
     *          The collision event called by the spell projectile which struck the protected wizard.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onSpellProjectileCollideEntity(SpellProjectileCollideEntityEvent collision) {
        if (collision.getCaster().hasEffect(getName()) && collision.getEntity().getUniqueId().equals(collision.getCaster().getPlayer().getUniqueId())) {
            collision.setCancelled(true);
            
            if (collision.getEntity() instanceof Player) {
                collision.getEntity().sendMessage("You were protected from " + ChatColor.LIGHT_PURPLE + collision.getSpell().getName() + "!");
            }
        }
    }
    
}
