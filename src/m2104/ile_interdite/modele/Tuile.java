/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m2104.ile_interdite.modele;

import m2104.ile_interdite.util.Utils;

/**
 *
 * @author jordy
 */
public class Tuile {
    
    //*********************ATTRIBUTS************************//
    
    private Utils.EtatTuile etatTuile;
    private String nom,tresor;
    private int posX, posY;
    
    //*********************CONSTRUCTEUR************************//
    
    public Tuile (String nom,String tresor,int posX, int posY){
        this.nom= nom;
        this.etatTuile = Utils.EtatTuile.ASSECHEE;
        this.posX = posX;
        this.posY = posY;
        this.tresor = tresor;

    }
    
    public Tuile (String nom,String tresor){
        this.nom= nom;
        this.tresor=tresor;
    }
    
    

    //*********************GETTERS SETTERS************************//
    
    public String getNom() {
        return nom;
    }

    public Utils.EtatTuile getEtatTuile() {
        return etatTuile;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setEtatTuile(Utils.EtatTuile etatTuile) {
        this.etatTuile = etatTuile;
    }

    public String getTresor() {
        return tresor;
    }
}
