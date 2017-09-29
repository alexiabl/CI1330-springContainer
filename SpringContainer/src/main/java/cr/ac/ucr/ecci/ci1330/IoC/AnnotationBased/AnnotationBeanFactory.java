package cr.ac.ucr.ecci.ci1330.IoC.AnnotationBased;


import cr.ac.ucr.ecci.ci1330.IoC.AbstractBeanFactory;
import cr.ac.ucr.ecci.ci1330.IoC.Bean.Bean;
import cr.ac.ucr.ecci.ci1330.IoC.ScopeType;
import nu.xom.Builder;
import nu.xom.Document;
import nu.xom.Element;

import java.util.List;

public class AnnotationBeanFactory extends AbstractBeanFactory {

    private AnnotationParser annotationParser;
    private String path;


    public AnnotationBeanFactory(String fileName) {
        this.path = "SpringContainer/src/main/resources/" + fileName;
        List<Class> annotadedClasses = this.getClassesFromPackage(path);
        for (Class aClass: annotadedClasses) {
            annotationParser = new AnnotationParser(aClass, this);
            createBeanInstances();
        }
    }

    @Override
    public Object getBean(String id) {
        try {
            if (beanHashMap.get(id).getScopeType().equals(ScopeType.PROTOTYPE)) {
                Bean bean = createBean(annotationParser.getAnnotationBeanContent());
                bean.setBeanInstance(injectBeanInstance(bean));
                return bean.getBeanInstance();
            }
            return beanHashMap.get(id).getBeanInstance();
        } catch (NullPointerException e) {
            System.out.println("El id '" + id + "' no identifica nigún bean.");
            return null;
        }
    }

    public List<Class> getClassesFromPackage(String configFile){
        Builder builder = new Builder();
        Document xmlDoc = null;
        List<Class> annotadedClasses = null;
        try {
            xmlDoc = builder.build(configFile);
            Element root = xmlDoc.getRootElement();
            String path = root.getAttribute("path").getValue();
            annotadedClasses = getClassesFromPackage(path);
        } catch (Exception e) {
            System.out.println("El path es incorrecto");
            e.printStackTrace();
        }
        return annotadedClasses;
    }


}

