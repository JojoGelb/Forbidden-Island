package m2104.ile_interdite.modele.Aventuriers;

import java.awt.Color;
import m2104.ile_interdite.modele.*;
import m2104.ile_interdite.util.Utils;

import java.util.ArrayList;

public class Explorateur extends Aventurier {


    public Explorateur(String nom, Tuile tuile, IleInterdite ILEINTERDITE) {
        super(nom, tuile, ILEINTERDITE);
        this.couleur = Color.GREEN;
    }


    @Override
    public ArrayList<Tuile> donnerCasesAdjacentes() {

        ArrayList<Tuile> tuilePossible = new ArrayList<>();
        
        tuilePossible = super.donnerCasesAdjacentes();

        Tuile[][] plateau = ileInterdite.getPlateau().getGrille();
        
        try{
            if((plateau[tuileAct.getPosX()-1][tuileAct.getPosY()-1]).getEtatTuile()!=Utils.EtatTuile.COULEE && (plateau[tuileAct.getPosX()-1][tuileAct.getPosY()-1])!=null){
                tuilePossible.add(plateau[tuileAct.getPosX()-1][tuileAct.getPosY()-1]);
            }
        }catch(Exception e){}
        try{
            if((plateau[tuileAct.getPosX()-1][tuileAct.getPosY()+1]).getEtatTuile()!=Utils.EtatTuile.COULEE && (plateau[tuileAct.getPosX()-1][tuileAct.getPosY()+1])!=null){
                tuilePossible.add(plateau[tuileAct.getPosX()-1][tuileAct.getPosY()+1]);
            }
        }catch(Exception e){}
        try{
            if((plateau[tuileAct.getPosX()+1][tuileAct.getPosY()-1]).getEtatTuile()!=Utils.EtatTuile.COULEE && (plateau[tuileAct.getPosX()+1][tuileAct.getPosY()+1])!=null){
                tuilePossible.add(plateau[tuileAct.getPosX()+1][tuileAct.getPosY()-1]);
            }
        }catch(Exception e){}
        try{
            if((plateau[tuileAct.getPosX()+1][tuileAct.getPosY()+1]).getEtatTuile()!=Utils.EtatTuile.COULEE && (plateau[tuileAct.getPosX()+1][tuileAct.getPosY()+1])!=null){
                tuilePossible.add(plateau[tuileAct.getPosX()+1][tuileAct.getPosY()+1]);
            }
        }catch(Exception e){}

        return tuilePossible;
    }

    
}

