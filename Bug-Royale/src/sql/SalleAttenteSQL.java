/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sql;

/**
 *
 * @author qperise
 */

import java.sql.*;
import java.util.ArrayList;

public class SalleAttenteSQL {
    private Connection connexion;
    private Statement stmt;

    public SalleAttenteSQL() {
        try {
        connexion = DriverManager.getConnection(
                "jdbc:mariadb://nemrod.ens2m.fr:3306/2025-2026_s2_vs1_bug_royale",
                "etudiant",
                "YTDTvj9TR3CDYCmP"
        );

        stmt = connexion.createStatement();

        creerTableSiBesoin();
        if (partieTerminee()) {
            viderSalle();
        }

    } catch (SQLException ex) {
        ex.printStackTrace();
    }
    }

    private void creerTableSiBesoin() throws SQLException {
          PreparedStatement requete = connexion.prepareStatement(
            "CREATE TABLE IF NOT EXISTS Salle_Attente ("
                    + "id INT AUTO_INCREMENT PRIMARY KEY,"
                    + "pseudo VARCHAR(50),"
                    + "insecte VARCHAR(50),"
                    + "pret BOOLEAN DEFAULT false,"
                    + "partie_lancee BOOLEAN DEFAULT false)"
    );

    requete.executeUpdate();
    requete.close();
    }

    public void ajouterJoueur(String pseudo, String insecte) {
//    try {
//        PreparedStatement requete = connexion.prepareStatement(
//                "INSERT INTO Salle_Attente (pseudo, insecte, pret) VALUES (?, ?, false)"
//        );
//
//        requete.setString(1, pseudo);
//        requete.setString(2, insecte);
//
//        requete.executeUpdate();
//        requete.close();
//
//    } catch (SQLException ex) {
//        ex.printStackTrace();
//    }

try {
    
       System.out.println("Vérification salle...");
PreparedStatement verif = connexion.prepareStatement(
    "SELECT COUNT(*) AS nb FROM Salle_Attente"
);

ResultSet rs = verif.executeQuery();

if(rs.next()){
    System.out.println("Nombre lignes salle = " + rs.getInt("nb"));
}    

        if (rs.next() && rs.getInt("nb") > 0) {
            viderSalle();
        }

        verif.close();

        PreparedStatement requete = connexion.prepareStatement(
            "INSERT INTO Salle_Attente (pseudo, insecte, pret) VALUES (?, ?, false)"
        );

        requete.setString(1, pseudo);
        requete.setString(2, insecte);

        requete.executeUpdate();
        requete.close();

    } catch (SQLException ex) {
        ex.printStackTrace();
    }
System.out.println("Partie lancée = " + partieLancee());
}   

    public void mettrePret(String pseudo) {
        try {
            PreparedStatement requete = connexion.prepareStatement(
                    "UPDATE Salle_Attente SET pret = true WHERE pseudo = ?"
            );

            requete.setString(1, pseudo);
            requete.executeUpdate();
            requete.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<Object[]> listeJoueurs() {
        ArrayList<Object[]> liste = new ArrayList<>();

        try {
            PreparedStatement requete = connexion.prepareStatement(
                    "SELECT pseudo, insecte, pret FROM Salle_Attente"
            );

            ResultSet resultat = requete.executeQuery();

            while (resultat.next()) {
                String pseudo = resultat.getString("pseudo");
                String insecte = resultat.getString("insecte");
                boolean pret = resultat.getBoolean("pret");

                liste.add(new Object[]{
                    pseudo,
                    insecte,
                    pret ? "Prêt" : "Pas prêt"
                });
            }

            requete.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return liste;
    }

    public boolean tousPrets() {
        try {
        PreparedStatement requete = connexion.prepareStatement(
                "SELECT COUNT(*) AS nb, SUM(CASE WHEN pret = true THEN 1 ELSE 0 END) AS nbPret FROM Salle_Attente"
        );

        ResultSet resultat = requete.executeQuery();

        if (resultat.next()) {
            int nb = resultat.getInt("nb");
            int nbPret = resultat.getInt("nbPret");

            return nb > 0 && nb == nbPret;
        }

        requete.close();

    } catch (SQLException ex) {
        ex.printStackTrace();
    }

    return false;
    }
    public void viderSalle() {
        try {
            System.out.println("VIDAGE DE LA SALLE");

        PreparedStatement requete = connexion.prepareStatement(
                "DELETE FROM Salle_Attente"
        );

        int nb = requete.executeUpdate();

        System.out.println(nb + " ligne(s) supprimée(s)");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void closeTable() {
        try {
            if (this.connexion != null && !this.connexion.isClosed()) {
                this.connexion.close();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void lancerPartie() {
    try {
        PreparedStatement requete = connexion.prepareStatement(
                "UPDATE Salle_Attente SET partie_lancee = true"
        );

        requete.executeUpdate();
        requete.close();

    } catch (SQLException ex) {
        ex.printStackTrace();
    }
}
    public boolean partieLancee() {
     try {
        PreparedStatement requete = connexion.prepareStatement(
                "SELECT partie_lancee FROM Salle_Attente LIMIT 1"
        );

        ResultSet rs = requete.executeQuery();

        if (rs.next()) {
            boolean lancee = rs.getBoolean("partie_lancee");
            requete.close();
            return lancee;
        }

        requete.close();

    } catch (SQLException ex) {
        ex.printStackTrace();
    }

    return false;
}
    public boolean partieTerminee() {
    try {
        PreparedStatement requete = connexion.prepareStatement(
                "SELECT COUNT(*) AS nb FROM Joueur"
        );

        ResultSet rs = requete.executeQuery();

        if (rs.next()) {
            return rs.getInt("nb") == 0;
        }

    } catch (SQLException ex) {
        ex.printStackTrace();
    }

    return true;
}
    
}
