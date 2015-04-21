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

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

/**
 * The <code>SpellCastListener</code> catches all <code>SpellCastEvent</code>s,
 * executing the currently selected spell of the <code>Wizard</code> that is
 * the its caster.
 * 
 * Each <code>SpellCastListener</code> MUST be explicitly registered for events
 * because it does not do this itself.
 * 
 * @author Trystan Cannon
 */
public final class SpellCastListener implements Listener {
    
    @EventHandler
    public void onSpellCastOnBlock(SpellCastOnBlockEvent spellCast) {
        if (spellCast.getSpell() == null) {
            sendNoSpellMessage(spellCast.getCaster().getPlayer());
            return;
        }
        
        if (!spellCast.getSpell().execute(spellCast.getCaster(), spellCast.getTargetBlock(), spellCast.getTargetFace(), null, null)) {
            spellCast.getCaster().getPlayer().sendMessage(ChatColor.RED + "Cannot cast " + spellCast.getSpell().getName() + " on a block!");
        }
    }
    
    @EventHandler
    public void onSpellCastOnAir(SpellCastOnAirEvent spellCast) {
        if (spellCast.getSpell() == null) {
            sendNoSpellMessage(spellCast.getCaster().getPlayer());
            return;
        }
        
        if (!spellCast.getSpell().execute(spellCast.getCaster(), null, null, null, null)) {
            spellCast.getCaster().getPlayer().sendMessage(ChatColor.RED + "Cannot cast " + spellCast.getSpell().getName() + " in the air!");
        }
    }
    
    private void sendErrorMessage(Player receiver, String message) {
        receiver.sendMessage(ChatColor.RED + message);
    }
    
    private void sendNoSpellMessage(Player receiver) {
        sendErrorMessage(receiver, "You have no spell selected!");
    }
    
}
