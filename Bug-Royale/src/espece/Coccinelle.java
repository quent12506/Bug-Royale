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
public class Coccinelle extends Espece{

    public Coccinelle() {
        super("coccinelle", 10, 90, null, 25, 20, null, "Se rend 20PV");
        try {
            this.sprite = ImageIO.read(getClass().getResource("/resources/coccinelle.png"));
            this.spriteProjectile = ImageIO.read(getClass().getResource("/resources/coccinelle_proj.png"));
        } catch (IOException ex) {
            Logger.getLogger(Joueur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
}
