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
package io.github.trystancannon.diagoncraftspells.file;

import io.github.trystancannon.diagoncraftspells.player.Wizard;
import io.github.trystancannon.diagoncraftspells.spell.Spell;
import io.github.trystancannon.diagoncraftspells.spell.SpellManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

/**
 * Contains static implementations of file loading methods that will be used
 * for reading data specifically for the plugin.
 * 
 * For example, reading the known spells save file for each player.
 * 
 * @author Trystan Cannon
 */
public final class Loader {
    
    public static Wizard loadWizardProfile(UUID playerId, String profilePath) {
        List<String> knownSpellNames = readFile(profilePath);
        
        if (knownSpellNames == null || playerId == null) {
            return null;
        }
        
        Wizard wizard = new Wizard(playerId);
        
        for (String spellName : knownSpellNames) {
            Spell spell = SpellManager.getSpell(spellName);
            
            if (spell != null) {
                wizard.addSpell(spell);
            }
        }
        
        return wizard;
    }
    
    /**
     * Reads all of the lines from a file. Returns <code>null</code> upon failure.
     * 
     * @param filePath
     *          The path of the file to be read.
     * 
     * @return
     *          A <code>List</code> of all lines in the file. <code>null</code> if there was a filure.
     */
    public static List<String> readFile(String filePath) {
        if (filePath == null) {
            return null;
        }
        
        File file = new File(filePath);
        
        if (!file.exists() || file.isDirectory()) {
            return null;
        }
        
        List<String> lines = new ArrayList<>();
        
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNextLine()) {
                lines.add(fileScanner.nextLine());
            }
            
            fileScanner.close();
        } catch (Exception failure) {
            return null;
        }
        
        return lines;
    }
    
}
