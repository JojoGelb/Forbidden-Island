/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m2104.ile_interdite.vue;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import javax.swing.*;

import gfx.ImageLoader;
import javax.swing.border.LineBorder;
import m2104.ile_interdite.modele.Aventurier;
import m2104.ile_interdite.modele.CarteTresors;
import m2104.ile_interdite.modele.Tresor;
import m2104.ile_interdite.modele.Tuile;
import m2104.ile_interdite.util.Message;
import m2104.ile_interdite.util.Utils;


//************************************************************************************************************
//                          JPANEL affichant la partie en cours
//*************************************************************************************************************

public class VuePlateau extends JPanel implements MouseListener {
    
    //**************************************Attributs***************************************//
    
    private final IHM ihm;
    
    //Variables stockant les choix du joueur
    private String caseSelectionne;
    private String AventurierSelectionne;
    private String carteSelectionne;
    private Aventurier AventurierEnCoursDeJeu, temp;
    private Message messageEnPreparation;
    
    //Boolean d'action et de selection d'élement à l'ecran
    private boolean plateauSelectionnable=false,aventurierSelectionnable = false,carteSelecionable = false,surligneEnAttente=false,defausseCartes=false; 
    private boolean actionDeplacement = false,actionAssecher = false,actionDonner, urgenceDefausse=false, actionCarte=false, urgenceDeplacement=false;  

    //Valeurs utiles
    private Tuile[][] grille;
    private int nivEau =1;
    private Color col = new Color(35,35,104);   //Couleur bleu des textes
    
    //Variables stockants les actions possibles du joueur
    private  ArrayList<Tuile> tuilesViables = new ArrayList<>();
    private ArrayList<Aventurier> aventuriersViables = new ArrayList<>();
    private ArrayList<String> cartesJouable = new ArrayList<>();
    private  ArrayList<String> carteDefaussable =new ArrayList<>();
    private ArrayList<String> aventuriersMainPleinne; //Arraylist d'aventurier ayant la main pleine et necessitant une defausse
    private ArrayList<String> aventurierSeNoyant; //Arraylist contenant les aventuriers se noyant et necessitant un deplacement
    
    //Affichage écran
    private HashMap<String,ImageLoader> tuilesPresentesSurPlateau =new HashMap<>();
    private HashMap<String,ImageLoader> AventuriersPresents =new HashMap<>();
    private HashMap<String,ImageLoader> pionsPresents = new HashMap<>();

    //Graphique du plateau

    private ImageLoader logo,fond,NiveauEau,indicateurNivEau,fondh;
    private JLabel labelAv0,labelAv1,labelAv2,labelAv3,labelMainJoueurActuel,labelJoueurActuel, labelTresorObtenues;
    private JTextArea labelActionEnPreparation; //j'ai pris ça pour pouvoir sauter des lignes facilement sans avoir à utiliser html
    private JButton boutonDeplacer,boutonAssecher,boutonDonner,boutonRecupererTresor,boutonValider,boutonCartePouvoir,boutonPasserTour;
    

    //*************************************************Constructeur*********************************************//
    
    
    public VuePlateau(IHM ihm){
        

        this.setLayout(null);

        this.ihm = ihm;
        this.setSize(1800, 1000);
        logo = new ImageLoader((System.getProperty("user.dir")) + "/ressources/icones/logo.png",0,0, (int) (getWidth()*0.35), (int)(getHeight()*0.2));
        fond = new ImageLoader((System.getProperty("user.dir"))+ "/ressources/FontMap.jpg",0,0,getWidth(), (int)(getHeight()));
        fondh = new ImageLoader((System.getProperty("user.dir")) + "/ressources/fondh.png",0,0, (int) (getWidth()), (int)(getHeight()));

        NiveauEau = new ImageLoader((System.getProperty("user.dir"))+ "/ressources/Niveau.png",0,200,150, 500);
        indicateurNivEau = new ImageLoader((System.getProperty("user.dir"))+ "/ressources/stick.png",0,0,0,0);
        aventuriersMainPleinne= new ArrayList<>();
        aventurierSeNoyant= new ArrayList<>();
        labelMainJoueurActuel = new JLabel("CARTE EN MAIN DU JOUEUR ACTUEL");

        this.add(labelMainJoueurActuel);
        Font font = new Font("Arial",Font.BOLD,24);
        labelMainJoueurActuel.setFont(font);
        Dimension size = labelMainJoueurActuel.getPreferredSize(); 
        labelMainJoueurActuel.setBounds(690, 740, size.width, size.height); 
        labelMainJoueurActuel.setForeground(col);
        
        labelJoueurActuel = new JLabel();
        this.add(labelJoueurActuel);
        labelJoueurActuel.setFont(font);
        labelJoueurActuel.setForeground(col);
        
        labelActionEnPreparation = new JTextArea("Veuillez selectionner une action à réaliser");
        this.add(labelActionEnPreparation);
        labelActionEnPreparation.setOpaque(true);
        labelActionEnPreparation.setEditable(false);
        labelActionEnPreparation.setBackground(Color.WHITE);
        labelActionEnPreparation.setBounds(180, 630, 400, 50);
        
        labelTresorObtenues = new JLabel("Tresor en possession de l'équipe");
        this.add(labelTresorObtenues);
        labelTresorObtenues.setFont(font);
        Dimension si = labelTresorObtenues.getPreferredSize(); 
        labelTresorObtenues.setBounds(10, 760, si.width, si.height); 
        labelTresorObtenues.setForeground(col);
        
        
        creationBouton();
        
        this.addMouseListener(this);


    }


    //*************************************************METHODES************************************************//
    
