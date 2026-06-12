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
import java.awt.Color;

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
    private long mouvementBloqueJusqua = 0;
    private Coordonnee vitesseActuelle = new Coordonnee(0, 0);
    private int HPMax;
    private double[][] matriceCollision;
    private long prochainUsageCapacite = 0;
    private long finCapacite = 0;
    private double multiplicateurVitesse = 1.0; 
    private boolean CollisionsActives = true;
    private double multiplicateurDegats = 1.0;
    private boolean touche=false;

    public Joueur(String nom, Espece espece, double x, double y, int HP, double[][] mur) { //Création manuelle d'un joueur, tout les attributs de la BDD à rentrer
        
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
        this.HPMax = HP;
        this.vitesse = espece.getVitesseDeplacement();
        this.matriceCollision = mur;
    }

    public Joueur() { //Création d'un joueur par défaut
        this.position = new Coordonnee();
        this.direction = new Coordonnee(1, 0);
        this.toucheO = false;
        this.toucheE = false;
        this.toucheN = false;
        this.toucheS = false;
        this.tirJoueur = false;
        this.HPMax = 100;
    }
   
    //Ensembles de getter, setter et toString
    public void setPosition(double x, double y) {
        this.position = new Coordonnee(x,y);
    }

    public boolean estMouvementBloque() {
        return System.currentTimeMillis() < this.mouvementBloqueJusqua;
    }

    public void bloquerMouvementPendant(long dureeMs) {
        this.mouvementBloqueJusqua = System.currentTimeMillis() + dureeMs;

        this.toucheO = false;
        this.toucheE = false;
        this.toucheN = false;
        this.toucheS = false;
    }

    public void reculerDepuis(Joueur autre, double distanceRecul) {
        if (autre == null) {
            return;
        }

        double centreX = this.position.getx() + this.espece.getLargeur() / 2.0;
        double centreY = this.position.gety() + this.espece.getHauteur() / 2.0;

        double autreCentreX = autre.getPosition().getx() + autre.getEspece().getLargeur() / 2.0;
        double autreCentreY = autre.getPosition().gety() + autre.getEspece().getHauteur() / 2.0;

        Coordonnee directionRecul = new Coordonnee(
                centreX - autreCentreX,
                centreY - autreCentreY
        );

        if (directionRecul.norm() == 0) {
            directionRecul = new Coordonnee(1, 0);
        }

        directionRecul = directionRecul.normalize();

        this.position = this.position.add(directionRecul.mult(distanceRecul));
        this.direction = directionRecul;
    }

    public void separerDe(Joueur autre) {
        if (autre == null || autre.getEspece() == null || this.espece == null) {
            return;
        }

        double thisLeft = this.position.getx();
        double thisRight = this.position.getx() + this.espece.getLargeur();
        double thisTop = this.position.gety();
        double thisBottom = this.position.gety() + this.espece.getHauteur();

        double autreLeft = autre.getPosition().getx();
        double autreRight = autre.getPosition().getx() + autre.getEspece().getLargeur();
        double autreTop = autre.getPosition().gety();
        double autreBottom = autre.getPosition().gety() + autre.getEspece().getHauteur();

        double chevauchementX = Math.min(thisRight, autreRight) - Math.max(thisLeft, autreLeft);
        double chevauchementY = Math.min(thisBottom, autreBottom) - Math.max(thisTop, autreTop);

        if (chevauchementX <= 0 || chevauchementY <= 0) {
            return;
        }

        double centreX = this.position.getx() + this.espece.getLargeur() / 2;
        double centreY = this.position.gety() + this.espece.getHauteur() / 2;

        double autreCentreX = autre.getPosition().getx() + autre.getEspece().getLargeur() / 2;
        double autreCentreY = autre.getPosition().gety() + autre.getEspece().getHauteur() / 2;

        if (chevauchementX < chevauchementY) {
            double correction = chevauchementX / 2 + 1;

            if (centreX < autreCentreX) {
                this.position = this.position.add(new Coordonnee(-correction, 0));
                autre.setPosition(autre.getX() + correction, autre.getY());
            } else {
                this.position = this.position.add(new Coordonnee(correction, 0));
                autre.setPosition(autre.getX() - correction, autre.getY());
            }
        } else {
            double correction = chevauchementY / 2.0 + 1.0;

            if (centreY < autreCentreY) {
                this.position = this.position.add(new Coordonnee(0, -correction));
                autre.setPosition(autre.getX(), autre.getY() + correction);
            } else {
                this.position = this.position.add(new Coordonnee(0, correction));
                autre.setPosition(autre.getX(), autre.getY() - correction);
            }
        }
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

    public int getHPMax() {
        return HPMax;
    }

    public void setHPMax(int HPMax) {
        this.HPMax = Math.max(1, HPMax);
    }
    
    

    @Override
    public String toString() {
        return "Joueur{" + "position=" + position + ", nom=" + nom + ", espece=" + espece.getStringEspece() + ", HP=" + HP + '}';
    }

    public void miseAJour(ProjectileSQL projectileSQL) {
        
        boolean murTouche;
        
        mettreAJourCapacite();

        Coordonnee deplacement = new Coordonnee(
                (toucheE ? 1 : 0) - (toucheO ? 1 : 0),
                (toucheS ? 1 : 0) - (toucheN ? 1 : 0)
        );

        double acceleration = 0.20;
        double ralentissement = 0.80;
        double facteurVitesse = 0.40;

        if (estMouvementBloque()) {
            this.vitesseActuelle = this.vitesseActuelle.mult(ralentissement);
        } else if (deplacement.norm() > 0) {
            Coordonnee directionVoulue = deplacement.normalize();
            Coordonnee vitesseVoulue = directionVoulue.mult(this.vitesse * multiplicateurVitesse * facteurVitesse);

            this.vitesseActuelle = this.vitesseActuelle.mult(1.0 - acceleration)
                    .add(vitesseVoulue.mult(acceleration));

            this.direction = directionVoulue;
        } else {
            this.vitesseActuelle = this.vitesseActuelle.mult(ralentissement);
        }

        if (this.vitesseActuelle.norm() > 0.05) {
            //murTouche = this.position.segmentIntercepteMur(this.matriceCollision,this.position,this.position.add(this.vitesseActuelle));
            if (!hitboxTouchee(this.position.add(this.vitesseActuelle))){
                this.position = this.position.add(this.vitesseActuelle);
            }else{
                repousserDepuisMur();
            }
            
            } else {
            this.vitesseActuelle = new Coordonnee(0, 0);
            }

            if (this.projectileTire != null) {
            this.projectileTire.MAJ(1, projectileSQL,this.matriceCollision);
                if (!this.projectileTire.isActif()){
                    this.projectileTire = null;
                }
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

        renduBarreVie(contexte, x, y, largeur);
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

        return !(this_right < autre_left || this_left > autre_right || 
                 this_bottom < autre_top || this_top > autre_bottom);
    }

    private void renduBarreVie(Graphics2D contexte, double x, double y, int largeurSprite) {
        int largeurBarre = 50;
        int hauteurBarre = 7;
        int margeAuDessus = 10;

        int barreX = (int) (x + largeurSprite / 2.0 - largeurBarre / 2.0);
        int barreY = (int) y - margeAuDessus - hauteurBarre;

        double pourcentageVie = Math.max(0.0, Math.min(1.0, (double) this.HP / this.HPMax));
        int largeurVie = (int) (largeurBarre * pourcentageVie);

        contexte.setColor(Color.DARK_GRAY);
        contexte.fillRect(barreX, barreY, largeurBarre, hauteurBarre);

        if (pourcentageVie > 0.5) {
            contexte.setColor(Color.GREEN);
        } else if (pourcentageVie > 0.25) {
            contexte.setColor(Color.ORANGE);
        } else {
            contexte.setColor(Color.RED);
        }

        contexte.fillRect(barreX, barreY, largeurVie, hauteurBarre);

        contexte.setColor(Color.BLACK);
        contexte.drawRect(barreX, barreY, largeurBarre, hauteurBarre);
        
        int tailleIndicateur = 10;
        int espace = 5;
        int indicateurX = barreX + largeurBarre + espace;
        int indicateurY = barreY - 2;

        if (capacitePrete()) {
            contexte.setColor(Color.GREEN);
        } else {
            contexte.setColor(Color.GRAY);
        }

        contexte.fillOval(indicateurX, indicateurY, tailleIndicateur, tailleIndicateur);

        contexte.setColor(Color.BLACK);
        contexte.drawOval(indicateurX, indicateurY, tailleIndicateur, tailleIndicateur);
    }
    
    public boolean hitboxTouchee(Coordonnee point){
        if (!CollisionsActives){
            return false;
        }
        
        touche=false;
        int xmin=(int) point.getx();
        int ymin=(int) point.gety();
        int xmax=xmin+this.espece.getSprite().getWidth();
        int ymax=ymin+this.espece.getSprite().getHeight();
        for (int i = ymin; i<=ymax+1; i++){
            for (int j = xmin; j<=xmax; j++){
                if (i >= 0 && i < matriceCollision.length && j >= 0 && j < matriceCollision[0].length){
                    if (this.matriceCollision[j][i]==1){
                        touche=true;
                    }
                }
            }
        }
        return touche;
    }
    
    private void repousserDepuisMur() {
        if (this.vitesseActuelle == null || this.vitesseActuelle.norm() == 0) {
            return;
        }

        double forceRepulsion = 2.0;

        Coordonnee directionRepulsion = this.vitesseActuelle.normalize().mult(-forceRepulsion);
        Coordonnee nouvellePosition = this.position.add(directionRepulsion);

        if (!hitboxTouchee(nouvellePosition)) {
            this.position = nouvellePosition;
        }

        this.vitesseActuelle = this.vitesseActuelle.mult(-0.25);
    }
    
    public void activerCapacite() {
        if (this.espece == null) {
            return;
        }

        long maintenant = System.currentTimeMillis();

        if (maintenant < prochainUsageCapacite) {
            return;
        }

        String nomEspece = this.espece.getStringEspece().toLowerCase();

        if (nomEspece.equals("scarabee")) {
            this.multiplicateurVitesse = 20.0;
            this.finCapacite = maintenant + 50;
            this.prochainUsageCapacite = maintenant + 5000;
        }
        if (nomEspece.equals("abeille")) {
            this.CollisionsActives = false;
            this.finCapacite = maintenant + 500;
            this.prochainUsageCapacite = maintenant + 5000;
        }
        if (nomEspece.equals("fourmi")) {
            this.multiplicateurDegats = 2.0;
            this.prochainUsageCapacite = maintenant + 10000;
        }
        if (nomEspece.equals("coccinelle")) {
            this.HP = Math.min(this.HPMax, this.HP + 20);
            this.prochainUsageCapacite = maintenant + 20000;
        }
    }

    private void mettreAJourCapacite() {
        if (System.currentTimeMillis() >= finCapacite) {
            if (!CollisionsActives && hitboxTouchee(this.position)) {
                this.CollisionsActives = false;
                this.reculerDepuis(this, vitesse);
                this.finCapacite = System.currentTimeMillis() + 10;
                return;
            }

            this.multiplicateurVitesse = 1.0;
            this.CollisionsActives = true;
        }
    }
    
    public boolean capacitePrete() {
        return System.currentTimeMillis() >= this.prochainUsageCapacite;
    }
    
    public int getMultiplicateurDegats() {
        return (int)multiplicateurDegats;
    }

    public void consommerBonusDegats() {
        this.multiplicateurDegats = 1;
    }
}