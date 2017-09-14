package cr.ac.ucr.ecci.ci1330.SpringContainer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

public abstract class AbstractBeanFactory implements BeanFactoryContainer {

    protected HashMap<String, Bean> beanHashMap;

    public AbstractBeanFactory() {
        this.beanHashMap = new HashMap<>();
    }

    public Bean createBean(String id) {
        return null;
    }

    public void destroyBean(String id) {
        Bean bean = beanHashMap.get(id);
        beanHashMap.remove(id);

        this.executeBeanInstanceMethod(bean, bean.getDestructMethod());
    }

    public Object getBean(String id) {
        if (beanHashMap.get(id).getScopeType().equals(ScopeType.PROTOTYPE)) {
            Bean bean = createBean(id);
            return bean.getBeanInstance();
        }
        return beanHashMap.get(id).getBeanInstance();
    }

    public Bean findBean(String id) {
        return null;
    }

    public void executeBeanInstanceMethod(Bean bean, String methodName) {
        Class instance = null;
        try {
            instance = Bean.class.getDeclaredField("beanInstance").getType(); // Recupera el tipo de la instancia del Bean
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        Method method = null;
        try {
            method = instance.getDeclaredMethod(methodName); // Recupera el metodo recibido por parametro
        } catch (NoSuchMethodException e) {
            // Si la instancia no tiene el metodo respectivo entonces no hace nada
        }

        if (method != null) { // Si existe el metodo respectivo
            try {
                method.invoke(bean.getBeanInstance());
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}