    //Methode d'affichage principal
    @Override
    public void paintComponent(Graphics g){
        super.paintComponents(g);
        
        
        
        fond.paintComponent(g);
        logo.paintComponent(g);
        
        affichageAventurier(g);

        affichageNiveauEau(g);
        affichageTuiles(g);

        affichageTresor(g);
        if (urgenceDeplacement){
            g.setColor(Color.red);
            enCasdUrgenceNoyade();
            fondh.paintComponent(g);

        }else{
            boutonAssecher.setEnabled(true);
            boutonDonner.setEnabled(true);
            boutonCartePouvoir.setEnabled(true);
            boutonRecupererTresor.setEnabled(true);
            boutonPasserTour.setEnabled(true);
            
            if (urgenceDefausse){
                defausse();
            }
        }
        affichageAventurierCourant(g);



        miseEnSurbrillance(g);
    }
    
    //gere la defausse au début du tour des joueurs
    public void defausse(){ 
        boutonValider.setEnabled(false);
        boutonAssecher.setEnabled(false);
        boutonDeplacer.setEnabled(false);
        boutonDonner.setEnabled(false);
        boutonCartePouvoir.setEnabled(false);
        boutonRecupererTresor.setEnabled(false);
        boutonPasserTour.setEnabled(false);

        carteSelecionable=true;
        int a =(AventurierEnCoursDeJeu.getCarteMain().size()-carteDefaussable.size()-5);
        if (a<0){a=0;}
        labelActionEnPreparation.setText("Choissisez "+ a + " à defausser, selectionner en une de plus pour annuler");

        if (carteDefaussable.size()>AventurierEnCoursDeJeu.getCarteMain().size()-5){
            carteDefaussable.clear();
        }

        if (carteDefaussable.size()==AventurierEnCoursDeJeu.getCarteMain().size()-5){
            boutonValider.setEnabled(true);
        }

        
        if (AventurierEnCoursDeJeu.getCarteMain().size()<=5){
            urgenceDefausse=false;
            carteSelecionable=false;
            boutonValider.setEnabled(true);
            boutonAssecher.setEnabled(true);
            boutonDeplacer.setEnabled(true);
            boutonDonner.setEnabled(true);
            boutonCartePouvoir.setEnabled(true);
            boutonRecupererTresor.setEnabled(true);
            boutonPasserTour.setEnabled(true);


        }
    }
    
    //gere le deplacement en cas d'innondation d'une tuile sous les pieds de joueurs
    public void enCasdUrgenceNoyade(){
        boutonAssecher.setEnabled(false);
        boutonDonner.setEnabled(false);
        boutonRecupererTresor.setEnabled(false);
        boutonPasserTour.setEnabled(false);



        for (Aventurier a:ihm.getAventuriers()) {
            if ((a.getNom().equalsIgnoreCase(aventurierSeNoyant.get(aventurierSeNoyant.size()-1)))&&AventurierEnCoursDeJeu!=a){
                temp = AventurierEnCoursDeJeu;
                    AventurierEnCoursDeJeu=a;
                    labelActionEnPreparation.setText("Des aventuriers se noient Choisissez une case ou aller");
            }
        }
        if (AventurierEnCoursDeJeu.getTuileAct().getEtatTuile()!= Utils.EtatTuile.COULEE){
            urgenceDeplacement=false;
            boutonAssecher.setEnabled(true);
            boutonDonner.setEnabled(true);
            boutonRecupererTresor.setEnabled(true);
            boutonPasserTour.setEnabled(true);
            boutonCartePouvoir.setEnabled(true);
            AventurierEnCoursDeJeu = temp;
            repaint();
            
        }
        for (CarteTresors c:AventurierEnCoursDeJeu.getCarteMain()){
            if (c.getNom().equalsIgnoreCase("Helicoptere")){
            }else{
                boutonCartePouvoir.setEnabled(false);
            }
        }
        if (!AventurierEnCoursDeJeu.getClass().getSimpleName().equalsIgnoreCase("pilote")
                &&!AventurierEnCoursDeJeu.getClass().getSimpleName().equalsIgnoreCase("plongeur") && boutonCartePouvoir.isEnabled() ){
            if (AventurierEnCoursDeJeu.donnerCasesAdjacentes().isEmpty()){
                ihm.notifierObservateurs(Message.finPartie(3));
            }
        }

    }
    
