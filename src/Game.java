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
    int tempoAtual;
    Time tempo;
    Font fonteTempo, fontePont;
    boolean pausado;
    static MenuFrame menu;
    private static Sound musica,somPause;
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
        musica = new Sound("sons/musica1.wav");
        musica.setVolume(volumeMusica);
        somPause = new Sound("sons/pause.wav");
        player = new Player(30, 0, 600);
        scene.addOverlay(player);
        scene.moveScene(player);
        moedas = new Coin[15];
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
//            controlarColisoes();
            draw();
            if (keyboard.keyDown(Keyboard.ESCAPE_KEY)) {
                abrirMenu();
            }
        }
    }

    private void draw() {
        player.move();
        player.update();
        scene.draw();
        window.drawText(String.format("%03d",(Object) tempo.getTotalSecond()) + "", 650, 40, Color.WHITE, fonteTempo);
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
        musica.pause();
        playSound(somPause);
    }

    void fecharMenu() {

    }

    void setPausado(boolean b) {
        pausado = b;
    }

    static void exitGame() {
        window.exit();
    }

    private void atualizarVidas() {
        for (int i = 0; i < player.getVidas(); i++) {
            player.hearts[i].x = 10 + i * 25;
            player.hearts[i].y = 10;
            player.hearts[i].draw();
        }
    }
//
//    private void controlarColisoes() {
//        Point origemPlayer = new Point((int)player.x,(int)player.y);
//        Point maxPlayer = new Point((int)(player.x + player.width),(int)(player.y + player.height));
//        Vector tiles = scene.getTilesFromRect(origemPlayer,maxPlayer);
//        for (int i = 0; i < tiles.size(); i++) {
//            TileInfo tile = (TileInfo)tiles.elementAt(i);
//            if((tile.id != 5)&&(player.collided(tile))){
//                System.out.println(tile.id+" ("+tile.x+","+tile.y+")");
//                if(player.getStateOfX() != Constantes.STOP){
//                    if(player.x <= tile.x  - 1){
//                        player.x = tile.x - player.width;
//                    } else{
//                        player.x = tile.x + tile.width;
//                    }
//                } else{
//                    if(player.y >= tile.y + tile.height - 1){
//                        player.y = tile.y + tile.height;
//                    } else {
//                        if(player.y + player.height >= tile.y) {
//                            player.y = tile.y - player.height;
//                        }
//                    }
//                }
//            }
//        }
//    }
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

    private void playSound(Sound somPause) {
        somPause.play();
    }
}
