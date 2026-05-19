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

    public SalleAttenteSQL() {
        try {
            connexion = DriverManager.getConnection(
                    "jdbc:mariadb://nemrod.ens2m.fr:3306/2025-2026_s2_vs1_bug_royale",
                    "etudiant",
                    "YTDTvj9TR3CDYCmP"
            );

            creerTableSiBesoin();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void creerTableSiBesoin() throws SQLException {
        PreparedStatement requete = connexion.prepareStatement(
                "CREATE TABLE IF NOT EXISTS Salle_Attente ("
                + "pseudo VARCHAR(50) PRIMARY KEY,"
                + "insecte VARCHAR(50),"
                + "pret BOOLEAN)"
        );
        requete.executeUpdate();
        requete.close();
    }

    public void ajouterJoueur(String pseudo, String insecte) {
        try {
            PreparedStatement requete = connexion.prepareStatement(
                    "INSERT INTO Salle_Attente VALUES (?, ?, false) "
                    + "ON DUPLICATE KEY UPDATE insecte = ?, pret = false"
            );

            requete.setString(1, pseudo);
            requete.setString(2, insecte);
            requete.setString(3, insecte);

            requete.executeUpdate();
            requete.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
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
                    "SELECT COUNT(*) AS nb, SUM(pret) AS nbPret FROM Salle_Attente"
            );

            ResultSet resultat = requete.executeQuery();

            if (resultat.next()) {
                int nb = resultat.getInt("nb");
                int nbPret = resultat.getInt("nbPret");

                return nb >= 2 && nb == nbPret;
            }

            requete.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }
    
}
