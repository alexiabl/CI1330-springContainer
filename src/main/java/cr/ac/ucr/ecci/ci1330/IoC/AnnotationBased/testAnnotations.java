package cr.ac.ucr.ecci.ci1330.IoC.AnnotationBased;

import cr.ac.ucr.ecci.ci1330.IoC.AbstractBeanFactory;

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
        AbstractBeanFactory beanFactory =null;
        for (Class aClass: annotatedClasses) {
            //beanFactory = new AnnotationBeanFactory(aClass);
        }
        beanFactory.getBean(annotatedClasses.get(0).getSimpleName());
    }

}
