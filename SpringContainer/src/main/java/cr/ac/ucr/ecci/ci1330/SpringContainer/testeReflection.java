package cr.ac.ucr.ecci.ci1330.SpringContainer;

/**
 * Created by majo_ on 17/9/2017.
 */
public class testeReflection {
    private int num;
    private Persona persona;

    public testeReflection(){
        this.num= 5;
    }

    public testeReflection(Persona persona){
        this.persona = persona;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

}
