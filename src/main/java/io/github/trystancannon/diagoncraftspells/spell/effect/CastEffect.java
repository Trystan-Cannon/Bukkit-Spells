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

import io.github.trystancannon.diagoncraftspells.core.DiagonCraftSpellsPlugin;
import io.github.trystancannon.diagoncraftspells.player.Wizard;
import io.github.trystancannon.diagoncraftspells.spell.Spell;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Cast effects represent spell effects which require the caster as well as
 * an affected entity to be recorded.
 * 
 * For example, the <code>Imperio</code> spell is active until the caster
 * casts the spell again. However, because nothing is affecting the caster,
 * we need a way to keep track of whether or not a wizard is currently using
 * the spell.
 * 
 * Therefore, the solution is a Cast Effect: An effect imparted upon the caster
 * when they cast the spell. Using cast effects, we can check if the caster
 * is already affected by Imperio's cast effect. If they are, then we can simply
 * remove it from them an the afflicted entity.
 * 
 * NOTE:
 *      - Each <code>CastEffect</code> has the same name as the spell cast with
 *        " Caster" appended to the end. For example, for Imperio, the name is
 *        "Imperio Caster."
 * 
 * @author Trystan Cannon
 */
public final class CastEffect extends SpellEffect {

    public CastEffect(Spell spellCasted, Wizard caster) {
        super(spellCasted.getName() + " Caster", spellCasted.getPlugin(), caster.getPlayer(), caster);
    }
    
    public static String getCastEffectName(Spell spell) {
        return spell.getName() + " Caster";
    }
    
    @Override
    public void start() {
        if (getEntity() instanceof Player) {
            Wizard wizard = DiagonCraftSpellsPlugin.getWizard(((Player) getEntity()).getUniqueId());
            
            if (wizard != null) {
                wizard.addEffect(this);
            }
        }
        
        produceAddEvent();
    }
    
    @Override
    public void remove() {
        if (getEntity() instanceof Player) {
            Wizard wizard = DiagonCraftSpellsPlugin.getWizard(((Player) getEntity()).getUniqueId());
            
            if (wizard != null) {
                wizard.removeEffectFromList(this);
            }
        }
        
        setRemoved(true);
        produceEndEvent();
    }

    /**
     * @deprecated
     *          Does nothing.
     */
    @Override
    public void run() {}
    
}
