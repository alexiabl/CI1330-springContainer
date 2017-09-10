package cr.ac.ucr.ecci.ci1330.SpringContainer;


import java.util.HashMap;

public abstract class AbstractBeanFactory implements BeanFactoryContainer {

    protected HashMap<String, Bean> beanHashMap;

    public AbstractBeanFactory() {
        this.beanHashMap = new HashMap<String, Bean>();
    }

    @Override
    public Bean createBean(String id) { return null; }

    @Override
    public void destroyBean(String id) {
        beanHashMap.remove(id);
        /*Aquí se ejecuta el método del bean, el destructMethod
        * Se llama al método executeDestructMethod*/
    }

    @Override
    public Object getBean(String id) {
        if(beanHashMap.get(id).getScopeType().equals(ScopeType.PROTOTYPE)){
            return createBean(id);
        }
        return beanHashMap.get(id).getBeanInstance();
    }

    private String detectAutowiringMode(String beanTag) {
        return null;
    }

    private Bean findBean(String id) { return null; }
}
