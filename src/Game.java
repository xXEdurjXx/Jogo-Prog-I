
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Vector;
import javax.swing.JOptionPane;
import jplay.GameImage;
import jplay.GameObject;
import jplay.Keyboard;
import jplay.Scene;
import jplay.Sound;
import jplay.Sprite;
import jplay.TileInfo;
import jplay.Time;
import jplay.Window;
//import jplay.TileInfo;

class Game {

    static Window window;
    static Scene scene;
    static Keyboard keyboard;
    static GameImage fundo;
    int tempoAtual, resp, vol, dif;
    Time tempo;
    Font fonteTempo, fontePont;
    private boolean pausado, playMusic;
    private MenuFrame menu;
    private LoginFrame loginMenu;
    private static Sound musica, somPause, gameOverSound, morreu;
    static Player player;
    private static ArrayList moedas;
    private String pont;
    private Sprite gameOverSheet;

    //Colisores  
    private GameObject rectBaixo;
    private GameObject rectcima;
    private GameObject rectLat1;
    private GameObject rectLat2;

    private int drawX = -30;
    private int drawY = 0;

    //private Enemy enemy;
    private Enemy[] enemys;

    void carregar(int volumeMusica) {
        window = MenuInicial.janela;
        scene = new Scene();
        scene.setDrawStartPos(drawX, drawY);
        //enemy = new Enemy("images/Mario.png", 6);
        enemys = new Enemy[3];

        scene.loadFromFile("fases/fase1.scn");
        fundo = new GameImage("images/fundo.png");
        gameOverSheet = new Sprite("images/game_over_sheet.png", 7);
        gameOverSheet.setCurrFrame(0);
        keyboard = window.getKeyboard();
        tempo = new Time(900, 900, false);
        tempo.setSecond(200);
        fonteTempo = new Font("Super Mario Bros. 3 Regular", Font.TRUETYPE_FONT, 40);
        fontePont = new Font("Super Mario Bros. 3 Regular", Font.TRUETYPE_FONT, 35);
        keyboard.setBehavior(Keyboard.ENTER_KEY, Keyboard.DETECT_INITIAL_PRESS_ONLY);
        keyboard.setBehavior(Keyboard.ESCAPE_KEY, Keyboard.DETECT_INITIAL_PRESS_ONLY);
        menu = new MenuFrame();
        loginMenu = new LoginFrame();
        musica = new Sound("sons/musica1.wav");
        musica.setVolume(volumeMusica);
        morreu = new Sound("sons/smb3_player_down.wav");
        morreu.setVolume(volumeMusica);
        morreu.setRepeat(true);
        somPause = new Sound("sons/pause.wav");
        gameOverSound = new Sound("sons/player_down.wav");
        gameOverSound.setVolume(resp);
        player = new Player(30, 0, 640);
        scene.addOverlay(player);
        scene.moveScene(player);
        //scene.addOverlay(enemy);
        moedas = new ArrayList();

        setInimigoPosition();
        criarColisores();
        gerarMoedas();

    }

    private void criarColisores() {
        rectBaixo = new GameObject();
        rectBaixo.width = 3 * player.width / 4;
        rectBaixo.height = 2 * player.height / 4;

        rectcima = new GameObject();
        rectcima.width = 3 * player.width / 4;
        rectcima.height = 2 * player.height / 4;

        rectLat1 = new GameObject();

        rectLat1.width = 2 * player.width / 4;
        rectLat1.height = 3 * player.height / 4;

        rectLat2 = new GameObject();

        rectLat2.width = 2 * player.width / 4;
        rectLat2.height = 3 * player.height / 4;
    }

    private void executar(boolean playMusic) {
        boolean continuar = true;
        if (playMusic) {
            //   musica.play();
        }
        while (continuar) {
            if (pausado == false) {
                draw();
                setPosColisores();
                colisao();
                scene.setDrawStartPos(drawX, 0);

            }
            if (keyboard.keyDown(Keyboard.ESCAPE_KEY)) {
                abrirMenu();
            }
        }
    }

    private void draw() {
        atualizarCena();
        player.move();
        player.update();
        scene.draw();

        for (Object inimigo : enemys) {

            Enemy e = (Enemy) inimigo;
            if (!e.foiMorto) {
                e.move();
                e.draw();
            }
        }
        window.drawText(String.format("%03d", (Object) tempo.getTotalSecond()) + "", 650, 40, Color.WHITE, fonteTempo);
        window.drawText(String.format("%05d", (Object) player.getPontuacao()), 100, 35, Color.BLACK, fontePont);
        atualizarVidas();
        atualizarMoedas();
        window.update();
    }

