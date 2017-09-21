package cr.ac.ucr.ecci.ci1330.SpringContainer;

/**
 * Created by majo_ on 17/9/2017.
 */
public class testeReflection {
    private int num;
    private Bean bean;

    public testeReflection(){
        this.num= 5;
    }

    public testeReflection(String j){}

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
