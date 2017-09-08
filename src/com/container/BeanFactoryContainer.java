package com.container;

public interface BeanFactoryContainer {

        Bean createBean();
        Bean getBean(String id);
        Bean destroyBean(String id);
}
