package m2104.ile_interdite.modele;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import m2104.ile_interdite.modele.Aventuriers.Explorateur;
import m2104.ile_interdite.modele.Aventuriers.Ingenieur;
import m2104.ile_interdite.modele.Aventuriers.Messager;
import m2104.ile_interdite.modele.Aventuriers.Navigateur;
import m2104.ile_interdite.modele.Aventuriers.Pilote;
import m2104.ile_interdite.modele.Aventuriers.Plongeur;

import m2104.ile_interdite.util.Message;
import m2104.ile_interdite.util.Utils;
import patterns.observateur.Observable;
import patterns.observateur.Observateur;


//************************************************************************************************************
//                    Partie fonctionelle du jeu avec interaction entre les classes
//*************************************************************************************************************


public class IleInterdite extends Observable<Message> {

    //**********************************Attributs*************************************//

    private int niveauEau,caseACouler;

    //Attributs pour les Cartes

    private ArrayList<CarteTresors> pileCarteTresors = new ArrayList<>();
    private ArrayList<CarteTresors> defausseCarteTresors = new ArrayList<>();
    private ArrayList<CarteInnondations> pileCarteInnondations = new ArrayList<>();
    private ArrayList<CarteInnondations> defausseCartesInnondations = new ArrayList<>();
    private ArrayList<Tresor> tresors = new ArrayList<>();
    private boolean tour1 = true;
    
    //Aventurier
    
    private ArrayList<Aventurier> aventuriers = new ArrayList<Aventurier>();

    //Attribut pour le Plateau

    private Plateau plateau = new Plateau();


    //*****************************Constructeur**********************************//
    
    
    
    public IleInterdite(Observateur<Message> observateur) {
        this.addObservateur(observateur);
        
        initCartes();
        initTresors();
        this.niveauEau =4;
        setCaseAcouler();
    }
    
    
    //*********************************METHODES**************************************//
    
    public void setCaseAcouler(){
        switch(niveauEau){
            case 1:
                caseACouler = 2;
                break;
            case 2:
                caseACouler = 2;
                break;
            case 3:
                caseACouler = 2;
                break;
            case 4:
                caseACouler = 3;
                break;
            case 5:
                caseACouler = 3;
                break;
            case 6:
                caseACouler = 4;
                break;
            case 7:
                caseACouler = 4;
                break;
            case 8:
                caseACouler = 5;
                break;
            case 9:
                caseACouler = 5;
                break;
            case 10:
                notifierObservateurs(Message.finPartie(1));
                break;
            default:
                System.out.println("LE NIVEAU D'EAU DEPASSE L'ENTENDEMENT");
                break;
        }
    }
    
