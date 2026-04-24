package joueur;

/**
 * Classe représentant un projectile avec physique 2D (vitesse constante)
 * @author Xama66
 */
public class Projectile {
    private Coordonnee position;      // Position du projectile
    private Coordonnee direction;     // Direction du projectile (définie par l'utilisateur)
    private Coordonnee vitesse;       // Vecteur vitesse
    private double rayon;             // Rayon du projectile pour les collisions
    private boolean actif;            // Si le projectile est encore en vol
    private double temps;             // Temps écoulé depuis le lancement

    public Projectile(Coordonnee position, Coordonnee direction, double rayon) {
        this.position = position;
        this.direction = direction;
        this.vitesse = Coordonnee.vecteurDirection(this.position, this.direction);
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

    public Coordonnee getVitesse() {
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

    public double getVitesseNorme() {
        return vitesse.norm();
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public void setDirection(Coordonnee nouvelleDirection) {
        this.direction = nouvelleDirection;
        this.vitesse = Coordonnee.vecteurDirection(position, direction);
    }


    public void lancerVersCible(Coordonnee cible) {
        this.direction = cible;
        this.vitesse = Coordonnee.vecteurDirection(position, direction);
        this.actif = true;
    }

    public void MAJ(double deltaT) {
        if (!actif){
            return;
        }
        Coordonnee deplacement = vitesse.mult(deltaT); // p = p0 + v*dt
        position = position.add(deplacement);

        temps += deltaT;
    }


    public boolean detecteCollisionCercle(Coordonnee centre, double rayon) {
        Coordonnee positionSuivante = position.add(vitesse.mult(0.016)); // ~16ms pour 60 FPS
        return Coordonnee.segmentIntercepteCercle(position, positionSuivante, centre, rayon + this.rayon);
    }

    public boolean detecteCollisionSurTrajectoire(Coordonnee positionPrecedente, Coordonnee centre, double rayon) {
        return Coordonnee.segmentIntercepteCercle(positionPrecedente, position, centre, rayon + this.rayon);
    }


    public Coordonnee calculerPositionAuTemps(double t) {
        Coordonnee pos0 = position.sub(vitesse.mult(temps));
        return pos0.add(vitesse.mult(t));
    }


    public Coordonnee calculerPositionADistance(double distance) {
        if (distance < 0) return null;
        double vitesseNorme = vitesse.norm();
        if (vitesseNorme == 0) return position;
        double t = distance / vitesseNorme;
        return calculerPositionAuTemps(temps + t);
    }


 
    public void arreter() {
        this.actif = false;
        this.vitesse = new Coordonnee(0, 0);
    }

    @Override
    public String toString() {
        return String.format("Projectile{pos=(%f,%f), dir=(%f,%f), vitesse=(%f,%f), rayon=%f, actif=%b}",
                position.getx(), position.gety(),
                direction.getx(), direction.gety(),
                vitesse.getx(), vitesse.gety(),
                rayon, actif);
    }
}