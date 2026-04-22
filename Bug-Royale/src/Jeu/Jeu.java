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
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import joueur.Joueur;
import sql.JoueurSQL;

/**
 *
 * @author hmas
 */
public class Jeu {
    
    private BufferedImage decor;
    private Joueur joueurLocal;
    private int n;
    private JoueurSQL lienSQL;

    public Jeu() {
        try {
            this.decor = ImageIO.read(getClass().getResource("../resources/jungle.png"));
        }
        catch (IOException ex) {
            Logger.getLogger(Jeu.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.joueurLocal= new Joueur("joueur1","insecte",50,170,320);
        this.n = 0;
        this.lienSQL = new JoueurSQL();
        this.lienSQL.creerJoueur(this.joueurLocal);
        initialisationTestMulti();
    }

    public Joueur getJoueurLocal() {
        return joueurLocal;
    }

    public int getN() {
        return n;
    }

    public void setJoueurLocal(Joueur joueurLocal) {
        this.joueurLocal = joueurLocal;
    }
    
    public void rendu (Graphics2D contexte){
        contexte.drawImage(this.decor, 0, 0, null);
        //this.joueurLocal.rendu(contexte);
        ArrayList<String> listeNom = this.lienSQL.listeNom();
        for (int i=0;i<listeNom.size();i++){
            Joueur joueurARendre = this.lienSQL.voirJoueurNom(listeNom.get(i));
            joueurARendre.rendu(contexte);
        }
        
    }
    
    public void miseAJour (){
        this.n +=1;
        Joueur joueurLocalBDD = this.lienSQL.voirJoueur(this.joueurLocal); //on récupère les infos du joueur local stockés sur la bdd
        this.joueurLocal.setPosition(joueurLocalBDD.getX(), joueurLocalBDD.getY()); //On update les infos variables du joueur local
        this.joueurLocal.setHP(joueurLocalBDD.getHP());
        
        this.joueurLocal.miseAJour();
        
        Joueur joueur3 = this.joueurLocal.miseAJourTestMulti(this.lienSQL.voirJoueurNom("joueur3"));
        this.lienSQL.modifierJoueur(joueur3);
        
        this.lienSQL.modifierJoueur(this.joueurLocal); //on update la table après modification
    }
    
    public boolean estTermine (int n){
        return (n==500);
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
    
    public void initialisationTestMulti(){
        Joueur joueur2=new Joueur("joueur2","scarab",60,50,50);
        Joueur joueur3=new Joueur("joueur3","fourmi",170,50,50);
        this.lienSQL.creerJoueur(joueur2);
        this.lienSQL.creerJoueur(joueur3);
    }
    
}
