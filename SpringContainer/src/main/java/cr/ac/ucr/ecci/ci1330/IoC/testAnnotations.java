package cr.ac.ucr.ecci.ci1330.IoC;

import cr.ac.ucr.ecci.ci1330.IoC.AnnotationBased.AnnotationBeanFactory;
import cr.ac.ucr.ecci.ci1330.IoC.annotadedClasses.Mascota;
import cr.ac.ucr.ecci.ci1330.IoC.annotadedClasses.Persona;

import java.util.LinkedList;
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
            beanFactory = new AnnotationBeanFactory(aClass);
        }
        beanFactory.getBean(annotatedClasses.get(0).getSimpleName());
    }
/*    List<Class> classesToScan = new LinkedList<>();
        classesToScan.add(Persona.class);
        classesToScan.add(Mascota.class);
    testAnnotations testAnnotations = new testAnnotations(classesToScan);
        testAnnotations.executeTest();*/

}
