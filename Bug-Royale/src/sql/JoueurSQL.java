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
import espece.Araignee;
import espece.Cafard;
import espece.Espece;
import espece.Sauterelle;
import espece.ScarabeeRhinoceros;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import outils.OutilsJDBC;
import joueur.Joueur;

public class JoueurSQL {
    
    private String adresseBase; 
    private String user;
    private String motdepasse;
    private Connection connexion;
    
    
    public JoueurSQL(){ //Methode pour connecter le jeu à la BDD
        this.adresseBase = "jdbc:mariadb://nemrod.ens2m.fr:3306/2025-2026_s2_vs1_bug_royale";
        this.user = "etudiant";
        this.motdepasse = "YTDTvj9TR3CDYCmP";
	
	try {
	this.connexion = DriverManager.getConnection(this.adresseBase, this.user, this.motdepasse);
	
	} catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
    
    public void creerJoueur(Joueur J){ //Création d'un joueur dans la base de donnée à partir d'un joueur existant localement
        try {
            PreparedStatement requete = connexion.prepareStatement("INSERT INTO Joueur VALUES (?, ?, ?, ?, ?)");
            
            requete.setString(1, J.getNom());
            requete.setDouble(2, J.getX());
            requete.setDouble(3, J.getY());
            requete.setInt(4, J.getHP());
            requete.setString(5, J.getEspece().getStringEspece());
            
            int nombreDAjouts = requete.executeUpdate();
         
            requete.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
    
     public void modifierJoueur(Joueur J){ //Modification d'un joueur dans la BDD à partir d'un joueur existant localement
        try {
            PreparedStatement requete = connexion.prepareStatement("UPDATE Joueur SET X = ?, Y = ?, HP = ?, Espece = ? WHERE Name = ?");
            
            requete.setDouble(1, J.getX());
            requete.setDouble(2, J.getY());
            requete.setInt(3, J.getHP());
            requete.setString(4, J.getEspece().getStringEspece());
            requete.setString(5, J.getNom());
           
            //voirTable();
            
            int nombreDeModifications = requete.executeUpdate();

            requete.close();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
     
public void supprimerJoueur(Joueur J){ //Suppression d'un joueur dans la BDD à partir d'un joueur existant localement
        try {
            PreparedStatement requete = connexion.prepareStatement("DELETE FROM Joueur WHERE Name = ?");
            
            requete.setString(1, J.getNom());
            
            int nombreDeSuppressions = requete.executeUpdate();

            requete.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public Joueur voirJoueur(Joueur J) { //Extraction d'un joueur dans la BDD à partir d'un joueur existant localement
        
        Joueur JOut = new Joueur();
    
    try {
        PreparedStatement requete = connexion.prepareStatement("SELECT * FROM Joueur WHERE Name = ?");
        requete.setString(1, J.getNom());
        
        ResultSet resultat = requete.executeQuery();
        
        if (resultat.next()) {
            
                JOut.setNom(resultat.getString("Name"));
                JOut.setPosition(resultat.getDouble("X"),resultat.getDouble("Y"));
                JOut.setHP(resultat.getInt("HP"));
                if ("ScarabeeRhinoceros".equals(resultat.getString("Espece"))){
                    Espece especeJOut=new ScarabeeRhinoceros();
                    JOut.setEspece(especeJOut);
                }
                if ("Araignee".equals(resultat.getString("Espece"))){
                    Espece especeJOut=new Araignee();
                    JOut.setEspece(especeJOut);
                }
                if ("Cafard".equals(resultat.getString("Espece"))){
                    Espece especeJOut=new Cafard();
                    JOut.setEspece(especeJOut);
                }
                if ("Sauterelle".equals(resultat.getString("Espece"))){
                    Espece especeJOut=new Sauterelle();
                    JOut.setEspece(especeJOut);
                }
                
        }
        
        resultat.close();
        requete.close();
        
    } catch (SQLException ex) {
        ex.printStackTrace();  
    }
    return JOut;
    }
    
    public Joueur voirJoueurNom(String nom) { //Extraction d'un joueur dans la BDD à partir du nom d'un joueur existant localement
        
        Joueur JOut = new Joueur();
    
    try {
        PreparedStatement requete = connexion.prepareStatement("SELECT * FROM Joueur WHERE Name = ?");
        requete.setString(1, nom);
        
        ResultSet resultat = requete.executeQuery();
        
        if (resultat.next()) {
            
                JOut.setNom(resultat.getString("Name"));
                JOut.setPosition(resultat.getDouble("X"),resultat.getDouble("Y"));
                JOut.setHP(resultat.getInt("HP"));
                if ("ScarabeeRhinoceros".equals(resultat.getString("Espece"))){
                    Espece especeJOut=new ScarabeeRhinoceros();
                    JOut.setEspece(especeJOut);
                }
                if ("Araignee".equals(resultat.getString("Espece"))){
                    Espece especeJOut=new Araignee();
                    JOut.setEspece(especeJOut);
                }
                if ("Cafard".equals(resultat.getString("Espece"))){
                    Espece especeJOut=new Cafard();
                    JOut.setEspece(especeJOut);
                }
                if ("Sauterelle".equals(resultat.getString("Espece"))){
                    Espece especeJOut=new Sauterelle();
                    JOut.setEspece(especeJOut);
                }
        }
        
        resultat.close();
        requete.close();
        
    } catch (SQLException ex) {
        ex.printStackTrace();  
    }
    return JOut;
    }
    
    public void voirTable(){ //Affichage de l'ensemble de la table dans le terminal
        try {
            PreparedStatement requete = connexion.prepareStatement("SELECT * FROM Joueur");
            ResultSet resultat = requete.executeQuery();
            OutilsJDBC.afficherResultSet(resultat);

            requete.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }   
    
    public ArrayList<String> listeNom(){ //Récupération de la liste des noms des joueurs présents
        ArrayList<String> listeNom = new ArrayList<String>();
        try {
            PreparedStatement requete = connexion.prepareStatement("SELECT Name FROM Joueur");
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
    
    public void closeTable(){ //Ferleture de la connection
        try {
            this.connexion.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}