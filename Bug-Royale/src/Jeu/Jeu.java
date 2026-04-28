/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Jeu;

/**
 *
 * @author hmas
 */

import espece.Araignee;
import espece.Cafard;
import espece.Espece;
import espece.Sauterelle;
import espece.ScarabeeRhinoceros;
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

    public Jeu() { //Initialisation du jeu
        try {
            this.decor = ImageIO.read(getClass().getResource("../resources/grass.png")); //Remplacer "jungle.png" par notre carte
        }
        catch (IOException ex) {
            Logger.getLogger(Jeu.class.getName()).log(Level.SEVERE, null, ex);
        }
        Espece especeJoueurLocal = new Cafard();
        this.joueurLocal= new Joueur("Quentin",especeJoueurLocal,170,320); //LIGNE A MODIFIER POUR DEFINIR SON JOUEUR
        this.n = 0; //Fin de jeu avec un compteur, solution temporaire
        this.lienSQL = new JoueurSQL(); //initialisation lien joueur-BDD
        this.projectileSQL = new ProjectileSQL(); //initialisation lien projectile-BDD
        this.lienSQL.creerJoueur(this.joueurLocal); //Crétion du joueur local dans la BDD -> entrée en multi
        //initialisationTestMulti(); //Ligne d'initialisation du test multi sur 1 pc
    }
    //Getter et setter
    public Joueur getJoueurLocal() {
        return joueurLocal;
    }

    public int getN() {
        return n;
    }

    public void setJoueurLocal(Joueur joueurLocal) {
        this.joueurLocal = joueurLocal;
    }
    
    public void rendu (Graphics2D contexte){ //Rendu du jeu
        contexte.drawImage(this.decor, 0, 0, null); //Background
        
        ArrayList<String> listeNom = this.lienSQL.listeNom(); //affichage de l'ensemble des joueurs présent en multi
        for (int i=0;i<listeNom.size();i++){
            Joueur joueurARendre = this.lienSQL.voirJoueurNom(listeNom.get(i));
            joueurARendre.rendu(contexte);
        }
//        if ((this.joueurLocal.getProjectileTire()!=null)&&(this.joueurLocal.getProjectileTire().isActif())){
//            this.joueurLocal.renduProjectile(contexte);
//        }
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
    
    public void miseAJour (){ //synchronisation avec la DDD, mise à jour du joueur local, localement et dans la BDD
        this.n +=1;
        Joueur joueurLocalBDD = this.lienSQL.voirJoueur(this.joueurLocal); //on récupère les infos du joueur local stockés sur la bdd
        this.joueurLocal.setPosition(joueurLocalBDD.getX(), joueurLocalBDD.getY()); //On update les infos variables du joueur local à partir des infos de la BDD
        this.joueurLocal.setHP(joueurLocalBDD.getHP());
        
        this.joueurLocal.miseAJour(); //On effectue la mise a jour local du joueur locale : action effectuees
        
//        Joueur joueur2 = this.joueurLocal.miseAJourTestMultiJ2(this.lienSQL.voirJoueurNom("joueur2")); // Deplacement pnj pour tester le multi avec 1 pc
//        this.lienSQL.modifierJoueur(joueur2); //on update la bdd du pnj
//        Joueur joueur3 = this.joueurLocal.miseAJourTestMultiJ3(this.lienSQL.voirJoueurNom("joueur3")); // Deplacement pnj pour tester le multi avec 1 pc
//        this.lienSQL.modifierJoueur(joueur3);
//        Joueur joueur4 = this.joueurLocal.miseAJourTestMultiJ4(this.lienSQL.voirJoueurNom("joueur4")); // Deplacement pnj pour tester le multi avec 1 pc
//        this.lienSQL.modifierJoueur(joueur4);
        
        if (this.joueurLocal.getProjectileTire()!=null){
            testProjectileTouche();
        }
        this.lienSQL.modifierJoueur(this.joueurLocal); //on update la table après modification
        
        
    }
    
    public boolean estTermine (){ //Fonctiuon pour mettre fin au jeu
        return (this.joueurLocal.getHP()<=0);
    }
    
    /*public boolean collisionEntreAvatarEtBanane() {
        if ((uneBanane.getX() >= joueur.getX() + joueur.getLargeur()) // trop à droite
                || (uneBanane.getX() + uneBanane.getLargeur() <= joueur.getX()) // trop à gauche
                || (uneBanane.getY() >= joueur.getY() + joueur.getHauteur()) // trop en bas
                || (uneBanane.getY() + uneBanane.getHauteur() <= joueur.getY())) { // trop en haut
            return false;
        } else {
            return true;
        }
    }*/
    
    public void initialisationTestMulti(){ //fonction pour créer 2 pnj pour tester le multi avec 1 pc
        Espece especeJoueur2 = new Sauterelle();
        Espece especeJoueur3 = new Cafard();
        Espece especeJoueur4 = new ScarabeeRhinoceros();
        Joueur joueur2=new Joueur("joueur2",especeJoueur2,50,50);
        Joueur joueur3=new Joueur("joueur3",especeJoueur3,100,50);
        Joueur joueur4=new Joueur("joueur4",especeJoueur4,150,150);
        this.lienSQL.creerJoueur(joueur2);
        this.lienSQL.creerJoueur(joueur3);
        this.lienSQL.creerJoueur(joueur4);
    }
    
}
