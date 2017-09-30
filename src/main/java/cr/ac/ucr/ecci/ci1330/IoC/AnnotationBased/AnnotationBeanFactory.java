package cr.ac.ucr.ecci.ci1330.IoC.AnnotationBased;


import cr.ac.ucr.ecci.ci1330.IoC.AbstractBeanFactory;
import cr.ac.ucr.ecci.ci1330.IoC.Bean.Bean;
import cr.ac.ucr.ecci.ci1330.IoC.ScopeType;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;
import nu.xom.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Creates an Annotation Bean Factory for the Annotation Based Configuration
 *
 * @author Alexia Borchgrevink
 */
public class AnnotationBeanFactory extends AbstractBeanFactory {

    private AnnotationParser annotationParser;
    private HashMap<String, Object> annotationsContent;

    public AnnotationBeanFactory(String xmlPath) {
        annotationsContent = new HashMap<>();
        List<Class> annotatedClasses = this.getClassesFromPackage(xmlPath);
        for (Class aClass : annotatedClasses) {
            this.annotationParser = new AnnotationParser(aClass, this);
        }
    }

    public HashMap<String, Object> getAnnotationsContent() {
        return annotationsContent;
    }

    /**
     * Obtains the attributes of a bean
     *
     * @param id
     * @return
     */
    @Override
    public HashMap<String, Object> obtainBeanAttributes(String id) {
        return (HashMap<String, Object>) annotationsContent.get(id);
    }

    /**
     * Reads the xmlAnnotations file for the classes to scan for Annotations.
     * @param configFile
     * @return List</Class>
     */
    public List<Class> getClassesFromPackage(String configFile) {
        Builder builder = new Builder();
        Document xmlDoc = null;
        List<Class> annotadedClasses = new ArrayList<>();
        try {
            xmlDoc = builder.build(configFile);
            Element root = xmlDoc.getRootElement();
            Elements classes = root.getChildElements();
            for (int i = 0; i < classes.size(); i++) {
                String packageName = classes.get(i).getAttribute("package").getValue();
                Class aClass = Class.forName(packageName);
                annotadedClasses.add(aClass);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return annotadedClasses;
    }

}

