/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gfx;

/**
 *
 * @author jordy
 */
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

//************************************************************************************************************
//                              Classe permettant de faire afficher une image
//*************************************************************************************************************

public class ImageLoader extends JPanel {
    protected Image image = null;  
    private int x,y,width,height ;

    public ImageLoader (String path, int x, int y, int width, int height) {
        super();
        this.setOpaque(false);

        this.x = x ;
        this.y = y ;
        this.width = width ;
        this.height = height ;

        try {
            this.image = ImageIO.read( new File(path));
        } catch (IOException ex) {
            System.err.println("Erreur en lecture de l'image " + path);
        }
    }
    
    //*********************************METHODES**********************************************//

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, x, y, width, height, null, this);
    }
    
    //Sert à parametrer l'image (position x,y sur ecran, largeur, hauteur)
    public void parametrer(int x , int y , int width, int height){
        this.x=x;
        this.y=y;
        this.width=width;
        this.height=height;
    }
    
    //Sert à changer la largeur et la hauteur de l'image
    public void setDimension(int width, int height){
        this.width=width;
        this.height=height;
    }
    
    //Sert à modifier la position de l'image
    public void setPosition(int x , int y){
        this.x=x;
        this.y=y;

    }
    
    //***********************************GETTERS****************************//

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