    //CECI EST UNE PROCEDURE TEMPORAIRE POUR TESTER L'INITIALISATION ET LES FONCTIONS DE JEUX
    //MERCI DE NE PAS TENTER D'ERREUR QUI NE PEUVENT ARRIVER DANS UN IHM
    //EX: SORTIR DE LA MAP
    /*public void tour(Aventurier av){
        Scanner scan = new Scanner(System.in);
        Scanner scanString = new Scanner(System.in);
        int x,y;
        String mot;
        
        while(av.getNbActionRestante()!=0){
            System.out.println("\nJoueur : "+ av.getClass().getSimpleName()+" Nb action restante: " + av.getNbActionRestante());
            System.out.println("Position du joueur:\n x: " + av.getTuileAct().getPosX() +"\n y: "+ av.getTuileAct().getPosY());
            System.out.println("Taper une action à faire: \n");
            System.out.println("Se déplacer: 1");
            System.out.println("Assécher : 2");
            System.out.println("Donner une carte au trésor : 3");
            System.out.println("Gagner un trésor : 4");
            System.out.println("Passer son tour : 5");
            
            int action = scan.nextInt();
            switch(action){
                case 1:
                    System.out.println("Donner la case ou se déplacer");
                    x = scan.nextInt();
                    y = scan.nextInt();
                    if(plateau.getGrille()[x][y]!=null)
                        av.deplacer(plateau.getGrille()[x][y].getNom());
                    else
                        System.out.println("action impossible case vide");
                    break;
                case 2:
                    System.out.println("Donner la case à assécher");
                    x = scan.nextInt();
                    y = scan.nextInt();
                    if(plateau.getGrille()[x][y]!=null)
                        av.Assecher(plateau.getGrille()[x][y].getNom());
                    else
                        System.out.println("action impossible case vide");
                    break;
                case 3:
                    System.out.println("Donner le nom de la classe du joueur à qui donner une carte au trésor");
                    mot = scanString.nextLine();
                    for(Aventurier ave: aventuriers){
                        if (ave.getClass().getSimpleName().equalsIgnoreCase(mot) && ave.getNom()!= av.getNom()){
                            System.out.println("Votre main :\n");
                            for(CarteTresors carte: av.getCarteMain()){
                                System.out.println(carte.getNom());
                            }
                            System.out.println("Nom de la carte à donner");
                            mot = scanString.nextLine();
                            for(CarteTresors carte: av.getCarteMain()){ //Le for sera cassé lorsque le nom de la carte sera retrouvé pour éviter plusieurs don en un coup
                                if (carte.getNom().equalsIgnoreCase(mot)){
                                    
                                    av.donner(carte, ave);
                                    break;
                                }
                            } 
                        }
                    }
                    break;
                case 4:
                    System.out.println("Quel trésor cherche vous à gagner? (CRISTAL, STATUE, PIERRE, CALICE)");
                    mot = scanString.nextLine();
                    av.addTresor(mot); 
                    break;
                case 5:
                    System.out.println("Fin des actions");
                    av.setNbActionRestante(0);
                    break;
                    
            }    
        }

        av.piocherCarteTresors();
        av.piocherCarteTresors();
        while(av.getCarteMain().size()>5){
            System.out.println("Trop de carte en main, il faut en défausser");
            System.out.println("Votre main :\n");
            for(CarteTresors carte: av.getCarteMain()){
                System.out.println(carte.getNom());
            }
            System.out.println("Quel carte défaussez vous?");
            mot = scanString.nextLine();
            for(CarteTresors carte: av.getCarteMain()){ //Le for sera cassé lorsque le nom de la carte sera retrouvé pour éviter plusieurs don en un coup
                if (carte.getNom().equalsIgnoreCase(mot)){
                    System.out.println("here");
                    av.defausseCarteMain(carte);
                    System.out.println(av.getCarteMain().size());
                break;
                }
            }
        }
        System.out.println("\n***********************Montée des eaux*******************************\n");
        System.out.println("niveau d'eau: " + niveauEau);
        
        
        System.out.println("Fin du tour de " + av.getNom());
        av.newTurn();
              
    }*/
    
    
    //---------------------------------------------------------------------------------------
    
    //Augmente le niveau d'eau d'un nombre "caseAcouler" de tuiles
    public void MonteDesEaux(){
        for (int i =0; i<caseACouler; i++){
            piocherCarteInnondations();
            //Peut etre envoyer la grille MESSAGE DE MAJ GRILLE
        }
        for(Aventurier av: aventuriers){
            if (!av.getClass().getSimpleName().equalsIgnoreCase("pilote")
                &&!av.getClass().getSimpleName().equalsIgnoreCase("plongeur") && av.donnerCasesAdjacentes().isEmpty()){
                int temp = 0;
                for(CarteTresors ca: av.getCarteMain()){
                    if(ca.getNom().equalsIgnoreCase("helicoptere")){
                        temp++;
                    }
                }
                if (temp ==0)
                    notifierObservateurs(Message.finPartie(3));  
            }
        }
        notifierObservateurs(Message.MiseAJour());
    }
    
    
    //Instancie les aventuriers de la partie: Appellé 1 fois par le controleur lors de la validation des joueurs
    public ArrayList<Aventurier> inscrireJoueurs(String[] nomsJoueurs) {
        int nb=10,nb1=10,nb2=10,nb3=10,nb4 =10;
        boolean bool = true;

        for(int i =0; i<nomsJoueurs.length; i++){ //random classe
            while (bool){
                nb = randomInt(1, 7);
                if (nb != nb1 && nb != nb2 && nb != nb3 && nb!= nb4){
                    bool = false;
                    if(nb1 == 10){
                        nb1 = nb;
                    }else if(nb2 == 10){
                        nb2 = nb;
                    }else if(nb3 == 10){
                        nb3 = nb;
                    }else if(nb4 == 10){
                        nb4 = nb;
                    }
                }
            }
           switch (nb){
               case 1:
                   aventuriers.add(new Explorateur(nomsJoueurs[i], plateau.getTuileNom("LaPorteDeCuivre"), this));
                   break;
               case 2:
                   aventuriers.add(new Ingenieur(nomsJoueurs[i], plateau.getTuileNom("LaPorteDeBronze"), this));
                   break;
               case 3:
                   aventuriers.add(new Messager(nomsJoueurs[i], plateau.getTuileNom("LaPortedArgent"), this));
                   break;
               case 4:
                   aventuriers.add(new Navigateur(nomsJoueurs[i], plateau.getTuileNom("LaPortedOr"), this));
                   break;
               case 5:
                   aventuriers.add(new Pilote(nomsJoueurs[i], plateau.getTuileNom("Heliport"), this));
                   break;
               case 6:
                   aventuriers.add(new Plongeur(nomsJoueurs[i], plateau.getTuileNom("LaPorteDeFer"), this));
                   break;                  
           }
           

           bool = true;
           
        }
        tour1 = false;

        return aventuriers;

    }
    
