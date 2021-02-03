package m2104.ile_interdite.modele.Aventuriers;

import java.awt.Color;
import m2104.ile_interdite.modele.*;
import m2104.ile_interdite.util.Message;
import m2104.ile_interdite.util.Utils;

//************************************************************************************************************
//                          Classe d'aventurier spécialisé
//*************************************************************************************************************

public class Pilote extends Aventurier {

    private int temp=1;
    public Pilote(String nom, Tuile tuile, IleInterdite ILEINTERDITE) {
        super(nom, tuile, ILEINTERDITE);
        this.couleur=Color.BLUE;
    }

    @Override
    public void deplacer(String tuiledDest){
        Tuile t = ileInterdite.getPlateau().getTuileNom(tuiledDest);

        if ((t.getEtatTuile()!= Utils.EtatTuile.COULEE) && temp>0 && this.donnerCasesAdjacentes().contains(t)==false){
            this.tuileAct = t;
            finAction();
            temp-=1;
        } else {
            super.deplacer(tuiledDest);
        }

    }

    @Override
    public void finAction() {
        nbActionRestante-=1;
        if(nbActionRestante ==0){
            this.temp=1;
            this.piocherCarteTresors(2);

            String nm = "";
            for(int i =0; i<ileInterdite.getAventuriers().size();i++){
                if(this.getNom().equalsIgnoreCase(ileInterdite.getAventuriers().get(i).getNom())){
                    if(i ==ileInterdite.getAventuriers().size()-1)
                        nm = ileInterdite.getAventuriers().get(0).getNom();
                    else
                        nm = ileInterdite.getAventuriers().get(i+1).getNom();
                }

            }
            ileInterdite.notifierObservateurs(Message.finTour(nm));
            newTurn();
    }
        ileInterdite.notifierObservateurs(Message.MiseAJour());}
}
