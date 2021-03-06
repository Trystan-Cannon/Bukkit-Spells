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
import io.github.trystancannon.diagoncraftspells.spell.effect.OrchideousEffect;

import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

/**
 * Spawns a temporary block of spruce leaves for 50 seconds.
 * 
 * These leaves will not decay.
 * 
 * @author Trystan Cannon (tccannon@live.com)
 */
public class Orchideous extends Spell {

    public static final String NAME = "Orchideous";
    
    public static final List<String> DESCRIPTION = Arrays.asList("Spawns a temporary block of", "spruce leaves for 50 seconds.");
    
    public Orchideous(Plugin plugin) {
        super(NAME, DESCRIPTION, plugin);
    }
    
    @Override
    public boolean execute(Wizard caster, Block targetBlock, BlockFace targetFace, Entity target, String[] args) {
        alertCast(caster);
        
        // Player has not block selected? Spawn it in front of the player.
        if (targetBlock == null) {
            targetBlock = caster.getPlayer().getWorld().getBlockAt(caster.getPlayer().getEyeLocation().add(caster.getPlayer().getEyeLocation().getDirection())).getRelative(BlockFace.SELF);
        } else {
            targetBlock = targetBlock.getRelative(targetFace);
        }
        
        if (targetBlock.getType() == Material.AIR) {
            new OrchideousEffect(getPlugin(), targetBlock.getLocation(), caster).start();
        }
        
        return true;
    }
    
}