    //Gere l'affichage des trésors récupéré par l'équipe
    public void affichageTresor(Graphics g){
        
        for(Aventurier av: ihm.getAventuriers()){
            for(Tresor t: av.getTresors()){
                for(String s: ihm.getAssets().getImgTresors().keySet()){
                    String temp = s.toLowerCase();
                    if(temp.equalsIgnoreCase(t.getNom())){
                        switch(temp){
                            case "pierre":
                                ihm.getAssets().getImgTresors().get(s).parametrer(30, 800, 70, 140);
                                ihm.getAssets().getImgTresors().get(s).paintComponent(g);
                                break;
                            case "calice":
                                ihm.getAssets().getImgTresors().get(s).parametrer(110, 800, 70, 140);
                                ihm.getAssets().getImgTresors().get(s).paintComponent(g);
                                break;
                            case "zephyr":
                                ihm.getAssets().getImgTresors().get(s).parametrer(190, 800, 70, 140);
                                ihm.getAssets().getImgTresors().get(s).paintComponent(g);
                                break;
                            case "cristal":
                                ihm.getAssets().getImgTresors().get(s).parametrer(270, 800, 70, 140);
                                ihm.getAssets().getImgTresors().get(s).paintComponent(g);
                                break;
                        }

                    }
                }
                
            }
        }

    }
    
    
    //affiche les tuiles du plateau à l'ecran et instancie la position des pions dessus
    public void affichageTuiles(Graphics g){

        if(grille != null && tuilesPresentesSurPlateau.isEmpty()){
            String temp = new String();

            for(String s: ihm.getAssets().getImgtuiles().keySet()){
                temp = s;
                for(int i =0;i<6; i++) { //ligne
                    for (int j = 0; j < 6; j++) { // colonne
                        if(grille[i][j]!= null && temp.equalsIgnoreCase(grille[i][j].getNom())){
                            if(grille[i][j].getEtatTuile() != Utils.EtatTuile.COULEE){
                                for (Aventurier a:ihm.getAventuriers()){
                                    if(a.getTuileAct().getNom().equalsIgnoreCase(temp)){
                                        pionsPresents.get(a.getClass().getSimpleName().toLowerCase()).setDimension(50,50);
                                        pionsPresents.get(a.getClass().getSimpleName().toLowerCase()).setPosition((550+i*120),20+j*120);
                                    }
                                }
                                for(int b =0; b<ihm.getAventuriers().size();b++){
                                    if(pionsPresents.get(ihm.getAventuriers().get(b).getClass().getSimpleName().toLowerCase()).getX()==pionsPresents.get(ihm.getAventuriers().get(0).getClass().getSimpleName().toLowerCase()).getX()&& pionsPresents.get(ihm.getAventuriers().get(b).getClass().getSimpleName().toLowerCase()).getY()==pionsPresents.get(ihm.getAventuriers().get(0).getClass().getSimpleName().toLowerCase()).getY())
                                        pionsPresents.get(ihm.getAventuriers().get(b).getClass().getSimpleName().toLowerCase()).setPosition(pionsPresents.get(ihm.getAventuriers().get(0).getClass().getSimpleName().toLowerCase()).getX()+b*10, pionsPresents.get(ihm.getAventuriers().get(0).getClass().getSimpleName().toLowerCase()).getY());
                                }
                                if(grille[i][j].getEtatTuile() == Utils.EtatTuile.INONDEE) {
                                    temp = temp + "_Inonde";
                                }

                                ihm.getAssets().getImgtuiles().get(temp).setDimension(120,120);
                                ihm.getAssets().getImgtuiles().get(temp).setPosition((int) (540+i*120),10+j*120);

                                tuilesPresentesSurPlateau.put(temp,ihm.getAssets().getImgtuiles().get(temp));

                                

                            }else {
                                ihm.getAssets().getImgtuiles().get(temp).setPosition((int) (540+i*120),10+j*120);
                            }
                        }
                    }
                }
            }

        }

        if(!tuilesPresentesSurPlateau.isEmpty()){
            for(String s: tuilesPresentesSurPlateau.keySet()){
                tuilesPresentesSurPlateau.get(s).paintComponent(g);
            }
        }
        
        if(!pionsPresents.isEmpty()){
            for(String s: this.pionsPresents.keySet()){
                pionsPresents.get(s).paintComponent(g);
            }
        }
    }

    //Affiche le niveau d'eau
    public void affichageNiveauEau(Graphics g){
        NiveauEau.paintComponent(g);
        g.setColor(Color.red);
        switch (nivEau) {
            case 1:
                indicateurNivEau.parametrer(60, 605, 40, 20);
                indicateurNivEau.paintComponent(g);
                break;
            case 2:
                indicateurNivEau.parametrer(60, 560, 40, 20);
                indicateurNivEau.paintComponent(g);
                break;
            case 3:
                indicateurNivEau.parametrer(60, 518, 40, 20);
                indicateurNivEau.paintComponent(g);
                break;
            case 4:
                indicateurNivEau.parametrer(60, 478, 40, 20);
                indicateurNivEau.paintComponent(g);
                break;
            case 5:
                indicateurNivEau.parametrer(60, 435, 40, 20);
                indicateurNivEau.paintComponent(g);
                break;
            case 6:
                indicateurNivEau.parametrer(60, 393, 40, 20);
                indicateurNivEau.paintComponent(g);
                break;
            case 7:
                indicateurNivEau.parametrer(60, 353, 40, 20);
                indicateurNivEau.paintComponent(g);
                break;
            case 8:
                indicateurNivEau.parametrer(60, 313, 40, 20);
                indicateurNivEau.paintComponent(g);
                break;
            case 9:
                indicateurNivEau.parametrer(60, 273, 40, 20);
                indicateurNivEau.paintComponent(g);
                break;
            case 10:
                indicateurNivEau.parametrer(60, 233, 40, 20);
                indicateurNivEau.paintComponent(g);
                break;
            default:
                indicateurNivEau.parametrer(60, 233, 40, 20);
                indicateurNivEau.paintComponent(g);
                break;
        }
    }

    //*********************AFFICHAGE DES AVENTURIERS*******************************////
    
