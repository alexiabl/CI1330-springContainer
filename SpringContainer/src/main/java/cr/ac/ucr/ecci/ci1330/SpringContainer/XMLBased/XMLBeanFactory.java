package main.java.cr.ac.ucr.ecci.ci1330.SpringContainer.XMLBased;

import main.java.cr.ac.ucr.ecci.ci1330.SpringContainer.AbstractBeanFactory;
import main.java.cr.ac.ucr.ecci.ci1330.SpringContainer.Bean;

import java.util.HashMap;

public class XMLBeanFactory extends AbstractBeanFactory {

    HashMap<String, String> tagsBeanContent;

    private void readXML(String xmlPath) {
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
