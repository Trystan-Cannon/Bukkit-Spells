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

import io.github.trystancannon.diagoncraftspells.player.Wizard;

import java.util.Arrays;
import java.util.List;
import org.bukkit.CropState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeSpecies;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.material.Crops;
import org.bukkit.material.Tree;
import org.bukkit.plugin.Plugin;

/**
 * Makes all plants within 4 block radius grow to their fullest extent.
 * 
 * Currently works for:
 *      - Trees
 *      - Wheat
 *      - Pumpkins
 *      - Melons
 *      - Potatoes
 *      - Carrots
 *      - Cocoa
 *      - Nether Warts
 * 
 * @author Trystan Cannon (tccannon@live.com)
 */
public class Herbivicus extends Spell {
    
    public static final int EFFECT_DIAMETER = 8;
    
    public static final String NAME = "Herbivicus";
    
    public static final List<String> DESCRIPTION = Arrays.asList("Makes all plants within a 4", "block radius grow to their", "fullest extent.");
    
    public Herbivicus(Plugin plugin) {
        super(NAME, DESCRIPTION, plugin);
    }

    @Override
    public boolean execute(Wizard caster, Block targetBlock, BlockFace targetFace, Entity target, String[] args) {
        World playerWorld = caster.getPlayer().getWorld();
        Location playerLocation = caster.getPlayer().getLocation();
        
        // Get block coordinates of the player.
        int originX = playerLocation.getBlockX();
        int originY = playerLocation.getBlockY();
        int originZ = playerLocation.getBlockZ();
        
        // Check all blocks surrounding the caster within the given radius.
        for (int x = -(EFFECT_DIAMETER / 2); x < EFFECT_DIAMETER / 2; x++) {
            for (int z = -(EFFECT_DIAMETER / 2); z < EFFECT_DIAMETER / 2; z++) {
                for (int y = 0; y < EFFECT_DIAMETER / 2; y++) {
                    // Grab the block at this location.
                    Block block = new Location(playerWorld, originX + x, originY + y, originZ + z).getBlock();
                    Material type = block.getType();
                    
                    // Make saplings grow.
                    if (type == Material.SAPLING) {
                        TreeSpecies species = ((Tree) block.getState().getData()).getSpecies();
                        
                        block.setType(Material.AIR);
                        playerWorld.generateTree(block.getLocation(), typeFromSpecies(species));
                    // Force wheat growth.
                    } else if (type == Material.CROPS) {
                        BlockState state = block.getState();
                        
                        Crops crops = (Crops) state.getData();
                        crops.setState(CropState.RIPE);
                        
                        state.setData(crops);
                        state.update(true);
                    } else if (type == Material.PUMPKIN_STEM) {
                        block.setType(Material.PUMPKIN);
                    } else if (type == Material.MELON_STEM) {
                        block.setType(Material.MELON_BLOCK);
                    } else if (type == Material.POTATO) {
                        block.setData((byte) 7);
                    } else if (type == Material.CARROT) {
                        block.setData((byte) 7);
                    } else if (type == Material.COCOA) {
                        block.setData((byte) 8);
                    } else if (type == Material.NETHER_WARTS) {
                        block.setData((byte) 3);
                    }
                }
            }
        }
        
        alertCast(caster);
        return true;
    }
    
    /**
     * @param species
     *          The species for which the type is returned.
     * @return
     *          The tree type for the given species.
     *          <code>null</code> if an unsupported species was given.
     */
    private static TreeType typeFromSpecies(TreeSpecies species) {
        TreeType type = null;
        
        switch(species) {
            case GENERIC:
                type = TreeType.TREE;
                break;
            case REDWOOD:
                type = TreeType.REDWOOD;
                break;
            case BIRCH:
                type = TreeType.BIRCH;
                break;
            case JUNGLE:
                type = TreeType.JUNGLE;
                break;
            case ACACIA:
                type = TreeType.ACACIA;
                break;
            case DARK_OAK:
                type = TreeType.DARK_OAK;
                break;
        }
        
        return type;
    }
    
}
