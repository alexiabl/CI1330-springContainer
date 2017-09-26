package cr.ac.ucr.ecci.ci1330.IoC.AnnotationBased;

import nu.xom.Element;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

/**
 * Created by majo_ on 22/9/2017.
 */
public class AnnotationParser {

    private List<Class> annotadedClasses;
    private AnnotationBeanFactory annotationBeanFactory;
    private HashMap<String, Object> annotationBeanContent;


    public AnnotationParser(AnnotationBeanFactory annotationBeanFactory) {
                this.annotationBeanFactory = annotationBeanFactory;
                this.annotationBeanContent = new HashMap<>();
                this.parseClassForAnnotations();
    }


    public void parseClassForAnnotations() {
        for (Class aClass: this.annotadedClasses) {
            Annotation classAnnotations[] = aClass.getAnnotations();
            System.out.println("Class annotations: " + aClass.getSimpleName());
            for (Annotation annotation : classAnnotations) {
                System.out.println(annotation.annotationType().getSimpleName());
            }
            Field fields[] = aClass.getDeclaredFields();
            System.out.println("Class attributes " + aClass.getSimpleName());
            for (Field field : fields) {
                Annotation fieldAnnotations[] = field.getAnnotations();
                if (field.isAnnotationPresent(Component.class)) {
                    String className = field.getAnnotation(Component.class).value();
                    System.out.println(className);

                }
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

    }


    public Object createBean() {
        return null;
    }
}