    //Affiche l'aventurier en cour de jeu
    public void affichageAventurierCourant(Graphics g){
        Graphics2D g1 = (Graphics2D) g;
        BasicStroke line = new BasicStroke(8.0f);
        g1.setStroke(line);
        g.setColor(Color.getHSBColor((float) 0.12, (float) 0.2, (float) 0.16));
        if(AventurierEnCoursDeJeu!= null){
            for(int i =0;i<AventurierEnCoursDeJeu.getCarteMain().size();i++){
                Set cles = ihm.getAssets().getImgCarteTresors().keySet();
                for (Object cle : cles) {
                    String temp = cle.toString();

                    if (AventurierEnCoursDeJeu.getCarteMain().get(i)!=null && AventurierEnCoursDeJeu.getCarteMain().get(i).getNom().equalsIgnoreCase(temp)) {

                        ihm.getAssets().getImgCarteTresors().get(temp).parametrer(500 + i * 150, 780, 110, 170);
                        ihm.getAssets().getImgCarteTresors().get(temp).paintComponent(g);
                        g.drawRoundRect(500 + i * 150, 780, 111, 171, 5, 5);

                    }

                }
            }


    //Stockage de varibales : a revoir mais passe pour corriger le bug des affichages ==>
            int x,y,width,height;
            x=ihm.getAssets().getImgPersonnages().get(this.AventurierEnCoursDeJeu.getClass().getSimpleName().toLowerCase()).getX();
            y=ihm.getAssets().getImgPersonnages().get(this.AventurierEnCoursDeJeu.getClass().getSimpleName().toLowerCase()).getY();
            width=ihm.getAssets().getImgPersonnages().get(this.AventurierEnCoursDeJeu.getClass().getSimpleName().toLowerCase()).getWidth();
            height=ihm.getAssets().getImgPersonnages().get(this.AventurierEnCoursDeJeu.getClass().getSimpleName().toLowerCase()).getHeight();
            //<==
            ihm.getAssets().getImgPersonnages().get(this.AventurierEnCoursDeJeu.getClass().getSimpleName().toLowerCase()).parametrer(250, 250, 150, 200);
            ihm.getAssets().getImgPersonnages().get(this.AventurierEnCoursDeJeu.getClass().getSimpleName().toLowerCase()).paintComponent(g);
            ihm.getAssets().getImgPersonnages().get(this.AventurierEnCoursDeJeu.getClass().getSimpleName().toLowerCase()).parametrer(x, y, width, height); //<==


            g.drawRoundRect(249, 249, 152, 202,5,5);
            
            labelJoueurActuel.setText("Tour de :"+ this.AventurierEnCoursDeJeu.getNom());
            labelJoueurActuel.setForeground(col);
            Dimension size = labelJoueurActuel.getPreferredSize();
            labelJoueurActuel.setBounds(225, 200, size.width, size.height); 
            
            
        }
    }

    
    //Affiche tous les aventuriers à droite de l'ecran selon getAventurier de ihm
    public void affichageAventurier(Graphics g){ 
        
        if(ihm.getAventuriers()!=null){
            if(AventuriersPresents.isEmpty()){
                for(int i =0; i < ihm.getAventuriers().size();i++){
                    ihm.getAssets().getImgPersonnages().get(ihm.getAventuriers().get(i).getClass().getSimpleName().toLowerCase()).parametrer(1570, 30 + i*240, 150, 200);
                    AventuriersPresents.put(ihm.getAventuriers().get(i).getClass().getSimpleName().toLowerCase(),ihm.getAssets().getImgPersonnages().get(ihm.getAventuriers().get(i).getClass().getSimpleName().toLowerCase()));
                
                        switch(i){
                            case 0:
                                labelAv0 = new JLabel(ihm.getAventuriers().get(i).getNom());
                                this.add(labelAv0);
                                Dimension size = labelAv0.getPreferredSize(); 
                                labelAv0.setBounds(1570, 10, size.width, size.height); 
                                //labelAv0.setFont(new Font(labelAv0.getFont().getName(),Font.PLAIN,labelAv0.getFont().getSize()*3));
                                labelAv0.setForeground(col);
                            
                                break;
                            case 1:
                                labelAv1 = new JLabel(ihm.getAventuriers().get(i).getNom());
                                this.add(labelAv1);
                                Dimension size1 = labelAv1.getPreferredSize(); 
                                labelAv1.setBounds(1570, 10 + 240, size1.width, size1.height); 
                                labelAv1.setForeground(col);
                                break;
                            case 2:
                                labelAv2 = new JLabel(ihm.getAventuriers().get(i).getNom());
                                this.add(labelAv2);
                                Dimension size2 = labelAv2.getPreferredSize(); 
                                labelAv2.setBounds(1570, 10 + 480, size2.width, size2.height); 
                                labelAv2.setForeground(col);
                                break;
                            case 3:
                                labelAv3 = new JLabel(ihm.getAventuriers().get(i).getNom());
                                this.add(labelAv3);
                                Dimension size3 = labelAv3.getPreferredSize(); 
                                labelAv3.setBounds(1570, 10 + 720, size3.width, size3.height); 
                                labelAv3.setForeground(col);
                                break;
                        }
                
               
                }
            }
            if(pionsPresents.isEmpty()){
                for(String s: this.AventuriersPresents.keySet()){ 
                    pionsPresents.put(s.toLowerCase(),ihm.getAssets().getImgPions().get(s));
                }
            }
            for(String s: this.AventuriersPresents.keySet()){ 
                ihm.getAssets().getImgPersonnages().get(s).paintComponent(g);
            }
        }
    }

    
    //Affiche à l'écran les actions qu'entreprends le joueur courant et ses possibilités
    public void miseEnSurbrillance(Graphics g){

        Graphics2D g1 = (Graphics2D) g;

        BasicStroke line = new BasicStroke(8.0f);
        g1.setStroke(line);
        g.setColor(Color.getHSBColor((float) 0.21, (float) 0.6, (float) 0.46)); // vert
        if (surligneEnAttente){
            g.setColor(Color.getHSBColor((float) 0.70,1,1));         //BLEU
        }
        if (!tuilesViables.isEmpty()){
            for (Tuile t:tuilesViables){ 
                if(t.getEtatTuile()==Utils.EtatTuile.ASSECHEE){
                    g.drawRoundRect(ihm.getAssets().getImgtuiles().get(t.getNom()).getX(),
                            ihm.getAssets().getImgtuiles().get(t.getNom()).getY(),
                            ihm.getAssets().getImgtuiles().get(t.getNom()).getWidth(),
                            ihm.getAssets().getImgtuiles().get(t.getNom()).getHeight(),5,5);
                }else if(t.getEtatTuile()==Utils.EtatTuile.INONDEE){
                    g.drawRoundRect(ihm.getAssets().getImgtuiles().get(t.getNom()+"_Inonde").getX(),
                            ihm.getAssets().getImgtuiles().get(t.getNom()+"_Inonde").getY(),
                            ihm.getAssets().getImgtuiles().get(t.getNom()+"_Inonde").getWidth(),
                            ihm.getAssets().getImgtuiles().get(t.getNom()+"_Inonde").getHeight(),5,5);
                }
            }
        }
        if(!cartesJouable.isEmpty()||urgenceDefausse){
            if(carteSelectionne!=null || urgenceDefausse){
                g.setColor(Color.getHSBColor((float) 0.70,1,1));         //BLEU
            }else{
                g.setColor(Color.getHSBColor((float) 0.21, (float) 0.6, (float) 0.46));
            }
            if (urgenceDefausse){
                for (int j = 0; j < AventurierEnCoursDeJeu.getCarteMain().size(); j++) {
                    for (String s : carteDefaussable) {
                        if (s.equalsIgnoreCase(AventurierEnCoursDeJeu.getCarteMain().get(j).getNom())) {
                            g.drawRect(500 + j * 150, 780, 110, 170);
                        }
                    }
                }
            }else if(actionCarte){
                for (int j = 0; j < AventurierEnCoursDeJeu.getCarteMain().size(); j++) {
                    for (String s : cartesJouable) {
                        if (((s.equalsIgnoreCase("Helicoptere") || s.equalsIgnoreCase("sacsDeSable"))) && s.equalsIgnoreCase(AventurierEnCoursDeJeu.getCarteMain().get(j).getNom())) { //Si pas heli et sac et ==carteMain
                            g.drawRect(500 + j * 150, 780, 110, 170);
                            
                        }
                    }
                }
            } else {
                for (int j = 0; j < AventurierEnCoursDeJeu.getCarteMain().size(); j++) {
                    for (String s : cartesJouable) {
                        if ((!(s.equalsIgnoreCase("Helicoptere") || s.equalsIgnoreCase("sacsDeSable"))) && s.equalsIgnoreCase(AventurierEnCoursDeJeu.getCarteMain().get(j).getNom())) { //Si pas heli et sac et ==carteMain
                            g.drawRect(500 + j * 150, 780, 110, 170);
                        }
                    }
                }
            }
        }
        if(!aventuriersViables.isEmpty()){
            if(AventurierSelectionne!=null){
                g.setColor(Color.getHSBColor((float) 0.70,1,1));         //BLEU
            }else{
                g.setColor(Color.getHSBColor((float) 0.21, (float) 0.6, (float) 0.46)); //vert
            }
            for(int i =0; i<ihm.getAventuriers().size(); i++){
                for(Aventurier av: aventuriersViables){
                    if(av==ihm.getAventuriers().get(i) && av!=AventurierEnCoursDeJeu){
                        g.drawRect(1570, 30+i*240, 150, 200);
                    }
                }
            }
        }
        surligneEnAttente=false;
    }

