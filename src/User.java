
/**
 *
 * @author Eduardo
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Formatter;
import java.util.logging.Level;
import java.util.logging.Logger;
public class User {

    public static void main(String[] args) {
        File dados = new File("temp");
        try {
            Scanner leitor = new Scanner(dados);
        } catch (FileNotFoundException ex) {
            System.err.println("Erro!");
        }
    }

}
