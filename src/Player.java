
/**
 *
 * @author Eduardo
 */
import jplay.Sprite;
import jplay.Sound;
import jplay.Keyboard;

public class Player extends Sprite {

    private int vidas;
    public int stateX;
    private int pontuacao;
    private Sound jumpSound, dieSound;
    Sprite[] hearts;
    Keyboard keyboard;

    private int xInicial;
    private int yInicial;

    public Player(int posX, int posY, int chao) {
        super("images/Mario.png", 6);
        pause();
        setCurrFrame(0);
        setTotalDuration(600);
        xInicial = posX;
        yInicial = posY;
        x = posX;
        y = posY;
        vidas = 3;
        hearts = new Sprite[vidas];
        for (int i = 0; i < hearts.length; i++) {
            hearts[i] = new Sprite("images/heart.png");
        }
        keyboard = Game.keyboard;
        setGravity(0.0075);
        setFloor(chao);
        setJumpVelocity(2);
        stateX = Constantes.STOP;
    }

    public Player() {
        this(0, 0, 800);
    }

    public void move() {
        moveX(Keyboard.LEFT_KEY, Keyboard.RIGHT_KEY, 0.6 * Game.window.deltaTime());
        jump();
        if (keyboard.keyDown(Keyboard.SPACE_KEY)) {
            pular();
        } else {
            if ((keyboard.keyDown(Keyboard.LEFT_KEY)) && (!isJumping())) {
                moveEsquerda();
            } else {
                if ((keyboard.keyDown(Keyboard.RIGHT_KEY)) && (!isJumping())) {
                    moveDireita();
                } else if (!isJumping()) {
                    parar();
                }
            }
        }
        fall();
    }

    private void moveDireita() {
        if (stateX != Constantes.RIGHT) {
            setSequence(0, 2);
            stateX = Constantes.RIGHT;
            play();
        }
    }

    private void moveEsquerda() {
        if (stateX != Constantes.LEFT) {
            setSequence(3, 5);
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

    public int getVidas() {
        return vidas;
    }

    public int getPontuacao() {
        return pontuacao;
    }

    private void pular() {
        if (stateX == Constantes.RIGHT) {
            while (isJumping()) {
                setCurrFrame(40);
            }
        }
        if (stateX == Constantes.LEFT) {
            setCurrFrame(41);
        }
    }

    public int getStateOfX() {
        return stateX;
    }

    void addPoints(int i) {
        pontuacao += i;
    }

    void die() {
        vidas--;
        x = xInicial;
        y = yInicial;
    }

    public void vida() {
        vidas++;

        hearts = new Sprite[vidas];
        for (int i = 0; i < hearts.length; i++) {
            hearts[i] = new Sprite("images/heart.png");
        }
    }

    void setVida(int j) {
        vidas = j;
        hearts = new Sprite[vidas];
        for (int i = 0; i < hearts.length; i++) {
            hearts[i] = new Sprite("images/heart.png");
        }

    }

}
