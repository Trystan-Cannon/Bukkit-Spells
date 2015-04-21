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
package io.github.trystancannon.diagoncraftspells.event;

import io.github.trystancannon.diagoncraftspells.player.Wizard;
import io.github.trystancannon.diagoncraftspells.spell.Spell;

import org.bukkit.event.Event;

/**
 * Abstract implementation of all spell cast events. Essentially, the Spell Cast
 * Event acts as a container for the caster and casted spell name.
 * 
 * @author Trystan Cannon
 */
public abstract class SpellCastEvent extends Event {
    
    /**
     * The spell the wizard is casting.
     */
    private final Spell spellCast;
    
    /**
     * The wizard who is casting the spell.
     */
    private final Wizard caster;
    
    public SpellCastEvent(Wizard caster, Spell spellCast) {
        this.caster = caster;
        this.spellCast = spellCast;
    }
    
    /**
     * @return
     *          The <code>Spell</code> being cast.
     */
    public Spell getSpell() {
        return spellCast;
    }
    
    /**
     * @return
     *          The <code>Wizard</code> casting the spell.
     */
    public Wizard getCaster() {
        return caster;
    }
    
}
