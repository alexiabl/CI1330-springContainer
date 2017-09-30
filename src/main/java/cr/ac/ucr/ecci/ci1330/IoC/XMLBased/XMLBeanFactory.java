package cr.ac.ucr.ecci.ci1330.IoC.XMLBased;

import cr.ac.ucr.ecci.ci1330.IoC.AbstractBeanFactory;
import cr.ac.ucr.ecci.ci1330.IoC.AnnotationBased.AnnotationBeanFactory;
import nu.xom.Element;

import java.util.HashMap;

/**
 * Creates a Bean Factory for the XML Configuration
 * @author María José Cubero
 * @author Renato Mainieri
 */
public class XMLBeanFactory extends AbstractBeanFactory {

    private XMLParser xmlParser;

    public XMLBeanFactory(String path) {
        this.xmlParser = new XMLParser(path, this);
        this.xmlParser.readXML();
        this.createBeanInstances();
    }

    /**
     * Obtains the attributes of a bean
     * @param id
     * @return
     */
    @Override
    protected HashMap<String, Object> obtainBeanAttributes(String id){
        if(xmlParser.getTagsBeanContent().containsKey(id)){
            return xmlParser.obtainBeanAttributes((Element) xmlParser.getTagsBeanContent().get(id));
        } else{
            return xmlParser.getAnnotationBeansAttributes().get(id);
        }
    }

}