    //Affiche le contenu du textArea pour indiquer au joueur ce qu'ils font
    public void affichageTextPreparationAction(){
        if(messageEnPreparation!= null){
            if(messageEnPreparation.getCommande()==Utils.Commandes.BOUGER){
                labelActionEnPreparation.setText("L'aventurier " + AventurierEnCoursDeJeu.getClass().getSimpleName()+ " se deplace sur la case " + caseSelectionne);
                //tuilesViables.addAll(AventurierEnCoursDeJeu.donnerCasesAdjacentes());
            }else if(messageEnPreparation.getCommande()==Utils.Commandes.ASSECHER){
                labelActionEnPreparation.setText("L'aventurier " + AventurierEnCoursDeJeu.getClass().getSimpleName()+ " asseche la case " + caseSelectionne);
            }else if(messageEnPreparation.getCommande()==Utils.Commandes.DONNER){
                labelActionEnPreparation.setText("L'aventurier " + AventurierEnCoursDeJeu.getClass().getSimpleName()+ " donne la carte " + carteSelectionne + " a " + AventurierSelectionne);
            }else if(messageEnPreparation.getCommande()==Utils.Commandes.RECUPERER_TRESOR){
                labelActionEnPreparation.setText("L'aventurier " + AventurierEnCoursDeJeu.getClass().getSimpleName()+ " récupère un trésor ");
            }
        }else{
            String mess = "";
            if(AventurierSelectionne!=null)
                mess = "Aventurier selectionné : " + AventurierSelectionne;
            if( carteSelectionne!=null)
                mess = mess + "\nCarte selectionné: " + carteSelectionne;
            if(caseSelectionne !=null)
                mess = mess + "\nTuile selectionné: " + caseSelectionne;
            
             labelActionEnPreparation.setText(mess);
        }
        this.repaint();
        
    }
    
    //Met à jour la grille et repeint la scène
    public void MajGrille(Tuile[][] grille){
        tuilesPresentesSurPlateau.clear();
        this.grille = grille;
        this.repaint();
    }
    
