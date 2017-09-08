package main.java.cr.ac.ucr.ecci.ci1330.SpringContainer;

public interface BeanFactoryContainer {

        Bean createBean();
        Bean getBean(String id);
        Bean destroyBean(String id);
}
