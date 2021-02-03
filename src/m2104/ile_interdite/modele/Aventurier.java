package m2104.ile_interdite.modele;

import java.awt.Color;
import m2104.ile_interdite.util.Utils;

import java.util.ArrayList;
import m2104.ile_interdite.util.Message;

//************************************************************************************************************
//                      Classe abstraite représentative des aventuriers du jeu
//*************************************************************************************************************
public abstract class Aventurier {
    
    //*********************************ATTRIBUTS************************************//
    
    protected IleInterdite ileInterdite;
    protected  Tuile tuileAct;
    protected  String nom;
    protected int nbActionRestante;
    protected ArrayList<CarteTresors> carteMain = new ArrayList<>();
    protected ArrayList<Tresor> tresors;
    protected Boolean Vivant=true;
    protected Color couleur;

    //*********************************CONSTRUCTEUR********************************//

    public Aventurier(String nom, Tuile tuile , IleInterdite ILEINTERDITE ) {
        this.nom = nom;
        this.tuileAct = tuile;
        this.tresors = new ArrayList<>();
        this.nbActionRestante =3;
        this.ileInterdite = ILEINTERDITE;
        piocherCarteTresors(2);

    }

    //*****************************METHODES*************************************//


    public void newTurn(){
        this.nbActionRestante=3;
    }


    
    //Deplace le joueur après vérification de la possibilité
    public void deplacer(String tuiledDest) {
        Tuile t = ileInterdite.getPlateau().getTuileNom(tuiledDest);
        //System.out.println(nbActionRestante + " action(s) restante(s) ");
        if (donnerCasesAdjacentes().contains(t)){
            this.tuileAct = t;
            finAction();
        } else {
            System.out.println("deplacement impossibles");
        }

    }
    
    //Methode permettant de placer le joueur sur nimporte quel tuile: appellé lors de l'utilisation d'une carte hélicoptère
    private void deplacerCarte(String tuiledDest,CarteTresors carte){
        Tuile t = ileInterdite.getPlateau().getTuileNom(tuiledDest);
        if(t.getEtatTuile()!=Utils.EtatTuile.COULEE){
            this.setTuileAct(t);
            ileInterdite.notifierObservateurs(Message.MiseAJour());
        }else{
            System.out.println("DeplacementCarteSpe: Mauvaise Carte ou tuile coule");
        }
    }
    
    //Methode permettant d'assécher nimporte quel tuile: appellé lors de l'utilisation d'une carte sacs de sable
    private void assecherCarte(String tuiledDest,CarteTresors carte){
        Tuile t = ileInterdite.getPlateau().getTuileNom(tuiledDest);
        if(t.getEtatTuile()==Utils.EtatTuile.INONDEE){
            t.setEtatTuile(Utils.EtatTuile.ASSECHEE);
            ileInterdite.notifierObservateurs(Message.MiseAJour());
        }else{
            System.out.println("Assechement Carte Spe: Mauvaise Carte ou tuile coule/asseche");
        }
    }


    //Les try catch servent dans le cas ou l'algorithme selectionne une case en dehors de la grille =>error
    //Cette méthode donne les cases adjacentes au personnage
    public ArrayList<Tuile> donnerCasesAdjacentes(){



        ArrayList<Tuile> tuilePossible = new ArrayList<>();

        Tuile[][] plateau = ileInterdite.getPlateau().getGrille();

        try{
            if((plateau[tuileAct.getPosX()+1][tuileAct.getPosY()]).getEtatTuile()!=Utils.EtatTuile.COULEE && (plateau[tuileAct.getPosX()+1][tuileAct.getPosY()])!=null){
                tuilePossible.add(plateau[tuileAct.getPosX()+1][tuileAct.getPosY()]);
            }

        }catch(Exception e){}
        try{
            if((plateau[tuileAct.getPosX()-1][tuileAct.getPosY()]).getEtatTuile()!=Utils.EtatTuile.COULEE && (plateau[tuileAct.getPosX()-1][tuileAct.getPosY()])!=null){
                tuilePossible.add(plateau[tuileAct.getPosX()-1][tuileAct.getPosY()]);
            }

        }catch(Exception e){}
        try{
            if((plateau[tuileAct.getPosX()][tuileAct.getPosY()-1]).getEtatTuile()!=Utils.EtatTuile.COULEE && (plateau[tuileAct.getPosX()][tuileAct.getPosY()-1])!=null){
                tuilePossible.add(plateau[tuileAct.getPosX()][tuileAct.getPosY()-1]);
            }
        }catch(Exception e){}
        try{
            if((plateau[tuileAct.getPosX()][tuileAct.getPosY()+1]).getEtatTuile()!=Utils.EtatTuile.COULEE && (plateau[tuileAct.getPosX()][tuileAct.getPosY()+1])!=null){
                tuilePossible.add(plateau[tuileAct.getPosX()][tuileAct.getPosY()+1]);
            }
        }catch(Exception e){}
        return tuilePossible;
    }

    //Cette méthode permet de faire piocher au joueur un nombre nb de carte Trésor
    public void piocherCarteTresors(int nb){
        for (int i =0;i<nb; i++){
            try{
                CarteTresors c = getIleInterdite().piocherCarteTresors();
                if (!c.getNom().equals("MonteeDesEaux")){
                    carteMain.add(c); }
            }catch(Exception e){
                System.out.println("La pile de carte est vide");
                this.ileInterdite.MelangeCarteTresorsDefausse();
            }

        }
        if (getCarteMain().size()>5){
            ileInterdite.notifierObservateurs((Message.laMainPleinne(getNom())));
        }
    }

