/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Caroline Estudo
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Eduardo
 */
import jplay.GameImage;
import jplay.Sound;
import jplay.Sprite;

public class Fim extends GameImage {

    public boolean foiPega;
    private Sound somMoeda;
    private double xInicial;

    public Fim() {
        super("images/barraMovel.png");
       
        foiPega = false;
        somMoeda = new Sound("sons/coins.wav");
        somMoeda.setRepeat(false);
    }

    public void playSound() {
        somMoeda.play();
    }

    public double getXInicial() {
       return xInicial;
    }

    public void setXInicial(double x) {
        xInicial = x;
    }
}

