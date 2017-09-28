package cr.ac.ucr.ecci.ci1330.IoC.XMLBased;

import cr.ac.ucr.ecci.ci1330.IoC.AbstractBeanFactory;
import cr.ac.ucr.ecci.ci1330.IoC.Bean;
import cr.ac.ucr.ecci.ci1330.IoC.ScopeType;
import nu.xom.Element;

/**
 * Created by majo_ on 22/9/2017.
 */
public class XMLBeanFactory extends AbstractBeanFactory {

    private XMLParser xmlParser;
    protected String path;

    public XMLBeanFactory(String fileName) {
        this.path = "SpringContainer/src/main/resources/"+fileName;
        this.xmlParser = new XMLParser(path, this);
        xmlParser.readXML();
        createBeanInstances();
    }


    @Override
    public Object getBean(String id) {
        try{
            if (beanHashMap.get(id).getScopeType().equals(ScopeType.PROTOTYPE)){
                Bean bean = createBean(xmlParser.obtainBeanAttributes((Element) xmlParser.getTagsBeanContent().get(id)));
                return bean.getBeanInstance();
            }
            return beanHashMap.get(id).getBeanInstance();
        }catch (NullPointerException e){
            System.out.println("El id '"+id+"' no identifica nigún bean.");
            return null;
        }
    }

    public XMLParser getXmlParser() {
        return xmlParser;
    }
}