    //créé les boutons et gère les actions liées
    private void creationBouton() {
        boutonDeplacer = new JButton("Deplacer");
        this.add(boutonDeplacer);
        Font font = new Font("Arial", Font.BOLD, 13);
        boutonDeplacer.setFont(font);
        boutonDeplacer.setBounds(220, 500, 100, 50);
        boutonDeplacer.setBorder(new LineBorder(Color.BLACK));
        boutonDeplacer.addActionListener(e -> {

            if (AventurierEnCoursDeJeu != null) {
                messageEnPreparation = null;
                plateauSelectionnable = true;
                carteSelecionable = false;
                aventurierSelectionnable = false;

                tuilesViables.clear();
                tuilesViables.addAll(AventurierEnCoursDeJeu.donnerCasesAdjacentes());
                aventuriersViables.clear();
                cartesJouable.clear();
                actionDeplacement = true;
                actionAssecher = false;
                actionDonner = false;
                labelActionEnPreparation.setText("Deplacement: Cliquer sur une tuile");
                repaint();
            }
        });


        boutonAssecher = new JButton("Assecher");
        this.add(boutonAssecher);
        boutonAssecher.setBounds(320, 500, 100, 50);
        boutonAssecher.setBorder(new LineBorder(Color.BLACK));
        boutonAssecher.addActionListener(e -> {
            if (AventurierEnCoursDeJeu != null) {
                messageEnPreparation = null;
                tuilesViables.clear();
                aventurierSelectionnable = false;
                carteSelecionable = false;
                // tuilesViables.addAll(AventurierEnCoursDeJeu.donnerCasesAdjacentes());
                for (Tuile t : AventurierEnCoursDeJeu.donnerCasesAdjacentes()) {
                    if (t.getEtatTuile() == Utils.EtatTuile.INONDEE) {
                        tuilesViables.add(t);
                    }
                }
                if (AventurierEnCoursDeJeu.getTuileAct().getEtatTuile() == Utils.EtatTuile.INONDEE) {
                    tuilesViables.add(AventurierEnCoursDeJeu.getTuileAct());
                }
                if (!tuilesViables.isEmpty()) {
                    actionAssecher = true;
                    actionDeplacement = false;
                    actionDonner = false;
                    labelActionEnPreparation.setText("Assechement: Cliquer sur une tuile");
                    repaint();
                    plateauSelectionnable = true;
                } else {
                    labelActionEnPreparation.setText("Rien à assecher");
                    repaint();
                }
            }
        });


        boutonDonner = new JButton("Donner");
        this.add(boutonDonner);
        boutonDonner.setBounds(220, 550, 100, 50);
        boutonDonner.setBorder(new LineBorder(Color.BLACK));
        boutonDonner.addActionListener(e -> {
            if (AventurierEnCoursDeJeu != null) { 
                messageEnPreparation = null;
                plateauSelectionnable = false;
                tuilesViables.clear();
                aventuriersViables.clear();
                if (AventurierEnCoursDeJeu.getClass().getSimpleName().equalsIgnoreCase("messager")) {
                    aventuriersViables.addAll(ihm.getAventuriers());
                } else {
                    for (Aventurier av : ihm.getAventuriers()) {
                        if (av.getTuileAct() == AventurierEnCoursDeJeu.getTuileAct()) {
                            aventuriersViables.add(av);
                        }
                    }
                }
                for (CarteTresors c : AventurierEnCoursDeJeu.getCarteMain()) {
                    if (!(c.getNom().equalsIgnoreCase("Helicoptere") || c.getNom().equalsIgnoreCase("sacDeSable"))) {
                        cartesJouable.add(c.getNom());
                    }
                }

                if (!aventuriersViables.isEmpty() && !cartesJouable.isEmpty()) {
                    aventurierSelectionnable = true;
                    carteSelecionable = true;
                    actionAssecher = false;
                    actionDeplacement = false;
                    actionDonner = true;
                    labelActionEnPreparation.setText("Don: selectionner le joueur et la carte à donner");
                    repaint();
                }

            }
        });

        boutonRecupererTresor = new JButton("<html>Gagner" + "<br><center>" + "Trésor</html>");
        this.add(boutonRecupererTresor);
        boutonRecupererTresor.setBounds(320, 550, 100, 50);
        boutonRecupererTresor.setBorder(new LineBorder(Color.BLACK));
        boutonRecupererTresor.addActionListener(e -> {
            if (AventurierEnCoursDeJeu != null) {

                messageEnPreparation = Message.recupererTresor(AventurierEnCoursDeJeu.getNom());
                affichageTextPreparationAction();
            }
        });


        boutonValider = new JButton("Valider Action");
        this.add(boutonValider);
        boutonValider.setBounds(270, 700, 100, 50);
        boutonValider.setBorder(new LineBorder(Color.BLACK));
        boutonValider.addActionListener(e -> {

            if (messageEnPreparation != null) {

                ihm.notifierObservateurs(messageEnPreparation);

                //Retour à 0
                cartesJouable.clear();
                tuilesViables.clear();
                aventuriersViables.clear();
                AventurierSelectionne = null;
                caseSelectionne = null;
                carteSelectionne = null;
                labelActionEnPreparation.setText("En attente d'une nouvelle action \n" + (AventurierEnCoursDeJeu.getNbActionRestante()) + " actions restantes");
                actionDeplacement = false;
                actionAssecher = false;
                actionDonner = false;
                actionCarte = false;
                plateauSelectionnable = false;


            }
            if (urgenceDefausse) {
                for (String c : carteDefaussable) {
                    messageEnPreparation = Message.defausser(AventurierEnCoursDeJeu.getNom().toLowerCase(), c);
                    ihm.notifierObservateurs(messageEnPreparation);

                }
                carteDefaussable.clear();

            }
            messageEnPreparation = null;
            repaint();
        });

        boutonCartePouvoir = new JButton("<html>Jouer Carte" + "<br><center>" + "Spécial</html>");
        this.add(boutonCartePouvoir);
        boutonCartePouvoir.setBounds(420, 550, 100, 50);
        boutonCartePouvoir.setBorder(new LineBorder(Color.BLACK));
        boutonCartePouvoir.addActionListener(e -> {
            // boutonPower.setFont(new Font("Arial",Font.BOLD,14));
            if (AventurierEnCoursDeJeu != null) {
                //On nettoye les selections
                AventurierSelectionne = null;
                caseSelectionne = null;
                carteSelectionne = null;
                //On autorise d'abord les zones de selection
                messageEnPreparation = null;
                aventurierSelectionnable = false;
                carteSelecionable = true;
                plateauSelectionnable = true;
                //On nettoye les arraylist afin de n'afficher que ce qui nous interesse
                tuilesViables.clear();
                aventuriersViables.clear();
                cartesJouable.clear();
                //On indique l'action à realiser
                actionCarte = true;

                labelActionEnPreparation.setText("Utilisation d'une carte spécial: Cliquer sur une carte et la tuile ciblé");
                repaint();
            }
        });
        boutonPasserTour = new JButton("<html>Passer Son" + "<br><center>" + "Tour</html>");
        this.add(boutonPasserTour);
        boutonPasserTour.setBounds(420, 500, 100, 50);
        boutonPasserTour.setBorder(new LineBorder(Color.BLACK));
        boutonPasserTour.addActionListener(e -> {
            if (AventurierEnCoursDeJeu != null) {
                messageEnPreparation = Message.passerTour(AventurierEnCoursDeJeu.getNom());
                labelActionEnPreparation.setText("Etes Vous sur ? (Valider = oui)");

            }
            repaint();
        });
    }
//***********************************************GETTERS SETTERS***************************************//


