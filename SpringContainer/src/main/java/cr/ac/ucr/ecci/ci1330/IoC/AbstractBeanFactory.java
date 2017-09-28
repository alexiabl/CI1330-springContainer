package cr.ac.ucr.ecci.ci1330.IoC;

import cr.ac.ucr.ecci.ci1330.IoC.beanGraph.BeanGraph;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by majo_ on 22/9/2017.
 */
public class AbstractBeanFactory implements BeanFactoryContainer {

    protected HashMap<String, Bean> beanHashMap;
    private BeanGraph beanGraph;

    public AbstractBeanFactory() {
        this.beanHashMap = new HashMap<>();
        this.beanGraph = new BeanGraph();
    }

    public HashMap<String, Bean> getBeanHashMap() {
        return beanHashMap;
    }

    public Bean createBean(HashMap<String, Object> beanInformation) {
        Bean bean = null;
        Class definedClass = null;
        try {
            definedClass = Class.forName((String) beanInformation.get("className"));
        } catch (ClassNotFoundException e) {
            System.out.println("La clase no existe");
        }
        if (definedClass != null) {
            bean = new Bean();
            bean.setId((String) beanInformation.get("id"));
            bean.setClassName((String) beanInformation.get("className"));
            if (beanInformation.containsKey("initMethod")) {
                bean.setInitMethod((String) beanInformation.get("initMethod"));
            }
            if (beanInformation.containsKey("destructMethod")) {
                bean.setDestructMethod((String) beanInformation.get("destructMethod"));
            }
            if (beanInformation.containsKey("autowiringMode")) {
                String autowiringName = (String) beanInformation.get("autowiringMode");
                if (autowiringName.equals("byName") || autowiringName.equals("byType")) {
                    bean.setAutowiringMode(AutowiringMode.valueOf((String) beanInformation.get("autowiringMode")));
                }
            }
            if (beanInformation.containsKey("scopeType")) {
                bean.setScopeType(ScopeType.valueOf((String) beanInformation.get("scopeType")));
            }
            if (beanInformation.containsKey("constructorDependencies")) {
                bean.setConstructorDependencies((List<Dependency>) beanInformation.get("constructorDependencies"));//por constructor
            }
            if (beanInformation.containsKey("setterDependencies")) {
                bean.setSetterDependencies((List<Dependency>) beanInformation.get("setterDependencies")); //por setter
            }
            beanHashMap.put((String) beanInformation.get("id"), bean);
            beanGraph.addNode(bean.getId());
        }
        return bean;
    }

    protected void createBeanInstances() {
        for (Map.Entry<String, Bean> entry : beanHashMap.entrySet()) {
            for (Dependency dependency : entry.getValue().getConstructorDependencies()) {
                beanGraph.addEdge(entry.getValue().getId(),dependency.getReference());
            }
            for (Dependency dependency : entry.getValue().getSetterDependencies()) {
                beanGraph.addEdge(entry.getValue().getId(),dependency.getReference());
            }
        }
        if (!beanGraph.reviewCyclesBean()) {
            for (Map.Entry<String, Bean> entry : beanHashMap.entrySet()) {
                Bean bean = entry.getValue();
                injectBeanInstance(bean);
            }
        }
        else {
            System.exit(0);
        }
    }

