package cr.ac.ucr.ecci.ci1330.IoC;

import cr.ac.ucr.ecci.ci1330.IoC.XMLBased.XMLBeanFactory;
import cr.ac.ucr.ecci.ci1330.IoC.annotadedClasses.Mascota;
import cr.ac.ucr.ecci.ci1330.IoC.annotadedClasses.Persona;

import java.util.LinkedList;
import java.util.List;


public class Main {
    public static void main(String[] args) {

        List<Class> classesToScan = new LinkedList<>();
        classesToScan.add(Persona.class);
        classesToScan.add(Mascota.class);
        testAnnotations testAnnotations = new testAnnotations(classesToScan);
        testAnnotations.executeTest();

    }
}
