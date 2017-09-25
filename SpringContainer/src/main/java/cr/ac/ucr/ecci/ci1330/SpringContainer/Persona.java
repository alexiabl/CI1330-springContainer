package cr.ac.ucr.ecci.ci1330.SpringContainer;

/**
 * Created by Renato on 23/09/2017.
 */
public class Persona {
    private int edad;
    private String nombre;

    public Persona() {
        this.edad = 18;
        this.nombre = "Alberto";
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void initMethod(){
        System.out.println("**Mensaje: Se llama al metodo initMethod de la clase Persona**");
    }

    public void destructMethod(){
        System.out.println("**Mensaje: Se llama al metodo destructMethod de la clase Persona**");
    }
}
