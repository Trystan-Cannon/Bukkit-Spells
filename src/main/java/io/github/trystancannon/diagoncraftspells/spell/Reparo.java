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

import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

/**
 * Repairs one item in a 1-block radius region around the location
 * at which the spell projectile lands.
 * 
 * @author Trystan Cannon
 */
public class Reparo extends ProjectileSpell {
    
    public static final String NAME = "Reparo";
    
    public static final List<String> DESCRIPTION = Arrays.asList("Repairs the item at which the", "spell is casted.", "",
                                                                 "NOTE: If there are multiple items", "near where the spell lands, only one,", "and perhaps not the correct one,",
                                                                 "will be repaired.");
    
    public Reparo(Plugin plugin) {
        super(NAME, DESCRIPTION, plugin);
    }

    @Override
    public void onProjectileStrikeGround(Wizard caster, Block blockStruck) {
        Location strikeLocation = blockStruck.getLocation();
        
        for (Item itemOnGround : strikeLocation.getWorld().getEntitiesByClass(Item.class)) {
            if (itemOnGround.getLocation().distanceSquared(strikeLocation) <= 3) {
                ItemStack item = itemOnGround.getItemStack();
                
                if (item.getDurability() != item.getType().getMaxDurability()) {
                    item.setDurability((short) 0);
                    caster.sendMessage("Repaired a " + ChatColor.LIGHT_PURPLE + item.getType().toString() + "!");
                    
                    return;
                }
            }
        }
    }
    
}
