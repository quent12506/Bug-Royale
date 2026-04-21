package joueur;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author florentgausin
 */

// Ajouter méthodes miseAJour et Rendu
// Ajouter methode setGauche et setDroite
public class Joueur {
    
    protected BufferedImage sprite;
    protected double x, y;
    private boolean toucheGauche, toucheDroite;

    public Joueur() {
        try {
            this.sprite = ImageIO.read(getClass().getResource("../resources/donkeyKong.png"));
        } catch (IOException ex) {
            Logger.getLogger(Joueur.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.x = 170;
        this.y = 320;
        this.toucheGauche = false;
        this.toucheDroite = false;
    }

    public void miseAJour() {
        if (this.toucheGauche) {
            x -= 5;
           }
        if (this.toucheDroite) {
            x += 5;
        }
        if (x > 380 - sprite.getWidth()) { // collision avec le bord droit de la scene
            x = 380 - sprite.getWidth();
        }
        if (x < 0) { // collision avec le bord gauche de la scene
            x = 0;
        }
    }

    public void rendu(Graphics2D contexte) {
        contexte.drawImage(this.sprite, (int) x, (int) y, null);
    }
    
    public void setToucheGauche(boolean etat) {
        this.toucheGauche = etat;
    }
    
    public void setToucheDroite(boolean etat) {
        this.toucheDroite = etat;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    
    public double getLargeur() {
        return sprite.getHeight();
    }

    public double getHauteur() {
        return sprite.getWidth();
    }
    
}

