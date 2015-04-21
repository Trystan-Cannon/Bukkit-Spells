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
import org.bukkit.event.HandlerList;

/**
 * Occurs when a wizard learns a new spell. This is used to inform the plugin
 * that a wizard's profile has changed, so it should be saved.
 * 
 * @author Trystan Cannon
 */
public final class SpellLearnedEvent extends Event {

    private static final HandlerList handlers = new HandlerList();
    
    /**
     * The wizard that just learned a spell.
     */
    private final Wizard wizard;
    
    /**
     * The spell that the wizard just learned.
     */
    private final Spell spellLearned;
    
    public SpellLearnedEvent(Wizard wizard, Spell spellLearned) {
        this.wizard = wizard;
        this.spellLearned = spellLearned;
    }
    
    /**
     * @return
     *          The wizard who just learned a spell.
     */
    public Wizard getWizard() {
        return wizard;
    }
    
    /**
     * @return
     *          The spell that the wizard just learned.
     */
    public Spell getSpell() {
        return spellLearned;
    }
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
}
