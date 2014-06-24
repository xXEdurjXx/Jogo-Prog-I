/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Eduardo
 */
import jplay.Sound;
import jplay.Sprite;

public class EnemyVoador extends Sprite {

    public boolean foiMorto = false;

    private double maxX;
    private double minX;
    private boolean esquerda = false;
    private int porraada = 0;
    private double tempApanhar = 0;

    private int maxApanha=3;
    private Sound inimigo;

    public EnemyVoador(String caminho, int numFrames) {
        super(caminho, numFrames);
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
        if (!foiMorto) {
            if (porraada == 0) {
                if (x <= minX) {
                    this.x = minX;
                    esquerda = false;
                }
                if (x >= maxX) {
                    this.x = maxX;
                    esquerda = true;
                }
                if (x > minX && esquerda) {

                    this.x -= 0.2 * Game.window.deltaTime();
                    moveEsquerda();

                } else {
                    if (!esquerda && x < maxX) {
                        this.x += 0.2 * Game.window.deltaTime();
                        moveDireita();

                    }
                }
            } else {
                tempApanhar += 0.01 * Game.window.deltaTime();
                //System.out.println(tempApanhar);
                apanhando();
            }

            fall();
        }
    }

    private void moveDireita() {
        if (stateX != Constantes.RIGHT) {
            setSequence(0, 3);
            stateX = Constantes.RIGHT;
            play();
        }
    }

    private void moveEsquerda() {
        if (stateX != Constantes.LEFT) {
            setSequence(0, 3);
            stateX = Constantes.LEFT;
            play();
        }
    }

    private void parar() {
        if (stateX == Constantes.RIGHT) {
            pause();
            setCurrFrame(0);
        } else if (stateX == Constantes.LEFT) {
            pause();
            setCurrFrame(3);
        }
        stateX = Constantes.STOP;
    }

    void die() {
        foiMorto = true;
    }

    public double getMax() {
        return maxX;
    }

    private void apanhando() {
        if (porraada <= maxApanha && tempApanhar <= 30) {
            pause();
            if (esquerda) {
                setCurrFrame(porraada + 5);
            }
            if (!esquerda) {
                setCurrFrame(porraada );

            }
            if(tempApanhar>=4){
                   
                
                //System.out.println("morreu");
                
                die();
            }

        } else {
            if (tempApanhar < 3) {
               
                porraada = 0;
                tempApanhar = 0;
            }
        }

    }

}
