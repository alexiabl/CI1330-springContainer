package cr.ac.ucr.ecci.ci1330.IoC.annotadedClasses;

import cr.ac.ucr.ecci.ci1330.IoC.AnnotationBased.Annotations.*;
import cr.ac.ucr.ecci.ci1330.IoC.ScopeType;

/**
 * Created by alexiaborchgrevink on 9/26/17.
 */
@Component(id="Mascota")
public class Mascota {

    //clase de prueba
    private String name;

    private int edad;

    @Scope(scopeType = ScopeType.PROTOTYPE)
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

    @PostConstruct
    public void initMethod(){
        System.out.println("**Mensaje: Se llama al metodo initMethod de la clase Persona**");
    }

    @PreDestruct
    public void destructMethod(){
        System.out.println("**Mensaje: Se llama al metodo destructMethod de la clase Persona**");
    }

}
