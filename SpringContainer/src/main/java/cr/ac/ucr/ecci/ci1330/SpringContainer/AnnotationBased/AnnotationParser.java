package cr.ac.ucr.ecci.ci1330.SpringContainer.AnnotationBased;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by majo_ on 22/9/2017.
 */
public class AnnotationParser {

    private Class aClass;
    private AnnotationBeanFactory annotationBeanFactory;


    public AnnotationParser(Class annotadedClass, AnnotationBeanFactory annotationBeanFactory){
        try {
            this.aClass = Class.forName(String.valueOf(annotadedClass));
        }
        catch (ClassNotFoundException e) {
            System.out.println("No se encontró la clase");
            e.printStackTrace();
        }
        this.annotationBeanFactory=annotationBeanFactory;
    }


    public void parseClassForAnnotations(){
        Annotation classAnnotations[] = aClass.getAnnotations();
        System.out.println("Annotations de la clase: "+aClass.getSimpleName());
        for (Annotation annotation : classAnnotations) {
            System.out.println(annotation.annotationType().getSimpleName());
        }
        Field fields[] = aClass.getDeclaredFields();
        System.out.println("Atributos de la clase "+aClass.getSimpleName());
        for (Field field:fields) {
            Annotation fieldAnnotations[] = field.getAnnotations();
            System.out.println("las annotations del atributo "+field.getName()+" son: ");
            if (fieldAnnotations.length>0) {
                for (Annotation annotation : fieldAnnotations) {
                    System.out.println(annotation.annotationType().getSimpleName());
                }
            }
        }
        System.out.println("Metodos de la clase "+aClass.getSimpleName());
        Method methods[] = aClass.getDeclaredMethods();
        for (Method method: methods) {
            Annotation methodAnnotations[] = method.getDeclaredAnnotations();
            System.out.println("las annotations del metodo "+method.getName()+" son: ");

            if (methodAnnotations.length>0) {
                for (Annotation annotation : methodAnnotations) {
                    System.out.println(annotation.annotationType().getSimpleName());
                }
            }
        }
    }

    public Object createBean(){
        return null;
    }
}
