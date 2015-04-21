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
import io.github.trystancannon.diagoncraftspells.spell.Conjunctivitis;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * Gives the player struck with this spell the "Nausea" effect. This effect
 * lasts indefinitely.
 * 
 * @author Trystan Cannon
 */
public final class ConjunctivitisEffect extends SpellEffect {

    public ConjunctivitisEffect(Plugin plugin, Player playerAffected, Wizard caster) {
        super(Conjunctivitis.NAME, plugin, playerAffected, caster);
    }

    @Override
    public void start() {
        getPlayerAffected().addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, Integer.MAX_VALUE, 1));
        super.start();
    }
    
    /**
     * @deprecated
     *          Does nothing.
     */
    @Override
    public void run() {
    }
    
    /**
     * WARNING:
     *      Removes ALL potion effects which are of type CONFUSION, not just
     *      those created by the conjunctivitis spell!
     */
    @Override
    public void remove() {
        getPlayerAffected().removePotionEffect(PotionEffectType.CONFUSION);
        super.remove();
    }
    
    /**
     * @return
     *          The player affected by this spell.
     */
    public Player getPlayerAffected() {
        return (Player) getEntity();
    }
    
}
