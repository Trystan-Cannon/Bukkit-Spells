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
package io.github.trystancannon.diagoncraftspells.item;

import io.github.trystancannon.diagoncraftspells.spell.Spell;
import io.github.trystancannon.diagoncraftspells.spell.SpellManager;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Each Spell Book is represented by an enchanted book in game. When a player
 * right clicks with the book in hand, the spell is added to their known
 * spells list, and the book is removed from their inventory.
 * 
 * However, if they already know the spell, they are simply reminded of this
 * and nothing else occurs.
 * 
 * @author Trystan Cannon
 */
public final class SpellBook {
    
    /**
     * The material for all spell book stacks to be made out of.
     */
    public static final Material MATERIAL = Material.ENCHANTED_BOOK;
    
    /**
     * The spell which this book teaches.
     */
    private final Spell spell;
    
    /**
     * The in-game item which represents this book.
     */
    private final ItemStack stack;
    
    public SpellBook(Spell spell) {
        this.spell = spell;
        this.stack = new ItemStack(MATERIAL, 1);
        ItemMeta stackMeta = stack.getItemMeta();
        
        stackMeta.setDisplayName("Spell Book: " + ChatColor.LIGHT_PURPLE + spell.getName());
        
        ArrayList<String> lore = new ArrayList<>();
        lore.add("Teaches the spell ");
        lore.add(ChatColor.LIGHT_PURPLE + spell.getName() + ChatColor.RESET + ":");
        
        for (String line : spell.getDescription()) {
            lore.add(line);
        }
        
        stackMeta.setLore(lore);
        stack.setItemMeta(stackMeta);
    }
    
    /**
     * Creates a <code>SpellBook</code> object from the given item stack.
     * 
     * @param item
     *          Spell book item stack.
     * 
     * @return
     *          A <code>SpellBook</code> from the given in-game stack.
     */
    public static SpellBook fromStack(ItemStack item) {
        if (!isItemSpellBook(item)) {
            return null;
        }
        
        return new SpellBook(SpellManager.getSpell(ChatColor.stripColor(item.getItemMeta().getDisplayName()).replaceFirst("Spell Book: ", "")));
    }
    
    /**
     * @return
     *          The spell which this book teaches.
     */
    public Spell getSpell() {
        return spell;
    }
    
    /**
     * @return
     *          The in-game item which represents this book.
     */
    public ItemStack getStack() {
        return stack;
    }
    
    /**
     * @param item
     * 
     * @return
     *          <code>true</code> if the given item is a spell book.
     */
    public static boolean isItemSpellBook(ItemStack item) {
        return item != null && item.hasItemMeta() && item.getItemMeta().getDisplayName() != null &&
               ChatColor.stripColor(item.getItemMeta().getDisplayName()).startsWith("Spell Book: ");
    }
    
}
