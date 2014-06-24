/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Eduardo
 */
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import jplay.Window;
import jplay.Mouse;

public class TelaRanking {

    private static Window window;
    private String senha;
    private final File dados;
    private int counter;
    private Scanner leitor;
    private final Font fonte, fonte2;
    private final Button exitButton, menuButton;
    private final Mouse mouse;

    public TelaRanking() {
        window = new Window(800,600);
        counter = 0;
        exitButton = new Button("images/exit_icon.png", 760, 10);
        menuButton = new Button("images/home_icon.png", 710, 10);
        dados = new File("data");
        fonte = new Font("Super Mario Bros. 3 Regular", Font.TRUETYPE_FONT, 40);
        fonte2 = new Font("Super Mario Bros. 3 Regular", Font.PLAIN, 30);
        mouse = window.getMouse();
        try {
            leitor = new Scanner(dados);
            window.clear(java.awt.Color.BLACK);
            exitButton.draw();
            menuButton.draw();
            window.drawText("top 5", 300, 150, java.awt.Color.WHITE, fonte);
            while (leitor.hasNext() && counter < 5) {
                window.drawText(leitor.next(), 550, (200 + counter * 30), java.awt.Color.WHITE, fonte2);
                window.drawText(leitor.next().toLowerCase(), 100, (200 + counter * 30), java.awt.Color.WHITE, fonte2);
                leitor.next();
                counter++;
            }
            leitor.close();
            window.update();
            executar();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TelaRanking.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void executar() {
//        while (true) {
            if (mouse.isOverObject(exitButton) && mouse.isLeftButtonPressed()) {
                window.exit();
            }
            if (mouse.isOverObject(menuButton) && mouse.isLeftButtonPressed()) {
                MenuInicial.executar();
            }
//        }
    }
}
