package cr.ac.ucr.ecci.ci1330.IoC;

/**
 * Created by majo_ on 27/9/2017.
 */
public class Estudiante {
    private String name;
    private String Age;
    private testeReflection testeReflection;

    public Estudiante(){
        name= "Maria";
    }

    public String getName() {
        return name;
    }


    public testeReflection getTesteReflection() {
        return testeReflection;
    }

    public void setTesteReflection(testeReflection testeReflection) {
        this.testeReflection = testeReflection;
    }

    public void setName(String name) {
        this.name = name;
    }
}
