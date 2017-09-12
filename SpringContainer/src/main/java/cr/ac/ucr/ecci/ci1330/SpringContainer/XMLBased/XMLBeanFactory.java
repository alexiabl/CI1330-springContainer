package cr.ac.ucr.ecci.ci1330.SpringContainer.XMLBased;

import cr.ac.ucr.ecci.ci1330.SpringContainer.AbstractBeanFactory;
import cr.ac.ucr.ecci.ci1330.SpringContainer.AutowiringMode;
import cr.ac.ucr.ecci.ci1330.SpringContainer.Bean;
import cr.ac.ucr.ecci.ci1330.SpringContainer.ScopeType;
import nu.xom.*;

import java.io.IOException;
import java.io.InputStream;
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
            /*int attributeCount = currentBean.getAttributeCount();
            System.out.println("El bean " + currentBean.getAttributeValue("id") + " tiene " + attributeCount + " atributos");
            if (attributeCount > 0) {
                if (hasScopeAttribute(currentBean, attributeCount, "scopeType")) {
                    String scope = currentBean.getAttribute("scopeType").getValue();
                    if (scope.equals("singleton")) {
                        System.out.println("El bean es singleton");
                    } else {
                        System.out.println("El bean es prototype");
                    }
                }
                else{
                    System.out.println("El bean es singleton");
                }
                for (int j = 0; j < attributeCount; j++) {
                    String attributeName = currentBean.getAttribute(j).getLocalName();
                    String attributeValue = currentBean.getAttribute(j).getValue();
                    System.out.println(attributeName + " - " + attributeValue);
                    //hay que definir si el bean se crea enviandose una lista de los atributos o donde guardar
                    // los atributos relacionados a un bean id para poder llamar a createBean(id) solo con el id
                }
            }*/
        }
    }

    @Override
    public Bean createBean(String id) {
        Bean bean = new Bean();
        Element element = tagsBeanContent.get(id);
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
        return bean;
    }

    private Bean readXMLBean(String id, String xmlPath) throws ParsingException, IOException {
        Bean foundBean = new Bean();
        Builder builder = new Builder();
        Document xmlDoc = builder.build(xmlPath);
        Element root = xmlDoc.getRootElement();
        Elements beans = root.getChildElements();
        for (int i = 0; i < beans.size(); i++) {
            Element currentBean = beans.get(i);
            String idValue = currentBean.getAttribute("id").getValue();
            //buscar en un hashmap de beans el bean con el id

        }
        return foundBean;

    }


    @Override
    public Bean getBean(String id) {
        return null;
    }

    @Override
    public void destroyBean(String id) {

    }
}
