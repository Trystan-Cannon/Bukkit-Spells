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
package io.github.trystancannon.diagoncraftspells.spell.effect;

import io.github.trystancannon.diagoncraftspells.player.Wizard;
import io.github.trystancannon.diagoncraftspells.spell.ArestoMomentum;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Stops the opponent from moving for 10 seconds whether it be a mob or player.
 * 
 * @author Trystan Cannon
 */
public class ArestoMomentumEffect extends SpellEffect {

    public ArestoMomentumEffect(Plugin plugin, LivingEntity entityAffected, Wizard caster) {
        super(ArestoMomentum.NAME, plugin, entityAffected, caster);
    }

    @Override
    public void start() {
        super.start();
        
        getEntity().addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 257));
        Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), this, 200L);
    }
    
    @Override
    public void run() {
        if (!isRemoved()) {
            remove();
        }
    }
    
    @Override
    public void remove() {
        super.remove();
        getEntity().removePotionEffect(PotionEffectType.SLOW);
    }
    
}