    public ArrayList<String> getAventuriersMainPleinne() {
        return aventuriersMainPleinne;
    }

    public Tuile getTuileNom(String nom){
        for(int i =0;i<6; i++) { //ligne
            for (int j = 0; j < 6; j++) { // colonne
                if(grille[i][j] != null && grille[i][j].getNom().equalsIgnoreCase(nom)){
                    return grille[i][j];
                }
            }
        }
        System.out.println("Tuile inexistante");
        return null;
    }
    
    public String getCaseSelectionne() {
        return caseSelectionne;
    }

    public String getAventurierSelectionne() {
        return AventurierSelectionne;
    }

    public String getCarteSelectionne() {
        return carteSelectionne;
    }

    public Aventurier getAventurierEnCoursDeJeu() {
        return AventurierEnCoursDeJeu;
    }

    public void setAventurierEnCoursDeJeu(Aventurier av){
        this.AventurierEnCoursDeJeu = av;
        if (aventuriersMainPleinne!= null && aventuriersMainPleinne.contains(av.getNom())){
            urgenceDefausse=true;
            aventuriersMainPleinne.remove(av.getNom());
        }
        repaint();

    }

    public void setNivEau(int nivEau) {
        this.nivEau = nivEau;
    }
    
    
    public IHM getIhm() {
        return ihm;
    }

    public ArrayList<String> getCarteDefaussable() {
        return carteDefaussable;
    }

    public Tuile[][] getGrille() {
        return grille;
    }

    public int getNivEau() {
        return nivEau;
    }

    public boolean isPlateauSelectionnable() {
        return plateauSelectionnable;
    }

    public boolean isAventurierSelectionnable() {
        return aventurierSelectionnable;
    }

    public boolean isCarteSelecionable() {
        return carteSelecionable;
    }

    public boolean isSurligneEnAttente() {
        return surligneEnAttente;
    }

    public boolean isDefausseCartes() {
        return defausseCartes;
    }

    public boolean isActionDeplacement() {
        return actionDeplacement;
    }

    public boolean isActionAssecher() {
        return actionAssecher;
    }

    public boolean isActionDonner() {
        return actionDonner;
    }

    public boolean isUrgenceDefausse() {
        return urgenceDefausse;
    }

    public boolean isActionCarte() {
        return actionCarte;
    }

    public ArrayList<Tuile> getTuilesViables() {
        return tuilesViables;
    }

    public ArrayList<Aventurier> getAventuriersViables() {
        return aventuriersViables;
    }

    public ArrayList<String> getCartesJouable() {
        return cartesJouable;
    }

    public ArrayList<String> getAventurierSeNoyant() {
        return aventurierSeNoyant;
    }

    public HashMap<String, ImageLoader> getTuilesPresentesSurPlateau() {
        return tuilesPresentesSurPlateau;
    }

    public HashMap<String, ImageLoader> getAventuriersPresents() {
        return AventuriersPresents;
    }

    public HashMap<String, ImageLoader> getPionsPresents() {
        return pionsPresents;
    }

    public ImageLoader getLogo() {
        return logo;
    }

    public ImageLoader getFond() {
        return fond;
    }

    public ImageLoader getNiveauEau() {
        return NiveauEau;
    }

    public ImageLoader getIndicateurNivEau() {
        return indicateurNivEau;
    }

    public boolean isUrgenceDeplacement() {
        return urgenceDeplacement;
    }

    public void setUrgenceDeplacement(boolean urgenceDeplacement) {
        this.urgenceDeplacement = urgenceDeplacement;
    }



//***********************************************MOUSE EVENT***************************************//

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        int posX = mouseEvent.getX(); int posY = mouseEvent.getY();

