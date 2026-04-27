package sql;

/**
 *
 * @author mlopez1
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import outils.OutilsJDBC;
import projectile.Projectile;

public class ProjectileSQL {
    
    private String adresseBase; 
    private String user;
    private String motdepasse;
    private Connection connexion;
    
    
    public ProjectileSQL(){ //Methode pour connecter le jeu à la BDD
        this.adresseBase = "jdbc:mariadb://nemrod.ens2m.fr:3306/2025-2026_s2_vs1_bug_royale";
        this.user = "etudiant";
        this.motdepasse = "YTDTvj9TR3CDYCmP";
	
	try {
	this.connexion = DriverManager.getConnection(this.adresseBase, this.user, this.motdepasse);
	
	} catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
    
    public void creerProjectile(Projectile P){ 
            PreparedStatement requete = connexion.prepareStatement("INSERT INTO Projectiles VALUES (?, ?, ?)");
            
            requete.setString(1, P.getProprietaire());
            requete.setDouble(2, P.getX());
            requete.setDouble(3, P.getY());
            
            int nombreDAjouts = requete.executeUpdate();
         
            requete.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
    
     public void modifierProjectile(Projectile P, String ancienProprietaire){ //Modification d'un projectile dans la BDD à partir d'un projectile existant localement
        try {
            PreparedStatement requete = connexion.prepareStatement("UPDATE Projectiles SET Proprietaire = ?, X = ?, Y = ? WHERE Proprietaire = ?");
            
            requete.setString(1, P.getProprietaire());
            requete.setDouble(2, P.getX());
            requete.setDouble(3, P.getY());
            requete.setString(4, ancienProprietaire);
           
            int nombreDeModifications = requete.executeUpdate();

            requete.close();
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
     
    public void supprimerProjectile(Projectile P){ //Suppression d'un projectile dans la BDD à partir d'un projectile existant localement
        try {
            PreparedStatement requete = connexion.prepareStatement("DELETE FROM Projectiles WHERE Proprietaire = ? AND X = ? AND Y = ?");
            
            requete.setString(1, P.getProprietaire());
            requete.setDouble(2, P.getX());
            requete.setDouble(3, P.getY());
            
            int nombreDeSuppressions = requete.executeUpdate();

            requete.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public Projectile voirProjectile(Projectile P) { //Extraction d'un projectile dans la BDD à partir d'un projectile existant localement
        
        Projectile POut = new Projectile();
    
    try {
        PreparedStatement requete = connexion.prepareStatement("SELECT * FROM Projectiles WHERE Proprietaire = ? AND X = ? AND Y = ?");
        requete.setString(1, P.getProprietaire());
        requete.setDouble(2, P.getX());
        requete.setDouble(3, P.getY());
        
        ResultSet resultat = requete.executeQuery();
        
        if (resultat.next()) {
            
                POut.setProprietaire(resultat.getString("Proprietaire"));
                POut.setPosition(resultat.getDouble("X"), resultat.getDouble("Y"));
                
        }
        
        resultat.close();
        requete.close();
        
    } catch (SQLException ex) {
        ex.printStackTrace();  
    }
    return POut;
    }
    
    public ArrayList<Projectile> voirProjectilesProprietaire(String proprietaire) { //Extraction de tous les projectiles d'un propriétaire
        
        ArrayList<Projectile> listeProjectiles = new ArrayList<Projectile>();
    
    try {
        PreparedStatement requete = connexion.prepareStatement("SELECT * FROM Projectiles WHERE Proprietaire = ?");
        requete.setString(1, proprietaire);
        
        ResultSet resultat = requete.executeQuery();
        
        while (resultat.next()) {
            
                Projectile P = new Projectile();
                P.setProprietaire(resultat.getString("Proprietaire"));
                P.setPosition(resultat.getDouble("X"), resultat.getDouble("Y"));
                listeProjectiles.add(P);
                
        }
        
        resultat.close();
        requete.close();
        
    } catch (SQLException ex) {
        ex.printStackTrace();  
    }
    return listeProjectiles;
    }
    
    public void voirTable(){ //Affichage de l'ensemble de la table dans le terminal
        try {
            PreparedStatement requete = connexion.prepareStatement("SELECT * FROM Projectiles");
            ResultSet resultat = requete.executeQuery();
            OutilsJDBC.afficherResultSet(resultat);

            requete.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }   
    
    public ArrayList<String> listeProprietaires(){ //Récupération de la liste des propriétaires présents
        ArrayList<String> listeProprietaires = new ArrayList<String>();
        try {
            PreparedStatement requete = connexion.prepareStatement("SELECT DISTINCT Proprietaire FROM Projectiles");
            ResultSet resultat = requete.executeQuery();
            
            while(resultat.next()){
                listeProprietaires.add(resultat.getString("Proprietaire"));
            }

            resultat.close();
            requete.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listeProprietaires;
    }
    
    public void closeTable(){ //Fermeture de la connection
        try {
            this.connexion.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
