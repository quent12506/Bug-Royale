/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sql;

/**
 *
 * @author abriton
 */

//import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import outils.OutilsJDBC;
import joueur.Joueur;

public class JoueurSQL {
    
    //Ok ! L'idée c'est que dans cette classe, tu implémentes TOUTES les actions posible avec la Table Joueur (sur le serveur distant)
    //Pour faire ça, déjà tu as besoin de pouvoir te connecter à la base de donnée, c'est pourquoi c'est judicieux de les mettre en 
    //attributs les choses dont t'as besoin pour te connecter.
    private String adresseBase;
    private String user;
    private String motdepasse;
    private Connection connexion; //lui c'est l'état de la connexion, autant en faire aussi un attribut.
    
    
    //Ici, on fait un constructeur qui va juste initialiser l'intermédiaire SQL
    public JoueurSQL(){
        this.adresseBase = "jdbc:mariadb://nemrod.ens2m.fr:3306/2025-2026_s2_vs1_bug_royale";
        this.user = "etudiant";
        this.motdepasse = "YTDTvj9TR3CDYCmP";
	
	//Vous avez vu que, avant de faire une requête, il fallait se connecter à la BD, ce que je te propose c'est de te connecter/déco UNE seule fois, et pas à 
	//chaque fois que tu fais une requête : La connection à la BD prend du TEMPS, si tu fais plusieurs co/déco, ça va être long :)
	try {
	this.connexion = DriverManager.getConnection(this.adresseBase, this.user, this.motdepasse);
	
	} catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
    
    //Je t'ai mis ici les 4 méthodes qui vont être importantes à coder, à toi de fustionner ça avec les bouts de code dans tes tests : 
    public void creerJoueur(Joueur J){
       //Voilà un exemple (va utiliser INSERT dans sa requête SQL), admettons qu'on a un Joueur J caractérisé par : son nom, son score, sa position X, sa position Y. On va ajouter cerre
	//ligne à notre table JOUEUR !
        try {
            PreparedStatement requete = connexion.prepareStatement("INSERT INTO Joueur VALUES (?, ?, ?, ?, ?)");
            requete.setString(1, J.getNom());
            requete.setDouble(2, J.getX());
            requete.setDouble(3, J.getY());
            requete.setInt(4, J.getHP());
            requete.setString(5, J.getEspece());
            //System.out.println(requete);
            int nombreDAjouts = requete.executeUpdate();
            //System.out.println(nombreDAjouts + " enregistrement(s) ajoute(s)");

            requete.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
    
     public void modifierJoueur(Joueur J){
       
        try {
            
            
            PreparedStatement requete = connexion.prepareStatement("UPDATE Joueur SET X = ?, Y = ?, HP = ?, Espece = ? WHERE Name = ?");
            
            
            requete.setDouble(1, J.getX());
            requete.setDouble(2, J.getY());
            requete.setInt(3, J.getHP());
            requete.setString(4, J.getEspece());
            requete.setString(5, J.getNom());
           
            System.out.println(requete);
            
            voirTable();
            
            int nombreDeModifications = requete.executeUpdate();
            System.out.println(nombreDeModifications + " enregistrement(s) modifie(s)");

            requete.close();

            
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
     
public void supprimerJoueur(Joueur J){
        try {
            PreparedStatement requete = connexion.prepareStatement("DELETE FROM Joueur WHERE Name = ?");
            requete.setString(1, J.getNom());
            //System.out.println(requete);
            
            // On exécute la requête (executeUpdate est utilisé pour INSERT, UPDATE et DELETE)
            int nombreDeSuppressions = requete.executeUpdate();
            //System.out.println(nombreDeSuppressions + " joueur(s) supprime(s) de la base de données.");

            requete.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public Joueur voirJoueur(Joueur J) {
        
        Joueur JOut = new Joueur();
    
    try {
        PreparedStatement requete = connexion.prepareStatement("SELECT * FROM Joueur WHERE Name = ?");
        requete.setString(1, J.getNom());
        
        ResultSet resultat = requete.executeQuery();
        
        // Si data trouvée : creer un nouveau joueur
        if (resultat.next()) {
            
            
                JOut.setNom(resultat.getString("Name"));
                JOut.setPosition(resultat.getDouble("X"),resultat.getDouble("Y"));
                JOut.setHP(resultat.getInt("HP"));
                JOut.setEspece(resultat.getString("Espece"));
            
            // Add other fields as necessary based on your Joueur class
        }
        
        resultat.close();
        requete.close();
        
    } catch (SQLException ex) {
        ex.printStackTrace();  
    }
    return JOut;
    }
    
    public Joueur voirJoueurNom(String nom) {
        
        Joueur JOut = new Joueur();
    
    try {
        PreparedStatement requete = connexion.prepareStatement("SELECT * FROM Joueur WHERE Name = ?");
        requete.setString(1, nom);
        
        ResultSet resultat = requete.executeQuery();
        
        // Si data trouvée : creer un nouveau joueur
        if (resultat.next()) {
            
            
                JOut.setNom(resultat.getString("Name"));
                JOut.setPosition(resultat.getDouble("X"),resultat.getDouble("Y"));
                JOut.setHP(resultat.getInt("HP"));
                JOut.setEspece(resultat.getString("Espece"));
            
            // Add other fields as necessary based on your Joueur class
        }
        
        resultat.close();
        requete.close();
        
    } catch (SQLException ex) {
        ex.printStackTrace();  
    }
    return JOut;
    }
    
    public void voirTable(){
        try {
            PreparedStatement requete = connexion.prepareStatement("SELECT * FROM Joueur");
            System.out.println(requete);
            ResultSet resultat = requete.executeQuery();
            OutilsJDBC.afficherResultSet(resultat);

            requete.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
   
    public void closeTable(){
       // On a lancé la connexion dans le Constructeur, il faut donc fermer la connexion quand tout est fini.
       // Dans le jeu, il y a de fortes chances que tu le fasses à la fin de la partie.
        try {
            this.connexion.close();
            //System.out.println("Connexion à la base de données fermée.");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }   
    
    public ArrayList<String> listeNom(){
        ArrayList<String> listeNom = new ArrayList<String>();
        try {
            PreparedStatement requete = connexion.prepareStatement("SELECT Name FROM Joueur");
            System.out.println(requete);
            ResultSet resultat = requete.executeQuery();
            
            while(resultat.next()){
                listeNom.add(resultat.getString("Name"));
            }

            resultat.close();
            requete.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listeNom;
    }
}
    
   //Si tu as une autre table, tu dois créer une autre classe similaire à celle-ci ! A présent, ton collègue qui travaille sur le moteur pourra
   //facilement utiliser tes méthodes pour mettre à jour la BDD ! En utilisant les méthodes que tu as crée pour lui :)
