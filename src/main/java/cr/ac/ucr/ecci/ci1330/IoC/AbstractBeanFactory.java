package cr.ac.ucr.ecci.ci1330.IoC;

import cr.ac.ucr.ecci.ci1330.IoC.BeanGraph.BeanGraph;
import cr.ac.ucr.ecci.ci1330.IoC.Bean.Bean;
import cr.ac.ucr.ecci.ci1330.IoC.Bean.Dependency;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Creates beans and their instances, injecting dependencies. Also, returns bean instance when user asks for it.
 *
 * @author María José Cubero
 * @author Renato Mainieri
 */
public class AbstractBeanFactory implements BeanFactoryContainer {

    protected HashMap<String, Bean> beanHashMap;
    protected BeanGraph beanGraph;

    public AbstractBeanFactory() {
        this.beanHashMap = new HashMap<>();
        this.beanGraph = new BeanGraph();
    }

    public HashMap<String, Bean> getBeanHashMap() {
        return beanHashMap;
    }

    /**
     * Obtains a bean information a creates a bean but not its instance.
     *
     * @param beanInformation
     * @return Created bean
     * @author María José Cubero
     */
    public Bean createBean(HashMap<String, Object> beanInformation) {
        Bean bean = null;
        Class definedClass = null;
        try {
            definedClass = Class.forName((String) beanInformation.get("className"));
        } catch (ClassNotFoundException e) {
            System.out.println("La clase " + beanInformation.get("className") + "no existe");
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
                if (autowiringName.equalsIgnoreCase("byName") || autowiringName.equalsIgnoreCase("byType")) {
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
            if (beanInformation.containsKey("lazy")) {
                bean.setLazy((Boolean) beanInformation.get("lazy"));
            }
            beanHashMap.put((String) beanInformation.get("id"), bean);
            beanGraph.addNode(bean.getId());
        }
        return bean;
    }

    /**
     * Adds bean dependecies ass edges of a node.
     *
     * @param bean
     * @author Renato Mainieri
     */
    protected void addEdges(Bean bean) {
        for (Dependency dependency : bean.getConstructorDependencies()) {
            beanGraph.addEdge(bean.getId(), dependency.getReference());
        }
        for (Dependency dependency : bean.getSetterDependencies()) {
            beanGraph.addEdge(bean.getId(), dependency.getReference());
        }
    }

    /**
     * Iterates the beansHashMap and creates the instances of each bean.
     *
     * @author María José Cubero
     * @author Renato Mainieri
     */
    protected void createBeanInstances() {
        for (Map.Entry<String, Bean> entry : beanHashMap.entrySet()) {
            addEdges(entry.getValue());
        }
        if (!beanGraph.reviewCyclesBean()) {
            for (Map.Entry<String, Bean> entry : beanHashMap.entrySet()) {
                Bean bean = entry.getValue();
                if (!bean.isLazy()) {
                    injectBeanInstance(bean);
                }
            }
        } else {
            System.exit(0);
        }
    }

    /**
     * Creates a bean instance with all of its needed dependencies.
     *
     * @param bean
     * @return Object of type bean instance
     * @author María José Cubero
     */
    protected Object injectBeanInstance(Bean bean) {
        if (bean.getBeanInstance() != null && bean.getScopeType().equals(ScopeType.SINGLETON)) {
            return bean.getBeanInstance();
        }
        Class newClass = null;
        try {
            newClass = Class.forName(bean.getClassName());
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
            boolean hasConstructor = false;
            if (bean.getConstructorDependencies().size() > 0) {
                hasConstructor = true;
                Object[] parameters = obtainBeanParameters(bean);
                if (parameters != null) {
                    bean.setBeanInstance(injectConstructorDependencies(newClass, parameters));
                } else {
                    beanHashMap.remove(bean.getId());
                }
            }
            if (bean.getSetterDependencies().size() > 0) { //es con setter
                Object[] parameters = obtainBeanParameters(bean);
                if (parameters != null) {
                    for (int i = 0; i < parameters.length; i++) {
                        String setterName = obtainSetterName(bean.getSetterDependencies().get(i).getName());
                        bean.setBeanInstance(injectSetterDependencies(hasConstructor, bean, newClass, setterName, parameters[i]));
                    }
                } else {
                    beanHashMap.remove(bean.getId());
                }
            }
            instance = bean.getBeanInstance();
        }
        executeBeanInstanceMethod(bean, bean.getInitMethod());
        return instance;
    }

    /**
     * Obtains the parameters of the bean, checks if it has constructor dependencies or setter dependencies, then calls obtainParameters
     *
     * @param bean
     * @return Object[]: an array of the bean parameters instantieted.
     * @author María José Cubero
     */
    protected Object[] obtainBeanParameters(Bean bean) {
        Object[] parameters = null;
        if (bean.getConstructorDependencies().size() > 0) {
            List<Dependency> constructorDependencies = bean.getConstructorDependencies();
            parameters = obtainParameters(constructorDependencies, bean);
            bean.getConstructorDependencies().clear();
        } else if (bean.getSetterDependencies().size() > 0) {
            List<Dependency> setterDependencies = bean.getSetterDependencies();
            parameters = obtainParameters(setterDependencies, bean);
        }
        return parameters;
    }

    /**
     * Obtains the parameters of the bean iterating the bean dependencies, checks if it has to get the current parameter byName or byType.
     *
     * @param dependencies
     * @param bean
     * @return Object[]: an array of the bean parameters instantieted.
     * @author María José Cubero
     */
    private Object[] obtainParameters(List<Dependency> dependencies, Bean bean) {
        Object[] parameters = new Object[dependencies.size()];
        for (int i = 0; i < dependencies.size(); i++) {
            Dependency currentDependency = dependencies.get(i);
            if (currentDependency.getAutowiringMode().equals(AutowiringMode.BYNAME)) {
                parameters[i] = obtainParameterByName(currentDependency.getReference(), bean);
                if (parameters[i].equals(null)) {
                    return null;
                }
            } else {
                parameters[i] = obtainParameterByType(currentDependency.getReference(), bean);
                if (parameters[i].equals(null)) {
                    return null;
                }
            }
        }
        return parameters;
    }

    /**
     * Creates a parameter instance when it is asked to be created byType.
     *
     * @param reference
     * @param bean
     * @return Object: a parameter instance.
     * @author María José Cubero
     */
    private Object obtainParameterByType(String reference, Bean bean) {
        Object instance = null;
        boolean found = false;
        Iterator<Map.Entry<String, Bean>> entries = beanHashMap.entrySet().iterator();
        while (!found && entries.hasNext()) {
            Map.Entry<String, Bean> entry = entries.next();
            if (entry.getValue().getClassName().equals(reference)) {
                found = true;
                Bean dependencyBean = entry.getValue();
                instance = injectBeanInstance(dependencyBean);
                return instance;
            }
        }
        if (!found) {
            System.out.println("La dependencia de " + bean.getId() + ", " + reference + ", no hace referencia a ningún bean");
        }
        return instance;
    }

    /**
     * Creates a parameter instance when it is asked to be created byName.
     *
     * @param reference
     * @param bean
     * @return Object: a parameter instance.
     * @author María José Cubero
     * @author Renato Mainieri
     */
    private Object obtainParameterByName(String reference, Bean bean) {
        if (beanHashMap.containsKey(reference)) {
            Bean dependencyBean = beanHashMap.get(reference);
            Object instance = injectBeanInstance(dependencyBean);
            return instance;
        } else {
            System.out.println("La dependencia de " + bean.getId() + ", " + reference + ", no hace referencia a ningún bean");
            return null;
        }
    }

    /**
     * Injects setter dependencies to a bean instance.
     *
     * @param hasConstructor: to define if the instance was already created
     * @param bean
     * @param newClass
     * @param setterName
     * @param parameter
     * @return Object: a bean instance with its setter dependencies injected.
     * @author María José Cubero
     * @author Renato Mainieri
     */
    protected Object injectSetterDependencies(Boolean hasConstructor, Bean bean, Class newClass, String setterName, Object parameter) {
        Object objectInstance = null;
        if (!hasConstructor) {
            try {
                //ver que pasa si el constructor tiene parametros
                objectInstance = newClass.newInstance();
            } catch (InstantiationException e) {
                System.out.println("ERROR: El constructor de " + newClass.getName() + " tiene parámetros cuando inyecta en un setter.");
                return null;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else {
            objectInstance = bean.getBeanInstance();
        }
        Method[] methods = null;
        try {
            methods = newClass.getMethods();
        } catch (Exception e) {
            System.out.println("No se pudo recuperar el método: " + setterName);
            e.printStackTrace();
        }
        try {
            obtainMethodtoInvoke(methods, setterName).invoke(objectInstance, parameter);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return objectInstance;
    }

    /**
     * Injects constructor dependencies to a bean instance.
     *
     * @param newClass
     * @param parameters
     * @return Object: a bean instance with its constructor dependencies injected.
     * @author María José Cubero
     * @author Renato Mainieri
     */
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

    /**
     * Looks for the constructor of the class that matches the specified constructor.
     *
     * @param constructors
     * @param parameters
     * @return int: the position of the matching constructor.
     * @author María José Cubero
     * @author Renato Mainieri
     */
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

    /**
     * Looks for a method that has to be invoked.
     *
     * @param methods
     * @param setterName
     * @return Method: method to invoke.
     * @author María José Cubero
     */
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
        if (found) {
            return methods[i];
        } else {
            System.out.println("El método de " + setterName + " no se encontró.");
            return null;
        }
    }

    /**
     * Obtains the name of the setter that has to be invoked.
     *
     * @param fieldName
     * @return String: name of the setter.
     * @author María José Cubero
     * @author Renato Mainieri
     */
    protected String obtainSetterName(String fieldName) {
        fieldName = fieldName.toLowerCase();
        fieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1).toLowerCase();
        String setterName = "set" + fieldName;
        return setterName;
    }

    protected HashMap<String, Object> obtainBeanAttributes(String id) {
        return null;
    }

    /**
     * Obtains a bean instance when it is asked by its id.
     *
     * @param id
     * @return Object: bean instance.
     * @author María José Cubero
     * @author Renato Mainieri
     */
    public Object getBean(String id) {
        try {
            if (beanHashMap.get(id).isLazy() && beanHashMap.get(id).getBeanInstance().equals(null)) {
                Bean bean = createBean(obtainBeanAttributes(id));
                beanHashMap.put(id, bean);
                addEdges(bean);
                bean.setBeanInstance(injectBeanInstance(bean));
                return bean.getBeanInstance();
            }
            if (beanHashMap.get(id).getScopeType().equals(ScopeType.PROTOTYPE)) {
                Bean bean = createBean(obtainBeanAttributes(id));
                bean.setBeanInstance(injectBeanInstance(bean));
                return bean.getBeanInstance();
            }
            return beanHashMap.get(id).getBeanInstance();
        } catch (NullPointerException e) {
            System.out.println("El id '" + id + "' no identifica nigún bean.");
            return null;
        }
    }

    /**
     * Calls the destruct method
     *
     * @param id
     * @author Renato Mainieri
     */
    public void destroyBean(String id) {
        Bean bean = beanHashMap.remove(id);
        executeBeanInstanceMethod(bean, bean.getDestructMethod());
    }

    /**
     * Executes the init/destruct method
     *
     * @param bean
     * @param methodName
     * @author Renato Mainieri
     */
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
