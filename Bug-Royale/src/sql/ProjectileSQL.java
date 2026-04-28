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
import joueur.Projectile;
import outils.Coordonnee;

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
        try {
            PreparedStatement requete = connexion.prepareStatement("INSERT INTO Projectiles VALUES (?, ?, ?)");
            
            requete.setString(1, P.getProprietaire().getNom());
            requete.setDouble(2, P.getPosition().getx());
            requete.setDouble(3, P.getPosition().gety());
            
            int nombreDAjouts = requete.executeUpdate();
         
            requete.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }
    
     public void modifierProjectile(Projectile P, String ancienProprietaire){ //Modification d'un projectile dans la BDD à partir d'un projectile existant localement
        try {
            PreparedStatement requete = connexion.prepareStatement("UPDATE Projectiles SET Proprietaire = ?, X = ?, Y = ? WHERE Proprietaire = ?");
            
            requete.setString(1, P.getProprietaire().getNom());
            requete.setDouble(2, P.getPosition().getx());
            requete.setDouble(3, P.getPosition().gety());
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
            
            requete.setString(1, P.getProprietaire().getNom());
            requete.setDouble(2, P.getPosition().getx());
            requete.setDouble(3, P.getPosition().gety());
            
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
            requete.setString(1, P.getProprietaire().getNom());
            requete.setDouble(2, P.getPosition().getx());
            requete.setDouble(3, P.getPosition().gety());
        
        ResultSet resultat = requete.executeQuery();
        JoueurSQL lienSQL = new JoueurSQL();
        
        if (resultat.next()) {
            
                POut.setProprietaire(lienSQL.voirJoueurNom(resultat.getString("Proprietaire")));
                Coordonnee pos = new Coordonnee();
                pos.setX(resultat.getDouble("X"));
                pos.setY(resultat.getDouble("Y"));
                POut.setPosition(pos);
                
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
        JoueurSQL lienSQL = new JoueurSQL();
        
        while (resultat.next()) {
            
                Projectile P = new Projectile();
                
                P.setProprietaire(lienSQL.voirJoueurNom(resultat.getString("Proprietaire")));
                Coordonnee pos = new Coordonnee();
                pos.setX(resultat.getDouble("X"));
                pos.setY(resultat.getDouble("Y"));
                P.setPosition(pos);
                listeProjectiles.add(P);
                
        }
        
        resultat.close();
        requete.close();
        
    } catch (SQLException ex) {
        ex.printStackTrace();  
    }
    return listeProjectiles;
    }
    
    public ArrayList<Projectile> voirEnsembleProjectiles() { //Extraction de tous les projectiles de la table dans une liste
        
        ArrayList<Projectile> listeProjectiles = new ArrayList<Projectile>();
    
    try {
        PreparedStatement requete = connexion.prepareStatement("SELECT * FROM Projectiles");
        
        ResultSet resultat = requete.executeQuery();
        JoueurSQL lienSQL = new JoueurSQL();
        
        while (resultat.next()) {
            
                Projectile P = new Projectile();
                
                P.setProprietaire(lienSQL.voirJoueurNom(resultat.getString("Proprietaire")));
                Coordonnee pos = new Coordonnee();
                pos.setX(resultat.getDouble("X"));
                pos.setY(resultat.getDouble("Y"));
                P.setPosition(pos);
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
