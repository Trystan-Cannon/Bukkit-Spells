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

import java.util.HashMap;
import org.bukkit.plugin.Plugin;

/**
 * The Spell Manager contains a list of all registered spells, allowing them to
 * be referenced by their name. This makes loading a spell name from a file possible
 * because the spell can be given an object provided a name.
 * 
 * @author Trystan Cannon
 */
public final class SpellManager {
    
    /**
     * List of all currently registered spells.
     */
    private static final HashMap<String, Spell> registeredSpells = new HashMap<>();
    
    /**
     * Plugin with which this object is registered for events.
     */
    private final Plugin plugin;
    
    public SpellManager(Plugin plugin) {
        this.plugin = plugin;
        
        registerSpell(new Ascendio(plugin));
        registerSpell(new Aquamenti(plugin));
        registerSpell(new ArundoAdicio(plugin));
        registerSpell(new AvadaKedavra(plugin));
        registerSpell(new ArestoMomentum(plugin));
        registerSpell(new AraniaExumai(plugin));
        registerSpell(new Apparate(plugin));
        registerSpell(new Bombarda(plugin));
        registerSpell(new Avis(plugin));
        registerSpell(new Colloshoo(plugin));
        registerSpell(new Confringo(plugin));
        registerSpell(new Crucio(plugin));
        registerSpell(new Conjunctivitis(plugin));
        registerSpell(new Crustulam(plugin));
        registerSpell(new Disllusinment(plugin));
        registerSpell(new Ebublio(plugin));
        registerSpell(new Duro(plugin));
        registerSpell(new ExpectoPatronum(plugin));
        registerSpell(new Expelliarmus(plugin));
        registerSpell(new MeteolojinxRecanto(plugin));
        registerSpell(new Ferula(plugin));
        registerSpell(new FiendFyre(plugin));
        registerSpell(new FiniteIncantatum(plugin));
        registerSpell(new Ennervate(plugin));
        registerSpell(new Episkey(plugin));
        registerSpell(new Flipendo(plugin));
        registerSpell(new FlipendoDuo(plugin));
        registerSpell(new FlipendoTrio(plugin));
        registerSpell(new Fumos(plugin));
        registerSpell(new Glacius(plugin));
        registerSpell(new IgnisResistitur(plugin));
        registerSpell(new Ignistas(plugin));
        registerSpell(new Impedimenta(plugin));
        registerSpell(new Levicorpus(plugin));
        registerSpell(new Liberacorpus(plugin));
        registerSpell(new Lumos(plugin));
        registerSpell(new LumosMaxima(plugin));
        registerSpell(new Nox(plugin));
        registerSpell(new Solem(plugin));
        registerSpell(new Protego(plugin));
        registerSpell(new Trinus(plugin));
        registerSpell(new Verdimillious(plugin));
        registerSpell(new Mosmorde(plugin));
        registerSpell(new Reducto(plugin));
        registerSpell(new PetrificusTotalus(plugin));
        registerSpell(new Ventus(plugin));
        registerSpell(new Imperio(plugin));
        registerSpell(new Obliviate(plugin));
        registerSpell(new Obscuro(plugin));
        registerSpell(new Entomorphis(plugin));
        registerSpell(new Homorphus(plugin));
        registerSpell(new Melofors(plugin));
        registerSpell(new Avifors(plugin));
        registerSpell(new Lapifors(plugin));
        registerSpell(new Rennervate(plugin));
        registerSpell(new Reparo(plugin));
        registerSpell(new Accio(plugin));
        registerSpell(new Serpensortia(plugin));
    }
    
    /**
     * Registers the given spell in the registered spells hash map. This will
     * the <code>Spell</code> object currently registered under the given spell's
     * name.
     * 
     * @param spell
     *          Spell to register.
     */
    public static void registerSpell(Spell spell) {
        getRegisteredSpells().put(spell.getName(), spell);
    }
    
    /**
     * @return
     *          The <code>HashMap</code> for all currently registered spells.
     *          Indexed by spell name.
     */
    public static HashMap<String, Spell> getRegisteredSpells() {
        return registeredSpells;
    }
    
    /**
     * @param spellName
     *          Name of the spell to retrieve.
     * 
     * @return
     *          The registered <code>Spell</code> with the given name.
     *          Can be <code>null</code> if not registered.
     */
    public static Spell getSpell(String spellName) {
        return getRegisteredSpells().get(spellName);
    }
    
    /**
     * @return
     *          The plugin with which this object is registered for events.
     */
    public Plugin getPlugin() {
        return plugin;
    }
    
}
