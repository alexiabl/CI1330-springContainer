package main.java.cr.ac.ucr.ecci.ci1330.SpringContainer;

import java.util.HashMap;

public abstract class AbstractBeanFactory implements BeanFactoryContainer {

    HashMap<String, Bean> beanHashMap = new HashMap<String, Bean>();

    public AbstractBeanFactory() {
        System.out.println("hola");
    }

    @Override
    public Bean createBean() {
        return null;
    }

    @Override
    public Bean destroyBean(String id) {
        return null;
    }

    @Override
    public Bean getBean(String id) {
        return null;
    }

    private String detectAutowiringMode(String id) {
        return null;
    }

    private Bean findBean(String id) { return null; }
}
