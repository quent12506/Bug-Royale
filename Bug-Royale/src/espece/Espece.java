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
    
    public Espece(String stringEspece, double vitesseDeplacement, int HPParDefaut, BufferedImage sprite) {
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
    
    
    
}
