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
package io.github.trystancannon.diagoncraftspells.player;

import io.github.trystancannon.diagoncraftspells.inventory.KnownSpellsGUI;
import io.github.trystancannon.diagoncraftspells.spell.Spell;
import io.github.trystancannon.diagoncraftspells.spell.effect.SpellEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Each <code>Player</code> which joins the server is a <code>Wizard</code>.
 * 
 * Wizard objects serve as a box for housing all of the spell effects that are
 * affecting a player as well as all of the spells that a player currently knows.
 * 
 * @author Trystan Cannon
 */
public final class Wizard {
    
    /**
     * All of the <code>SpellEffect</code>s currently affecting the player.
     */
    private final List<SpellEffect> effects = new ArrayList<>();
    
    /**
     * All of the <code>Spell</code>s that this wizard currently knows.
     */
    private final List<Spell> knownSpells = new ArrayList<>();
    
    /**
     * The <code>Spell</code> currently selected by the wizard. This spell will
     * be cast whenever the player interacts with their wand.
     */
    private Spell selectedSpell;
    
    /**
     * The current page number that the wizard is on in their Known Spells GUI.
     */
    private int currentPageIndex = 1;
    
    /**
     * The player represented by this <code>Wizard</code> object.
     */
    private final UUID playerId;
    
    public Wizard(UUID playerId) {
        this.playerId = playerId;
    }
    
    /**
     * @return
     *          The player which represents this <code>Wizard</code>.
     *          This may be <code>null</code> if the player is offline.
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(playerId);
    }
    
    /**
     * Wrapper for <code>Wizard.getPlayer().sendMessage(...)</code>.
     * 
     * @param message
     *          The message to send the player who is this wizard.
     */
    public void sendMessage(String message) {
        getPlayer().sendMessage(message);
    }
    
    /**
     * Wrapper for <code>wizard.getPlayer().getUniqueId()</code>.
     * 
     * @return
     *          The unique id for the player representing this <code>Wizard</code>.
     */
    public UUID getUniqueId() {
        return playerId;
    }
    
    /**
     * Adds the given effect to the <code>Wizard</code>'s current effects.
     * 
     * @param effect
     *              Effect to add.
     */
    public synchronized void addEffect(SpellEffect effect) {
        effects.add(effect);
    }
    
    /**
     * @param effectName
     * 
     * @return
     *          <code>true</code> if this wizard is currently being affected by
     *          the effect with the given name.
     */
    public synchronized boolean hasEffect(String effectName) {
        for (SpellEffect effect : effects) {
            if (effect.getName().equals(effectName)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Gets a list of all of the <code>SpellEffect</code> objects of the given
     * type which are currently affecting this wizard.
     * 
     * @param effectName
     * 
     * @return
     *          A list of all of the <code>SpellEffect</code> objects of the given
     *          type which are currently affecting this wizard.
     *          <code>null</code> if there were no effects found.
     */
    public synchronized List<SpellEffect> getEffect(String effectName) {
        if (!hasEffect(effectName)) {
            return null;
        }
        
        List<SpellEffect> currentEffects = new ArrayList<>();
        
        for (SpellEffect effect : effects) {
            if (effect.getName().equals(effectName)) {
                currentEffects.add(effect);
            }
        }
        
        return currentEffects;
    }
    
    /**
     * Removes the given effect from the wizard if they are indeed
     * being afflicted by it.
     * 
     * This does NOT remove the effect's presence on the wizard. That must
     * be done manually.
     * 
     * @param effect 
     */
    public synchronized void removeEffectFromList(SpellEffect effect) {
        if (effects.contains(effect)) {
            effects.remove(effect);
        }
    }
    
    /**
     * @return
     *          The list of all effects currently affecting this <code>Wizard</code>.
     */
    public synchronized List<SpellEffect> getEffects() {
        return effects;
    }
    
    /**
     * Adds the given spell to the Wizard's known spell list if they do not
     * already know it.
     * 
     * @param spell
     *              Spell to add.
     */
    public void addSpell(Spell spell) {
        if (!knownSpells.contains(spell)) {
            knownSpells.add(spell);
        }
    }
    
    /**
     * @return
     *          The list of all spells currently known by this <code>Wizard</code>.
     */
    public List<Spell> getKnownSpells() {
        return knownSpells;
    }
    
    /**
     * @return
     *          The currently selected spell for this wizard.
     */
    public Spell getSelectedSpell() {
        return selectedSpell;
    }
    
    /**
     * Sets the selected spell for this wizard. The selected spell will be casted
     * when the player interacts with their wand.
     * 
     * @param spell
     *              Spell to select.
     */
    public void setSelectedSpell(Spell spell) {
        selectedSpell = spell;
    }
    
    /**
     * @return
     *          The index of the page that the wizard is currently on in their
     *          Known Spells GUI.
     */
    public int getCurrentPageIndex() {
        return currentPageIndex;
    }
    
    /**
     * Sets the current page index of the wizard in their Known Spells GUI. Given
     * a value less than 1 or greater than the maximum page limit will cause the
     * index to remain the same.
     * 
     * @param pageIndex
     *          Page index to which the GUI will turn.
     */
    public void setCurrentPageIndex(int pageIndex) {
        if (pageIndex > 0 && pageIndex <= getMaxPageNumber()) {
            currentPageIndex = pageIndex;
        }
    }
    
    /**
     * @return
     *          The greatest possible page index for the wizard in their Known Spells GUI.
     */
    public int getMaxPageNumber() {
        double pages = (double) knownSpells.size() / KnownSpellsGUI.MAX_SPELLS_PER_PAGE;
        
        if (pages > 1) {
            if (pages - (int) pages == 0) {
                return (int) pages;
            }
            
            return (int) (pages - (pages - (int) pages) + 1);
        }
        
        return 1;
    }
    
    /**
     * @param spell
     * 
     * @return
     *          <code>true</code> if the wizard knows the given spell.
     */
    public boolean knowsSpell(Spell spell) {
        if (spell == null) {
            return false;
        }
        
        for (Spell knownSpell : knownSpells) {
            if (knownSpell.equals(spell)) {
                return true;
            }
        }
        
        return false;
    }
    
}
