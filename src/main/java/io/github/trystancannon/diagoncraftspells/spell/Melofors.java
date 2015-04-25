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

import io.github.trystancannon.diagoncraftspells.event.SpellProjectileCollideEntityEvent;
import io.github.trystancannon.diagoncraftspells.player.Wizard;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

/**
 * Places a pumpkin in the stuck player's helmet slot.
 * 
 * In the case that the player has a helmet already, it is either placed into
 * the next open spot in their inventory, or it is dropped naturally in front
 * of them.
 * 
 * @author Trystan Cannon
 */
public class Melofors extends ProjectileSpell {

    public static String NAME = "Melofors";
    
    public static List<String> DESCRIPTION = Arrays.asList("Places a pumkin on the", "head of the struck", "player.");
    
    public Melofors(Plugin plugin) {
        super(NAME, DESCRIPTION, plugin);
    }

    @Override
    public void onProjectileStrikeLivingEntity(LivingEntity entity, SpellProjectileCollideEntityEvent collisionEvent, Wizard caster) {
        if (!collisionEvent.isCancelled() && entity instanceof Player) {
            Player playerStruck = (Player) entity;
            
            // Player doesn't have a helmet.
            if (playerStruck.getEquipment().getHelmet() == null) {
                givePumpkinHelmet(playerStruck);
                return;
            }
            
            // Player has a helmet? Try to put their helmet in their
            // inventory.
            for (int slot = 0; slot < playerStruck.getInventory().getSize(); slot++) {
                ItemStack stack = playerStruck.getInventory().getItem(slot);
                
                // Found an empty spot -> Put the helmet in there.
                if (stack == null) {
                    playerStruck.getInventory().setItem(slot, playerStruck.getEquipment().getHelmet());
                    givePumpkinHelmet(playerStruck);
                    
                    return;
                }
            }
            
            // No empty slots found? Drop the helmet naturally in front of them.
            playerStruck.getWorld().dropItemNaturally(playerStruck.getLocation(), playerStruck.getEquipment().getHelmet());
            givePumpkinHelmet(playerStruck);
        }
    }
    
    /**
     * Gives a player a pumpkin for a helmet, replacing their old one.
     * Updates the player's inventory.
     * 
     * @param player
     *          The player who will get a pumpkin for a helmet.
     */
    private static void givePumpkinHelmet(Player player) {
        player.getEquipment().setHelmet(new ItemStack(Material.PUMPKIN, 1));
        player.updateInventory();
    }
    
}
