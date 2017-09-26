package cr.ac.ucr.ecci.ci1330.SpringContainer;

import cr.ac.ucr.ecci.ci1330.SpringContainer.AnnotationBased.*;

/**
 * Created by Renato on 23/09/2017.
 */

@Component(value = "Persona")
public class Persona {

    private int edad;
    private String nombre;

    @Scope(scopeType = ScopeType.PROTOTYPE)
    public Persona() {
        this.edad = 18;
        this.nombre = "Alberto";
    }


    public int getEdad() {
        return edad;
    }

    @Autowired
    public void setEdad(int edad) {
        this.edad = edad;
    }


    public String getNombre() {
        return nombre;
    }

    @Autowired
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @PostConstruct
    public void initMethod(){
        System.out.println("**Mensaje: Se llama al metodo initMethod de la clase Persona**");
    }

    @PreDestruct
    public void destructMethod(){
        System.out.println("**Mensaje: Se llama al metodo destructMethod de la clase Persona**");
    }
}
