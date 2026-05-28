/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Jeu;

/**
 *
 * @author hmas
 */
import java.awt.Color;
import java.awt.Font;
import espece.Abeille;
import espece.Araignee;
import espece.Fourmi;
import espece.Espece;
import espece.Sauterelle;
import espece.Scarabee;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import joueur.Joueur;
import joueur.Projectile;
import sql.JoueurSQL;
import sql.ProjectileSQL;
import outils.Coordonnee;


/**
 *
 * @author hmas
 */
public class Jeu {
    
    private BufferedImage decor;
    private Joueur joueurLocal;
    private int n;
    private JoueurSQL lienSQL;
    private ProjectileSQL projectileSQL;

    public Jeu(String pseudo, String insecte) {
        this(pseudo, insecte, Espece.depuisNom(insecte).getHPParDefaut());
    }

    public Jeu(String pseudo, String insecte, int HP) { //Initialisation du jeu
        try {
            this.decor = ImageIO.read(getClass().getResource("/resources/Map.png")); //Remplacer "jungle.png" par notre carte
        }
        catch (IOException ex) {
            Logger.getLogger(Jeu.class.getName()).log(Level.SEVERE, null, ex);
        }
        Espece especeJoueurLocal = Espece.depuisNom(insecte);
        this.n = 0;

        this.lienSQL = new JoueurSQL();
        this.projectileSQL = new ProjectileSQL(this.lienSQL);

        Coordonnee spawn = choisirSpawn(pseudo);

        this.joueurLocal = new Joueur(pseudo, especeJoueurLocal, spawn.getx(), spawn.gety(), HP);
        this.lienSQL.creerJoueur(this.joueurLocal); //Crétion du joueur local dans la BDD -> entrée en multi
    }
    //Getter et setter
    public Joueur getJoueurLocal() {
        return joueurLocal;
    }

    public JoueurSQL getLienSQL() {
        return lienSQL;
    }

    public ProjectileSQL getProjectileSQL() {
        return projectileSQL;
    }

    public int getN() {
        return n;
    }

    public void setJoueurLocal(Joueur joueurLocal) {
        this.joueurLocal = joueurLocal;
    }
    
    public void kill(){
        ArrayList<String> listeNom = this.lienSQL.listeNom(); //affichage de l'ensemble des joueurs présent en multi
        for (int i=0;i<listeNom.size();i++){
            Joueur joueurASupprimer = this.lienSQL.voirJoueurNom(listeNom.get(i));
            this.lienSQL.supprimerJoueur(joueurASupprimer);
        }
        this.projectileSQL.viderTable();
//        ArrayList<Projectile> listeProjectile = this.projectileSQL.voirEnsembleProjectiles(); //affichage de l'ensemble des projectiles présent dans la BDD
//        for (int i=0;i<listeProjectile.size();i++){
//            this.projectileSQL.supprimerProjectile(listeProjectile.get(i));
//        }
        this.lienSQL.closeTable();
        this.projectileSQL.closeTable();
    }
    
    public void rendu (Graphics2D contexte){ //Rendu du jeu
        contexte.drawImage(this.decor, 0, 0, null); //Background
        
        ArrayList<String> listeNom = this.lienSQL.listeNom(); //affichage de l'ensemble des joueurs présent en multi
        for (int i=0;i<listeNom.size();i++){
            Joueur joueurARendre = this.lienSQL.voirJoueurNom(listeNom.get(i));
            joueurARendre.rendu(contexte);
        }
        if ((this.joueurLocal.getProjectileTire()!=null)&&(this.joueurLocal.getProjectileTire().isActif())){
            this.joueurLocal.renduProjectile(contexte);
        }
        ArrayList<Projectile> listeProjectile = this.projectileSQL.voirEnsembleProjectiles(); //affichage de l'ensemble des projectiles présent dans la BDD
        for (int i=0;i<listeProjectile.size();i++){
            listeProjectile.get(i).rendu(contexte);
        }
     

    }
    
    public void testProjectileTouche(){
        ArrayList<String> listeNom = this.lienSQL.listeNom(); //affichage de l'ensemble des joueurs présent en multi
        for (int i=0;i<listeNom.size();i++){
            Joueur joueurATester = this.lienSQL.voirJoueurNom(listeNom.get(i));
            if((this.joueurLocal.getProjectileTire().joueurTouche(joueurATester))
                    &&(!(this.joueurLocal.getNom().equals(joueurATester.getNom())))){
                joueurATester.setHP(0);
                this.lienSQL.modifierJoueur(joueurATester);
            }
        }
    }
    
    public void testCollisionsJoueurs() {
        ArrayList<String> listeNom = this.lienSQL.listeNom();

        // Vérifie les collisions du joueur local avec tous les autres joueurs
        for (int i = 0; i < listeNom.size(); i++) {
            Joueur joueurATester = this.lienSQL.voirJoueurNom(listeNom.get(i));

            // Ne vérifie pas la collision du joueur avec lui-même
            if (this.joueurLocal.getNom().equals(joueurATester.getNom())) {
                continue;
            }

            // Vérifie s'il y a collision
            if (this.joueurLocal.estEnCollisionAvec(joueurATester)) {
                // Les deux joueurs perdent 5 HP en cas de collision
                int currentHP = this.joueurLocal.getHP();
                this.joueurLocal.setHP(Math.max(0, currentHP - 5));

                int otherHP = joueurATester.getHP();
                joueurATester.setHP(Math.max(0, otherHP - 5));

                // Met à jour la base de données
                this.lienSQL.modifierJoueur(joueurATester);
            }
        }
    }

