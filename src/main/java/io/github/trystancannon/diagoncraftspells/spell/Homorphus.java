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

import io.github.trystancannon.diagoncraftspells.core.DiagonCraftSpellsPlugin;
import io.github.trystancannon.diagoncraftspells.event.SpellProjectileCollideEntityEvent;
import io.github.trystancannon.diagoncraftspells.player.Wizard;
import io.github.trystancannon.diagoncraftspells.spell.effect.DisguiseEffect;
import io.github.trystancannon.diagoncraftspells.spell.effect.SpellEffect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Removes any disguise from the struck player.
 * 
 * @author Trystan Cannon
 */
public class Homorphus extends ProjectileSpell {
    
    public static final String NAME = "Homorphus";
    
    public static final List<String> DESCRIPTION = Arrays.asList("Removes any disguise from the", "struck player.");
    
    public Homorphus(Plugin plugin) {
        super(NAME, DESCRIPTION, plugin);
    }

    @Override
    public void onProjectileStrikeLivingEntity(LivingEntity entity, SpellProjectileCollideEntityEvent collisionEvent, Wizard caster) {
        if (!collisionEvent.isCancelled() && entity instanceof Player) {
            Player playerStruck = (Player) entity;
            
            final List<SpellEffect> disguiseEffects = new ArrayList<>();
            
            // To prevent concurrent modification exceptions, the disguise effects
            // are collected into a separate list.
            for (SpellEffect effect : caster.getEffects()) {
                if (effect instanceof DisguiseEffect && effect.hasStarted() && DiagonCraftSpellsPlugin.getDisguiseAPI().isDisguised(playerStruck)) {
                    disguiseEffects.add(effect);
                }
            }
            
            // Remove all of the collected disguise effects.
            for (SpellEffect effect : disguiseEffects) {
                effect.remove();
            }
        }
    }
    
}
