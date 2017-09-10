package cr.ac.ucr.ecci.ci1330.SpringContainer;

public interface BeanFactoryContainer {

    Bean createBean(String id);

    Object getBean(String id);

    void destroyBean(String id);
}
