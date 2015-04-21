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

import io.github.trystancannon.diagoncraftspells.event.SpellProjectileCollideEntityEvent;
import io.github.trystancannon.diagoncraftspells.player.Wizard;
import io.github.trystancannon.diagoncraftspells.spell.effect.CastEffect;
import io.github.trystancannon.diagoncraftspells.spell.effect.ImperioEffect;
import io.github.trystancannon.diagoncraftspells.spell.effect.SpellEffect;

import java.util.Arrays;
import java.util.List;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Allows the caster to take control of the player struck, meaning that they
 * may move the player around by changing the direction in which they are looking.
 * 
 * The effect is removed by casting the spell again.
 * 
 * Looking down, however, places the controlled player at the feet of the caster.
 * 
 * @author Trystan Cannon
 */
public class Imperio extends ProjectileSpell {
    
    public static final String NAME = "Imperio";
    
    public static final List<String> DESCRIPTION = Arrays.asList("Alliows the caster to", "take control of", "the player struck.", "",
                                                                 "Essentially, the caster may move", "the player around by changing the",
                                                                 "direction in which they are looking.", "To stop, simply cast the spell again.");
    
    public Imperio(Plugin plugin) {
        super(NAME, DESCRIPTION, plugin);
    }
    
    @Override
    public void onProjectileStrikeLivingEntity(LivingEntity entity, SpellProjectileCollideEntityEvent collisionEvent, Wizard caster) {
        if (!collisionEvent.isCancelled() && entity instanceof Player && ((Player) entity).getUniqueId().equals(caster.getUniqueId())) {
            new ImperioEffect(getPlugin(), (Player) entity, caster).start();
            new CastEffect(this, caster).start();
        }
    }

    @Override
    public boolean execute(Wizard caster, Block targetBlock, BlockFace targetFace, Entity target, String[] args) {
        if (caster.hasEffect(CastEffect.getCastEffectName(this))) {
            for (SpellEffect castEffect : caster.getEffect(CastEffect.getCastEffectName(this))) {
                castEffect.remove();
            }
            
            alertCast(caster);
            return true;
        }
        
        return super.execute(caster, targetBlock, targetFace, target, args);
    }
    
}