    public Game(boolean executarMusica, int volumeMusica, int dificuldade) {
        playMusic = executarMusica;
        vol = volumeMusica;
        dif = dificuldade;
        carregar(volumeMusica);
        executar(executarMusica);
        exitGame();
    }

    private void abrirMenu() {
        tempoAtual = (int) tempo.getTotalSecond();
        pausado = true;
        menu.setVisible(true);
        musica.stop();
        playSound(somPause);
        while (pausado) {
            if (menu.mandouSair) {
                exitGame();
            }
            if (menu.mandouContinuar) {
                menu.setMandouContinuar(false);
                fecharMenu();
            }
        }
    }

    void fecharMenu() {
        tempo.setTime(0, 0, tempoAtual);
        playSound(musica);
        pausado = false;
    }

    void exitGame() {
        menu.setMandouSair(false);
        resp = JOptionPane.showConfirmDialog(null, "Deseja salvar sua pontuação?");
        if (resp == JOptionPane.NO_OPTION) {
            resp = JOptionPane.showConfirmDialog(null, "Deseja ver as melhores pontuações?");
            if (resp == JOptionPane.NO_OPTION) {
                window.exit();
            } else {
                TelaRanking tela = new TelaRanking();
            }
        } else if (resp == JOptionPane.YES_OPTION) {
            loginMenu.setNovaPont(player.getPontuacao());
            loginMenu.setVisible(true);
        } else {
            fecharMenu();
        }
    }

    private void atualizarVidas() {
        if (player.getVidas() >= 0) {
            for (int i = 0; i < player.getVidas(); i++) {
                player.hearts[i].x = 10 + i * 25;
                player.hearts[i].y = 10;
                player.hearts[i].draw();
            }
        } else {
            gameOver();
        }
    }

    private void atualizarMoedas() {
        for (Object moeda : moedas) {

            //for (int i = 0; i < moedas.size(); i++) {
            Coin m = (Coin) moeda;
            if (player.collided(m) && (!m.foiPega)) {
                player.addPoints(10);
                m.foiPega = true;
                m.playSound();
            }
            if (!m.foiPega) {

                m.update();
                m.draw();
            }
        }
    }

    private void playSound(Sound som) {
        som.play();
    }

    void gameOver() {
        musica.stop();
        pausado = true;
        playSound(gameOverSound);
        gameOverSheet.setSequenceTime(0, 6, false, 7);
        String m = "game over";
        gameOverSheet.play();
        boolean continuar = true;
        while (continuar) {
            gameOverSheet.update();
            gameOverSheet.draw();
            window.drawText("game over", 250, 200, Color.WHITE, fonteTempo);
            window.drawText("esc para sair", 225, 300, Color.WHITE, fontePont);
            window.drawText("enter para reiniciar o jogo", 25, 400, Color.WHITE, fontePont);
            window.update();
            if (keyboard.keyDown(Keyboard.ESCAPE_KEY)) {
                exitGame();
            } else if (keyboard.keyDown(Keyboard.ENTER_KEY)) {
                Game novoJogo = new Game(playMusic, vol, dif);
            }
        }
    }

    private void colisao() {

        colisaoInimigos();

        Point pt1 = new Point((int) (player.x - drawX), (int) player.y);
        Point pt2 = new Point((int) (player.x + player.width - drawX), (int) (player.y + player.height));

        Vector v = scene.getTilesFromPosition(pt1, pt2);
        for (int i = 0; i < v.size(); i++) {

            TileInfo rectAtual = (TileInfo) v.get(i);

            if (rectcima.collided(rectAtual)) {

                player.y++;
                player.setVelocityY(0);
                break;

            }

            if (rectLat2.collided(rectAtual)) {

                player.x = rectAtual.x + drawX - player.width;

                break;
            }

            if (rectLat1.collided(rectAtual)) {

                // player.x =rectAtual.x+rectAtual.width-drawX;
                player.x = rectAtual.x + drawX + rectAtual.width;

//                System.out.println("colidiu");
                break;
            }
            if (rectBaixo.collided(rectAtual)) {

                player.setFloor((int) (rectAtual.y + 1));
                //System.out.println(player.y+"    " +rectAtual.y);
                break;

            } else {
                player.setFloor(640);
            }
        }
    }

    private void setPosColisores() {

        rectBaixo.x = player.x + player.width / 8 - drawX;
        rectBaixo.y = player.y + 2 * player.height / 4;

        rectcima.x = player.x + player.width / 8 - drawX;
        rectcima.y = player.y;

        rectLat1.x = player.x - drawX;
        rectLat1.y = player.y + player.height / 8;

        rectLat2.x = player.x + 2 * player.width / 4 - drawX;
        rectLat2.y = player.y + player.height / 8;
    }

