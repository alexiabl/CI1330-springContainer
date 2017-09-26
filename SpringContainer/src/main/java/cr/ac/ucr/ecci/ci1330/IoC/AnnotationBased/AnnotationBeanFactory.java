package cr.ac.ucr.ecci.ci1330.IoC.AnnotationBased;


import cr.ac.ucr.ecci.ci1330.IoC.AbstractBeanFactory;

public class AnnotationBeanFactory extends AbstractBeanFactory{

    private AnnotationParser annotationParser;


    public AnnotationBeanFactory(Class className) {
        annotationParser = new AnnotationParser(className, this);
    }

    public Object getBean(){
        return null;
    }
}
