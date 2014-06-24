/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Caroline Estudo
 */
import jplay.Sound;
import jplay.Sprite;

public class BarraMovel extends Sprite {

    public boolean foiMorto = false;

    private double maxX;
    private double minX;
    private boolean esquerda = false;
    private int porraada = 0;
    private double tempApanhar = 0;

    private int maxApanha = 3;
    private Sound inimigo;

    public BarraMovel() {
        super("images/barraMovel.png", 1);
        pause();
        setCurrFrame(0);
        setTotalDuration(600);

        stateX = Constantes.STOP;
    }

    public void setMin(double min) {
        this.minX = min;

    }

    public double getMin() {
        return minX;
    }

    public void setPosMinMax(double x, double y, double minX) {
        this.x = x;
        this.y = y;
        this.minX = minX;

        setFloor((int) (y));
    }

    public void setMax(double maxX) {

        this.maxX = maxX;
    }
    public int stateX;

    public void move() {
        //moveX(0.6 * Game.window.deltaTime());

//        if (x <= minX) {
//            this.x = minX;
//            esquerda = false;
//        }
//        if (x >= maxX) {
//            this.x = maxX;
//            esquerda = true;
//        }
//        if (x > minX && esquerda) {
//
//            this.x -= 0.1 * Game.window.deltaTime();
//           
//
//        } else {
//            if (!esquerda && x < maxX) {
//                this.x += 0.1 * Game.window.deltaTime();
//
//            }
//        }

        fall();

    }

   


   

    public double getMax() {
        return maxX;
    }

}
