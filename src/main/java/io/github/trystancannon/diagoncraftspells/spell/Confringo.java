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
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

/**
 * Launches a fire charge in the direction the caster is looking.
 * 
 * @author Trystan Cannon
 */
public class Confringo extends Spell {
    
    public static final String NAME = "Confringo";
    
    public static final List<String> DESCRIPTION = Arrays.asList("Launches a fire charge", "in the direction", "you are looking.");

    public Confringo(Plugin plugin) {
        super(NAME, DESCRIPTION, plugin);
    }
    
    @Override
    public boolean execute(Wizard caster, Block targetBlock, BlockFace targetFace, Entity target, String[] args) {
        Player player = caster.getPlayer();
        Location chargeLocation = player.getEyeLocation();
        Vector direction = chargeLocation.getDirection().multiply(2);
        
        // Make sure the fire charge spawns a little in front of the caster.
        chargeLocation.add(chargeLocation.getDirection().multiply(2));
        Entity fireCharge = player.getWorld().spawnEntity(chargeLocation, EntityType.FIREBALL);
        
        fireCharge.setVelocity(direction.multiply(2));
        alertCast(caster);
        
        return true;
    }
    
}