    //Instancie les trésors
    
    public void initTresors(){
        this.tresors.add(new Tresor("calice"));
        this.tresors.add(new Tresor("cristal"));
        this.tresors.add(new Tresor("zephyr"));
        this.tresors.add(new Tresor("pierre"));
    }
    
    //Permet de mélanger la pile des cartes si besoin

    private void melangeCartesTresors(){
        Collections.shuffle(pileCarteTresors);
    }

    private void melangeCartesInnondations(){
        Collections.shuffle(pileCarteInnondations);
    }

    //Permet de vider les défausses des cartes, de remettre les cartes dans la pile et les mélanger

    public void MelangeCarteTresorsDefausse(){
        if (getDefausseCarteTresors().isEmpty() == false){
        for (int i = defausseCarteTresors.size()-1; i>=0;i--){
            pileCarteTresors.add(this.defausseCarteTresors.get(i));
            defausseCarteTresors.remove(this.defausseCarteTresors.get(i));
        }
        melangeCartesTresors();
        }
    }

    private void MelangeCarteInnondationsDefausse(){
        if (getDefausseCartesInnondations().isEmpty() == false) {
            for (int i = defausseCartesInnondations.size()-1; i >= 0; i--) {
                pileCarteInnondations.add(defausseCartesInnondations.get(i));
                defausseCartesInnondations.remove(defausseCartesInnondations.get(i));
            }
            melangeCartesInnondations();
        }
    }

    //Permet d'initialiser les piles de cartes en une seule méthode

    private void initCartes(){
        setPileCarteTresors();
        setPileCarteInnondations(plateau.getTuiles());
        setDefausseCarteTresors();
        setDefausseCartesInnondations();

    }

    //Permet de défausser une carte

    public void defausserCarteTresors(CarteTresors uneCarte){
        defausseCarteTresors.add(uneCarte);
    }

    public void defausserCarteInnondations(CarteInnondations uneCarte){
        if(plateau.getTuileNom(uneCarte.getNom()).getEtatTuile()!=Utils.EtatTuile.COULEE){
                defausseCartesInnondations.add(uneCarte);
        }
    }

    //Permet de piocher une carte (Trésors ou Innondation)

    
    public CarteTresors piocherCarteTresors(){
        CarteTresors cartePiochee;
        if (getPileCarteTresors().isEmpty()){
            MelangeCarteTresorsDefausse();
            System.out.println("MELANGE CARTE DEFAUSSE");
        }

        cartePiochee = getPileCarteTresors().get(getPileCarteTresors().size()-1);
        if(cartePiochee.getNom().equalsIgnoreCase("MonteeDesEaux") && tour1){
            melangeCartesTresors();
            cartePiochee = piocherCarteTresors();
        }else if (cartePiochee.getNom().equalsIgnoreCase("MonteeDesEaux")){
            defausserCarteTresors(cartePiochee);
            MelangeCarteInnondationsDefausse();
            niveauEau +=1;
            setCaseAcouler();
            //System.out.println("Montée du niveau d'eau à: "+ niveauEau);
            notifierObservateurs(Message.augmentationEau(niveauEau));
            
            getPileCarteTresors().remove(getPileCarteTresors().size()-1);
        }else {
            getPileCarteTresors().remove(getPileCarteTresors().size() - 1);
        }


        return cartePiochee;
    }
    

