package cr.ac.ucr.ecci.ci1330.IoC.XMLBased;

import cr.ac.ucr.ecci.ci1330.IoC.AnnotationBased.AnnotationParser;
import cr.ac.ucr.ecci.ci1330.IoC.AutowiringMode;
import cr.ac.ucr.ecci.ci1330.IoC.Bean.Bean;
import cr.ac.ucr.ecci.ci1330.IoC.Bean.Dependency;
import cr.ac.ucr.ecci.ci1330.IoC.ScopeType;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Parse the elements of the xml file to get all the information of a Bean to create it later.
 * @author María José Cubero
 * @author Renato Mainieri
 */

public class XMLParser {

    private String path;
    private XMLBeanFactory xmlBeanFactory;
    private HashMap<String, Element> tagsBeanContent;
    private AnnotationParser annotationParser;
    private HashMap<String, HashMap<String, Object>> annotationBeansAttributes;

    public XMLParser(String path, XMLBeanFactory xmlBeanFactory) {
        this.path = path;
        this.xmlBeanFactory = xmlBeanFactory;
        this.tagsBeanContent = new HashMap();
        this.annotationBeansAttributes= new HashMap<>();
    }

    /**
     * Read the XML file Element by Element, obtains the information of a bean and calls a method to create it.
     * @author María José Cubero
     * @author Renato Mainieri
     */
    public void readXML() {
        Builder builder = new Builder();
        Document xmlDoc = null;
        try {
            xmlDoc = builder.build(path);
        } catch (Exception e) {
            System.out.println("El path es incorrecto");
            e.printStackTrace();
        }
        Element root = xmlDoc.getRootElement();
        Elements beanTags = root.getChildElements();
        for (int i = 0; i < beanTags.size(); i++) {
            Element currentBeanTag = beanTags.get(i);
            if(currentBeanTag.getLocalName().equals("classesToScan")){
                implementHybridConfiguration(currentBeanTag);
            }
            else if (!this.xmlBeanFactory.getBeanHashMap().containsKey(currentBeanTag.getAttributeValue("id"))){
                this.xmlBeanFactory.createBean(obtainBeanAttributes(currentBeanTag));
                this.tagsBeanContent.put(currentBeanTag.getAttributeValue("id"), currentBeanTag);
            }
        }
    }

    /**
     * Implements hybrid configuration when its required.
     * @author María José Cubero
     * @param currentBeanTag
     */
    private void implementHybridConfiguration(Element currentBeanTag){
        Elements classes = currentBeanTag.getChildElements();
        try {
            for (int j = 0; j < classes.size(); j++) {
                String packageName = classes.get(j).getAttribute("package").getValue();
                Class aClass = Class.forName(packageName);
                this.annotationParser= new AnnotationParser(aClass);
                Bean bean= this.xmlBeanFactory.createBean(annotationParser.getAnnotationBeanContent());
                annotationBeansAttributes.put(bean.getId(), annotationParser.getAnnotationBeanContent());
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Obtains the attributes of a bean receiving them from the element.
     * @author María José Cubero
     * @param beanTag Element
     * @return HashMap<String, Object> beanAttributes
     */
    public HashMap obtainBeanAttributes(Element beanTag) {
        HashMap<String, Object> beanAttributes = new HashMap();
        beanAttributes.put("id", beanTag.getAttributeValue("id"));
        beanAttributes.put("className", beanTag.getAttributeValue("className"));
        if (beanTag.getAttribute("initMethod") != null) {
            beanAttributes.put("initMethod", beanTag.getAttributeValue("initMethod"));
        }
        if (beanTag.getAttribute("destructMethod") != null) {
            beanAttributes.put("destructMethod", beanTag.getAttributeValue("destructMethod"));
        }
        if (beanTag.getAttribute("scopeType") != null) {
            String scopeType = beanTag.getAttributeValue("scopeType").toUpperCase();
            beanAttributes.put("scopeType", ScopeType.valueOf(scopeType).toString());
        }
        if (beanTag.getAttribute("lazy") != null) {
            beanAttributes.put("lazy", true);
        }
        setBeanDependencies(beanAttributes, beanTag);
        return beanAttributes;
    }

    /**
     * Obtains the dependencies of the current element, and puts the dependencies on beanAttributes.
     * @author María José Cubero Hidalgo
     * @param beanAttributes
     * @param beanTag
     */
    public void setBeanDependencies(HashMap<String, Object> beanAttributes, Element beanTag) {
        Elements dependenciesTags = beanTag.getChildElements();
        if (dependenciesTags.size() > 0) {
            List<Dependency> constructorDependencies = new ArrayList<>();
            List<Dependency> setterDependencies = new ArrayList<>();
            for (int i = 0; i < dependenciesTags.size(); i++) {
                Element dependencyTag = dependenciesTags.get(i);
                if (dependencyTag.getLocalName().equals("constructor-args")) {
                    Dependency dependency = new Dependency(beanTag.getAttributeValue("id"), dependencyTag.getAttributeValue("reference"));
                    if (dependencyTag.getAttribute("autowiringMode") != null) {
                        dependency.setAutowiringMode(AutowiringMode.valueOf(dependencyTag.getAttributeValue("autowiringMode").toUpperCase()));
                    }
                    constructorDependencies.add(dependency);
                }
                if (dependencyTag.getLocalName().equals("setter-arg")) {
                    dependencyTag = beanTag.getChildElements().get(i);
                    Dependency dependency = new Dependency(beanTag.getAttributeValue("id"), dependencyTag.getAttributeValue("reference"), dependencyTag.getAttributeValue("name"));
                    if (dependencyTag.getAttribute("autowiringMode") != null) {
                        String autowiringMode = dependencyTag.getAttributeValue("autowiringMode").toUpperCase();
                        dependency.setAutowiringMode(AutowiringMode.valueOf(autowiringMode));
                    }
                    setterDependencies.add(dependency);
                }
            }
            beanAttributes.put("constructorDependencies", constructorDependencies);
            beanAttributes.put("setterDependencies", setterDependencies);
        }
    }


    public HashMap getTagsBeanContent() {
        return tagsBeanContent;
    }

    public HashMap<String, HashMap<String, Object>> getAnnotationBeansAttributes() {
        return annotationBeansAttributes;
    }
}
