package m2104.ile_interdite.modele.Aventuriers;

import java.awt.Color;
import m2104.ile_interdite.modele.*;

//************************************************************************************************************
//                          Classe d'aventurier spécialisé
//*************************************************************************************************************

public class Messager extends Aventurier {


    public Messager(String nom, Tuile tuile, IleInterdite ILEINTERDITE) {
        super(nom, tuile, ILEINTERDITE);
        this.couleur = Color.WHITE;
    }

    @Override
    public void donner(CarteTresors carte, Aventurier aventurier){
            aventurier.getCarteMain().add(carte);
            this.carteMain.remove(carte);
            finAction();
    }
}