    public void piocherCarteInnondations(){
        CarteInnondations cartePiochee;
        cartePiochee = getPileCarteInnondations().get(pileCarteInnondations.size()-1);

        for(int i =0;i<6; i++) { //ligne
            for (int j = 0; j < 6; j++) { // colonne
                if(plateau.getGrille()[i][j]!= null && plateau.getGrille()[i][j].getNom().equals(cartePiochee.getNom())){
                    if(plateau.getGrille()[i][j].getEtatTuile() == Utils.EtatTuile.ASSECHEE)
                        plateau.getGrille()[i][j].setEtatTuile(Utils.EtatTuile.INONDEE);
                    else if(plateau.getGrille()[i][j]!= null && plateau.getGrille()[i][j].getEtatTuile() == Utils.EtatTuile.INONDEE){
                        plateau.getGrille()[i][j].setEtatTuile(Utils.EtatTuile.COULEE);
                        if (plateau.getGrille()[i][j].getNom().equals("Heliport")){
                            notifierObservateurs(Message.finPartie(2));
                        }else{
                            for (Aventurier a : aventuriers) {
                                if (a.getTuileAct().getNom().equals((plateau.getGrille()[i][j]).getNom())) {
                                    notifierObservateurs(Message.noyade(a.getNom()));
                                }
                            }
                        }
                       if ( //Si les 2 cases du plateau concernant un trésor coulent et que le trésor n'a pas été récupéré: FINJEU
                       ((plateau.getTuileNom("LeJardinDesMurmures").getEtatTuile()==Utils.EtatTuile.COULEE && plateau.getTuileNom("LeJardinDesMurmures").getEtatTuile()==Utils.EtatTuile.COULEE)&&tresors.contains(getTresorsNom("Zephyr"))) ||
                       ((plateau.getTuileNom("LaCarverneDuBrasier").getEtatTuile()==Utils.EtatTuile.COULEE && plateau.getTuileNom("LaCarverneDesOmbres").getEtatTuile()==Utils.EtatTuile.COULEE)&&tresors.contains(getTresorsNom("CRISTAL"))) ||
                       ((plateau.getTuileNom("LeTempleDuSoleil").getEtatTuile()==Utils.EtatTuile.COULEE && plateau.getTuileNom("LeTempleDeLaLune").getEtatTuile()==Utils.EtatTuile.COULEE)&&tresors.contains(getTresorsNom("PIERRE"))) ||
                       ((plateau.getTuileNom("LePalaisDesMarees").getEtatTuile()==Utils.EtatTuile.COULEE && plateau.getTuileNom("LePalaisDeCorail").getEtatTuile()==Utils.EtatTuile.COULEE)&&tresors.contains(getTresorsNom("CALICE")))){
                           notifierObservateurs(Message.finPartie(4));
                       }
                    }
                    //System.out.println("Augmentation du niveau d'eau de la tuile" + plateau.getGrille()[i][j].getNom());
                }
            }
        }
        getPileCarteInnondations().remove(pileCarteInnondations.size()-1);
        defausserCarteInnondations(cartePiochee);
    }
    
    //Genere un int au hazard
    public int randomInt(int borneInf, int borneSup){
        Random random = new Random();
        int nb;
        nb = borneInf+random.nextInt(borneSup-borneInf);
        return nb;
    }
    
    //Verifie si les aventuriers ont gagné
    public boolean checkVictoire(){
        if(tresors.isEmpty()){
            for(Aventurier av: aventuriers){
                if(!av.getTuileAct().getNom().equalsIgnoreCase("heliport"))
                    return false;
            }
            return true;
        }
        return false;
    }

     //**********************************Getter et Setters***********************************//
    
    //Retourne l'aventurier possédant ce nom dans l'arrayList d'aventurier
     public Aventurier getAventurierNom(String nom){
         for(Aventurier a:aventuriers) { //ligne
             if (a.getNom().equalsIgnoreCase(nom)) {
                return a;
             }
         }
         System.out.println("Aventurier inexistant");
         return null;
     }
     
