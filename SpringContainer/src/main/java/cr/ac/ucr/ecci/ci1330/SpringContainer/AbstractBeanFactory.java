package cr.ac.ucr.ecci.ci1330.SpringContainer;


import nu.xom.Element;

import java.util.HashMap;

public abstract class AbstractBeanFactory implements BeanFactoryContainer {

    protected HashMap<String, Bean> beanHashMap;

    public AbstractBeanFactory() {
        this.beanHashMap = new HashMap<String, Bean>();
    }

    public Bean createBean(Element object) { return null; }

    public void destroyBean(String id) {
        beanHashMap.remove(id);
        /*Aquí se ejecuta el método del bean, el destructMethod
        * Se llama al método executeDestructMethod*/
    }

    public Object getBean(String id) {
        if(beanHashMap.get(id).getScopeType().equals(ScopeType.PROTOTYPE)){
            return createBean(id);
        }
        return beanHashMap.get(id).getBeanInstance();
    }

    protected String detectAutowiringMode(String beanTag) {
        return null;
    }

    protected Bean findBean(String id, String path) { return null; }
}
