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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import joueur.Joueur;

/**
 *
 * @author hmas
 */
public class Jeu {
    
    private BufferedImage decor;
    private int score;
    private Joueur joueurLocal;

    public Jeu() {
        try {
            this.decor = ImageIO.read(getClass().getResource("../resources/jungle.png"));
        }
        catch (IOException ex) {
            Logger.getLogger(Jeu.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.score = 0;
        this.joueurLocal= new Joueur("joueur1","insecte",50);
    }

    public Joueur getJoueurLocal() {
        return joueurLocal;
    }
    
    public void rendu (Graphics2D contexte){
        contexte.drawImage(this.decor, 0, 0, null);
        this.joueurLocal.rendu(contexte);
        contexte.drawString("Score : " + this.score, 10, 20);
    }
    
    public void miseAJour (){
        this.joueurLocal.miseAJour();
    }
    
    public boolean estTermine (){
        return (true);
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
    
}
