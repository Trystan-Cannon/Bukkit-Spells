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
package io.github.trystancannon.diagoncraftspells.wand;

import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Implementation of wands which allow spells to be casted by the player. There
 * are different kinds of wands delineated by the meta data of the item which
 * represents it in-game.
 * 
 * Each wand is a stick with a special display name, designating the wand as a
 * certain type.
 * 
 * The display name of the item stack for the wand will be "Wand," and the lore
 * of the item will reflect the type of the wand.
 * 
 * @author Trystan Cannon
 */
public final class Wand {
    
    public static enum WandType {
        APPLEWOOD, ELM, CHERRY, FIR, BLACKTHORN, CYPRESS, EBONY, ROSEWOOD, POPLAR,
        MAHOGANY, HORNBEAM, HOLLY, MAPLE, OAK, HAWTHORNE, ELDER;
        
        @Override
        public String toString() {
            switch(this) {
                case APPLEWOOD:
                    return "Applewood";
                case ELM:
                    return "Elm";
                case CHERRY:
                    return "Cherry";
                case FIR:
                    return "Fir";
                case BLACKTHORN:
                    return "Blackthorn";
                case CYPRESS:
                    return "Cypress";
                case EBONY:
                    return "Ebony";
                case ROSEWOOD:
                    return "Rosewood";
                case POPLAR:
                    return "Poplar";
                case MAHOGANY:
                    return "Mahogany";
                case HORNBEAM:
                    return "Hornbeam";
                case HOLLY:
                    return "Holly";
                case MAPLE:
                    return "Maple";
                case OAK:
                    return "Oak";
                case HAWTHORNE:
                    return "Hawthorne";
                case ELDER:
                    return "Elder";
            }
            
            return null;
        }
    }
    
    /**
     * Material which represents the in-game version of all wands.
     */
    public static final Material MATERIAL = Material.STICK;
    
    /**
     * Generates a wand with the given type.
     * 
     * @param type
     *          Type of wand desired.
     * 
     * @return
     *          The generated <code>ItemSack</code> for the desired wand.
     */
    public static ItemStack getWand(WandType type) {
        ItemStack stack = new ItemStack(MATERIAL, 1);
        ItemMeta stackMeta = stack.getItemMeta();
        
        stackMeta.setDisplayName("Wand");
        stackMeta.setLore(Arrays.asList(type.toString()));
        
        stack.setItemMeta(stackMeta);
        return stack;
    }
    
    /**
     * @param stack
     *          Item to check if it's a wand.
     *
     * @return
     *          <code>true</code> if the item is a wand.
     */
    public static boolean isItemWand(ItemStack stack) {
        return stack != null && stack.getItemMeta() != null && stack.getItemMeta().getDisplayName() != null && stack.getItemMeta().getDisplayName().equals("Wand");
    }
    
}
