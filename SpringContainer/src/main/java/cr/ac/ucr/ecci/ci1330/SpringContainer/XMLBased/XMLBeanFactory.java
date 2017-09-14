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
        }
    }

    @Override
    public Bean createBean(String id) {
        //ver el modo de autowiring
        //java reflection(ver si la clase existe )
        // si la clase no existe hay que borrarlo del mapa de tags donde ya se agregó
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
        if (! beanHashMap.containsKey(id)){
            beanHashMap.put(id, bean);
        }
        executeBeanInstanceMethod(bean, bean.getInitMethod());
        return bean;
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
