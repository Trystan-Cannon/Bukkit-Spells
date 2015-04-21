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

import java.io.File;
import java.io.PrintWriter;
import org.bukkit.entity.Player;

/**
 * Contains static implementations of file saving methods that will be used
 * for writing data specifically for the plugin.
 * 
 * For example, writing the known spells save file for each player.
 * 
 * @author Trystan Cannon
 */
public final class Saver {
    
    /**
     * Saves the given <code>Wizard</code> object to a file at the given
     * path.
     * 
     * Each profile is saved in a file whose name is the UUID of the player. The
     * contents is each spell name that the player knows on a new line.
     * 
     * @param folderPath
     *              Path of the folder in which to save the profile.
     * @param wizard
     *              Wizard to save.
     * 
     * @return
     *          <code>true</code> if the file saved without failure.
     */
    public static boolean saveWizardProfile(String folderPath, Wizard wizard) {
        if (wizard == null) {
            return false;
        }
        
        // Title the file with the wizard's (player's) unique id.
        File saveFile = new File(folderPath + "/" + wizard.getUniqueId() + ".txt");
        
        if (!saveFile.exists()) {
            try {
                // No need to check the return value because it would be
                // false only if the file already existed, but we checked that above.
                saveFile.createNewFile();
            } catch (Exception failure) {
                return false;
            }
        } else if (saveFile.isDirectory()) {
            return false;
        }
        
        // Write the name of each spell on a new line in the save file.
        try (PrintWriter writer = new PrintWriter(saveFile)) {
            for (Spell spell : wizard.getKnownSpells()) {
                writer.println(spell.getName());
            }
            
            writer.flush();
            writer.close();
        } catch (Exception failure) {
            return false;
        }
        
        return true;
    }
    
}
