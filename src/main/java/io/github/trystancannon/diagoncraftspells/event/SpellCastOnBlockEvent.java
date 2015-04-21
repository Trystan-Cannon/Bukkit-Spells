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

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.HandlerList;

/**
 * The <code>SpellCastOnBlockEvent</code> occurs when a wizard right clicks on a
 * block with their wand in hand.
 * 
 * The spell currently bound to the wand, if any, is executed with block as a
 * parameter.
 * 
 * @author Trystan Cannon
 */
public class SpellCastOnBlockEvent extends SpellCastEvent {

    private static final HandlerList handlers = new HandlerList();

    /**
     * The block targeted for this spell cast.
     */
    private final Block targetBlock;
    
    /**
     * The block face targeted for this spell cast.
     */
    private final BlockFace targetFace;
    
    public SpellCastOnBlockEvent(Wizard caster, Spell spellCast, Block targetBlock, BlockFace targetFace) {
        super(caster, spellCast);
        this.targetBlock = targetBlock;
        this.targetFace = targetFace;
    }
    
    /**
     * @return
     *          The target block for this cast.
     */
    public Block getTargetBlock() {
        return targetBlock;
    }
    
    /**
     * @return
     *          The target block face for this cast.
     */
    public BlockFace getTargetFace() {
        return targetFace;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
}
