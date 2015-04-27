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
package io.github.trystancannon.diagoncraftspells.spell;

import com.comphenix.protocol.Packets;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ConnectionSide;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import io.github.trystancannon.diagoncraftspells.player.Wizard;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Makes the affected chest-piece cause the player, and his armor, to
 * appear invisible.
 * 
 * This effect achieves persistence by applying lore to the chest-plate, setting
 * its first line to "Affected by Bedaz." This line is removed if the spell
 * is casted onto it again.
 * 
 * An event handler then cancels outgoing entity equipment packets for the player.
 * 
 * @deprecated Untested.
 * @author Trystan Cannon (tccannon@live.com)
 */
public class Bedaz extends ProjectileSpell {
    
    public static final String NAME = "Bedaz";
    
    public static final List<String> DESCRIPTION = Arrays.asList("Binds invisibility to a chest-piece.");
    
    /**
     * The line of lore which MUST begin the lore of an affected chest-piece. This
     * is used to create persistence with this effect.
     */
    public static final String LORE_IDENTIFIER = "Affected by Bedaz.";
    
    public Bedaz(Plugin plugin) {
        super(NAME, DESCRIPTION, plugin);
        ProtocolLibrary.getProtocolManager().addPacketListener(new BedazPacketAdapter(plugin));
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent click) {
        if (click.getSlotType() == SlotType.ARMOR) {
            ItemStack stack = click.getCurrentItem();
            
            // Player removed amror.
            if (stack != null && isItemBedazed(stack)) {
                click.getWhoClicked().removePotionEffect(PotionEffectType.INVISIBILITY);
            // Player placed armor in solot.
            } else {
                click.getWhoClicked().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 15));
            }
        }
    }
    
    @Override
    public void onProjectileStrikeGround(Wizard caster, Block blockStruck) {
        Location strikeLocation = blockStruck.getLocation();
        
        for (Item itemOnGround : strikeLocation.getWorld().getEntitiesByClass(Item.class)) {
            if (itemOnGround.getLocation().distanceSquared(strikeLocation) <= 3 &&
                isItemChestPiece(itemOnGround.getItemStack()) && !isItemBedazed(itemOnGround.getItemStack()))
            {
                // Get the item's meta data.
                ItemStack stack = itemOnGround.getItemStack();
                ItemMeta meta = (ItemMeta) stack.getItemMeta();
                
                // Create new lore for it with the proper data for us to identify
                // it as an affected item.
                List<String> lore = Arrays.asList(LORE_IDENTIFIER, "");
                
                // Retain all current lore, if any.
                if (meta.hasLore()) {
                    for (String line : meta.getLore()) {
                        lore.add(line);
                    }
                }
                
                // Apply the new lore.
                meta.setLore(lore);
                stack.setItemMeta(meta);
                itemOnGround.setItemStack(stack);
                
                return;
            }
        }
    }
    
    private final class BedazPacketAdapter extends PacketAdapter {
        
        public BedazPacketAdapter(Plugin plugin) {
            super(plugin, ConnectionSide.SERVER_SIDE, Packets.Server.ENTITY_EQUIPMENT);
        }
        
        @Override
        public void onPacketSending(PacketEvent send) {
            PacketContainer packet = send.getPacket();
            ItemStack stack = packet.getItemModifier().read(0);
            
            // Modify only chest pieces which have not yet been affected.
            if (stack != null && isItemChestPiece(stack) && isItemBedazed(stack)) {
                send.setPacket(packet = packet.deepClone());
                
                // Make the item seem to disappear.
                packet.getItemModifier().write(0, null);
            }
        }
        
    }
    
    /**
     * @return
     *          <code>true</code> if the given stack is an armor chest-piece.
     */
    private static boolean isItemChestPiece(ItemStack stack) {
        return stack.getType().name().contains("CHESTPLATE");
    }
    
    /**
     * @param stack
     *          Item to check.
     * @return
     *          <code>true</code> if the stack has been affected by this spell.
     */
    private static boolean isItemBedazed(ItemStack stack) {
        return stack.hasItemMeta() && stack.getItemMeta().hasLore() &&
               stack.getItemMeta().getLore().get(0) != null &&
               stack.getItemMeta().getLore().get(0).equals(LORE_IDENTIFIER);
    }
    
}
