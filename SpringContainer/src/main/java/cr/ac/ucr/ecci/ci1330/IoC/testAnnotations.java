package cr.ac.ucr.ecci.ci1330.IoC;

import cr.ac.ucr.ecci.ci1330.IoC.AnnotationBased.AnnotationBeanFactory;

import java.util.List;

/**
 * Created by alexiaborchgrevink on 9/28/17.
 */
public class testAnnotations {

    private List<Class> annotatedClasses;
    public testAnnotations(List<Class> annotadedClasses){
        this.annotatedClasses=annotadedClasses;
    }

    public void executeTest(){
        for (Class aClass: annotatedClasses) {
            AbstractBeanFactory beanFactory = new AnnotationBeanFactory(aClass);

        }
    }
}
