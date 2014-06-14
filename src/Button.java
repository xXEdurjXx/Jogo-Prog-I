/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Eduardo
 */
import jplay.GameImage;
public class Button extends GameImage { 
    public Button(String caminho,int x,int y){
        super(caminho);
        this.x = x;
        this.y = y;
    }
}
