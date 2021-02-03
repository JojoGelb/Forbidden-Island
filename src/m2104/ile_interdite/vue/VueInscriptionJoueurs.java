package m2104.ile_interdite.vue;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Arrays;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import gfx.ImageLoader;
import m2104.ile_interdite.util.Message;

//************************************************************************************************************
//                          JPANEL affichant la partie inscription du jeu
//*************************************************************************************************************


public class VueInscriptionJoueurs extends JPanel {
    private final IHM ihm;

    private JComboBox<Integer> choixNbJoueurs;
    private JLabel [] labelNomJoueurs = new JLabel[4];
    private JTextField [] saisieNomJoueurs = new JTextField[4];
    private final JButton inscrire = new JButton("Inscrire");
    private JComboBox<Integer> choixNiveau;

    private String[] nomJoueurs;

    private ImageLoader logo,fond;

    private Color col = new Color(35,35,104); //Bleu du texte



    //**************************Constructeur*****************************//

    public VueInscriptionJoueurs(IHM ihm) {
        this.ihm = ihm;

        this.setLayout(null);
        this.setSize(1800, 1000);

        logo = new ImageLoader((System.getProperty("user.dir")) + "/ressources/icones/logo.png",650,150, (int) (getWidth()*0.35), (int)(getHeight()*0.2));
        logo.setPosition(550, 100);
        fond = new ImageLoader((System.getProperty("user.dir"))+ "/ressources/FontMap.jpg",0,0,getWidth(), (int)(getHeight()));

        JPanel panelChoix = new JPanel(new GridLayout(7,2));

        // nombre de joueurs
        choixNbJoueurs = new JComboBox<>(new Integer[] { 2, 3, 4 });
        JLabel labelChoixNbJoueurs = new JLabel("Nombre de joueurs :");
        labelChoixNbJoueurs.setForeground(col);
        labelChoixNbJoueurs.setFont(new Font("Arial",Font.BOLD,23));
        panelChoix.add(labelChoixNbJoueurs);
        panelChoix.add(choixNbJoueurs);

        // Saisie des noms de joueurs
        for(int i = 0; i < saisieNomJoueurs.length; i++) {
            saisieNomJoueurs[i] = new JTextField();
            labelNomJoueurs[i] = new JLabel("Nom du joueur n° " + (i + 1) + " :");
            labelNomJoueurs[i].setForeground(col);
            labelNomJoueurs[i].setFont(new Font("Arial",Font.BOLD,23));
            panelChoix.add(labelNomJoueurs[i]);
            panelChoix.add(saisieNomJoueurs[i]);
            labelNomJoueurs[i].setEnabled(i < 2);
            saisieNomJoueurs[i].setEnabled(i < 2);
            saisieNomJoueurs[i].setText("aventurier " + (i+1));
        }

        choixNiveau = new JComboBox<>(new Integer[] { 1, 2, 3, 4 });
        JLabel labelDifficulte = new JLabel("Niveau de difficulté :");
        labelDifficulte.setForeground(col);
        labelDifficulte.setFont(new Font("Arial",Font.BOLD,23));
        panelChoix.add(labelDifficulte);
        panelChoix.add(choixNiveau);

        JPanel caseVide = new JPanel();
        caseVide.setOpaque(false);
        panelChoix.add(caseVide); // Une case vide
        panelChoix.add(inscrire);


        panelChoix.setOpaque(false);
        this.add(panelChoix);
        panelChoix.setBounds(600, 380, 500, 300);



        // Choix du nombre de joueurs
        choixNbJoueurs.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int nb = (Integer) choixNbJoueurs.getSelectedItem();

                for(int i = 0; i < saisieNomJoueurs.length; i++) {
                    labelNomJoueurs[i].setEnabled(i < nb);
                    saisieNomJoueurs[i].setEnabled(i < nb);
                }
            }
        });

        // Inscription des joueurs
        inscrire.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Remplissage du tableau contenant le nom des joueurs
                int nbJoueurs = (int) choixNbJoueurs.getSelectedItem();

                nomJoueurs = new String[nbJoueurs];
                for (int i = 0; i < nbJoueurs; ++i) {
                    nomJoueurs[i] = saisieNomJoueurs[i].getText();
                }

                int niveauSelectionne;
                niveauSelectionne = (int) choixNiveau.getSelectedItem();

                ihm.notifierObservateurs(Message.validerJoueurs(niveauSelectionne));
            }
        });

        this.setVisible(true);
    }
    
    //*******************************METHODES*************************************//
    
    //Methode d'affichage
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); //To change body of generated methods, choose Tools | Templates.
        fond.paintComponent(g);
        logo.paintComponent(g);
        }


    //******************************GETTERS********************************//

    public String[] getNomJoueurs() {
        return Arrays.copyOf(this.nomJoueurs, this.nomJoueurs.length);
    }

}