package m2104.ile_interdite.modele.Aventuriers;

import java.awt.Color;
import m2104.ile_interdite.modele.*;
import m2104.ile_interdite.util.Utils;

//************************************************************************************************************
//                          Classe d'aventurier spécialisé
//*************************************************************************************************************

public class Plongeur extends Aventurier {


    public Plongeur(String nom, Tuile tuile, IleInterdite ILEINTERDITE) {
        super(nom, tuile, ILEINTERDITE);
        this.couleur = Color.BLACK;
    }


    @Override
    public void deplacer(String tuiledDest) {
        Tuile t = ileInterdite.getPlateau().getTuileNom(tuiledDest);

        if (donnerCasesAdjacentes().contains(t)){
            this.tuileAct = t;
            if (tuileAct.getEtatTuile()== Utils.EtatTuile.INONDEE){
                nbActionRestante+=1;
            }
            finAction();
        } else {
            System.out.println("deplacement impossibles");
        }

    }

}
