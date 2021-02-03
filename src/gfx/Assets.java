package gfx;

import java.io.*;
import java.util.Hashtable;
import java.util.Objects;

//************************************************************************************************************
//                                  Classe chargeant les images du jeu
//*************************************************************************************************************


public class Assets {

    //***************************************************Attributs*********************************************//
    
    //Hashtable contenant les images des sous dossiers de ressources
    private Hashtable<String,ImageLoader> imgtuiles = new Hashtable<>();
    private Hashtable<String,ImageLoader> imgCarteTresors = new Hashtable<>();
    private Hashtable<String,ImageLoader> imgIcones = new Hashtable<>();
    private Hashtable<String,ImageLoader> imgPersonnages = new Hashtable<>();
    private Hashtable<String,ImageLoader> imgPions = new Hashtable<>();
    private Hashtable<String,ImageLoader> imgTresors = new Hashtable<>();

    //***********************************************Constructeur**********************************************//
    public Assets(){


        File test = new File((System.getProperty("user.dir"))+"/ressources");
        int b = 0, c =0 ;
        Hashtable<String, ImageLoader> acopier = new Hashtable<>();
        File[] temp;

        FilenameFilter LEFILTRE = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.contains(".png")|| name.contains(".jpeg")|| name.contains(".jpg");
            }
        };

    for(File f: Objects.requireNonNull(test.listFiles())){
        if (f.isDirectory()) {
            temp = f.listFiles(LEFILTRE);
            acopier.clear();
            for (File file : temp) {
                if (b % 1000 == 0) {
                    b = 0;
                    c += 100;
                }
                b += 100;
                acopier.put(file.getName().split(".png")[0], new ImageLoader(file.getPath(), 0 + b, 0 + c, 100, 100));
            }
            c = 0;
            b = 0;

            switch (f.getName()) {
                case "cartes":
                    imgCarteTresors.putAll(acopier);
                    //System.out.println("cartes");
                    break;
                case "icones":
                    imgIcones.putAll(acopier);
                    //System.out.println("icones");
                    break;
                case "personnages":
                    imgPersonnages.putAll(acopier);
                    //System.out.println("personnages");
                    break;
                case "pions":
                    imgPions.putAll(acopier);
                    //System.out.println("pions");
                    break;
                case "tresors":
                    imgTresors.putAll(acopier);
                    //System.out.println("tresors");
                    break;
                case "tuiles":
                    imgtuiles.putAll(acopier);
                    //System.out.println("tuiles");
                    break;
            }

        }
    }
    }
    
    //*******************************GETTERS********************************//
    
    
    public Hashtable<String, ImageLoader> getImgtuiles() {
        return imgtuiles;
    }

    public Hashtable<String, ImageLoader> getImgCarteTresors() {
        return imgCarteTresors;
    }

    public Hashtable<String, ImageLoader> getImgIcones() {
        return imgIcones;
    }

    public Hashtable<String, ImageLoader> getImgPersonnages() {
        return imgPersonnages;
    }

    public Hashtable<String, ImageLoader> getImgPions() {
        return imgPions;
    }

    public Hashtable<String, ImageLoader> getImgTresors() {
        return imgTresors;
    }
}
