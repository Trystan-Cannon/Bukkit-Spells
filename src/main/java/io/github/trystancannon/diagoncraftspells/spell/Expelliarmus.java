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

import io.github.trystancannon.diagoncraftspells.core.DiagonCraftSpellsPlugin;
import io.github.trystancannon.diagoncraftspells.event.SpellProjectileCollideEntityEvent;
import io.github.trystancannon.diagoncraftspells.player.Wizard;
import io.github.trystancannon.diagoncraftspells.wand.Wand;

import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Expels the wand from the hand of the struck Wizard.
 * 
 * @author Trystan Cannon
 */
public class Expelliarmus extends ProjectileSpell {

    public static final String NAME = "Expelliarmus";
    
    public static final List<String> DESCRIPTION = Arrays.asList("Expels the wand from", "the hand of the", "struck opponent wizard.");
    
    public Expelliarmus(Plugin plugin) {
        super(NAME, DESCRIPTION, plugin);
    }
    
    @Override
    public void onProjectileStrikeLivingEntity(LivingEntity entity, SpellProjectileCollideEntityEvent collisionEvent, Wizard caster) {
        if (collisionEvent.isCancelled() || !(entity instanceof Player)) {
            return;
        }
        
        Wizard opponent = DiagonCraftSpellsPlugin.getWizard(((Player) entity).getUniqueId());
        
        if (opponent == null || !Wand.isItemWand(opponent.getPlayer().getInventory().getItemInHand())) {
            return;
        }
        
        Player player = opponent.getPlayer();
        
        player.getWorld().dropItemNaturally(player.getLocation().add(player.getLocation().getDirection().multiply(2)), player.getItemInHand());
        player.getInventory().setItemInHand(null);
        
        player.sendMessage("You have been disarmed by " + ChatColor.RED + caster.getPlayer().getDisplayName() + ChatColor.RESET + " via " + ChatColor.LIGHT_PURPLE + getName() + "!");
    }

    @Override
    public boolean execute(Wizard caster, Block targetBlock, BlockFace targetFace, Entity target, String[] args) {
        fireProjectile(caster);
        caster.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + getName() + "!");
        
        return true;
    }
}
