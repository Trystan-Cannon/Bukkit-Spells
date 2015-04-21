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
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

/**
 * "Ascendio" spell:
 *      Makes the caster fly 5 blocks into the air.
 * 
 * @author Trystan Cannon
 */
public class Ascendio extends Spell {

    /**
     * The name of this spell: "Ascendio".
     */
    public static final String NAME = "Ascendio";
    
    /**
     * The description of this spell.
     */
    public static final List<String> DESCRIPTION = Arrays.asList("Launches the caster", "5 blocks into the air.");
    
    public Ascendio(Plugin plugin) {
        super(NAME, DESCRIPTION, plugin);
    }
    
    /**
     * Adjusts the caster's velocity to bring them upwards 5 blocks, alerting
     * them that they've casted the Ascendio spell.
     * 
     * @param caster
     *          The <code>Wizard</code> who is casting this spell.
     * @param targetBlock
     *          <code>null</code>
     * @param target
     *          <code>null</code>
     * @param args
     *          <code>null</code>
     * 
     * @return
     *          <code>true</code> if the spell was casted successfully. This is always <code>true</code>.
     */
    @Override
    public boolean execute(Wizard caster, Block targetBlock, BlockFace targetFace, Entity target, String[] args) {
        caster.getPlayer().setVelocity(caster.getPlayer().getVelocity().setY(1.1));
        caster.getPlayer().setFallDistance(0F);
        alertCast(caster);
        
        return true;
    }
    
}
