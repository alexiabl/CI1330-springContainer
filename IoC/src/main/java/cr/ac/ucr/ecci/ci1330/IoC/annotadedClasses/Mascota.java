package cr.ac.ucr.ecci.ci1330.IoC.annotadedClasses;

import cr.ac.ucr.ecci.ci1330.IoC.testeReflection;

/**
 * Created by alexiaborchgrevink on 9/26/17.
 */
public class Mascota {

    //clase de prueba
    private String name;

    private int edad;


    public Mascota(){
        this.name= "pinocho";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

}
