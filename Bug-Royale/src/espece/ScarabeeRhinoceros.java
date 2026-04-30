/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package espece;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import joueur.Joueur;

/**
 *
 * @author hugom
 */
public class ScarabeeRhinoceros extends Espece{

    public ScarabeeRhinoceros() {
        super("ScarabeeRhinoceros", 3.2, 180, null);
        try {
            this.sprite = ImageIO.read(getClass().getResource("../resources/rhinoceros.png"));
        } catch (IOException ex) {
            Logger.getLogger(Joueur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}
