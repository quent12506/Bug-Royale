/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package espece;

import java.awt.image.BufferedImage;

/**
 *
 * @author hugom
 */
public class Espece {
    public String stringEspece;
    private double vitesseDeplacement;
    private int HPParDefaut;
    protected BufferedImage sprite;
    private double vitesseProjectile;
    
    public Espece(String stringEspece, double vitesseDeplacement, int HPParDefaut, BufferedImage sprite, double vitesseProjectile) {
        this.stringEspece=stringEspece;
        this.vitesseDeplacement = vitesseDeplacement;
        this.HPParDefaut = HPParDefaut;
        this.sprite = sprite;
    }

    public BufferedImage getSprite() {
        return sprite;
    }

    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    public String getStringEspece() {
        return stringEspece;
    }

    public void setStringEspece(String stringEspece) {
        this.stringEspece = stringEspece;
    }

    public double getVitesseDeplacement() {
        return vitesseDeplacement;
    }

    public void setVitesseDeplacement(double vitesseDeplacement) {
        this.vitesseDeplacement = vitesseDeplacement;
    }

    public int getHPParDefaut() {
        return HPParDefaut;
    }

    public void setHPParDefaut(int HPParDefaut) {
        this.HPParDefaut = HPParDefaut;
    }
    
    public double getLargeur() { 
        return sprite.getWidth(); 
    }
    public double getHauteur() { 
        return sprite.getHeight(); 
    }

    public void setVitesseProjectile(double vitesseProjectile) {
        this.vitesseProjectile = vitesseProjectile;
    }
    

    public static Espece depuisNom(String nomEspece) {
        return switch (nomEspece) {
            case "Fourmi", "fourmi" ->
                    new Fourmi();
            case "Abeille", "abeille" ->
                    new Abeille();
            case "Sauterelle", "sauterelle" ->
                    new Sauterelle();
            case "Scarabée", "Scarabee", "scarabée", "scarabee" ->
                    new Scarabee();
            case "Araignée", "Araignee", "araignée", "araignee" ->
                    new Araignee();
            default ->
                    new Araignee();
        };
    }
    
    
    
}