    //Cette methode sert à donner une carte Trésor dans la main de l'aventurier à un autre aventurier dans le cas ou ils sont sur une même tuile
    public void donner(CarteTresors carte, Aventurier aventurier){

        if (this.tuileAct==aventurier.tuileAct) {
            aventurier.getCarteMain().add(carte);
            this.carteMain.remove(carte);
            finAction();
        }else{
            System.out.println("Trop éloigné de l'autre joueur");
        }

    }
    
    //Cette méthode est appellé lorqu'une carte spécial est utilisé et permet d'appliquer ses effets
    public void ActionCarteSpecial(String tuile,String nomCarte){
        CarteTresors carte = getCarteNom(nomCarte);
        if(nomCarte.equalsIgnoreCase("helicoptere")){
            deplacerCarte(tuile, carte);
            defausseCarteMain(carte);
        }else if(nomCarte.equalsIgnoreCase("sacsDeSable")){
            assecherCarte(tuile, carte);
            defausseCarteMain(carte);
        }
    }




    //Cette méthode permet de change l'état d'une tuile si les conditions sont remplis
    public void Assecher(String tuile){
            
        Tuile t = ileInterdite.getPlateau().getTuileNom(tuile);
        
        if(donnerCasesAdjacentes().contains(t)||  t== this.tuileAct) {
            if (t.getEtatTuile() == Utils.EtatTuile.INONDEE) {
                ileInterdite.getPlateau().getTuileNom(tuile).setEtatTuile(Utils.EtatTuile.ASSECHEE);
                finAction();
            } else if (t.getEtatTuile() == Utils.EtatTuile.ASSECHEE) {
                System.out.println("Vous ne pouvez assécher une tuile déja sèche");
            } else
                System.out.println("Vous ne pouvez assécher une tuile qui a coulé");
        }else
            System.out.println("Impossible d'atteindre la case selectionné");
    }
    
    //Cette méthode sert à faire gagner un trésor au joueur si les conditions sont remplis
    public void addTresor(){
        String nm;
        if(!this.getTuileAct().getTresor().equalsIgnoreCase("none")){
            nm = this.getTuileAct().getTresor().toLowerCase();
            int temp=0;
            for (CarteTresors c:carteMain){
                if(c.getNom().equalsIgnoreCase(nm)){
                    temp++;
                }
            }
            if(temp==4){
                Tresor t = getIleInterdite().getTresorsNom(nm);
                this.tresors.add(t);
                
                getIleInterdite().getTresors().remove(t);
                
                //Détruit des 4 cartes Trésors que le joueur possedait
                for (int i = carteMain.size()-1; i>=0;i--){
                    if(carteMain.get(i).getNom().equalsIgnoreCase(nm)){
                        carteMain.remove(i);
                    }
                }
                
                finAction();
            }else{
                System.out.println("dommage tu ne peux pas, recommence ! Dans la joie et la bonne humeur");
            }
        }
    }

    //Cette methode permet de défausser une carte choisie de la main du joueur

    public void defausseCarteMain(CarteTresors c){
        if (getCarteMain().contains(c)){
            getCarteMain().remove(c);
            getIleInterdite().defausserCarteTresors(c);
        }
        else{
            System.out.println("La carte n'existe pas.");
        }

    }
    
    //Cette méthode permet de faire avancer le compteur d'action du joueur et de notifier au controleur quand le tour est finit
    public void finAction(){
        nbActionRestante-=1;
        if(ileInterdite.checkVictoire()){
            ileInterdite.notifierObservateurs(Message.finPartie(0));
        }
        if(nbActionRestante == 0){

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
        ileInterdite.notifierObservateurs(Message.MiseAJour());

    }
    
    //**************************************GETTERS-SETTERS*********************************************//
    
    //retourne l'aventurier qui suit dans l'arrayList de ileInterdite
    public Aventurier getAventurierSuivant(){
        for(int i =0; i<ileInterdite.getAventuriers().size();i++){
            if(ileInterdite.getAventuriers().get(i)==this && i!=ileInterdite.getAventuriers().size()-1)
                return ileInterdite.getAventuriers().get(i+1);
        }
        return ileInterdite.getAventuriers().get(0);
    }
    
    public Boolean estVivant() {
        return Vivant;
    }

    public void seNoit() {
        Vivant = false;
    }

    public int getNbActionRestante() {
        return nbActionRestante;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }


    public void setTuileAct(Tuile tuileAct) {
        this.tuileAct = tuileAct;
    }

    public Tuile getTuileAct() {
        return tuileAct;
    }

    public ArrayList<CarteTresors> getCarteMain() {
        return carteMain;
    }

    public IleInterdite getIleInterdite() {
        return ileInterdite;
    }
    
    //return la première carte de la main ayant ce nom
    public CarteTresors getCarteNom(String carteNom){
        for(int i =0; i<carteMain.size();i++){
            if (carteMain.get(i).getNom().equalsIgnoreCase(carteNom)){
                return carteMain.get(i);
            }
                
        }
        return null;
    }

    public Color getCouleur() {
        return couleur;
    }

    public void setCouleur(Color couleur) {
        this.couleur = couleur;
    }

    public ArrayList<Tresor> getTresors() {
        return tresors;
    }

    public void setNbActionRestante(int nbActionRestante) {
        this.nbActionRestante = nbActionRestante;
    }
}
