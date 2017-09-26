package cr.ac.ucr.ecci.ci1330.IoC.AnnotationBased;


import cr.ac.ucr.ecci.ci1330.IoC.AbstractBeanFactory;

import java.util.List;

public class AnnotationBeanFactory extends AbstractBeanFactory{

    private AnnotationParser annotationParser;


    public AnnotationBeanFactory(List<Class> annotadedClasses, Class className) {
        annotationParser = new AnnotationParser(annotadedClasses,this);
    }

    public Object getBean(){
        return null;
    }
}
