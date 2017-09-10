package main.java.cr.ac.ucr.ecci.ci1330.SpringContainer;

import java.io.*;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {

        HashMap<String, String> hashMap = new HashMap<String, String>();

        File file = new File("XMLPruebilla.txt");
        String path = file.getAbsolutePath();
        System.out.println("La ruta del fichero es: " + path + "\n");

        String cadena;
        String id = "";
        int caracter;
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file); // En vez de file, también puede ser el pathname
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader b = new BufferedReader(fileReader);
        try {
            while ((caracter = b.read()) != -1) { // Como el b.read() devuelve un número, entonces hasta que llegue al final
                if ((char) caracter == '<') {
                    caracter = b.read();
                    if ((char) caracter == 'b') { // Suponiendo que luego de todo "<b" sigue un bean
                        cadena = "<" + "b";
                        boolean finalTag = false; // boolean utilizado para saber que no encontré el cierre del bean
                        while (!finalTag) {
                            caracter = b.read();
                            cadena += (char) caracter;
                            if ((char) caracter == '<') {
                                caracter = b.read();
                                cadena += (char) caracter;
                                if ((char) caracter == '/') {
                                    caracter = b.read();
                                    cadena += (char) caracter;
                                    if ((char) caracter == 'b') { // Cuando encuentra el cierre del bean
                                        cadena = cadena + b.readLine();
                                        hashMap.put(id,cadena);
                                        System.out.println("Su id es: "+ id + "\n" + cadena);
                                        cadena = "";
                                        finalTag = true;
                                    }
                                }
                            } else if ((char) caracter == 'i') {
                                caracter = b.read();
                                cadena += (char) caracter;
                                if ((char) caracter == 'd') {
                                    caracter = b.read();
                                    cadena += (char) caracter;
                                    if ((char) caracter == '=') { //Cuando encuentra el inicio del id
                                        caracter = b.read();
                                        cadena += (char) caracter;
                                        caracter = b.read(); // Se hace un segundo b.read() para no guardar el "="
                                        cadena += (char) caracter;
                                        id = "" + (char) caracter; // Se reinicia el id
                                        while ((char) caracter != '"') {
                                            caracter = b.read();
                                            cadena += (char) caracter;
                                            if ((char) caracter != '"') {  // agreguelo a id, mientras no haya terminado
                                                id += (char) caracter;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            b.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("\nPublicación del contenido del hashMap:\n");
        System.out.println(hashMap.get("libro")+"\n");
        System.out.println(hashMap.get("autor"));
    }
}
