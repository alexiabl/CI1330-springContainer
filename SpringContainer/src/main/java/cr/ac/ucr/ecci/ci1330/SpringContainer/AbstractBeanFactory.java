package main.java.cr.ac.ucr.ecci.ci1330.SpringContainer;

import java.util.HashMap;

public abstract class AbstractBeanFactory implements BeanFactoryContainer{

    HashMap<String, Bean> beanHashMap = new HashMap<String, Bean>();

    public AbstractBeanFactory() {
    }

    @Override
    public Bean createBean() {
        return null;
    }

    private String detectAutowiringMode(String id) {
        return null;
    }
}