    ////********************************ZONE AVENTURIER***************************************////
        if((posX>1570 && posX<1830 && posY>30 && posY<30+240* AventuriersPresents.size()) && aventurierSelectionnable){

            for (String element : this.AventuriersPresents.keySet()){
                
                int x = AventuriersPresents.get(element).getX();
                int y = AventuriersPresents.get(element).getY();
                int witdh = AventuriersPresents.get(element).getWidth();
                int height = AventuriersPresents.get(element).getHeight();
                if (( posX > x &&  posX < x+witdh) && (posY > y && posY < y+height) && !element.equalsIgnoreCase(AventurierEnCoursDeJeu.getClass().getSimpleName())){
                    //System.out.println("Aventurier selectionné: " + element);
                    this.AventurierSelectionne = element;
                    if(actionDonner){
                        messageEnPreparation = Message.donner(AventurierEnCoursDeJeu.getNom(), AventurierSelectionne, carteSelectionne);
                        affichageTextPreparationAction();
                        aventuriersViables.clear();
                        for(Aventurier av: ihm.getAventuriers()){
                            if(av.getClass().getSimpleName().toLowerCase().equalsIgnoreCase(AventurierSelectionne)){
                                aventuriersViables.add(av);
                            }
                        }
                        surligneEnAttente=true;
                        this.repaint();
                    }
                }
            }
    ////********************************ZONE GRILLE***************************************////
        } else if(posX>539 && posX<1261 && posY >9 && posY<731 && plateauSelectionnable){ //Methodes en cas de clique plateau
            for (String element : this.tuilesPresentesSurPlateau.keySet()){

                int x = tuilesPresentesSurPlateau.get(element).getX();
                int y = tuilesPresentesSurPlateau.get(element).getY();
                int witdh = tuilesPresentesSurPlateau.get(element).getWidth();
                int height = tuilesPresentesSurPlateau.get(element).getHeight();

                if (( posX > x &&  posX < x+witdh) && (posY > y && posY < y+height)){
                    //System.out.println("Tuile selectionné: " + element.split("_Inonde")[0]);
                    this.caseSelectionne=element.split("_Inonde")[0];
                    
                    if (actionDeplacement) {
                        messageEnPreparation = Message.bouger(AventurierEnCoursDeJeu.getNom(), caseSelectionne);
                        affichageTextPreparationAction();
                        tuilesViables.clear();
                        tuilesViables.add(getTuileNom(caseSelectionne));
                        surligneEnAttente=true;
                    }else if(actionAssecher){
                        messageEnPreparation = Message.assecher(AventurierEnCoursDeJeu.getNom(),caseSelectionne);
                        affichageTextPreparationAction();
                        tuilesViables.clear();
                        tuilesViables.add(getTuileNom(caseSelectionne));
                        surligneEnAttente=true;
                        
                    
                    }
                    if(carteSelectionne!=null && actionCarte && (carteSelectionne.equalsIgnoreCase("helicoptere")||carteSelectionne.equalsIgnoreCase("sacsDeSable"))){
                        messageEnPreparation = Message.carteAction(AventurierEnCoursDeJeu.getNom(),carteSelectionne,caseSelectionne);
                        tuilesViables.clear();
                        tuilesViables.add(getTuileNom(caseSelectionne));
                        surligneEnAttente=true;
                    }
                    this.repaint();
                }

            }
////********************************ZONE CARTE***************************************////
        }else if (posX>500 && posX<500+150*AventurierEnCoursDeJeu.getCarteMain().size() && posY>780 && posY<950 && carteSelecionable ){
            for (int i =0; i<AventurierEnCoursDeJeu.getCarteMain().size();i++){

                int x = 500+ i*150;
                int y = 780;
                int witdh = 110;
                int height = 170;

                if (( posX > x &&  posX < x+witdh) && (posY > y && posY < y+height)){
                    for (String s : ihm.getAssets().getImgCarteTresors().keySet()){
                        if (AventurierEnCoursDeJeu.getCarteMain().get(i).getNom().equalsIgnoreCase(s)){ //check si l'aventurier à la carte en main
                            this.carteSelectionne = s;
                            if (urgenceDefausse) {      //En cas de defausse
                                this.carteDefaussable.add(carteSelectionne);
                                this.repaint();
                                //System.out.println(carteSelectionne);
                                carteSelectionne=null;
                            }else{
                                carteDefaussable.clear();
                                carteDefaussable.add(s);
                            }
                          
                        }
                    }
                    //System.out.println("Carte selectionné: " + carteSelectionne);
                    cartesJouable.clear();
                    if(actionDonner){
                        messageEnPreparation = Message.donner(AventurierEnCoursDeJeu.getNom(), AventurierSelectionne, carteSelectionne);
                        affichageTextPreparationAction();
                        cartesJouable.clear();
                        for(CarteTresors ct: AventurierEnCoursDeJeu.getCarteMain()){
                            if(ct.getNom().equalsIgnoreCase(carteSelectionne)){
                                cartesJouable.add(carteSelectionne);
                            }
                        }
                        surligneEnAttente=true;
                        this.repaint();
                    }
                    if (actionCarte){
                        cartesJouable.clear();
                        cartesJouable.add(carteSelectionne);
                        surligneEnAttente=true;
                        this.repaint();
                    }
                    if(actionCarte && (carteSelectionne.equalsIgnoreCase("helicoptere")
                            ||carteSelectionne.equalsIgnoreCase("sacsDeSable")) && caseSelectionne!=null){
                        messageEnPreparation = Message.carteAction(AventurierEnCoursDeJeu.getNom(),carteSelectionne,caseSelectionne);

                        this.repaint();
                    }
                }
            }
        }else{
            
            System.out.println("click dans le vide");
        }
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}