    //Retourne l'aventurier possédant la classe cl dans l'arrayList d'aventurier
     public Aventurier getAventurierClasse(String cl){
         for(Aventurier a:aventuriers) { //ligne
            if (a.getClass().getSimpleName().equalsIgnoreCase(cl)) {
               return a;
            }
         }
         System.out.println("Aventurier inexistant");
         return null;
    }

    public ArrayList<CarteTresors> getPileCarteTresors() {
        return pileCarteTresors;
    }

    public ArrayList<CarteTresors> getDefausseCarteTresors() {
        return defausseCarteTresors;
    }

    public ArrayList<CarteInnondations> getPileCarteInnondations() {
        return pileCarteInnondations;
    }

    public ArrayList<CarteInnondations> getDefausseCartesInnondations() {
        return defausseCartesInnondations;
    }

    public Plateau getPlateau() {
        return plateau;
    }
    
    //Retourne le trésor possédant ce nom dans l'arrayList de trésor
     public Tresor getTresorsNom(String nom) {
       for(Tresor t:tresors){
           if (t.getNom().equalsIgnoreCase(nom)){
               return t;
           }
       }
        System.out.println("tresor existe pas");
       return null;
    }
     
    private void setPileCarteInnondations(ArrayList<Tuile> desTuiles) {

        for (Tuile uneTuile : desTuiles){
            pileCarteInnondations.add(new CarteInnondations(uneTuile.getNom()));
        }
        melangeCartesInnondations();
    }

    public ArrayList<Tresor> getTresors() {
        return tresors;
    }
    
        /**Remarque : les setters ci-dessous INITIALISENT (+ MELANGE pour les piles) les défausses et piles de cartes.
     * Ces setters sont à utiliser qu'une seule fois : lors de l'initialisation du jeu. **/

    private void setPileCarteTresors() {

        pileCarteTresors.add(new CarteTresors("Helicoptere"));
        pileCarteTresors.add(new CarteTresors("Helicoptere"));
        pileCarteTresors.add(new CarteTresors("Helicoptere"));

        pileCarteTresors.add(new CarteTresors("SacsDeSable"));
        pileCarteTresors.add(new CarteTresors("SacsDeSable"));

        pileCarteTresors.add(new CarteTresors("MonteeDesEaux"));
        pileCarteTresors.add(new CarteTresors("MonteeDesEaux"));
        pileCarteTresors.add(new CarteTresors("MonteeDesEaux"));
        
        pileCarteTresors.add(new CarteTresors("Calice"));
        pileCarteTresors.add(new CarteTresors("Calice"));
        pileCarteTresors.add(new CarteTresors("Calice"));
        pileCarteTresors.add(new CarteTresors("Calice"));

        pileCarteTresors.add(new CarteTresors("Zephyr"));
        pileCarteTresors.add(new CarteTresors("Zephyr"));
        pileCarteTresors.add(new CarteTresors("Zephyr"));
        pileCarteTresors.add(new CarteTresors("Zephyr"));

        pileCarteTresors.add(new CarteTresors("Pierre"));
        pileCarteTresors.add(new CarteTresors("Pierre"));
        pileCarteTresors.add(new CarteTresors("Pierre"));
        pileCarteTresors.add(new CarteTresors("Pierre"));

        pileCarteTresors.add(new CarteTresors("Cristal"));
        pileCarteTresors.add(new CarteTresors("Cristal"));
        pileCarteTresors.add(new CarteTresors("Cristal"));
        pileCarteTresors.add(new CarteTresors("Cristal"));

        melangeCartesTresors();

    }

    public void setDefausseCarteTresors() {
        defausseCarteTresors= new ArrayList<>();
    }

    public void setDefausseCartesInnondations() {
        defausseCartesInnondations= new ArrayList<>();
    }

    public ArrayList<Aventurier> getAventuriers() {
        return aventuriers;
    }

    public void setAventuriers(ArrayList<Aventurier> aventuriers) {
        this.aventuriers = aventuriers;
    }

    public int getNiveauEau() {
        return niveauEau;
    }

    public int getCaseACouler() {
        return caseACouler;
    }
    
    
    public void setNiveauEau(int niveauEau) {
        this.niveauEau = niveauEau;
    }
    
    
}
