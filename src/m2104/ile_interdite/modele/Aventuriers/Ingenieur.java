package m2104.ile_interdite.modele.Aventuriers;

import java.awt.Color;
import m2104.ile_interdite.modele.*;
import m2104.ile_interdite.util.Message;
import m2104.ile_interdite.util.Utils;

import static m2104.ile_interdite.util.Utils.EtatTuile.INONDEE;

//************************************************************************************************************
//                              Classe Aventurier avec pouvoir
//*************************************************************************************************************

public class Ingenieur extends Aventurier {

    private int temp ;
    public Ingenieur(String nom, Tuile tuile, IleInterdite ILEINTERDITE) {
        super(nom, tuile, ILEINTERDITE);
        this.couleur = Color.RED;
        temp=2;
    }
    
    
    //Permet d'assecher 2 fois plus
    @Override
        public void Assecher(String tuile) {
            if (ileInterdite.getPlateau().getTuileNom(tuile)== this.tuileAct|| donnerCasesAdjacentes().contains(ileInterdite.getPlateau().getTuileNom(tuile))) {
                if (ileInterdite.getPlateau().getTuileNom(tuile).getEtatTuile() == INONDEE) {
                    ileInterdite.getPlateau().getTuileNom(tuile).setEtatTuile(Utils.EtatTuile.ASSECHEE);
                    ileInterdite.notifierObservateurs(Message.MiseAJour());
                    
                    temp -= 1;

                    if (temp==1 ){
                        boolean aucuneTuileASecher = true;
                        for (Tuile uneTuile : this.donnerCasesAdjacentes()){
                            if(uneTuile.getEtatTuile() == INONDEE){
                                aucuneTuileASecher = false;
                            }
                        }
                        if (aucuneTuileASecher){
                            finAction();
                        }
                    }
                    
                    if (temp==0){
                        temp=2;
                        finAction();
                    }
                } else if (ileInterdite.getPlateau().getTuileNom(tuile).getEtatTuile() == Utils.EtatTuile.ASSECHEE) {
                    System.out.println("Vous asséchez une tuile déja sèche");
                } else
                    System.out.println("Vous ne pouvez assécher une tuile qui a coulé");
            }
        }

    @Override
    public void deplacer(String tuiledDest) {
        super.deplacer(tuiledDest);
        temp =2;
    }

    @Override
    public void donner(CarteTresors carte, Aventurier aventurier) {
        super.donner(carte, aventurier);

        temp = 2;
    }

    @Override
    public void addTresor() {
        super.addTresor();

        temp = 2;
    }

    @Override
    public void newTurn() {
        super.newTurn();
        this.temp=2;
    }

}








