package cr.ac.ucr.ecci.ci1330.IoC.XMLBased;

import cr.ac.ucr.ecci.ci1330.IoC.AutowiringMode;
import cr.ac.ucr.ecci.ci1330.IoC.Dependency;
import cr.ac.ucr.ecci.ci1330.IoC.ScopeType;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by majo_ on 22/9/2017.
 */
public class XMLParser {

    private String path;
    private XMLBeanFactory xmlBeanFactory;
    private HashMap<String, Element> tagsBeanContent;

    public XMLParser(String path, XMLBeanFactory xmlBeanFactory) {
        this.path = path;
        this.xmlBeanFactory = xmlBeanFactory;
        this.tagsBeanContent = new HashMap();
    }

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
            xmlBeanFactory.createBean(obtainBeanAttributes(currentBeanTag));
            tagsBeanContent.put(currentBeanTag.getAttributeValue("id"), currentBeanTag);

        }
    }

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
        //meter a la lista de dependencias del Bean

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
            beanAttributes.put("dependencies", constructorDependencies);
            beanAttributes.put("dependency", setterDependencies);
        }
        return beanAttributes;
    }


    public HashMap getTagsBeanContent() {
        return tagsBeanContent;
    }
}
