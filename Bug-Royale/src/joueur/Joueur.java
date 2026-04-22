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
    private String nom;
    private String espece;
    private int HP;

    public Joueur(String nom, String espece, int HP, double x, double y) { //Création manuelle d'un joueur, tout les attributs de la BDD à rentrer
        try {
            this.sprite = ImageIO.read(getClass().getResource("../resources/donkeyKong.png"));
        } catch (IOException ex) {
            Logger.getLogger(Joueur.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.position = new Coordonnee(x, y);
        this.toucheO = false;
        this.toucheE = false;
        this.toucheN = false;
        this.toucheS = false;
        this.nom = nom;
        this.espece = espece;
        this.HP = HP;
        
    }

    public Joueur() { //Création d'un joueur par défaut
        try {
            this.sprite = ImageIO.read(getClass().getResource("../resources/donkeyKong.png"));
        } catch (IOException ex) {
            Logger.getLogger(Joueur.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.toucheO = false;
        this.toucheE = false;
        this.toucheN = false;
        this.toucheS = false;
    }
   
    //Ensembles de getter, setter et toString
    public void setPosition(double x, double y) {
        this.position = new Coordonnee(x,y);
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
    
    public boolean getToucheEst(){
        return this.toucheE;
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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEspece() {
        return espece;
    }

    public void setEspece(String espece) {
        this.espece = espece;
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }

    @Override
    public String toString() {
        return "Joueur{" + "position=" + position + ", nom=" + nom + ", espece=" + espece + ", HP=" + HP + '}';
    }

    public void miseAJour() { //déplacement du joueur local
        
        Coordonnee direction = new Coordonnee(
            (toucheE ? 1 : 0) - (toucheO ? 1 : 0),
            (toucheS ? 1 : 0) - (toucheN ? 1 : 0)
        );
        
        position = position.add(direction.normalize().mult(VITESSE));
    }
    
    public Joueur miseAJourTestMulti(Joueur J3){ //Classe de test pour vérifier que le programme actualise tout les joueurs présents
        if ((J3.getY()<150)&&(J3.getX()==100)){
            J3.setPosition(J3.getX(), J3.getY()+10);
        }
        if ((J3.getY()>50)&&(J3.getX()==110)){
            J3.setPosition(J3.getX(), J3.getY()-10);
        }
        if (J3.getY()==150){
            J3.setPosition(J3.getX()+10, J3.getY());
        }
        if (J3.getY()==50){
            J3.setPosition(J3.getX()-10, J3.getY());
        }
        return J3;
    }

    public void rendu(Graphics2D contexte) { //affichage d'un joueur
        contexte.drawImage(this.sprite, (int) position.getx(), (int) position.gety(), null);
    }

    
    
    
    
    
}