    private void gerarMoedas() {
        Point pt1 = new Point((int) player.x, (int) 0);
        Point pt2 = new Point(1000000, 600);

        Vector v = scene.getTilesFromPosition(pt1, pt2);

        int j = 0;
        int i = 0;
        while (i < v.size()) {

            TileInfo rectAtual = (TileInfo) v.get(i);

            if (rectAtual.id == 7 || rectAtual.id == 8 || rectAtual.id == 9) {

                Coin m = new Coin();

                m.x = rectAtual.x + drawX;
                m.setXInicial(m.x);
                m.y = rectAtual.y - m.height;
                moedas.add(m);
            }
            i++;
        }

    }

    private void atualizarCena() {

        if (player.x >= MenuInicial.janela.getWidth() / 2) {
            drawX -= 2;
            player.x = MenuInicial.janela.getWidth() / 2 - Constantes.LEFT;

            for (Object inimigo : enemys) {

                Enemy e = (Enemy) inimigo;
                e.setMin(e.getMin() - 2);
                e.setMax(e.getMax() - 2);
                e.x -= 2;
            }
            for (Object moeda : moedas) {

                Coin m = (Coin) moeda;

                if (!m.foiPega) {
                    m.x -= 2;
                }
            }
        }
        if (player.x <= 2) {
            drawX += 2;
            player.x = 3;

            for (Object inimigo : enemys) {

                Enemy e = (Enemy) inimigo;
                e.setMin(e.getMin() + 2);
                e.setMax(e.getMax() + 2);
                e.x += 2;

            }
            for (Object moeda : moedas) {

                //for (int i = 0; i < moedas.size(); i++) {
                Coin m = (Coin) moeda;

                if (!m.foiPega) {
                    m.x += 2;

                }
            }
        }
    }

    private void setInimigoPosition() {
        Point pt1 = new Point((int) drawX, (int) 0);
        Point pt2 = new Point(1000000, 1000000);

        Vector v = scene.getTilesFromPosition(pt1, pt2);

        int j = 0;
        int i = 0;

        while (i < v.size()) {

            TileInfo rectAtual = (TileInfo) v.get(i);

            if (rectAtual.id == 10) {
                enemys[j] = new Enemy("images/inimigo.png", 10);
                enemys[j].setPosMinMax(rectAtual.x - enemys[j].width, rectAtual.y, rectAtual.x - enemys[j].width);

                // System.out.println("gerou");
            } else {
                if (rectAtual.id == 11) {

                    enemys[j].setMax(rectAtual.x - enemys[j].width);

                    // System.out.println("pronto");
                    j++;
                }
            }
            i++;

        }
    }

    private void setPosicaoMoeda() {
        for (Object moeda : moedas) {

            //for (int i = 0; i < moedas.size(); i++) {
            Coin m = (Coin) moeda;

            if (!m.foiPega) {

                m.x = m.getXInicial();
            }
        }
    }

    private void colisaoInimigos() {

        for (Object inimigo : enemys) {

            Enemy e = (Enemy) inimigo;
            if (!e.foiMorto) {
                GameObject rect3 = new GameObject();
                rect3.x = player.x;
                rect3.y = player.y + player.height;
                rect3.width = player.width;
                rect3.height = player.height / 2;
                if (rect3.collided(e)) {

                    player.isJumping();
                    player.setVelocityY(-1.3);
                    e.toma();
                    System.out.println(e.foiMorto);
                    break;
                }
                GameObject rect = new GameObject();
                rect.x = player.x - player.width / 4;
                rect.y = player.y;
                rect.width = 3 * player.width / 4;
                rect.height = player.height / 3;
                GameObject rect2 = new GameObject();
                rect2.x = player.x + 2 * player.width / 4;
                rect2.y = player.y;
                rect2.width = 3 * player.width / 4;
                rect2.height = player.height / 3;

                if (rect.collided(e) || rect2.collided(e)) {

                    player.stop();

                    morreu.play();
                    e.stop();
                    double t = 0;
                    while (t < 60) {
                        System.out.println(t);
                        t += 0.001 * window.deltaTime();
                    }
                    drawX = -30;
                    setPosColisores();
                    setInimigoPosition();
                    setPosicaoMoeda();

                    player.die();

                    morreu.stop();
                    break;
                }

                if (player.y > 580) {
                    e.stop();
                    player.stop();
                    drawX = -30;
                    setPosColisores();
                    setInimigoPosition();
                    setPosicaoMoeda();

                    morreu.play();
                    double t = 0;

                    while (t < 60) {
                        System.out.println(t);
                        t += 0.001 * window.deltaTime();
                    }
                    morreu.stop();
                    System.out.println("morreu");
                    player.die();

                }
            }
        }
    }

}
