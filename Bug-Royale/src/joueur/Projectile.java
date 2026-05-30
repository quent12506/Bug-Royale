package joueur;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import outils.Coordonnee;
import sql.ProjectileSQL;

/**
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
    private Coordonnee vitesseActuelle = new Coordonnee(0, 0);

    public Projectile(Joueur proprietaire, Coordonnee position, Coordonnee cible, double rayon) {
        this.position = position;
        this.cible = cible;
        this.direction = this.position.vecteurDirection(this.position, this.cible);
        this.rayon = rayon;
        this.actif = true;
        this.temps = 0;
        this.proprietaire = proprietaire;
        this.vitesseActuelle = new Coordonnee(0, 0);
        chargerProjectileDepuisEspece();

        if (this.proprietaire != null && this.proprietaire.getEspece() != null) {
            this.vitesse = this.proprietaire.getEspece().getVitesseProjectile();
        } else {
            this.vitesse = 5;
        }
    }

    
    public Projectile() {
        this.position = new Coordonnee(0, 0);
        this.cible = new Coordonnee(1, 0);
        this.direction = new Coordonnee(1, 0);
        this.rayon = 5;
        this.actif = true;
        this.temps = 0;
        this.vitesse = 5;
        this.vitesseActuelle = new Coordonnee(0, 0);

    }

    private void chargerProjectileDepuisEspece() {
        if (this.proprietaire == null || this.proprietaire.getEspece() == null) {
            return;
        }

        this.sprite = this.proprietaire.getEspece().getSpriteProjectile();

        if (this.sprite == null) {
            System.err.println("Sprite projectile manquant pour l'espèce : "
                    + this.proprietaire.getEspece().getStringEspece());
        }
    }

    public void setPosition(Coordonnee position) {
        this.position = position;

        if (this.position != null && this.cible != null) {
            this.direction = this.position.vecteurDirection(this.position, this.cible);
        }
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
        chargerProjectileDepuisEspece();

        if (this.proprietaire != null && this.proprietaire.getEspece() != null) {
            this.vitesse = this.proprietaire.getEspece().getVitesseProjectile();
        }
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
        if (nouvelleDirection == null || nouvelleDirection.norm() == 0) {
            this.direction = new Coordonnee(1, 0);
        } else {
            this.direction = nouvelleDirection.normalize();
        }
        this.vitesseActuelle = new Coordonnee(0, 0);
    }


    public void lancerVersCible(Coordonnee cible) {
        if (this.position == null || cible == null) {
            return;
        }
        this.cible = cible;
        Coordonnee d = new Coordonnee();
        this.direction = d.vecteurDirection(position, cible);
        this.vitesseActuelle = new Coordonnee(0, 0);
        this.actif = true;
    }

    public void MAJ(double deltaT, ProjectileSQL projectileSQL) {
        if (!actif) {
            return;
        }

        if (projectileSQL == null || this.proprietaire == null || this.direction == null || this.position == null) {
            this.actif = false;
            return;
        }

        temps += deltaT;

        if (temps >= 50) {
            actif = false;
            projectileSQL.supprimerProjectile(this);
            return;
        }

        double acceleration = 0.20;
        //double ralentissement = 0.80;

        Coordonnee vitesseVoulue = this.direction.normalize().mult(this.vitesse);
        this.vitesseActuelle = this.vitesseActuelle.mult(1.0 - acceleration)
                .add(vitesseVoulue.mult(acceleration));

        if (this.vitesseActuelle.norm() > 0.05) {
            this.position = this.position.add(this.vitesseActuelle.mult(deltaT));
        } else {
            this.vitesseActuelle = new Coordonnee(0, 0);
        }

        projectileSQL.modifierProjectile(this, this.getProprietaire().getNom());
    }

    public boolean joueurTouche(Joueur JoueurATester){ //Joueur touche si le centre du projectile est dans la hitbox de l'insecte (taille du sprite)
        if (this.sprite == null || this.position == null || JoueurATester == null || JoueurATester.getEspece() == null) {
            return false;
        }

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

    public void rendu(Graphics2D contexte) {
        if (this.sprite == null || this.position == null) {
            return;

        }

        double x = this.position.getx();
        double y = this.position.gety();

        double dx = this.direction.getx();
        double dy = this.direction.gety();

        if (dx == 0 && dy == 0) {
            return;
        }

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

    @Override
    public String toString() {
        return String.format("Projectile{pos=(%f,%f), cible=(%f,%f), direction=(%f,%f), vitesse=%f, actif=%b}",
                position.getx(), position.gety(),
                cible.getx(), cible.gety(),
                direction.getx(), direction.gety(),
                
                vitesse, actif);
    }

    public void setVitesse(double vitesse) {
        this.vitesse = vitesse;
    }
    
    
}