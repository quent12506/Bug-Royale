package joueur;

import espece.Espece;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import outils.Coordonnee;
import sql.JoueurSQL;
import sql.ProjectileSQL;
import java.awt.geom.AffineTransform;

/**
 *
 * @author florentgausin
 */
public class Joueur {

    private double vitesse;
    protected Coordonnee position;
    private boolean toucheO, toucheE, toucheN, toucheS, tirJoueur;
    private String nom;
    private Espece espece;
    private Coordonnee direction;
    private int HP;
    private Projectile projectileTire;

    public Joueur(String nom, Espece espece, double x, double y, int HP) { //Création manuelle d'un joueur, tout les attributs de la BDD à rentrer
        
        this.position = new Coordonnee(x, y); 
        this.direction = new Coordonnee(1,0);
        this.toucheO = false;
        this.toucheE = false;
        this.toucheN = false;
        this.toucheS = false;
        this.tirJoueur = false;
        this.nom = nom;
        this.espece = espece;
        this.HP = HP;
        this.vitesse = espece.getVitesseDeplacement();
        
    }

    public Joueur() { //Création d'un joueur par défaut
        this.position = new Coordonnee();
        this.direction = new Coordonnee(1, 0);
        this.toucheO = false;
        this.toucheE = false;
        this.toucheN = false;
        this.toucheS = false;
        this.tirJoueur = false;
    }
   
    //Ensembles de getter, setter et toString
    public void setPosition(double x, double y) {
        this.position = new Coordonnee(x,y);
    }

    public boolean isTirJoueur() {
        return tirJoueur;
    }

    public void setTirJoueur(boolean tirJoueur) {
        this.tirJoueur = tirJoueur;
    }

    public Projectile getProjectileTire() {
        return projectileTire;
    }

    public void setProjectileTire(Projectile projectileTire) {
        this.projectileTire = projectileTire;
    }

    public Coordonnee getPosition() {
        return position;
    }
    
    public Coordonnee getDirection(){
        return direction;
    }
    
    public void setDirection(Coordonnee direction){
        this.direction = direction;
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

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Espece getEspece() {
        return espece;
    }

    public void setEspece(Espece espece) {
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
        return "Joueur{" + "position=" + position + ", nom=" + nom + ", espece=" + espece.getStringEspece() + ", HP=" + HP + '}';
    }

    public void miseAJour(ProjectileSQL projectileSQL) {

        Coordonnee deplacement = new Coordonnee(
                (toucheE ? 1 : 0) - (toucheO ? 1 : 0),
                (toucheS ? 1 : 0) - (toucheN ? 1 : 0)
        );


        if (deplacement.norm() > 0) {
            this.direction = deplacement.normalize();
            this.position = this.position.add(this.direction.mult(this.vitesse));
        }


        if (this.projectileTire!=null){
            this.projectileTire.MAJ(1,projectileSQL);
        }
        
//        if (this.tirJoueur){
//            Coordonnee cible = new Coordonnee();
//            cible.setX(vitesse);
//            this.projectileTire = new Projectile(this.position,cible);
//        }
    }
    
    public void joueurMort(JoueurSQL lienSQL){
        lienSQL.supprimerJoueur(this);
    }

//    public void rendu(Graphics2D contexte) { //affichage d'un joueur
//        if (this.espece != null){
//        contexte.drawImage(this.espece.getSprite(), (int) position.getx(), (int) position.gety(), null);
//        }
//    }

    public void rendu(Graphics2D contexte) {
        if (this.espece == null || this.espece.getSprite() == null || this.direction == null) {
            return;
        }

        BufferedImage sprite = this.espece.getSprite();

        double x = this.position.getx();
        double y = this.position.gety();

        double dx = this.direction.getx();
        double dy = this.direction.gety();

        double angle = Math.atan2(dy, dx) + Math.PI / 2.0; //A adapter

        int largeur = sprite.getWidth();
        int hauteur = sprite.getHeight();

        double centreX = x + largeur / 2.0;
        double centreY = y + hauteur / 2.0;

        AffineTransform ancienneTransformation = contexte.getTransform();

        contexte.translate(centreX, centreY);
        contexte.rotate(angle);
        contexte.translate(-largeur / 2.0, -hauteur / 2.0);

        contexte.drawImage(sprite, 0, 0, null);

        contexte.setTransform(ancienneTransformation);
    }

    
    public void renduProjectile(Graphics2D contexte) {
        contexte.drawImage(this.projectileTire.getSprite(), (int) this.projectileTire.getPosition().getx(), (int) this.projectileTire.getPosition().gety(), null);
    }

    public boolean estEnCollisionAvec(Joueur autre) {
        if (autre == null || autre.getEspece() == null || this.espece == null) {
            return false;
        }
        
        double this_left = this.position.getx();
        double this_right = this.position.getx() + this.espece.getLargeur();
        double this_top = this.position.gety();
        double this_bottom = this.position.gety() + this.espece.getHauteur();
        
        double autre_left = autre.getPosition().getx();
        double autre_right = autre.getPosition().getx() + autre.getEspece().getLargeur();
        double autre_top = autre.getPosition().gety();
        double autre_bottom = autre.getPosition().gety() + autre.getEspece().getHauteur();
        
        // Vérifie si les rectangles se chevauchent
        return !(this_right < autre_left || this_left > autre_right || 
                 this_bottom < autre_top || this_top > autre_bottom);
    }

}