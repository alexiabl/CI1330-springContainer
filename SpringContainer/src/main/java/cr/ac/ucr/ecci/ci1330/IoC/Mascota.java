package cr.ac.ucr.ecci.ci1330.IoC;

/**
 * Created by alexiaborchgrevink on 9/26/17.
 */
public class Mascota {

    //clase de prueba
    private String name;

    private int edad;

    public Mascota(String name, int edad){
        this.name=name;
        this.edad=edad;
    }

    public Mascota(){

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
