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

        Class instance = null;
        try {
            instance = Bean.class.getDeclaredField("beanInstance").getType(); //creo que asi agarra la clase
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        Method methods[] = instance.getDeclaredMethods();
        Method method;
        boolean execute = false;
        for (int i = 0; i < methods.length && !execute; i++) { // Busca el metodo que corresponda al destructMethod
            method = methods[i];
            if (method.toString() == bean.getDestructMethod()) { // Cuando lo encuentra, lo ejecuta y se sale del ciclo.
                try {
                    method.invoke(bean.getBeanInstance());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                execute = true;
            }
        }
        bean = null; //??
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

    public void executeBeanInitMethod(Bean bean) {
        Class instance = null;
        try {
            instance = Bean.class.getDeclaredField("beanInstance").getType(); //creo que asi agarra la clase
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        Method methods[] = instance.getDeclaredMethods();
        Method method;
        boolean execute = false;
        for (int i = 0; i < methods.length && !execute; i++) {  // Busca el metodo que corresponda al initMethod
            method = methods[i];
            if (method.toString() == bean.getInitMethod()) { // Cuando lo encuentra, lo ejecuta y se sale del ciclo
                try {
                    method.invoke(bean.getBeanInstance());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
                execute = true;
            }
        }
    }
}
