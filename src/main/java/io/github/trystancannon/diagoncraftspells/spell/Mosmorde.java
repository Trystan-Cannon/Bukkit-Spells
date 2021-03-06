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
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.plugin.Plugin;

/**
 * Makes it night and launches a white creeper shaped firework from the caster's wand.
 * 
 * @author Trystan Cannon
 */
public class Mosmorde extends Spell {
    
    public static final String NAME = "Mosmorde";
    
    public static final List<String> DESCRIPTION = Arrays.asList("Makes it night and fires a white", "creeper shaped firework out", "of the caster's wand.");
    
    public static final int FIREWORK_POWER = 1;
    
    public Mosmorde(Plugin plugin) {
        super(NAME, DESCRIPTION, plugin);
    }

    @Override
    public boolean execute(Wizard caster, Block targetBlock, BlockFace targetFace, Entity target, String[] args) {
        caster.getPlayer().getWorld().setTime(15000);
        
        Firework firework = (Firework) caster.getPlayer().getWorld().spawnEntity(caster.getPlayer().getEyeLocation(), EntityType.FIREWORK);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        
        fireworkMeta.addEffect(FireworkEffect.builder().flicker(false).withColor(Color.WHITE).trail(true).with(FireworkEffect.Type.CREEPER).build());
        fireworkMeta.setPower(FIREWORK_POWER);
        
        firework.setFireworkMeta(fireworkMeta);
        alertCast(caster);
        
        return true;
    }
    
}
