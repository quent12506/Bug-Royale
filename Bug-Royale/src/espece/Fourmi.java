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
public class Fourmi extends Espece{

    public Fourmi() {
        super("fourmi", 15, 120, null, 25, 40, null, "Double dégats");
        try {
            this.sprite = ImageIO.read(getClass().getResource("/resources/fourmi.png"));
            this.spriteProjectile = ImageIO.read(getClass().getResource("/resources/fourmi_proj.png"));
        } catch (IOException ex) {
            Logger.getLogger(Joueur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
