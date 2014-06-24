/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Eduardo
 */
import com.sun.prism.paint.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import jplay.Window;
public class TelaRanking {
    private static Window window;
    private String senha;
    private String[] usuarios;
    private int[] pontuacoes;
    private final File dados;
    private int counter;
    private Scanner leitor;
    private Font fonte,fonte2;
    public TelaRanking(){
        window = Game.window;
        usuarios = new String[5];
        pontuacoes = new int[5];
        counter = 0;
        dados = new File("data");
        fonte = new Font("Super Mario Bros. 3 Regular", Font.TRUETYPE_FONT, 40);
        fonte2 = new Font("Super Mario Bros. 3 Regular",Font.PLAIN, 30);
        try {
            leitor = new Scanner(dados);
            window.clear(java.awt.Color.BLACK);
            window.drawText("top 5", 300 , 150, java.awt.Color.WHITE, fonte);
            while (leitor.hasNext()&&counter < 5){
                window.drawText(leitor.next(), 550,(200 + counter*30), java.awt.Color.WHITE, fonte2);
                window.drawText(leitor.next().toLowerCase(), 100,(200 + counter*30), java.awt.Color.WHITE, fonte2);
                leitor.next();
                counter++;
            }
            leitor.close();
            window.update();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TelaRanking.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
