package cr.ac.ucr.ecci.ci1330.SpringContainer.AnnotationBased;


import cr.ac.ucr.ecci.ci1330.SpringContainer.AbstractBeanFactory;

public class AnnotationBeanFactory extends AbstractBeanFactory{

    private AnnotationParser annotationParser;


    public AnnotationBeanFactory(Class className) {
        annotationParser = new AnnotationParser(className, this);
        annotationParser.parseClassForAnnotations();

    }

    public Object getBean(){
        return null;
    }
}