    public void miseAJour (){ //synchronisation avec la DDD, mise à jour du joueur local, localement et dans la BDD
        this.n +=1;
        Joueur joueurLocalBDD = this.lienSQL.voirJoueur(this.joueurLocal); //on récupère les infos du joueur local stockés sur la bdd
        this.joueurLocal.setPosition(joueurLocalBDD.getX(), joueurLocalBDD.getY()); //On update les infos variables du joueur local à partir des infos de la BDD
        this.joueurLocal.setDirection(joueurLocalBDD.getDirection());
        this.joueurLocal.setHP(joueurLocalBDD.getHP());
        
        this.joueurLocal.miseAJour(this.projectileSQL); //On effectue la mise a jour local du joueur locale : action effectuees
        
//        testCollisionsJoueurs();
//        
//        Joueur joueur2 = this.joueurLocal.miseAJourTestMultiJ2(this.lienSQL.voirJoueurNom("joueur2")); // Deplacement pnj pour tester le multi avec 1 pc
//        this.lienSQL.modifierJoueur(joueur2); //on update la bdd du pnj
//        Joueur joueur3 = this.joueurLocal.miseAJourTestMultiJ3(this.lienSQL.voirJoueurNom("joueur3")); // Deplacement pnj pour tester le multi avec 1 pc
//        this.lienSQL.modifierJoueur(joueur3);
//        Joueur joueur4 = this.joueurLocal.miseAJourTestMultiJ4(this.lienSQL.voirJoueurNom("joueur4")); // Deplacement pnj pour tester le multi avec 1 pc
//        this.lienSQL.modifierJoueur(joueur4);
        
//        if (this.joueurLocal.getProjectileTire()!=null){
//            testProjectileTouche();
//        }
        this.lienSQL.modifierJoueur(this.joueurLocal); //on update la table après modification
        
        //this.lienSQL.voirTable();
        
        
    }
    
    public boolean estTermine (){ //Fonctiuon pour mettre fin au jeu
        return (this.joueurLocal.getHP()<=0);
    }

    private Coordonnee choisirSpawn(String pseudo) {
        ArrayList<Coordonnee> positionsJoueurs = this.lienSQL.listePositionsJoueursSauf(pseudo);

        double largeurMap = 689.0;
        double hauteurMap = 700.0;

        if (this.decor != null) {
            largeurMap = this.decor.getWidth();
            hauteurMap = this.decor.getHeight();
        }

        ArrayList<Coordonnee> spawnPoints = creerSpawnPoints(largeurMap, hauteurMap);

        double distanceMinimale = 180.0;

        for (Coordonnee spawn : spawnPoints) {
            if (estAssezLoinDesAutres(spawn, positionsJoueurs, distanceMinimale)) {
                return spawn;
            }
        }

        return trouverSpawnLePlusEloigne(spawnPoints, positionsJoueurs);
    }

    private ArrayList<Coordonnee> creerSpawnPoints(double largeurMap, double hauteurMap) {
        ArrayList<Coordonnee> spawnPoints = new ArrayList<>();

        double centreX = largeurMap / 2.0;
        double centreY = hauteurMap / 2.0;

        double marge = 90.0;

        spawnPoints.add(new Coordonnee(centreX, centreY));

        spawnPoints.add(new Coordonnee(marge, marge));
        spawnPoints.add(new Coordonnee(largeurMap - marge, marge));
        spawnPoints.add(new Coordonnee(marge, hauteurMap - marge));
        spawnPoints.add(new Coordonnee(largeurMap - marge, hauteurMap - marge));

        spawnPoints.add(new Coordonnee(centreX, marge));
        spawnPoints.add(new Coordonnee(centreX, hauteurMap - marge));
        spawnPoints.add(new Coordonnee(marge, centreY));
        spawnPoints.add(new Coordonnee(largeurMap - marge, centreY));

        return spawnPoints;
    }

    private boolean estAssezLoinDesAutres(Coordonnee spawn, ArrayList<Coordonnee> positionsJoueurs, double distanceMinimale) {
        for (Coordonnee positionJoueur : positionsJoueurs) {
            if (spawn.distance(positionJoueur) < distanceMinimale) {
                return false;
            }
        }

        return true;
    }

    private Coordonnee trouverSpawnLePlusEloigne(ArrayList<Coordonnee> spawnPoints, ArrayList<Coordonnee> positionsJoueurs) {
        if (spawnPoints.isEmpty()) {
            return new Coordonnee(344, 350);
        }

        if (positionsJoueurs.isEmpty()) {
            return spawnPoints.get(0);
        }

        Coordonnee meilleurSpawn = spawnPoints.get(0);
        double meilleureDistance = -1.0;

        for (Coordonnee spawn : spawnPoints) {
            double distanceLaPlusProche = Double.MAX_VALUE;

            for (Coordonnee positionJoueur : positionsJoueurs) {
                double distance = spawn.distance(positionJoueur);

                if (distance < distanceLaPlusProche) {
                    distanceLaPlusProche = distance;
                }
            }

            if (distanceLaPlusProche > meilleureDistance) {
                meilleureDistance = distanceLaPlusProche;
                meilleurSpawn = spawn;
            }
        }

        return meilleurSpawn;
    }
    
}
