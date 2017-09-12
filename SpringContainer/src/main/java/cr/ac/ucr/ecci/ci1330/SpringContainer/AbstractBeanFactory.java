package cr.ac.ucr.ecci.ci1330.SpringContainer;


import nu.xom.Element;

import java.util.HashMap;

public abstract class AbstractBeanFactory implements BeanFactoryContainer {

    protected HashMap<String, Bean> beanHashMap;

    public AbstractBeanFactory() {
        this.beanHashMap = new HashMap<String, Bean>();
    }

    public Bean createBean(String id) {
        return null;
    }

    public void destroyBean(String id) {
        beanHashMap.remove(id);
        /*Aquí se ejecuta el método del bean, el destructMethod
        * Se llama al método executeDestructMethod*/
    }

    public Object getBean(String id) {
        if (beanHashMap.get(id).getScopeType().equals(ScopeType.PROTOTYPE)) {
            Bean bean = createBean(id);
            //aquí va el init method
            return bean.getBeanInstance();
        }
        return beanHashMap.get(id).getBeanInstance();
    }

    public Bean findBean(String id) {
        return null;
    }
}
