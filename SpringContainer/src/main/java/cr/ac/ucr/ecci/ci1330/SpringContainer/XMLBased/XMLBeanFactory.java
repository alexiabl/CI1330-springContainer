package cr.ac.ucr.ecci.ci1330.SpringContainer.XMLBased;


import cr.ac.ucr.ecci.ci1330.SpringContainer.AbstractBeanFactory;
import cr.ac.ucr.ecci.ci1330.SpringContainer.AutowiringMode;
import cr.ac.ucr.ecci.ci1330.SpringContainer.Bean;
import cr.ac.ucr.ecci.ci1330.SpringContainer.ScopeType;
import nu.xom.*;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class XMLBeanFactory extends AbstractBeanFactory {

    HashMap<String, Element> tagsBeanContent;
    private String xmlPath;

    public XMLBeanFactory(String xmlPath) {
        this.xmlPath = xmlPath;
        this.tagsBeanContent = new HashMap<>();
        try {
            readXML();
        } catch (ParsingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readXML() throws ParsingException, IOException {
        Builder builder = new Builder();
        Document xmlDoc = builder.build(xmlPath);
        Element root = xmlDoc.getRootElement();
        Elements beans = root.getChildElements();
        for (int i = 0; i < beans.size(); i++) {
            Element currentBean = beans.get(i);
            if (!tagsBeanContent.containsKey(currentBean.getAttributeValue("id"))) {
                tagsBeanContent.put(currentBean.getAttributeValue("id"), currentBean);
                createBean(currentBean.getAttributeValue("id"));
            }
        }
    }

    @Override
    public Bean createBean(String id) {
        Element element = tagsBeanContent.get(id);
        Bean bean = null;

        Class definedClass = null;
        try {
            definedClass = Class.forName(element.getAttributeValue("className"));
        } catch (ClassNotFoundException e) {
            tagsBeanContent.remove(id);
            System.out.println("La clase no existe");
        }

        if (definedClass != null) {

            /*if (element.getAttributeValue("autowiringMode").equals(AutowiringMode.valueOf("TYPE")) || element.getAttribute("autowiringMode").equals(null)) {

            } else { //byname

            }*/
            bean = new Bean();
            bean.setId(element.getAttributeValue("id"));
            bean.setClassName(element.getAttributeValue("className"));
            if (element.getAttribute("initMethod") != null) {
                bean.setInitMethod(element.getAttributeValue("initMethod"));
            }
            if (element.getAttribute("destructMethod") != null) {
                bean.setDestructMethod(element.getAttributeValue("destructMecthod"));
            }
            if (element.getAttribute("autowiringMode") != null) {
                String autowiringMode = element.getAttributeValue("autowiringMode").toUpperCase();
                bean.setAutowiringMode(AutowiringMode.valueOf(autowiringMode));
            }
            if (element.getAttribute("scopeType") != null) {
                String scopeType = element.getAttributeValue("scopeType").toUpperCase();
                bean.setScopeType(ScopeType.valueOf(scopeType));
            }

            bean.setBeanInstance(createBeanInstance(element));
            if (!beanHashMap.containsKey(id)&& bean.getBeanInstance()!= null) {
                System.out.println("agrego "+bean.getClassName());
                beanHashMap.put(id, bean);
            }
            executeBeanInstanceMethod(bean, bean.getInitMethod());
        }
      //  System.out.println("Se creó bean "+ bean.getId());
        return bean;
    }

    private Object createBeanInstance(Element element) {
        Object objectInstance = null;
        String className = element.getAttributeValue("className");
        Class newClass = null;
        try {
            newClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            System.out.println("no se encontró la clase en el forName. M: createBeanInstance");
        }
        //verificar que se está llamando a un constructor
        if (element.getAttribute("injectConstructor") != null) {
            //hay que hacer excepcion para cuando no trae parametros pero trae el senalador
            Elements parameters=  element.getChildElements();
            if(parameters.size()==0){
                try {
                    objectInstance= newClass.newInstance();
                } catch (Exception e) {
                    System.out.println("no se creó bien la instancia de la clase");
                }
            }else{
                objectInstance= injectConstructorDependencies(element, newClass);
            }
            if(objectInstance== null){
                return null;
            }
        }
        //verificar que está llamando a un setter
        else if (element.getAttribute("injectSetter") != null) {
            objectInstance= injectSetterDependencies(element, newClass);
            if(objectInstance== null){
                return null;
            }
        }
        // Crea una instancia con un constructor sin parámetros
        else {
            try {
                objectInstance= newClass.newInstance();
            } catch (Exception e) {
                System.out.println("no se creó bien la instancia de la clase");
            }
        }
        return objectInstance;
    }

    private Object injectConstructorDependencies(Element element, Class newClass) {
        Object objectInstance = null;
        try {
            objectInstance = newClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Object[] parameters = {new Object()};
        try {
            try {
                parameters = obtainConstructorDependencies(element);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if(parameters==null){
                return null;
            }
        } catch (ParsingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Constructor[] constructors = newClass.getConstructors();
        Constructor constructor = constructors[lookForConstructor(constructors, parameters)];
        System.out.println("parametro rar: "+constructor.getParameterCount());
        try {
            constructor.newInstance(objectInstance, parameters);
        } catch (Exception e) {
            System.out.println("no se creó bien la instancia con el constructor. M: createBeanInstance");
            e.printStackTrace();
        }
        return  objectInstance;
    }

    private Object injectSetterDependencies(Element element, Class newClass) {
        Object objectInstance = null;
        try {
            objectInstance = newClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        Element setterParameter = element.getChildElements().get(0);
        String fieldName = setterParameter.getAttributeValue("name");
        fieldName = fieldName.replace(fieldName.substring(0, 1), fieldName.substring(0, 1).toUpperCase());
        String setterName = "set" + fieldName;
        String classReference = setterParameter.getAttributeValue("reference");
        String id= setterParameter.getAttributeValue("id");
        Object parameter = null;
        if (beanHashMap.containsKey(id)) {
            parameter = beanHashMap.get(id).getBeanInstance();
        } else { //si el bean al que se hace referencia aún no está creado
            try{
                parameter = findBean(classReference).getBeanInstance();
            }catch (NullPointerException e){
                System.out.println("El bean de la clase "+classReference+" no fue declarado");
                return null;
            }
        }
        Method[] methods = null;
        try {
            methods = newClass.getMethods();
        } catch (Exception e) {
            System.out.println("No se pudo recuperar el método: " + setterName);
            e.printStackTrace();
        }
        boolean found= false;
        int i=0;
        while(!found&& i<methods.length){
            if(methods[i].getName().equals(setterName)){
                found= true;
            }else{
                i++;
            }
        }
        try {
            methods[i].invoke(objectInstance, parameter);

        } catch (Exception e) {
            System.out.println("falla el invoke y no mete bien el parámetro del setter. M: createBeanInstance");
        }
        return objectInstance;
    }

    private int lookForConstructor(Constructor[] constructors, Object[] parameters) {
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
        return i - 1;
    }

    private Object[] obtainConstructorDependencies(Element element) throws ClassNotFoundException, ParsingException, IOException {
        Elements constructorArgs = element.getChildElements();
        Object[] parameters = new Object[0];
        for (int i = 0; i < constructorArgs.size(); i++) {
            Element parameter = constructorArgs.get(i);
            String id = parameter.getAttributeValue("id");
            String classReference= parameter.getAttributeValue("reference");
            if (beanHashMap.containsKey(id)) {
                parameters[i] = beanHashMap.get(id).getBeanInstance();
            } else { //si el bean al que se hace referencia aún no está creado
                try{
                    parameters[i] = findBean(classReference).getBeanInstance();
                }catch (NullPointerException e){
                    System.out.println("El bean de la clase "+classReference+" no fue declarado");
                    return null;
                }
            }
        }
        return parameters;
    }

    @Override
    public Bean findBean(String id) {
        Bean foundBean = new Bean();
        Builder builder = new Builder();
        Document xmlDoc = null;
        try {
            xmlDoc = builder.build(xmlPath);
        } catch (ParsingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Element root = xmlDoc.getRootElement();
        Elements beans = root.getChildElements();
        int i = 0;
        boolean found = false;
        while (!found && i < beans.size()) {
            Element currentBean = beans.get(i);
            String idValue = currentBean.getAttributeValue("id");
            if (idValue.equals(id)) { //realiza la comparación de clases
                found = true;
                tagsBeanContent.put(currentBean.getAttributeValue("id"), currentBean);
                foundBean = createBean(idValue);
            }
            i++;
        }
        if(!found){
          foundBean= null;
        }
        //ver que pasa si no encontró en bean
        return foundBean;
    }

}
