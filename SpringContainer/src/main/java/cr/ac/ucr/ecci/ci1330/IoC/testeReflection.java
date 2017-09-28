package cr.ac.ucr.ecci.ci1330.IoC;

import cr.ac.ucr.ecci.ci1330.IoC.annotadedClasses.Mascota;
import cr.ac.ucr.ecci.ci1330.IoC.annotadedClasses.Persona;

/**
 * Created by majo_ on 22/9/2017.
 */
public class testeReflection {
    private int num;
    private Bean bean;
    Bean n;
    Persona p;
    Persona a;
    Mascota mascota;
    Estudiante estudiante;
    Estudiante est;

    public testeReflection(){
        this.p=p;
        //this.a= a;
        this.num= 5;
       this.mascota= new Mascota();
        estudiante= new Estudiante();

    }

    public Estudiante getEst() {
        return est;
    }

    public void setEst(Estudiante est) {
        this.est = est;
    }

    public Persona getP() {
        return p;
    }

    public void setP(Persona p) {
        this.p = p;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Bean getBean() {
        return bean;
    }

    public void setBean(Bean bean) {
        this.bean = bean;
    }

    public Bean getN() {
        return n;
    }

    public void setN(Bean n) {
        this.n = n;
    }

    public Persona getA() {
        return a;
    }

    public void setA(Persona a) {
        this.a = a;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }
}
