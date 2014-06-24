
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
    private ArrayList enemys;
    private ArrayList enemysVoadores;
    private ArrayList barraMovel;
    private Fim fimJogo;

    void carregar(int volumeMusica) {
        window = MenuInicial.janela;
        scene = new Scene();
        drawX = -30;
        scene.setDrawStartPos(drawX, drawY);

        //enemy = new Enemy("images/Mario.png", 6);
        enemys = new ArrayList();
        enemysVoadores = new ArrayList();
        barraMovel = new ArrayList();

        fimJogo = new Fim();
        if (dif == 0) {
            scene.loadFromFile("fases/fase1.scn");
            System.out.println("difi");
        }
        if (dif == 1) {
            scene.loadFromFile("fases/fase2.scn");
            System.out.println("dif11111i");

        }
        if (dif == 2) {
            scene.loadFromFile("fases/fase3.scn");
            System.out.println("difi22222");

        }
        if (dif == 3) {
            scene.loadFromFile("fases/fase4.scn");
            System.out.println("difi333333");
        }
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
        setInimigoVoadorPosition();
        setFimJogo();
        setBarraPosition();
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

        ganhou();
        itensDraw();

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

    void vitoria() {
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
            window.drawText("uhul fofuxo! Você é brabo", 250, 200, Color.WHITE, fonteTempo);
            window.drawText("Pressino esc para sair", 225, 300, Color.WHITE, fontePont);
            window.drawText("ou enter para reiniciar o jogo", 25, 400, Color.WHITE, fontePont);
            window.update();
            if (keyboard.keyDown(Keyboard.ESCAPE_KEY)) {
                exitGame();
            } else if (keyboard.keyDown(Keyboard.ENTER_KEY)) {

                Game game = new Game(playMusic, vol, dif);
                game.setVida(player.getVidas());
            }
        }
    }

    public void setVida(int i) {
        player.setVida(i);
    }

    private void colisao() {

        colisaoInimigos();
        colisaoBarras();

        Point pt1 = new Point((int) (player.x - drawX), (int) player.y);
        Point pt2 = new Point((int) (player.x + player.width - drawX), (int) (player.y + player.height));

        Vector v = scene.getTilesFromPosition(pt1, pt2);
        for (int i = 0; i < v.size(); i++) {

            TileInfo rectAtual = (TileInfo) v.get(i);

            if (rectAtual.id != 7 && rectAtual.id != 8 && rectAtual.id != 14 && rectAtual.id != 9 && rectAtual.id != 11 && rectAtual.id != 12 && rectAtual.id != 13) {
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

            if (rectAtual.id == 7) {

                Coin m = new Coin();

                m.x = rectAtual.x + 2 * drawX / 3;
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
            itensAtualizar(-2);

        }
        if (player.x <= 2) {
            drawX += 2;
            player.x = 3;

            itensAtualizar(2);

        }
    }

    private void setInimigoVoadorPosition() {
        Point pt1 = new Point((int) drawX, (int) 0);
        Point pt2 = new Point(1000000, 1000000);

        Vector v = scene.getTilesFromPosition(pt1, pt2);

        int j = 0;
        int i = 0;
        enemysVoadores = new ArrayList();
        while (i < v.size()) {

            TileInfo rectAtual = (TileInfo) v.get(i);

            if (rectAtual.id == 14) {
                EnemyVoador inimigo = new EnemyVoador("images/fly_goomba.png", 4);
                inimigo.setPosMinMax(rectAtual.x - inimigo.width, rectAtual.y + rectAtual.width, rectAtual.x - inimigo.width);
                enemysVoadores.add(inimigo);
                // System.out.println("gerou");
            } else {
                if (rectAtual.id == 15) {

                    EnemyVoador inim = (EnemyVoador) enemysVoadores.get(j);
                    inim.setMax(rectAtual.x - inim.width);

                    // System.out.println("pronto");
                    j++;
                }
            }
            i++;

        }
    }

    private void setInimigoPosition() {
        Point pt1 = new Point((int) drawX, (int) 0);
        Point pt2 = new Point(1000000, 1000000);

        Vector v = scene.getTilesFromPosition(pt1, pt2);

        int j = 0;
        int i = 0;
        enemys = new ArrayList();
        while (i < v.size()) {

            TileInfo rectAtual = (TileInfo) v.get(i);

            if (rectAtual.id == 8) {
                Enemy inimigo = new Enemy("images/inimigo.png", 10);
                inimigo.setPosMinMax(rectAtual.x - inimigo.width, rectAtual.y + rectAtual.width, rectAtual.x - inimigo.width);
                enemys.add(inimigo);
                // System.out.println("gerou");
            } else {
                if (rectAtual.id == 9) {

                    Enemy inim = (Enemy) enemys.get(j);
                    inim.setMax(rectAtual.x - inim.width);

                    // System.out.println("pronto");
                    j++;
                }

            }
            i++;

        }
    }

    private void setBarraPosition() {
        Point pt1 = new Point((int) drawX, (int) 0);
        Point pt2 = new Point(1000000, 1000000);

        Vector v = scene.getTilesFromPosition(pt1, pt2);

        int j = 0;
        int i = 0;
        barraMovel = new ArrayList();
        while (i < v.size()) {

            TileInfo rectAtual = (TileInfo) v.get(i);

            if (rectAtual.id == 11) {
                BarraMovel b = new BarraMovel();
                b.setPosMinMax(rectAtual.x - b.width, rectAtual.y + rectAtual.width, rectAtual.x - b.width);
                barraMovel.add(b);
            } else {
                if (rectAtual.id == 12) {

                    BarraMovel barra = (BarraMovel) barraMovel.get(j);
                    barra.setMax(rectAtual.x - barra.width);
                    System.out.println("gerou");

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
                    setInimigoVoadorPosition();
                    setFimJogo();
                    setBarraPosition();
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

                    setInimigoVoadorPosition();
                    setFimJogo();
                    setBarraPosition();
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

        //voadores
        for (Object inimigo : enemysVoadores) {

            EnemyVoador e = (EnemyVoador) inimigo;
            if (!e.foiMorto) {
                GameObject rect3 = new GameObject();
                rect3.x = player.x;
                rect3.y = player.y + player.height;
                rect3.width = player.width;
                rect3.height = player.height / 2;
                if (rect3.collided(e)) {

                    player.isJumping();
                    player.setVelocityY(-1.3);
                    e.setFloor(600);

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
                    setInimigoVoadorPosition();
                    setFimJogo();
                    setBarraPosition();
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
                    setInimigoVoadorPosition();
                    setFimJogo();
                    setBarraPosition();
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

    private void itensDraw() {
        for (Object inimigo : enemys) {

            Enemy e = (Enemy) inimigo;
            if (!e.foiMorto) {
                e.move();
                e.draw();
            }
        }
        for (Object inimigo : enemysVoadores) {

            EnemyVoador e = (EnemyVoador) inimigo;
            if (!e.foiMorto) {
                e.move();
                e.draw();
                if (e.y > 500) {

                    e.foiMorto = true;
                }
            }
        }
        for (Object barra : barraMovel) {

            BarraMovel b = (BarraMovel) barra;

            b.move();
            b.draw();

        }
        fimJogo.draw();

    }

    private void itensAtualizar(int i) {
        fimJogo.x += i;
        for (Object inimigo : enemys) {

            Enemy e = (Enemy) inimigo;
            e.setMin(e.getMin() + i);
            e.setMax(e.getMax() + i);
            e.x += i;
        }
        for (Object inimigo : enemysVoadores) {

            EnemyVoador e = (EnemyVoador) inimigo;
            e.setMin(e.getMin() + i);
            e.setMax(e.getMax() + i);
            e.x += i;
        }
        for (Object barra : barraMovel) {

            BarraMovel e = (BarraMovel) barra;
            e.setMin(e.getMin() + i);
            e.setMax(e.getMax() + i);
            e.x += i;
        }
        for (Object moeda : moedas) {

            Coin m = (Coin) moeda;

            if (!m.foiPega) {
                m.x += i;
            }
        }
    }

    public void setFimJogo() {
        Point pt1 = new Point((int) player.x, (int) 0);
        Point pt2 = new Point(1000000, 600);

        Vector v = scene.getTilesFromPosition(pt1, pt2);

        int j = 0;
        int i = 0;
        while (i < v.size()) {

            TileInfo rectAtual = (TileInfo) v.get(i);

            if (rectAtual.id == 13) {

                fimJogo.x = rectAtual.x - fimJogo.width;
                fimJogo.setXInicial(fimJogo.x);
                fimJogo.y = rectAtual.y - fimJogo.height;
                break;
            }
            i++;
        }

    }

    private void colisaoBarras() {

        for (Object barra : barraMovel) {

            BarraMovel e = (BarraMovel) barra;
            GameObject rect3 = new GameObject();
            rect3.x = player.x;
            rect3.y = player.y + player.height;
            rect3.width = player.width;
            rect3.height = player.height / 2;
            if (rect3.collided(e)) {

                player.setFloor((int) (e.y + 1));
                break;
            } else {
                player.setFloor(640);
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

            GameObject rect4 = new GameObject();
            rect4.x = player.x;
            rect4.y = player.y - player.height / 3;
            rect4.width = player.width;
            rect4.height = player.height / 2;
            if (rect4.collided(e)) {

                player.y++;
                player.setVelocityY(0);
                break;

            }

            if (rect.collided(e)) {

                player.x = e.x + drawX - player.width;

                break;
            }

            if (rect2.collided(e)) {

                // player.x =rectAtual.x+rectAtual.width-drawX;
                player.x = e.x + drawX + e.width;

//                System.out.println("colidiu");
                break;
            }

        }
    }

    private void ganhou() {

        if (player.collided(fimJogo)) {

            VidaExtra vida = new VidaExtra();
            vida.playSomVida();

            player.vida();
            double i = 0;
            while (i < 60) {
                i += 0.1 * window.deltaTime();

            }
            vitoria();
        }
    }

}
