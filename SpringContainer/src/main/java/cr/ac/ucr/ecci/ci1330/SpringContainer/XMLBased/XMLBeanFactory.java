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
            System.out.println("El bean "+beans.get(i).getLocalName()+"tiene "+beans.get(i).getAttributeCount()+" atributos");
        }





    }

    private Bean readXMLBean(String id) {
        return null;
    }

    @Override
    public Bean getBean(String id) {
        return null;
    }

    @Override
    public void destroyBean(String id) {

    }
}