    protected Object injectBeanInstance(Bean bean) {
        Class newClass = null;
        try {
            newClass= Class.forName(bean.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Object instance = null;
        if (!bean.hasDependencies()) {
            try {
                instance = newClass.newInstance();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            bean.setBeanInstance(instance);
        } else {
            Object[] parameters = obtainBeanParameters(bean);
            if (bean.getConstructorDependencies().size() > 0) {
                bean.setBeanInstance(injectConstructorDependencies(newClass, parameters));
            } else { //es con setter
                String setterName = obtainSetterName(bean.getSetterDependencies().get(0).getName());
                bean.setBeanInstance(injectSetterDependencies(newClass, setterName, parameters[0]));
            }
        }
        executeBeanInstanceMethod(bean, bean.getInitMethod());
        return instance;
    }

    protected boolean hasDependencies(Bean bean) {
        if (bean.getConstructorDependencies().size() > 0 || bean.getSetterDependencies().size() > 0) {
            return true;
        }
        return false;
    }

    protected Object[] obtainBeanParameters(Bean bean) {
        Object[] parameters;
        if (bean.getConstructorDependencies().size() > 0) {
            parameters = new Object[bean.getConstructorDependencies().size()];
            List<Dependency> constructorDependencies = bean.getConstructorDependencies();
            for (int i = 0; i < constructorDependencies.size(); i++) {
                Dependency currentDependency = constructorDependencies.get(i);
                if (currentDependency.getAutowiringMode().equals(AutowiringMode.BYNAME)) {
                    Bean dependencyBean = beanHashMap.get(currentDependency.getReference());
                    Object instance = injectBeanInstance(dependencyBean);
                    parameters[i] = instance;
                } else {

                }
            }
        } else {
            parameters = new Object[]{new Object()};;

            Dependency dependency = bean.getSetterDependencies().get(0);
            if (dependency.getAutowiringMode().equals(AutowiringMode.BYNAME)) {
                Bean dependencyBean = beanHashMap.get(dependency.getReference());
                Object instance = injectBeanInstance(dependencyBean);
                parameters[0] = instance;
            } else {

            }
        }
        return parameters;
    }


    protected Object injectSetterDependencies(Class newClass, String setterName, Object parameter) {
        Object objectInstance = null;
        try {
            //ver que pasa si el constructor tiene parametros
            objectInstance = newClass.newInstance();
        } catch (InstantiationException e) {
            System.out.println("ERROR: El constructor de " + newClass.getName() + " tiene parámetros cuando inyecta en un setter.");
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Method[] methods = null;
        try {
            methods = newClass.getMethods();
        } catch (Exception e) {
            System.out.println("No se pudo recuperar el método: " + setterName);
            e.printStackTrace();
        }
            System.out.println(setterName);
            System.out.println(obtainMethodtoInvoke(methods, setterName).getName());
            System.out.println(objectInstance.toString());
            System.out.println("param "+parameter.toString());
        try {
            obtainMethodtoInvoke(methods, setterName).invoke(objectInstance, parameter);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        System.out.println(newClass.getName());
        return objectInstance;
    }

    //devuelve la instancia con las dependencias agregadas
    protected Object injectConstructorDependencies(Class newClass, Object[] parameters) {
        Object objectInstance = new Object[]{new Object()};
        Constructor[] constructors = newClass.getConstructors();
        Constructor constructor = null;
        int constructorPosition = lookForConstructor(constructors, parameters);
        if (constructorPosition == -1) {
            System.out.println("El constructor de la clase: " + newClass.getName() + " no hace match con los parámetros brindados.");
            return null;
        }
        constructor = constructors[constructorPosition];
        try {
            objectInstance = constructor.newInstance(parameters);
        } catch (NullPointerException e) {
            System.out.println("no se creó bien la instancia con el constructor. M: injectconstDep");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return objectInstance;
    }

    protected int lookForConstructor(Constructor[] constructors, Object[] parameters) {
        boolean found = false;
        boolean equal = true;
        int i = 0;
        int j;
        while (!found && i < constructors.length) {
            Class[] parameterTypes = constructors[i].getParameterTypes();
            if (constructors[i].getParameterCount() == parameters.length) {
                j = 0;
                equal = true;
                while (equal && j < parameters.length) {
                    String parameter = parameters[j].getClass().toString();
                    if (parameter.equals(parameterTypes[j].toString())) {
                        if (j == parameters.length - 1) {
                            found = true;
                        }
                    } else {
                        equal = false;
                    }
                    j++;
                }
            }
            i++;
        }
        if (!found) {
            return -1;
        }
        return i - 1;
    }

    //determina cual es el método que se va a invocar a partit del nombre del mismo
    protected Method obtainMethodtoInvoke(Method[] methods, String setterName) {
        boolean found = false;
        int i = 0;
        while (!found && i < methods.length) {
            if (methods[i].getName().equals(setterName)) {
                found = true;
            } else {
                i++;
            }
        }
        return methods[i];
    }

    protected String obtainSetterName(String fieldName) {
        fieldName = fieldName.replace(fieldName.substring(0, 1), fieldName.substring(0, 1).toUpperCase());
        String setterName = "set" + fieldName;
        return setterName;
    }

    public Object getBean(String id) {
        return null;
    }


    public void destroyBean(String id) {
        Bean bean = beanHashMap.remove(id);
        executeBeanInstanceMethod(bean, bean.getDestructMethod());
    }

    public Bean findBean(String id) {
        return null;
    }


    public void executeBeanInstanceMethod(Bean bean, String methodName) {
        Class instance = null;
        try {
            instance = Class.forName(bean.getClassName()); // Recupera el tipo de la instancia del Bean
        } catch (ClassNotFoundException e) {
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
