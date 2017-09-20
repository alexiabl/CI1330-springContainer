package cr.ac.ucr.ecci.ci1330.SpringContainer.XMLBased;


import cr.ac.ucr.ecci.ci1330.SpringContainer.AbstractBeanFactory;
import cr.ac.ucr.ecci.ci1330.SpringContainer.AutowiringMode;
import cr.ac.ucr.ecci.ci1330.SpringContainer.Bean;
import cr.ac.ucr.ecci.ci1330.SpringContainer.ScopeType;
import nu.xom.*;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public class XMLBeanFactory <T> extends AbstractBeanFactory {

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
        //ver el modo de autowiring
        //java reflection(ver si la clase existe )
        // si la clase no existe hay que borrarlo del mapa de tags donde ya se agregó
        Element element = tagsBeanContent.get(id);
        Class definedClass= null;

        if (element.getAttributeValue("autowiringMode").equals(AutowiringMode.valueOf("TYPE"))|| element.getAttribute("autowiringMode").equals(null)){

        }
        else {
            //byname
        }
        Bean bean = new Bean();
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
        //faltan setters y constructores y crear la instancia
        //falta lo de reference
        if (! beanHashMap.containsKey(id)){
            beanHashMap.put(id, bean);
        }
        executeBeanInstanceMethod(bean, bean.getInitMethod());
        return bean;
    }

    private T createBeanInstance(Element element) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException {
        T objectInstance = null;
        String className= element.getAttributeValue("className");
        Class newClass = Class.forName(className);
        //verificar que esté llamando al constructor
        if(element.getAttribute("injectConstructor")!= null){
            Object[] parameters= obtainConstructorDependencies(element);

            Constructor[] constructors= newClass.getDeclaredConstructors();
            Constructor constructor= constructors[lookForConstructor(constructors, parameters)];
            try {
                constructor.newInstance(objectInstance, parameters);
            } catch (InvocationTargetException e) {
                System.out.println("no ingresó bien los parametros. Metodo: createBeanInstance");
                e.printStackTrace();
            }
        }
        //verificar que está llamando a un setter
        else{

        }
        return objectInstance;
    }

    private int lookForConstructor(Constructor[] constructors, Object[] parameters){
        boolean found= false;
        boolean equal= true;
        int i= 0;
        int j;

        while (!found && i< constructors.length){
            Class [] parameterTypes= constructors[i].getParameterTypes();
            if (constructors[i].getParameterCount()== parameters.length){
                j= 0;
                equal= true;
                while (equal && j< parameters.length){
                    String parameter= parameters[j].getClass().toString();
                    if(parameter.equals(parameterTypes[j].toString()) && (j== parameters.length-1)){
                        found= true;
                    }
                    else{
                        equal= false;
                    }
                    j++;
                }
            }
            i++;
        }
        return i-1;
    }

    private Object[] obtainConstructorDependencies(Element element) throws ClassNotFoundException {
        Elements constructorArgs= element.getChildElements();
        Object[] parameters = new Object[0];
        for (int i= 0; i < constructorArgs.size(); i++){
            Element parameter= constructorArgs.get(i);
            String classReference= parameter.getAttributeValue("reference");
            if (beanHashMap.containsKey(classReference)){
                parameters[i]= beanHashMap.get(classReference).getBeanInstance();
            }else{ //si el bean al que se hace referencia aún no está creado
                parameters[i]= createBean(classReference).getBeanInstance();
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
            if (idValue.equals(id)) {
                found = true;
                tagsBeanContent.put(idValue, currentBean);
                foundBean = createBean(idValue);
            }
            i++;
        }
        return foundBean;
    }

}
