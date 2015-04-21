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
import io.github.trystancannon.diagoncraftspells.spell.Ventus;

import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

/**
 * Moves the struck <code>LivingEntity</code> randomly for 50 seconds by
 * assigning a new random, x-z velocity every quarter of a second.
 * 
 * @author Trystan Cannon
 */
public class VentusEffect extends SpellEffect {

    /**
     * The number of server ticks for which this effect is active once started.
     */
    public static final int LIFE_SPAN = 1000;
    
    /**
     * The number of ticks between each scheduled call to this effect's
     * <code>run</code> method.
     */
    public static final int UPDATE_INTERVAL = 5;
    
    /**
     * The minimum value a randomly generated velocity x or z component can have.
     */
    public static final double VELOCITY_COMPONENT_MIN = -0.5D;
    
    /**
     * The maximum value a randomly generated velocity x or z component can have.
     */
    public static final double VELOCITY_COMPONENT_MAX = 0.5D;
    
    /**
     * The number of ticks for which this effect has been active since being started.
     */
    private int ticksLived = 0;
    
    /**
     * Used for the generation of random velocities on the x and z axes during
     * the effect.
     */
    private final Random generator = new Random();
    
    public VentusEffect(Plugin plugin, LivingEntity entityAffected, Wizard caster) {
        super(Ventus.NAME, plugin, entityAffected, caster);
    }

    @Override
    public void start() {
        super.start();
        run();
    }
    
    @Override
    public void run() {
        if (ticksLived < LIFE_SPAN && !isRemoved()) {
            getEntity().setVelocity(getRandomVelocity());
            Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), this, UPDATE_INTERVAL);
            
            ticksLived += UPDATE_INTERVAL;
            return;
        }
        
        if (!isRemoved()) {
            remove();
        }
    }
    
    /**
     * @return
     *          A new random velocity vector with no vertical (y) component.
     */
    private Vector getRandomVelocity() {
        double x = VELOCITY_COMPONENT_MIN + (VELOCITY_COMPONENT_MAX - VELOCITY_COMPONENT_MIN) * generator.nextDouble();
        double z = VELOCITY_COMPONENT_MIN + (VELOCITY_COMPONENT_MAX - VELOCITY_COMPONENT_MIN) * generator.nextDouble();
        
        return new Vector(x, 0, z);
    }
    
}
