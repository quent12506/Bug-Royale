/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import joueur.Joueur;
import outils.OutilsJDBC;
import joueur.Projectile;
import outils.Coordonnee;

public class ProjectileSQL {

    private String adresseBase;
    private String user;
    private String motdepasse;
    private Connection connexion;
    private JoueurSQL lienJoueurSQL;

    public ProjectileSQL(JoueurSQL lienJoueurSQL) {
        this.adresseBase = "jdbc:mariadb://nemrod.ens2m.fr:3306/2025-2026_s2_vs1_bug_royale";
        this.user = "etudiant";
        this.motdepasse = "YTDTvj9TR3CDYCmP";
        this.lienJoueurSQL = lienJoueurSQL;

        try {
            this.connexion = DriverManager.getConnection(this.adresseBase, this.user, this.motdepasse);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ProjectileSQL() {
        this(new JoueurSQL());
    }

    ///////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////
    /**
    * Crée un projectile dans la BDD, d'attributs : Proprietaire, X, Y.
    * @param P instance de projectile appartenant au moteur
    */
    public void creerProjectile(Projectile P) {
        try {
            PreparedStatement requete = connexion.prepareStatement(
                    "INSERT INTO Projectiles (Proprietaire, X, Y, Dx, Dy) VALUES (?, ?, ?, ?, ?)"
            );

            requete.setString(1, P.getProprietaire().getNom());
            requete.setDouble(2, P.getPosition().getx());
            requete.setDouble(3, P.getPosition().gety());
            requete.setDouble(4, P.getDirection().getx());
            requete.setDouble(5, P.getDirection().gety());

            int nombreDAjouts = requete.executeUpdate();
            requete.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    ///////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////
    /**
    * Modification d'un projectile dans la BDD à partir d'un projectile existant localement, d'attributs : Proprietaire, X, Y.
    * @param P instance de projectile appartenant au moteur
    * @param ancienProprietaire ancien propriétaire du projectile
    */
    public void modifierProjectile(Projectile P, String ancienProprietaire) {
        try {
            PreparedStatement requete = connexion.prepareStatement("UPDATE Projectiles SET Proprietaire = ?, X = ?, Y = ?, DX = ?, DY = ? WHERE Proprietaire = ?");

            requete.setString(1, P.getProprietaire().getNom());
            requete.setDouble(2, P.getPosition().getx());
            requete.setDouble(3, P.getPosition().gety());
            requete.setDouble(4, P.getDirection().getx());
            requete.setDouble(5, P.getDirection().gety());
            requete.setString(6, ancienProprietaire);

            //System.out.println(requete);
            int nombreDeModifications = requete.executeUpdate();
            //System.out.println(nombreDeModifications + " enregistrement(s) ajoute(s)");

            requete.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    ///////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////
    /**
    * Suppression d'un projectile dans la BDD à partir d'un projectile existant localement, d'attributs : Proprietaire, X, Y.
    * @param P instance de projectile appartenant au moteur
    */
    public void supprimerProjectile(Projectile P) {
        if (P == null || P.getProprietaire() == null) {
            return;
        }
        try {
            PreparedStatement requete = connexion.prepareStatement(
                    "DELETE FROM Projectiles WHERE Proprietaire = ?"
            );

            requete.setString(1, P.getProprietaire().getNom());

            int nombreDeSuppressions = requete.executeUpdate();

            requete.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    ///////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////
    /**
    * Extraction d'un projectile dans la BDD à partir d'un projectile existant localement, d'attributs : Proprietaire
    * @param P instance de projectile appartenant au moteur
    * @return Projectile une instance de projectile telle qu'il se trouve dans la BDD
    */
    public Projectile voirProjectile(Projectile P) {

        Projectile POut = new Projectile();

        try {
            PreparedStatement requete = connexion.prepareStatement("SELECT * FROM Projectiles WHERE Proprietaire = ? AND X = ? AND Y = ?");
            requete.setString(1, P.getProprietaire().getNom());
            requete.setDouble(2, P.getPosition().getx());
            requete.setDouble(3, P.getPosition().gety());

            ResultSet resultat = requete.executeQuery();

            if (resultat.next()) {

                POut.setProprietaire(this.lienJoueurSQL.voirJoueurNom(resultat.getString("Proprietaire")));
                Coordonnee pos = new Coordonnee();
                pos.setX(resultat.getDouble("X"));
                pos.setY(resultat.getDouble("Y"));
                POut.setPosition(pos);

                Coordonnee dir = new Coordonnee(
                        resultat.getDouble("DX"),
                        resultat.getDouble("DY")
                );
                POut.setDirection(dir);

            }

            requete.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return POut;
    }

    ///////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////
    /**
    * Extraction de tous les projectiles d'un propriétaire
    * @param proprietaire nom du propriétaire
    * @return ArrayList Liste des projectiles du propriétaire
    */
    public ArrayList<Projectile> voirProjectilesProprietaire(String proprietaire) {

        ArrayList<Projectile> listeProjectiles = new ArrayList<Projectile>();

        try {
            PreparedStatement requete = connexion.prepareStatement("SELECT * FROM Projectiles WHERE Proprietaire = ?");
            requete.setString(1, proprietaire);

            ResultSet resultat = requete.executeQuery();

            while (resultat.next()) {

                Projectile P = new Projectile();

                P.setProprietaire(this.lienJoueurSQL.voirJoueurNom(resultat.getString("Proprietaire")));
                Coordonnee pos = new Coordonnee();
                pos.setX(resultat.getDouble("X"));
                pos.setY(resultat.getDouble("Y"));
                P.setPosition(pos);

                Coordonnee dir = new Coordonnee(
                        resultat.getDouble("DX"),
                        resultat.getDouble("DY")
                );
                P.setDirection(dir);
                listeProjectiles.add(P);

            }

            requete.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listeProjectiles;
    }

    ///////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////
    /**
    * Extraction de tous les projectiles de la table
    * @return ArrayList Liste de tous les projectiles
    */
    public ArrayList<Projectile> voirEnsembleProjectiles() {

        ArrayList<Projectile> listeProjectiles = new ArrayList<Projectile>();

        try {
            PreparedStatement requete = connexion.prepareStatement("SELECT * FROM Projectiles");

            ResultSet resultat = requete.executeQuery();

            while (resultat.next()) {
                Projectile P = new Projectile();
                Joueur proprietaire = this.lienJoueurSQL.voirJoueurNom(resultat.getString("Proprietaire"));
                if (proprietaire == null || proprietaire.getEspece() == null) {
                    continue; // on ignore ce projectile orphelin
                }
                P.setProprietaire(proprietaire);
                Coordonnee pos = new Coordonnee();
                pos.setX(resultat.getDouble("X"));
                pos.setY(resultat.getDouble("Y"));
                P.setPosition(pos);

                Coordonnee dir = new Coordonnee(
                        resultat.getDouble("DX"),
                        resultat.getDouble("DY")
                );
                P.setDirection(dir);

                listeProjectiles.add(P);
            }

            requete.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listeProjectiles;
    }


    ///////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////
    /**
    * Affichage de l'ensemble de la table dans le terminal
    */
    public void voirTable() {
        try {
            PreparedStatement requete = connexion.prepareStatement("SELECT * FROM Projectiles");
            ResultSet resultat = requete.executeQuery();
            OutilsJDBC.afficherResultSet(resultat);

            requete.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    ///////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////
    /**
    * Récupération de la liste des propriétaires présents
    * @return ArrayList Liste des propriétaires (seulement leur nom)
    */
    public ArrayList<String> listeProprietaires() {
        ArrayList<String> listeProprietaires = new ArrayList<String>();
        try {
            PreparedStatement requete = connexion.prepareStatement("SELECT DISTINCT Proprietaire FROM Projectiles");
            ResultSet resultat = requete.executeQuery();

            while (resultat.next()) {
                listeProprietaires.add(resultat.getString("Proprietaire"));
            }

            requete.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listeProprietaires;
    }

    ///////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////
    /**
    * Vide l'ensemble de la table
    */
    public void viderTable() {
        try {
            PreparedStatement requete = connexion.prepareStatement("DELETE FROM Projectiles WHERE 1");

            int nombreDeSuppressions = requete.executeUpdate();

            //System.out.println(nombreDeSuppressions);

            requete.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    ///////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////
    /**
    * Fermeture de la connection
    */
    public void closeTable() {
        try {
            if (this.connexion != null && !this.connexion.isClosed()) {
                this.connexion.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
