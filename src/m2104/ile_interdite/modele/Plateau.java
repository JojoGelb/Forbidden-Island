/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m2104.ile_interdite.modele;

import java.util.ArrayList;
import java.util.Collections;

//************************************************************************************************************
//                              Plateau du jeu contenant les 24 tuiles
//*************************************************************************************************************
public class Plateau {
    
    //*********************ATTRIBUTS**********************//
    
    private ArrayList<Tuile> tuiles; //Une prémap
    private Tuile[][] plateau;
    
    
    //*********************CONSTRUCTEUR********************//
    
    
    public Plateau(){
        this.tuiles = new ArrayList<>();

        //Tuiles de trésors
        tuiles.add(new Tuile("LeTempleDuSoleil","PIERRE"));          
        tuiles.add(new Tuile("LeTempleDeLaLune","PIERRE"));              
        
        tuiles.add(new Tuile("LaCarverneDuBrasier","CRISTAL"));       
        tuiles.add(new Tuile("LaCarverneDesOmbres","CRISTAL"));   
        
        tuiles.add(new Tuile("LeJardinDesMurmures","Zephyr"));       
        tuiles.add(new Tuile("LeJardinDesHurlements","Zephyr")); 
        
        tuiles.add(new Tuile("LePalaisDesMarees","CALICE"));       
        tuiles.add(new Tuile("LePalaisDeCorail","CALICE"));     
        
        //Tuiles de spawn
        tuiles.add(new Tuile("LaPorteDeCuivre","none"));   //vert       
        tuiles.add(new Tuile("Heliport","none"));           //bleu    
        tuiles.add(new Tuile("LaPortedOr","none"));        //jaune           
        tuiles.add(new Tuile("LaPortedArgent","none"));    //blanc           
        tuiles.add(new Tuile("LaPorteDeFer","none"));      //noir              
        tuiles.add(new Tuile("LaPorteDeBronze","none"));   //rouge    
        
        //Tuiles normale
        tuiles.add(new Tuile("LeLagonPerdu","none"));        
        tuiles.add(new Tuile("LaForetPourpre","none"));     
        tuiles.add(new Tuile("LeValDuCrepuscule","none"));      
        tuiles.add(new Tuile("LesFalaisesDeLOubli","none")); 
        tuiles.add(new Tuile("LePontDesAbimes","none"));         
        tuiles.add(new Tuile("Observatoire","none"));  
        tuiles.add(new Tuile("LeMaraisBrumeux","none"));
        tuiles.add(new Tuile("LaTourDuGuet","none"));
        tuiles.add(new Tuile("LesDunesDeLIllusion","none"));
        tuiles.add(new Tuile("LeRocherFantome","none"));
        
        genPlateau();
    }
    
    //********************METHODES*************************//
    
    //Instancie le plateau
    public void genPlateau(){
        
        int nb =0;
        
        plateau = new Tuile[6][6];
        Collections.shuffle(tuiles);


        for(int i =0;i<6; i++){ //ligne
            System.out.println(" ");
            for(int j =0;j<6; j++){ // colonne
                if(((i==0 || i==5) && (j<2||j>3)) || ((i==1||i==4) && (j==0 || j==5))){
                    plateau[i][j] = null;
                }else{
                    plateau[i][j] = new Tuile(tuiles.get(nb).getNom(),tuiles.get(nb).getTresor(),i,j);
                    nb++;
                }
            }
        }
    }


        /*****************GETTER SETTER**********************/

    public ArrayList<Tuile> getTuiles() {
        return tuiles;
    }

    public Tuile getTuileNom(String nom){
        for(int i =0;i<6; i++) { //ligne
            for (int j = 0; j < 6; j++) { // colonne
                if(plateau[i][j] != null && plateau[i][j].getNom().equalsIgnoreCase(nom)){
                    return plateau[i][j];
                }
            }
        }
        System.out.println("Tuile inexistante");
        return null;
    }

    public Tuile[][] getGrille() {
        return plateau;
    }
}
