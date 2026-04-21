package joueur;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import outils.Coordonnee;

/**
 *
 * @author florentgausin
 */
public class Joueur {

    private static final double VITESSE = 5.0;

    protected BufferedImage sprite;
    protected Coordonnee position;
    private boolean toucheO, toucheE, toucheN, toucheS;

    public Joueur() {
        try {
            this.sprite = ImageIO.read(getClass().getResource("../resources/donkeyKong.png"));
        } catch (IOException ex) {
            Logger.getLogger(Joueur.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.position = new Coordonnee(170, 320);
        this.toucheO = false;
        this.toucheE = false;
        this.toucheN = false;
        this.toucheS = false;
    }

    public void miseAJour() {
        Coordonnee direction = new Coordonnee(
            (toucheE ? 1 : 0) - (toucheO ? 1 : 0),
            (toucheS ? 1 : 0) - (toucheN ? 1 : 0)
        );

        position = position.add(direction.normalize().mult(VITESSE));

//        if (position.getx() > 380 - sprite.getWidth()) {
//            position = new Coordonnee(380 - sprite.getWidth(), position.gety());
//        }
//        if (position.getx() < 0) {
//            position = new Coordonnee(0, position.gety());
//        }
    }

    public void rendu(Graphics2D contexte) {
        contexte.drawImage(this.sprite, (int) position.getx(), (int) position.gety(), null);
    }

    public void setToucheOuest(boolean etat) { 
        this.toucheO = etat; 
    }
    
    public void setToucheEst(boolean etat)   { 
        this.toucheE = etat; 
    }
    public void setToucheNord(boolean etat)  { 
        this.toucheN = etat; 
    }
    public void setToucheSud(boolean etat)   { 
        this.toucheS = etat; 
    }

    public double getX() { 
        return position.getx(); 
    }
    public double getY() { 
        return position.gety(); 
    }

    public double getLargeur() { 
        return sprite.getWidth(); 
    }
    public double getHauteur() { 
        return sprite.getHeight(); 
    }
}