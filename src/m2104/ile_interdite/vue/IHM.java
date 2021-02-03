package m2104.ile_interdite.vue;

import gfx.Assets;
import java.awt.*;
import java.util.ArrayList;
import m2104.ile_interdite.modele.Aventurier;
import m2104.ile_interdite.util.Message;
import patterns.observateur.Observable;
import patterns.observateur.Observateur;
import javax.swing.*;

//************************************************************************************************************
//                          Classe gérant l'aspect graphique du jeu
//*************************************************************************************************************


public class IHM extends Observable<Message> {
    
    //*******************************************Attributs***************************************//

    //Les 3 Jpanels composant la partie graphique du jeu
    private VueInscriptionJoueurs vueInscription;
    private VuePlateau vuePlateau;
    private VueFinPartie vueFin;

    //Une arrayList d'aventurier mis à jour par le controleur
    private ArrayList<Aventurier> aventuriers;
    
     //Les images du jeu préchargé pour évité les bugs
    private Assets assets;
    
    //Fenetres et panel
    private boolean bool = false;
    private JFrame fenetre;
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel(cardLayout);

//**************************************CONSTRUCTEUR*******************************************//
    
    public IHM(Observateur<Message> observateur) {
        parametrerFenetre();
        this.addObservateur(observateur);
        
        //Charge les images du jeu
        this.assets = new Assets();
        
        //Instancie les différentes vues
        this.vueInscription = new VueInscriptionJoueurs(this);
        this.vuePlateau = new VuePlateau(this);
        
        
        fenetre.add(mainPanel);
        mainPanel.add("plateau",vuePlateau);
        mainPanel.add("inscription",vueInscription);
        cardLayout.show(mainPanel,"inscription");
        
        fenetre.setVisible(true);

        
    }
    
    //***********************Methodes**********************************//

    //Set les valeurs du JFRAME
    public void parametrerFenetre(){
        fenetre = new JFrame();
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenetre.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width-1900)/4, (Toolkit.getDefaultToolkit().getScreenSize().height-1100)/4);        fenetre.setResizable(false);
        fenetre.setSize(1800, 1000);
        fenetre.setResizable(false);
    }
    
    
    //Les méthodes suivantes change le Jpanel affiché à l'ecran
    public void afficherVuePlateau() {
        cardLayout.show(mainPanel,"plateau");
    }
    
    public void afficherVueFin(){
        if(bool == false){
            mainPanel.add("fin",vueFin);
            bool = true;
        }
        cardLayout.show(mainPanel,"fin");
    }
    
    public void afficherVueInscription(){
        cardLayout.show(mainPanel,"inscription");
    }
    
    //Ferme la fenetre
    public void FinIhm(){
        fenetre.dispose();
    }
    
    //***************************GETTERS SETTERS**************************************//
    
    public VueInscriptionJoueurs getVueInscription() {
        return vueInscription;
    }
    
    public void setFinPartie(int nb){
        vueFin = new VueFinPartie(this, nb);
    }

    public VuePlateau getVuePlateau() {
        return vuePlateau;
    }

    public Assets getAssets() {
        return assets;
    }
    
    public void setAventuriers(ArrayList<Aventurier> aventuriers) {
        this.aventuriers = aventuriers;
    }

    public ArrayList<Aventurier> getAventuriers() {
        return aventuriers;
    }
    
    
    
    
    

}
