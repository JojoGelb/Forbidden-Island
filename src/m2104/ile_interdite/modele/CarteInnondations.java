package m2104.ile_interdite.modele;

//************************************************************************************************************
//                              Cartes innondation du jeu
//*************************************************************************************************************

public class CarteInnondations {

    //*****************************************Attributs******************************************//

    private String nom;

    //****************************************Constructeur******************************************//

    CarteInnondations(String unNom){
        setNom(unNom);
    }


    //**************************************Méthodes*************************************//

    @Override
    public String toString() {
        return nom;
    }
    
    //*******************************Getters et Setters ********************************//

    public String getNom() {
        return nom;
    }

    private void setNom(String nom) {
        this.nom = nom;
    }
}
