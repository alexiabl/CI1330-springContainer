package cr.ac.ucr.ecci.ci1330.IoC;

/**
 * Created by majo_ on 22/9/2017.
 */
public class testeReflection {
    private int num;
    private Bean bean;
    Bean n;
    Persona p;
    Persona a;

    public testeReflection(){
        this.num= 5;
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
}
