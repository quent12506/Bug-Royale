/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Jeu;

/**
 *
 * @author hmas
 */

import ig.EcouteurClavier;
import ig.EcouteurSouris;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;
import joueur.Projectile;
import outils.Coordonnee;
import sql.JoueurSQL;
import sql.ProjectileSQL;

/**
 * Exemple de fenetre de jeu en utilisant uniquement des commandes
 *
 * @author guillaume.laurent
 */
public class FenetreDeJeu extends JFrame implements ActionListener {

    private BufferedImage framebuffer;
    private Graphics2D contexte;
    private JLabel jLabel1;
    private Jeu jeu;
    private Timer timer;
    private EcouteurClavier ecouteurClavier;
    private EcouteurSouris ecouteurSouris;

    public FenetreDeJeu() {
        // initialisation de la fenetre
        this.setSize(689, 700); //MODIFIER LA FENETRE GRAPHIQUE EN FONCTION DE LA TAILLE DE LA MAP
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jLabel1 = new JLabel();
        this.jLabel1.setPreferredSize(new java.awt.Dimension(689, 700)); 
        this.setContentPane(this.jLabel1);
        this.pack();
        
        this.ecouteurClavier = new EcouteurClavier();
        this.addKeyListener(ecouteurClavier);
        
        this.ecouteurSouris = new EcouteurSouris();
        this.addMouseListener(ecouteurSouris);

        // Creation du buffer pour l'affichage du jeu et recuperation du contexte graphique
        this.framebuffer = new BufferedImage(this.jLabel1.getWidth(), this.jLabel1.getHeight(), BufferedImage.TYPE_INT_ARGB);
        this.jLabel1.setIcon(new ImageIcon(framebuffer));
        this.contexte = this.framebuffer.createGraphics();
        
        // Creation du jeu
        this.jeu = new Jeu();
        
        // Creation du timer
        this.timer = new Timer(40, this);
        this.timer.start();
    }
    
    @Override
    public void actionPerformed(ActionEvent e){ //"tour de jeu" -> tout les 40 ms on actualise le jeu et on l'affiche
        actionListened();
        this.jeu.miseAJour();
        this.jeu.rendu(contexte);
        this.jLabel1.repaint();
        
        if (this.jeu.estTermine()){
            this.jeu.getJoueurLocal().joueurMort(this.jeu.getLienSQL());
            this.timer.stop();
        }
    }
    public static void main(String[] args) { //affichage fenetre de jeu
        FenetreDeJeu fenetre = new FenetreDeJeu();
        fenetre.setVisible(true);
    }
    
    public void actionListened(){ //Lien entre l'écouteur de clavier et le joueur local
        this.jeu.getJoueurLocal().setToucheEst(this.ecouteurClavier.isEst());
        this.jeu.getJoueurLocal().setToucheOuest(this.ecouteurClavier.isOuest());
        this.jeu.getJoueurLocal().setToucheNord(this.ecouteurClavier.isNord());
        this.jeu.getJoueurLocal().setToucheSud(this.ecouteurClavier.isSud());
        
        if (this.ecouteurSouris.isClick()){
            Coordonnee cible = new Coordonnee(this.ecouteurSouris.getX(),this.ecouteurSouris.getY());
            Projectile projectile = new Projectile(this.jeu.getJoueurLocal(), this.jeu.getJoueurLocal().getPosition(),cible,5);
            this.jeu.getJoueurLocal().setProjectileTire(projectile);
            this.ecouteurSouris.setClick(false);
            this.jeu.getProjectileSQL().creerProjectile(projectile);
        }
        
    }

}
