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
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

/**
 * Teleports the caster 5 blocks in the direction he is looking.
 * 
 * @author Trystan Cannon
 */
public class Apparate extends Spell {

    public static final String NAME = "Apparate";
    
    public static final List<String> DESCRIPTION = Arrays.asList("Teleports the caster", "5 blocks in the", "direction they", "are looking.");
    
    public Apparate(Plugin plugin) {
        super(NAME, DESCRIPTION, plugin);
    }
    
    @Override
    public boolean execute(Wizard caster, Block targetBlock, BlockFace targetFace, Entity target, String[] args) {
        Location targetLocation = caster.getPlayer().getTargetBlock((Set) null, 5).getLocation().setDirection(caster.getPlayer().getLocation().getDirection());
        // Move the player one block closer to themselves, so they do not get stuck
        // in a block.
        targetLocation.subtract(caster.getPlayer().getLocation().getDirection());
        
        caster.getPlayer().teleport(targetLocation);
        alertCast(caster);
        
        caster.getPlayer().setFallDistance(0);
        return true;
    }
    
}
