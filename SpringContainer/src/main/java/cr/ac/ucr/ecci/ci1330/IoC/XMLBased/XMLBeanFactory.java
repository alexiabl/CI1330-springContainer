package cr.ac.ucr.ecci.ci1330.IoC.XMLBased;

import cr.ac.ucr.ecci.ci1330.IoC.AbstractBeanFactory;
import cr.ac.ucr.ecci.ci1330.IoC.AnnotationBased.AnnotationBeanFactory;
import cr.ac.ucr.ecci.ci1330.IoC.Bean.Bean;
import cr.ac.ucr.ecci.ci1330.IoC.ScopeType;
import nu.xom.Element;

import java.util.HashMap;

/**
 * Created by majo_ on 22/9/2017.
 */
public class XMLBeanFactory extends AbstractBeanFactory {

    private XMLParser xmlParser;
    private String path;
    private AnnotationBeanFactory annotationBeanFactory;

    public XMLBeanFactory(String fileName) {
        this.path = "SpringContainer/src/main/resources/" + fileName;
        this.xmlParser = new XMLParser(path, this);
        xmlParser.readXML();
        // annotationBeanFactory= new AnnotationBeanFactory();
        createBeanInstances();
    }

    @Override
    protected HashMap<String, Object> obtainBeanAttributes(String id){
        return xmlParser.obtainBeanAttributes((Element) xmlParser.getTagsBeanContent().get(id));
    }

    public AnnotationBeanFactory getAnnotationBeanFactory() {
        return annotationBeanFactory;
    }

    public void setAnnotationBeanFactory(AnnotationBeanFactory annotationBeanFactory) {
        this.annotationBeanFactory = annotationBeanFactory;
    }
}
