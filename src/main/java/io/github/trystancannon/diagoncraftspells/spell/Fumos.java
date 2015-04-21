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
import io.github.trystancannon.diagoncraftspells.spell.effect.SpellEffect;
import io.github.trystancannon.diagoncraftspells.spell.effect.FumosEffect;

import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;

/**
 * Creates a cloud of <code>Effect.SMOKE</code> around the caster until they
 * disable it by recasting the spell.
 * 
 * @author Trystan Cannon
 */
public class Fumos extends Spell {

    public static final String NAME = "Fumos";
    
    public static final List<String> DESCRIPTION = Arrays.asList("Creates a cloud of smoke", "around the caster until", "the spell is recasted.");
    
    public Fumos(Plugin plugin) {
        super(NAME, DESCRIPTION, plugin);
    }
    
    @Override
    public boolean execute(Wizard caster, Block targetBlock, BlockFace targetFace, Entity target, String[] args) {
        // Only apply the effect if the player is not currently being affected by it.
        if (!caster.hasEffect(getName())) {
            new FumosEffect(getPlugin(), caster.getPlayer(), caster).start();
        } else {
            // Even though there should only be one Fumos effect right now,
            // this is a catch all in case something else has happened.
            for (SpellEffect effect : caster.getEffect(getName())) {
                effect.remove();
            }
        }
        
        caster.getPlayer().sendMessage(ChatColor.LIGHT_PURPLE + getName() + "!");
        return true;
    }
    
}
