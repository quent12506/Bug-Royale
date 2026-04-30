package joueur;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import outils.Coordonnee;
import sql.ProjectileSQL;

/**
 * Classe représentant un projectile avec physique 2D (vitesse constante)
 * @author mlopez1
 */
public class Projectile {
    private Coordonnee position;      // Position du projectile
    private Coordonnee cible;         // Direction du projectile (définie par l'utilisateur)
    private Coordonnee direction;     // Vecteur direction normalise
    private double vitesse;
    private double rayon;             // Rayon du projectile pour les collisions
    private boolean actif;            // Si le projectile est encore en vol
    private double temps;             // Temps écoulé depuis le lancement
    private Joueur proprietaire;
    protected BufferedImage sprite;

    public Projectile(Joueur proprietaire, Coordonnee position, Coordonnee cible, double rayon) {
        this.position = position;
        this.cible = cible;
        this.direction = this.position.vecteurDirection(this.position, this.cible);
        this.rayon = rayon;
        this.actif = true;
        this.temps = 0;
        this.vitesse = 5;
        this.proprietaire = proprietaire;
        try {
            this.sprite = ImageIO.read(getClass().getResource("../resources/bdf.png"));
        } catch (IOException ex) {
            Logger.getLogger(Joueur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Projectile() {
        try {
            this.sprite = ImageIO.read(getClass().getResource("../resources/bdf.png"));
        } catch (IOException ex) {
            Logger.getLogger(Joueur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setPosition(Coordonnee position) {
        this.position = position;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    public Joueur getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(Joueur proprietaire) {
        this.proprietaire = proprietaire;
    }
    
    

    public Coordonnee getPosition() {
        return position;
    }

    public Coordonnee getDirection() {
        return direction;
    }

    public double getVitesse() {
        return vitesse;
    }

    public double getRayon() {
        return rayon;
    }

    public boolean isActif() {
        return actif;
    }

    public double getTemps() {
        return temps;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public void setDirection(Coordonnee nouvelleDirection) {
        this.cible = nouvelleDirection;
        Coordonnee d = new Coordonnee();
        this.direction = d.vecteurDirection(position, cible);
    }


    public void lancerVersCible(Coordonnee cible) {
        this.cible = cible;
        Coordonnee d = new Coordonnee();
        this.direction = d.vecteurDirection(position, cible);
        this.actif = true;
    }

    public void MAJ(double deltaT, ProjectileSQL projectileSQL) {
        if (!actif){ //on met a jour seulement les projectiles actifs
            return;
        }
        //Deplacement du projectile
        Coordonnee deplacement = direction.mult(vitesse*deltaT); // p = p0 + v*dt
        position = position.add(deplacement);

        //Update BDD
        projectileSQL.modifierProjectile(this, this.getProprietaire().getNom());
        
        temps += deltaT;
        if (temps>=50){
            actif=false;
            projectileSQL.supprimerProjectile(this);
        }
        
    }

    public boolean joueurTouche(Joueur JoueurATester){ //Joueur touche si le centre du projectile est dans la hitbox de l'insecte (taille du sprite)
        double xMin=JoueurATester.getPosition().getx();
        double xMax=JoueurATester.getPosition().getx()+JoueurATester.getEspece().getSprite().getWidth();
        double yMin=JoueurATester.getPosition().gety();
        double yMax=JoueurATester.getPosition().gety()+JoueurATester.getEspece().getSprite().getHeight();
        return ((xMin<=this.position.getx()+this.sprite.getWidth()/2)&&
                (xMax>=this.position.getx()+this.sprite.getWidth()/2)&&
                (yMin<=this.position.gety()+this.sprite.getHeight()/2)&&
                (yMax>=this.position.gety()+this.sprite.getHeight()/2));
    }

    public boolean detecteCollisionCercle(Coordonnee centre, double rayon) {
        Coordonnee positionSuivante = position.add(direction.mult(vitesse)); // pour l'instant, le deltaT de déplacement vaut 1
        return Coordonnee.segmentIntercepteCercle(position, positionSuivante, centre, rayon + this.rayon);
    }

    public boolean detecteCollisionSurTrajectoire(Coordonnee positionPrecedente, Coordonnee centre, double rayon) {
        return Coordonnee.segmentIntercepteCercle(positionPrecedente, position, centre, rayon + this.rayon);
    }


    public Coordonnee calculerPositionAuTemps(double t) {//A MODIFIER
        Coordonnee pos0 = position.sub(direction.mult(temps*vitesse));
        return pos0.add(direction.mult(t*vitesse));
    }


    public Coordonnee calculerPositionADistance(double distance) {
        if (distance < 0) return null;
        if (vitesse == 0) return position;
        double t = distance / vitesse;
        return calculerPositionAuTemps(temps + t);
    }


 
    public void arreter() {
        this.actif = false;
        this.vitesse = 0;
    }
    
    public void rendu(Graphics2D contexte) { //affichage d'un joueur
        contexte.drawImage(sprite, (int) position.getx(), (int) position.gety(), null);
    }

    @Override
    public String toString() {
        return String.format("Projectile{pos=(%f,%f), cible=(%f,%f), direction=(%f,%f), vitesse=%f, actif=%b}",
                position.getx(), position.gety(),
                cible.getx(), cible.gety(),
                direction.getx(), direction.gety(),
                
                vitesse, actif);
    }
}