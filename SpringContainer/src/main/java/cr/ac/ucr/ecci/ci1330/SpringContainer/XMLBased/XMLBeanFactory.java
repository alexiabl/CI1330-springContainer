package cr.ac.ucr.ecci.ci1330.SpringContainer.XMLBased;

import cr.ac.ucr.ecci.ci1330.SpringContainer.AbstractBeanFactory;
import cr.ac.ucr.ecci.ci1330.SpringContainer.Bean;
import nu.xom.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;


public class XMLBeanFactory extends AbstractBeanFactory {

    HashMap<String, String> tagsBeanContent;

    public void readXML(String xmlPath) throws ParsingException, IOException {
        Builder builder = new Builder();
        Document xmlDoc = builder.build(xmlPath);
        Element root = xmlDoc.getRootElement();
        Elements beans = root.getChildElements();
        for (int i=0; i<beans.size(); i++){
            Element currentBean = beans.get(i);
            int attributeCount = currentBean.getAttributeCount();
            System.out.println("El bean "+currentBean.getAttributeValue("id")+" tiene "+attributeCount+" atributos");
            if (attributeCount>0){
                if (hasScopeAttribute(currentBean, attributeCount, "scopeType")) {
                    String scope = currentBean.getAttribute("scopeType").getValue();
                    if (scope.equals("singleton")) {
                        System.out.println("El bean es singleton");
                    }
                    else {
                        System.out.println("El bean es prototype");
                    }
                }
                for (int j=0;j<attributeCount;j++) {
                        String attributeName = currentBean.getAttribute(j).getLocalName();
                        String attributeValue = currentBean.getAttribute(j).getValue();
                        System.out.println(attributeName + " - " + attributeValue);
                        //hay que definir si el bean se crea enviandose una lista de los atributos o donde guardar
                        // los atributos relacionados a un bean id para poder llamar a createBean(id) solo con el id
                    }
                }
            }
        }

    private boolean hasScopeAttribute(Element element, int attributeCount, String attributeName){
        for (int i=0; i<attributeCount; i++){
            if (element.getAttribute(i).getLocalName().equals(attributeName)){
                return true;
            }
        }
        return false;
    }

    private Bean readXMLBean(String id, String xmlPath) throws ParsingException, IOException {
        Bean foundBean = new Bean();
        Builder builder = new Builder();
        Document xmlDoc = builder.build(xmlPath);
        Element root = xmlDoc.getRootElement();
        Elements beans = root.getChildElements();
        for (int i=0; i<beans.size(); i++) {
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
