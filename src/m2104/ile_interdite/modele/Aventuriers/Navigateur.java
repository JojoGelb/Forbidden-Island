package m2104.ile_interdite.modele.Aventuriers;

import java.awt.Color;
import m2104.ile_interdite.modele.*;

//************************************************************************************************************
//                          Classe d'aventurier spécialisé
//*************************************************************************************************************

public class Navigateur extends Aventurier {

    private int temp ;

    public Navigateur(String nom, Tuile tuile, IleInterdite ILEINTERDITE) {
        super(nom, tuile, ILEINTERDITE);
        this.couleur = Color.YELLOW;
        temp=2;
    }


    public void deplacerUnJoueur(String tuiledDest,String nom) {
        if (ileInterdite.getAventurierNom(nom).donnerCasesAdjacentes().contains(tuiledDest)){
            ileInterdite.getAventurierNom(nom).setTuileAct(ileInterdite.getPlateau().getTuileNom(tuiledDest));
            if(temp==2){
                finAction();
            }
            temp -= 1;
            
            if (temp==0){
                temp=2;
            }
        }else{
            System.out.println("L'aventurier ne peux pas y aller");
        }
    }
    @Override
    public void newTurn() {
        super.newTurn();
        this.temp=2;
    }
}
