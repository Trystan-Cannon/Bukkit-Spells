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
import io.github.trystancannon.diagoncraftspells.spell.Duro;

import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;

/**
 * Encases a <code>LivingEntity</code> in <code>Material.STONE</code> for 10
 * seconds (200 server ticks).
 *
 * This event must be started.
 *
 * @author Trystan Cannon
 */
public final class DuroEffect extends PrisonEffect {

    /**
     * The number of ticks for which this spell effect SHOULD last. This may
     * be adjusted by the instantiating object.
     */
    public static final int LIFE_SPAN = 200;
    
    /**
     * The material out of which the prison is constructed.
     */
    public static final Material PRISON_MATERIAL = Material.STONE;

    public DuroEffect(Plugin plugin, LivingEntity entityAffected, Wizard caster) {
        super(Duro.NAME, plugin, entityAffected, caster, PRISON_MATERIAL, LIFE_SPAN);
    }

}
