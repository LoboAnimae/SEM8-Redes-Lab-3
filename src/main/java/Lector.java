/*
    UVG
    Redes
    Davis Alvarez - 15842
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class Lector {
    private BufferedReader teclado;
    private String texto ;

    public Lector(){
        teclado = new BufferedReader (new InputStreamReader(System.in));
        texto = "";
    }


    public String leerLinea(){
        try {
            this.texto = teclado.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this.texto;
    }
}
