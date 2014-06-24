/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Eduardo
 */
import jplay.Sprite;
import jplay.Sound;

public class Coin extends Sprite {

    public boolean foiPega;
    private Sound somMoeda;
    private double xInicial;

    public Coin() {
        super("images/coin.png", 4);
        setLoop(true);
        setTotalDuration(1000);
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
