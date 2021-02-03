package m2104.ile_interdite.controleur;

import m2104.ile_interdite.modele.Aventurier;
import m2104.ile_interdite.modele.IleInterdite;
import m2104.ile_interdite.util.Message;
import m2104.ile_interdite.vue.IHM;
import patterns.observateur.Observateur;

//************************************************************************************************************
//                                              Controleur
//*************************************************************************************************************



public class Controleur implements Observateur<Message> {
    
    //************************************ATTRIBUTS****************************************//

    private IleInterdite ileInterdite;
    private IHM ihm;
    
    //************************************CONSTRUCTEUR*************************************//

    public Controleur() {
        this.ileInterdite = new IleInterdite(this);
        this.ihm = new IHM(this);
    }
    
    //************************************METHODES*****************************************//

    @Override
    public void traiterMessage(Message msg) {

        switch (msg.getCommande()) {
            case VALIDER_JOUEURS:
                System.out.println("CONTROLEUR: Valider joueur");
                ileInterdite.setNiveauEau(msg.getNb());
                ihm.setAventuriers(this.ileInterdite.inscrireJoueurs(this.ihm.getVueInscription().getNomJoueurs()));
                this.ihm.afficherVuePlateau();
                
                this.ihm.getVuePlateau().setAventurierEnCoursDeJeu(this.ileInterdite.getAventuriers().get(0));
                this.ihm.getVuePlateau().setNivEau(msg.getNb());

                ileInterdite.MonteDesEaux();
                ileInterdite.MonteDesEaux();// innonde les 6 premières tuiles
                break;
            case BOUGER:
                System.out.println("Controleur : Deplacement en: " + msg.getIdTuile());
                this.ileInterdite.getAventurierNom(msg.getIdAventurier()).deplacer(msg.getIdTuile());
                break;
            case ASSECHER:
                this.ileInterdite.getAventurierNom(msg.getIdAventurier()).Assecher(msg.getIdTuile());
                System.out.println("Controleur : Assechement");
                break;
            case DONNER:
                this.ileInterdite.getAventurierNom(msg.getIdAventurier()).donner(ileInterdite.getAventurierNom(msg.getIdAventurier()).getCarteNom(msg.getIdCarte()), ileInterdite.getAventurierClasse(msg.getIdAventurier2()));
                System.out.println("Controleur : Don de carte");
                break;
            case RECUPERER_TRESOR:
                this.ileInterdite.getAventurierNom(msg.getIdAventurier()).addTresor();
                System.out.println("Controleur : Recuperation de tresor");
                break;
            case DEPLACER:
                break;
            case MISE_A_JOUR:
                this.ihm.getVuePlateau().MajGrille(ileInterdite.getPlateau().getGrille());
                this.ihm.setAventuriers(this.ileInterdite.getAventuriers());
                System.out.println("Controleur : Mise à jour ihm");
                break;
            case FIN_TOUR:
                this.ihm.getVuePlateau().setAventurierEnCoursDeJeu(ileInterdite.getAventurierNom(msg.getIdAventurier()));
                System.out.println("Controleur : Fin du tour");
                this.ileInterdite.MonteDesEaux();
                break;
            case PASSER_TOUR:
                ileInterdite.getAventurierNom(msg.getIdAventurier()).setNbActionRestante(1);
                ileInterdite.getAventurierNom(msg.getIdAventurier()).finAction();
                System.out.println("Controleur : Passe son tour");

                break;
            case AUGMENTATION_EAU:
                this.ihm.getVuePlateau().setNivEau(msg.getNb());
                break;
            case FIN_PARTIE:
                System.out.println("Controleur : FIN PARTIE " + msg.getNb());
                ihm.setFinPartie(msg.getNb());
                ihm.afficherVueFin();
                break;
            case MAIN_PLEINNE:
                this.ihm.getVuePlateau().getAventuriersMainPleinne().add(msg.getIdAventurier());
                ihm.getVuePlateau().repaint();
                break;
            case DEFAUSSER:
                System.out.println("Controleur : Défausse ");
                for (Aventurier a:ileInterdite.getAventuriers()){
                    if (a.getNom().equalsIgnoreCase(msg.getIdAventurier())){
                        a.defausseCarteMain(a.getCarteNom(msg.getIdCarte()));
                    }
                }
                break;
            case CARTE_ACTION:
                System.out.println("Controleur: joue carte Action" + msg.getIdAventurier() + msg.getIdTuile() + msg.getIdCarte());
                ileInterdite.getAventurierNom(msg.getIdAventurier()).ActionCarteSpecial(msg.getIdTuile(), msg.getIdCarte());
                break;
            case NOYADE:
                ihm.getVuePlateau().getAventurierSeNoyant().add(msg.getIdAventurier());
                ihm.getVuePlateau().setUrgenceDeplacement(true);
                break;
            case REINIT_JEU:
                System.out.println("Controleur : Restart");
                this.ileInterdite = new IleInterdite(this);
                ihm.FinIhm();
                this.ihm = new IHM(this);
                break;
        }

    }
}
