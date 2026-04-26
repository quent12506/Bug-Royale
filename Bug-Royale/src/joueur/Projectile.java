package joueur;

import outils.Coordonnee;

/**
 * Classe représentant un projectile avec physique 2D (vitesse constante)
 * @author Xama66
 */
public class Projectile {
    private Coordonnee position;      // Position du projectile
    private Coordonnee cible;     // Direction du projectile (définie par l'utilisateur)
    private Coordonnee direction;       // Vecteur direction normalise
    private double vitesse;
    private double rayon;             // Rayon du projectile pour les collisions
    private boolean actif;            // Si le projectile est encore en vol
    private double temps;             // Temps écoulé depuis le lancement

    public Projectile(Coordonnee position, Coordonnee direction, double rayon) {
        this.position = position;
        this.cible = cible;
        Coordonnee d = new Coordonnee();
        this.direction = d.vecteurDirection(this.position, this.cible);
        this.rayon = rayon;
        this.actif = true;
        this.temps = 0;
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

    public void MAJ(double deltaT) { //A MODIFIER
        if (!actif){
            return;
        }
        Coordonnee deplacement = direction.mult(vitesse*deltaT); // p = p0 + v*dt
        position = position.add(deplacement);

        temps += deltaT;
    }


    public boolean detecteCollisionCercle(Coordonnee centre, double rayon) {
        Coordonnee positionSuivante = position.add(direction.mult(0.016*vitesse)); // ~16ms pour 60 FPS
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

    @Override
    public String toString() {
        return String.format("Projectile{pos=(%f,%f), dir=(%f,%f), vitesse=(%f,%f), rayon=%f, actif=%b}",
                position.getx(), position.gety(),
                cible.getx(), cible.gety(),
                direction.getx(), direction.gety(),
                
                rayon, actif);
    }
}