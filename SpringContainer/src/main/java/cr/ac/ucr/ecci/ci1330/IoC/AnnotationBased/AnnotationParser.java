package cr.ac.ucr.ecci.ci1330.IoC.AnnotationBased;

import nu.xom.Element;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * Created by majo_ on 22/9/2017.
 */
public class AnnotationParser {

    private Class aClass;
    private AnnotationBeanFactory annotationBeanFactory;
    private HashMap<Class, Element> beanContent;


    public AnnotationParser(Class annotadedClass, AnnotationBeanFactory annotationBeanFactory){
        try {
            this.aClass = Class.forName(annotadedClass.getName());
            if (this.aClass.isAnnotationPresent(Component.class)){
                this.annotationBeanFactory=annotationBeanFactory;
                this.beanContent=new HashMap<>();
            }
        }
        catch (ClassNotFoundException e) {
            System.out.println("No se encontró la clase");
            e.printStackTrace();
        }
    }


    public void parseClassForAnnotations(){
        Annotation classAnnotations[] = aClass.getAnnotations();
        if (classAnnotations.length>0) {
            System.out.println("Class annotations: " + aClass.getSimpleName());
            for (Annotation annotation : classAnnotations) {
                System.out.println(annotation.annotationType().getSimpleName());
            }
            Field fields[] = aClass.getDeclaredFields();
            System.out.println("Class attributes " + aClass.getSimpleName());
            for (Field field : fields) {
                Annotation fieldAnnotations[] = field.getAnnotations();
                System.out.println("Field annotations of " + field.getName() + " are: ");
                if (fieldAnnotations.length > 0) {
                    for (Annotation annotation : fieldAnnotations) {
                        System.out.println(annotation.annotationType().getSimpleName());
                    }
                }
            }
            Method methods[] = aClass.getDeclaredMethods();
            for (Method method : methods) {
                Annotation methodAnnotations[] = method.getDeclaredAnnotations();
                System.out.println("Method annotations of " + method.getName() + " are: ");

                if (methodAnnotations.length > 0) {
                    for (Annotation annotation : methodAnnotations) {
                        System.out.println(annotation.annotationType().getSimpleName());
                    }
                }
            }
        }
        else{
            System.out.println("Class "+this.aClass.getName()+" missing Component annotation needed to scan class");
        }
    }

    public Object createBean(){
        return null;
    }
}
