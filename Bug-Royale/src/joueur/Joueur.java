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
    private int HP;
    private Projectile projectileTire;

    public Joueur(String nom, Espece espece, double x, double y) { //Création manuelle d'un joueur, tout les attributs de la BDD à rentrer
        
        this.position = new Coordonnee(x, y);
        this.toucheO = false;
        this.toucheE = false;
        this.toucheN = false;
        this.toucheS = false;
        this.tirJoueur = false;
        this.nom = nom;
        this.espece = espece;
        this.HP = espece.getHPParDefaut();
        this.vitesse = espece.getVitesseDeplacement();
        
    }

    public Joueur() { //Création d'un joueur par défaut
        
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
        
        Coordonnee direction = new Coordonnee( //déplacement du joueur local
            (toucheE ? 1 : 0) - (toucheO ? 1 : 0),
            (toucheS ? 1 : 0) - (toucheN ? 1 : 0)
        );
        
        position = position.add(direction.normalize().mult(vitesse));
        
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
    
    public Joueur miseAJourTestMultiJ3(Joueur J3){ //Classe de test pour vérifier que le programme actualise tout les joueurs présents
        if ((J3.getY()<150)&&(J3.getX()==100)){
            J3.setPosition(J3.getX(), J3.getY()+J3.getEspece().getVitesseDeplacement());
        }
        if ((J3.getY()>50)&&(J3.getX()==110)){
            J3.setPosition(J3.getX(), J3.getY()-J3.getEspece().getVitesseDeplacement());
        }
        if (J3.getY()>=150){
            J3.setPosition(J3.getX()+10, J3.getY());
        }
        if (J3.getY()<=50){
            J3.setPosition(J3.getX()-10, J3.getY());
        }
        return J3;
    }
    
    public Joueur miseAJourTestMultiJ2(Joueur J2){ //Classe de test pour vérifier que le programme actualise tout les joueurs présents
        if ((J2.getY()<150)&&(J2.getX()==50)){
            J2.setPosition(J2.getX(), J2.getY()+J2.getEspece().getVitesseDeplacement());
        }
        if ((J2.getY()>50)&&(J2.getX()==60)){
            J2.setPosition(J2.getX(), J2.getY()-J2.getEspece().getVitesseDeplacement());
        }
        if (J2.getY()>=150){
            J2.setPosition(J2.getX()+10, J2.getY());
        }
        if (J2.getY()<=50){
            J2.setPosition(J2.getX()-10, J2.getY());
        }
        return J2;
    }
    
    public Joueur miseAJourTestMultiJ4(Joueur J3){ //Classe de test pour vérifier que le programme actualise tout les joueurs présents
        if ((J3.getY()<150)&&(J3.getX()==150)){
            J3.setPosition(J3.getX(), J3.getY()+J3.getEspece().getVitesseDeplacement());
        }
        if ((J3.getY()>50)&&(J3.getX()==160)){
            J3.setPosition(J3.getX(), J3.getY()-J3.getEspece().getVitesseDeplacement());
        }
        if (J3.getY()>=150){
            J3.setPosition(J3.getX()+10, J3.getY());
        }
        if (J3.getY()<=50){
            J3.setPosition(J3.getX()-10, J3.getY());
        }
        return J3;
    }

    public void rendu(Graphics2D contexte) { //affichage d'un joueur
        contexte.drawImage(this.espece.getSprite(), (int) position.getx(), (int) position.gety(), null);
    }
    
    public void renduProjectile(Graphics2D contexte) {
        contexte.drawImage(this.projectileTire.getSprite(), (int) this.projectileTire.getPosition().getx(), (int) this.projectileTire.getPosition().gety(), null);
    }

    
    
    
    
    
}