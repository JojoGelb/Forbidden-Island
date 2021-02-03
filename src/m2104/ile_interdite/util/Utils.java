package m2104.ile_interdite.util;


//************************************************************************************************************
//                          Banque d'enum
//*************************************************************************************************************

public class Utils {

    public static enum Commandes {
        VALIDER_JOUEURS("Valider l'inscription des joueurs"),
        BOUGER("Déplacer son pion"),
        ASSECHER("Assécher une tuile"),
        DONNER("Donner une carte à un autre joueur"),
        RECUPERER_TRESOR("Récupérer le trésor de la tuile"),
        TERMINER("Terminer son tour"),
        RECEVOIR("Recevoir la carte donnée par un autre joueur"),
        CHOISIR_CARTE("Utiliser une carte trésor"),
        CHOISIR_TUILE("Sélectionner une tuile"),
        DEPLACER("Déplacer un autre joueur"),
        VOIR_DEFAUSSE("Un joueur souhaite voir la défausse de cartes Tirage"),
        MISE_A_JOUR("Met à jour les composants"),
        FIN_TOUR("Passe au tour du joueur suivant"),
        AUGMENTATION_EAU("Augmentation du niveau d'eau"),
        FIN_PARTIE("Fin de la partie"),
        NOYADE("La case à sombrée, il faut fuir"),
        DEFAUSSER("défossage"),
        MAIN_PLEINNE("La main est pleinne"),
        PASSER_TOUR("PASSAGE"),
        CARTE_ACTION("Joue une carte action"),
        REINIT_JEU("Réinitialisation jeu");

        private final String libelle ;

        Commandes(String libelle) {
            this.libelle = libelle ;
        }

        @Override
        public String toString() {
            return this.libelle ;
        }
    }

    public static enum EtatTuile {
        ASSECHEE("Asséchée"),
        INONDEE("Inondée"),
        COULEE("Coulée");

        private final String libelle ;

        EtatTuile(String libelle) {
            this.libelle = libelle ;
        }
    }

}
