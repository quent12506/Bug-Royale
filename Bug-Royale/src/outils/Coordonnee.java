package outils; // Définit le package du projet

/**
 * Classe représentant une coordonnée 2D
 * @author mlopez1
 */
public class Coordonnee { // Déclaration de la classe Coordonnee
    private double x; // Abscisse de la coordonnée
    private double y; // Ordonnée de la coordonnée
    
    public Coordonnee(){
        this.x=0;
        this.y=0;
    }

    public Coordonnee(double x, double y) { // Constructeur prenant x et y
        this.x = x; // Initialise l'abscisse
        this.y = y; // Initialise l'ordonnée
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getx() {
        return this.x; // Retourne l'abscisse
    }

    public double gety() {
        return this.y; // Retourne l'ordonnée
    }

    @Override
    public String toString() {
        return "Coordonnee{" + "x=" + x + ", y=" + y + '}';
    }

    public Coordonnee add(Coordonnee b) { // Additionne deux coordonnées
        return new Coordonnee(this.x + b.x, this.y + b.y); // Retourne la somme
    }

    public Coordonnee sub(Coordonnee b) { // Soustrait deux coordonnées
        return new Coordonnee(this.x - b.x, this.y - b.y); // Retourne la différence
    }

    public Coordonnee mult(double k) { // Multiplie la coordonnée par un scalaire
        return new Coordonnee(this.x * k, this.y * k); // Retourne le résultat
    }

    public double norm() { // Calcule la norme (longueur du vecteur)
        return Math.sqrt(x * x + y * y); // Retourne la norme
    }

    public Coordonnee normalize() { // Normalise le vecteur (longueur 1)
        double n = this.norm(); // Calcule la norme
        if (n == 0) return new Coordonnee(0, 0); // Si norme nulle, retourne (0,0)
        return new Coordonnee(this.x / n, this.y / n); // Sinon, divise chaque composante par la norme
    }

    public Coordonnee rotate(double angle) { // Fait tourner le vecteur d'un angle donné (en radians)
        double nx = this.x * Math.cos(angle) - this.y * Math.sin(angle); // Calcule la nouvelle abscisse
        double ny = this.x * Math.sin(angle) + this.y * Math.cos(angle); // Calcule la nouvelle ordonnée
        return new Coordonnee(nx, ny); // Retourne la nouvelle coordonnée
    }
    
    public Coordonnee vecteurDirection(Coordonnee v1, Coordonnee v2){
        Coordonnee v3 = new Coordonnee();
        v3.setX(v2.getx()-v1.getx());
        v3.setY(v2.gety()-v1.gety());
        return v3.normalize();
    }

    public static boolean segmentIntercepteCercle(Coordonnee A, Coordonnee B, Coordonnee C, double r) { // Détecte l'intersection segment-cercle
        Coordonnee AB = B.sub(A); // Vecteur AB
        Coordonnee AC = C.sub(A); // Vecteur AC
        double t = (AB.x * AC.x + AB.y * AC.y) / (AB.x * AB.x + AB.y * AB.y); // Projection scalaire
        t = Math.max(0, Math.min(1, t)); // Clamp t entre 0 et 1
        Coordonnee projete = new Coordonnee(A.x + t * AB.x, A.y + t * AB.y); // Point projeté sur le segment
        double dist = projete.sub(C).norm(); // Distance du point projeté au centre du cercle
        return dist <= r; // Retourne vrai si le segment coupe le cercle
    }
    public Coordonnee symetrie(Coordonnee centre) {
        return new Coordonnee(2 * centre.x - this.x, 2 * centre.y - this.y);
    }

    @Override
    public boolean equals(Object obj) { // Vérifie l'égalité avec un autre objet
        if (this == obj) { // Si c'est le même objet
            return true; // Ils sont égaux
        }
        if (obj == null) { // Si l'objet est null
            return false; // Pas égal
        }
        if (getClass() != obj.getClass()) { // Si ce n'est pas la même classe
            return false; // Pas égal
        }
        final Coordonnee other = (Coordonnee) obj; // Cast en Coordonnee
        if (this.x != other.x) { // Compare x
            return false; // Pas égal
        }
        return this.y == other.y; // Compare y
    }
}
