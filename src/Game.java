/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Eduardo
 */
import java.awt.Color;
import java.awt.Font;
import javax.swing.JOptionPane;
//import java.awt.Point;
//import java.util.ArrayList;
//import java.util.Vector;
import jplay.GameImage;
import jplay.Keyboard;
import jplay.Scene;
import jplay.Sound;
import jplay.Time;
import jplay.Window;
//import jplay.TileInfo;

class Game {

    static Window window;
    static Scene scene;
    static Keyboard keyboard;
    static GameImage fundo;

    static void setMandouSair(boolean b) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    int tempoAtual, resp;
    Time tempo;
    Font fonteTempo, fontePont;
    private boolean pausado, mandouSair, mandouContinuar;
    private MenuFrame menu;
    private LoginFrame loginMenu;
    private static Sound musica, somPause;
    static Player player;
    private static Coin[] moedas;
    private String pont;

    void carregar(int volumeMusica) {
        window = MenuInicial.janela;
        scene = new Scene();
        scene.loadFromFile("fases/fase1.scn");
        fundo = new GameImage("images/fundo.png");
        scene.setDrawStartPos(-30, 470);
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
        somPause = new Sound("sons/pause.wav");
        player = new Player(30, 0, 600);
        scene.addOverlay(player);
        scene.moveScene(player);
        moedas = new Coin[30];
        for (int i = 0; i < moedas.length; i++) {
            moedas[i] = new Coin();
            moedas[i].x = 55 + 25 * i;
            moedas[i].y = 560;
        }
    }

    private void executar(boolean playMusic) {
        boolean continuar = true;
        if (playMusic) {
            musica.play();
        }
        while (continuar) {
            if (pausado == false) {
                draw();
            }
            if (keyboard.keyDown(Keyboard.ESCAPE_KEY)) {
                abrirMenu();
            }
        }
    }

    private void draw() {
        player.move();
        player.update();
        scene.draw();
        window.drawText(String.format("%03d", (Object) tempo.getTotalSecond()) + "", 650, 40, Color.WHITE, fonteTempo);
        window.drawText(String.format("%05d", (Object) player.getPontuacao()), 100, 35, Color.BLACK, fontePont);
        atualizarVidas();
        atualizarMoedas();
        window.update();
    }

    public Game(boolean executarMusica, int volumeMusica, int dificuldade) {
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
            window.exit();
        } else if (resp == JOptionPane.YES_OPTION) {
            loginMenu.setNovaPont(player.getPontuacao());
            loginMenu.setVisible(true);            
        } else {
            fecharMenu();
        }
    }

    private void atualizarVidas() {
        for (int i = 0; i < player.getVidas(); i++) {
            player.hearts[i].x = 10 + i * 25;
            player.hearts[i].y = 10;
            player.hearts[i].draw();
        }
    }

    private void atualizarMoedas() {
        for (Coin moeda : moedas) {
            if (player.collided(moeda) && (!moeda.foiPega)) {
                player.addPoints(10);
                moeda.foiPega = true;
                moeda.playSound();
            }
            if (!moeda.foiPega) {
                moeda.update();
                moeda.draw();
            }
        }
    }

    private void playSound(Sound som) {
        som.play();
    }
}
