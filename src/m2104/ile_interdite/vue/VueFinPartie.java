
package m2104.ile_interdite.vue;

import gfx.ImageLoader;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import m2104.ile_interdite.util.Message;

//************************************************************************************************************
//                          JPANEL de la fin de partie
//*************************************************************************************************************
public class VueFinPartie extends JPanel{
    
    //************************************Attributs*********************************************//
    
    private IHM ihm;
    private ImageLoader logo,fond;
    private JLabel labelVic;
    private JTextArea labelCommentaire;
    private int nbVic;
    private Color col =new Color(35,35,104);   //Couleur bleu des textes
    
    

    public VueFinPartie(IHM ihm,int nb) {
        
        this.setLayout(null);
        this.nbVic = nb;

        this.ihm = ihm;
        this.setSize(1800, 1000);
        logo = new ImageLoader((System.getProperty("user.dir")) + "/ressources/icones/logo.png",0,0, (int) (getWidth()*0.35), (int)(getHeight()*0.2));
        fond = new ImageLoader((System.getProperty("user.dir"))+ "/ressources/FontMap.jpg",0,0,getWidth(), (int)(getHeight()));
        
        labelVic = new JLabel();
        labelCommentaire  = new JTextArea();
        labelCommentaire.setOpaque(false);
        this.add(labelVic);
        instanceFin();
        
        JButton bouttonRetour = new JButton("Menu Inscription");
        this.add(bouttonRetour);
        bouttonRetour.setBounds(800, 800, 110, 50);
        bouttonRetour.setBorder(new LineBorder(Color.BLACK));
        bouttonRetour.setFont(new Font("Arial", Font.BOLD, 13));
        bouttonRetour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ihm.notifierObservateurs(Message.restart());
            }
        });
        
    }
    
    //Affiche une version différente selon ce que le constructeur à indiqué
    public void instanceFin(){
        Font font = new Font("Arial",Font.BOLD,52);
        labelVic.setFont(font);
        Dimension sz;
        switch(nbVic){
            case 0:
                labelVic.setText("VICTOIRE DES AVENTURIERS");
                sz = labelVic.getPreferredSize(); 
                labelVic.setBounds(520, 200, sz.width, sz.height); 
                this.add(labelCommentaire);
                labelCommentaire.setText("Bravo: Vous avez réussi à fuir l'ile interdite \n               en possession des 4 trésors:");
                labelCommentaire.setFont(new Font("Arial",Font.BOLD,40));
                labelCommentaire.setForeground(col);
                sz = labelVic.getPreferredSize(); 
                labelCommentaire.setBounds(480, 350,  sz.width+150, sz.height+30);
                
                break;
            case 1:
                labelVic.setText("DEFAITE DES AVENTURIERS"); //Niv eau trop haut
                sz = labelVic.getPreferredSize(); 
                labelVic.setBounds(520, 200, sz.width, sz.height);
                this.add(labelCommentaire);
                labelCommentaire.setText("\tDommage: \n   Le niveau d'eau est devenu trop grand");
                labelCommentaire.setFont(new Font("Arial",Font.BOLD,40));
                labelCommentaire.setForeground(col);
                sz = labelVic.getPreferredSize(); 
                labelCommentaire.setBounds(480, 350,  sz.width+150, sz.height+30);
                break;
            case 2:
                labelVic.setText("DEFAITE DES AVENTURIERS"); //Héli coulé
                sz = labelVic.getPreferredSize(); 
                labelVic.setBounds(520, 200, sz.width, sz.height);
                this.add(labelCommentaire);
                labelCommentaire.setText("\tDommage: \n  L'héliport a coulé : impossible de fuir");
                labelCommentaire.setFont(new Font("Arial",Font.BOLD,40));
                labelCommentaire.setForeground(col);
                sz = labelVic.getPreferredSize(); 
                labelCommentaire.setBounds(480, 350,  sz.width+150, sz.height+30);
                break;
            case 3:
                labelVic.setText("DEFAITE DES AVENTURIERS"); //Noyade
                sz = labelVic.getPreferredSize(); 
                labelVic.setBounds(520, 200, sz.width, sz.height);
                this.add(labelCommentaire);
                labelCommentaire.setText("              Dommage: \n  Un Aventurier s'est noyé");
                labelCommentaire.setFont(new Font("Arial",Font.BOLD,40));
                labelCommentaire.setForeground(col);
                sz = labelVic.getPreferredSize(); 
                labelCommentaire.setBounds(630, 350,  sz.width+150, sz.height+30);
                break;
            case 4:
                labelVic.setText("DEFAITE DES AVENTURIERS"); //Tresor coulé
                sz = labelVic.getPreferredSize(); 
                labelVic.setBounds(520, 200, sz.width, sz.height);
                this.add(labelCommentaire);
                labelCommentaire.setText("             Dommage: \n       Un trésor a coulé");
                labelCommentaire.setFont(new Font("Arial",Font.BOLD,40));
                labelCommentaire.setForeground(col);
                sz = labelVic.getPreferredSize(); 
                labelCommentaire.setBounds(630, 350,  sz.width+150, sz.height+30);
                break;
            default:
                labelVic.setText("Vous n'avez aucune raison d'être ici");
                sz = labelVic.getPreferredSize(); 
                labelVic.setBounds(520, 200, sz.width, sz.height);
                break;
        }


        labelVic.setForeground(col);
    }

    //Fonction d'affichage
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        fond.paintComponent(g);
        logo.paintComponent(g);
        if(nbVic ==0){
            int temp = 0;
                for(String s: ihm.getAssets().getImgTresors().keySet()){
                    if(s.equalsIgnoreCase("cristal") || s.equalsIgnoreCase("calice")|| s.equalsIgnoreCase("zephyr")|| s.equalsIgnoreCase("pierre")){
                        ihm.getAssets().getImgTresors().get(s).parametrer(590 + temp*150, 550, 104, 165);
                        ihm.getAssets().getImgTresors().get(s).paintComponent(g);
                        temp++;
                }
                }
        }
        
    }
    
    
    
}
