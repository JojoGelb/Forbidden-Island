package m2104.ile_interdite.modele;

//************************************************************************************************************
//                              Cartes au trésor du jeu
//*************************************************************************************************************

public class CarteTresors {

    //***************************************Attributs**************************************//

    private String nom;     //Nom de la carte (exemple : "Sac de Sable","Hélicoptère","Calice de l'Onde"...)

    /*****************************************Constructeur****************************************/

    CarteTresors(String nom){
        setNom(nom);
    }
    
    //**************************************Méthodes***************************************//

    @Override
    public String toString() {
        return nom;
    }
    
    //**************************************Getters et Setters********************************************//

    public String getNom() {
        return nom;
    }

    private void setNom(String unNom) {
        this.nom = unNom;
    }
}
