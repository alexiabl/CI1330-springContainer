package com.container;

import java.util.HashMap;

public abstract class AbstractBeanFactory implements BeanFactoryContainer{

    HashMap<String, Bean>   = new HashMap<String, Bean>();

    public AbstractBeanFactory() {
    }

    @Override
    public Bean createBean() {
        return null;
    }

    private String detectAutowiringMode(String id) {

    }
}
