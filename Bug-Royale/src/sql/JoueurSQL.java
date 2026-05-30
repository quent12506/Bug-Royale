/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sql;

/**
 *
 * @author abriton
 */
import espece.*;
import java.sql.*;
import java.util.ArrayList;
import outils.OutilsJDBC;
import joueur.Joueur;
import outils.Coordonnee;

public class JoueurSQL {

    private String adresseBase;
    private String user;
    private String motdepasse;
    private Connection connexion;
    private Statement stmt;

    public JoueurSQL() { //Methode pour connecter le jeu à la BDD
        this.adresseBase = "jdbc:mariadb://nemrod.ens2m.fr:3306/2025-2026_s2_vs1_bug_royale";
        this.user = "etudiant";
        this.motdepasse = "YTDTvj9TR3CDYCmP";

        try {
            this.connexion = DriverManager.getConnection(this.adresseBase, this.user, this.motdepasse);
            this.stmt = this.connexion.createStatement();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

    }

    ///////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////
    /**
    * Crée un joueur dans la BDD, d'attibut : Nom, X, Y, Dx, Dy, HP, espèce.
    * @param J instance de joueur appartenant au moteur
    */

    public void creerJoueur(Joueur J) {
        try {
            PreparedStatement requete = connexion.prepareStatement(
                    "INSERT INTO Joueur (Name, X, Y, Dx, Dy, HP, Espece) "
                            + "VALUES (?, ?, ?, ?, ?, ?, ?)"
            );

            requete.setString(1, J.getNom());
            requete.setDouble(2, J.getX());
            requete.setDouble(3, J.getY());
            requete.setDouble(4, J.getDirection().getx());
            requete.setDouble(5, J.getDirection().gety());
            requete.setInt(6, J.getHP());
            requete.setString(7, J.getEspece().getStringEspece());

            requete.executeUpdate();
            requete.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    ///////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////
    /**
    * Modification d'un joueur dans la BDD à partir d'un joueur existant localement, d'attibut : Nom, X, Y, Dx, Dy, HP, espèce.
    * @param J instance de joueur appartenant au moteur
    */
    
    public void modifierJoueur(Joueur J) {
        try {
            PreparedStatement requete = connexion.prepareStatement("UPDATE Joueur SET Name = ?, X = ?, Y = ?, Dx = ?, Dy = ?, HP = ?, Espece = ? WHERE Name = ?");

            requete.setString(1, J.getNom());
            requete.setDouble(2, J.getX());
            requete.setDouble(3, J.getY());
            requete.setDouble(4, J.getDirection().getx());
            requete.setDouble(5, J.getDirection().gety());
            requete.setInt(6, J.getHP());
            requete.setString(7, J.getEspece().getStringEspece());
            requete.setString(8, J.getNom());

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
    * Suppression d'un joueur dans la BDD à partir d'un joueur existant localement, d'attibut : Nom, X, Y, Dx, Dy, HP, espèce.
    * @param J instance de joueur appartenant au moteur
    */
    public void supprimerJoueur(Joueur J) {
        try {
            PreparedStatement requete = connexion.prepareStatement("DELETE FROM Joueur WHERE Name = ?");
            requete.setString(1, J.getNom());
            requete.executeUpdate();
            requete.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    ///////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////
    /**
    * Extraction d'un joueur dans la BDD à partir d'un joueur existant localement, d'attibut : Nom, X, Y, Dx, Dy, HP, espèce.
    * @param J instance de joueur appartenant au moteur
    * @return Joueur une instance de joueur telle qu'il se trouve dans la BDD
    */
    public Joueur voirJoueur(Joueur J) { //Extraction d'un joueur dans la BDD à partir d'un joueur existant localement

        Joueur JOut = new Joueur();

        try {
            PreparedStatement requete = connexion.prepareStatement("SELECT * FROM Joueur WHERE Name = ?");
            requete.setString(1, J.getNom());

            ResultSet resultat = requete.executeQuery();

            if (resultat.next()) {

                JOut.setNom(resultat.getString("Name"));
                JOut.setPosition(resultat.getDouble("X"), resultat.getDouble("Y"));
                JOut.setDirection(new Coordonnee(
                        resultat.getDouble("Dx"),
                        resultat.getDouble("Dy")
                ).normalize());

                JOut.setHP(resultat.getInt("HP"));

                switch (resultat.getString("Espece")) {
                    case "Scarabée", "Scarabee", "scarabée", "scarabee" ->
                            JOut.setEspece(new Scarabee());
                    case "Abeille", "abeille" ->
                            JOut.setEspece(new Abeille());
                    case "Coccinelle", "coccinelle" ->
                            JOut.setEspece(new Coccinelle());
                    case "Fourmi", "fourmi" ->
                            JOut.setEspece(new Fourmi());
                    default -> throw new IllegalArgumentException("Espèce inconnue : " + resultat.getString("Espece"));
                }
            }

            requete.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return JOut;
    }

    ///////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////
    /**
    * Extraction d'un joueur dans la BDD à partir d'un joueur existant localement, d'attibut : Nom, X, Y, Dx, Dy, HP, espèce.
    * @param nom nom du joueur
    * @return Joueur une instance de joueur telle qu'il se trouve dans la BDD
    */
    public Joueur voirJoueurNom(String nom) {

        Joueur JOut = new Joueur();

        try {
            PreparedStatement requete = connexion.prepareStatement("SELECT * FROM Joueur WHERE Name = ?");
            requete.setString(1, nom);

            ResultSet resultat = requete.executeQuery();

            if (resultat.next()) {

                JOut.setNom(resultat.getString("Name"));
                JOut.setPosition(resultat.getDouble("X"), resultat.getDouble("Y"));
                JOut.setDirection(new Coordonnee(
                        resultat.getDouble("Dx"),
                        resultat.getDouble("Dy")
                ).normalize());
                JOut.setHP(resultat.getInt("HP"));

                switch (resultat.getString("Espece")) {
                    case "Scarabée", "Scarabee", "scarabée", "scarabee" ->
                            JOut.setEspece(new Scarabee());
                    case "Abeille", "abeille" ->
                            JOut.setEspece(new Abeille());
                    case "Coccinelle", "coccinelle" ->
                            JOut.setEspece(new Coccinelle());
                    case "Fourmi", "fourmi" ->
                            JOut.setEspece(new Fourmi());
                    default -> throw new IllegalArgumentException("Espèce inconnue : " + resultat.getString("Espece"));
                }

                requete.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return JOut;
    }

    ///////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////
    /**
    * Affichage de l'ensemble de la table dans le terminal
    */
    public void voirTable() {
        try {
            PreparedStatement requete = connexion.prepareStatement("SELECT * FROM Joueur");
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
    * Récupération de la liste des noms des joueurs présents
    * @return ArrayList Liste des joueurs (seulement leur nom)
    */
    public ArrayList<String> listeNom() {
        ArrayList<String> listeNom = new ArrayList<String>();
        try {
            PreparedStatement requete = connexion.prepareStatement("SELECT Name FROM Joueur");
            ResultSet resultat = requete.executeQuery();

            while (resultat.next()) {
                listeNom.add(resultat.getString("Name"));
            }

            requete.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return listeNom;
    }
    
    public void mettreAJourDirection(String nom, double dx, double dy) {
    try {
        PreparedStatement requete = connexion.prepareStatement("UPDATE Joueur SET Dx = ?, Dy = ? WHERE Name = ?");
        requete.setDouble(1, dx);
        requete.setDouble(2, dy);
        requete.setString(3, nom);
        
        requete.executeUpdate();
        requete.close();
        
    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}

    public ArrayList<Coordonnee> listePositionsJoueursSauf(String nomAExclure) {
        ArrayList<Coordonnee> positions = new ArrayList<>();

        try {
            PreparedStatement requete = connexion.prepareStatement(
                    "SELECT X, Y FROM Joueur WHERE Name <> ?"
            );

            requete.setString(1, nomAExclure);

            ResultSet resultat = requete.executeQuery();

            while (resultat.next()) {
                positions.add(new Coordonnee(
                        resultat.getDouble("X"),
                        resultat.getDouble("Y")
                ));
            }

            requete.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return positions;
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
    
    
    public int nombreJoueursVivants() {
    int nb = 0;

    try {
        PreparedStatement requete = connexion.prepareStatement(
                "SELECT COUNT(*) AS total FROM Joueur WHERE HP > 0"
        );

        ResultSet rs = requete.executeQuery();

        if (rs.next()) {
            nb = rs.getInt("total");
        }

        requete.close();

    } catch (SQLException ex) {
        ex.printStackTrace();
    }

    return nb;
}
  
    
}
