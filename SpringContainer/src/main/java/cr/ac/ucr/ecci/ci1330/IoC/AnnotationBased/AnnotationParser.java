package cr.ac.ucr.ecci.ci1330.IoC.AnnotationBased;

import cr.ac.ucr.ecci.ci1330.IoC.Dependency;
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


    public AnnotationParser(List<Class> annotadedClasses, AnnotationBeanFactory annotationBeanFactory) {
                this.annotationBeanFactory = annotationBeanFactory;
                this.annotationBeanContent = new HashMap<>();
                this.annotadedClasses=annotadedClasses;
                this.parseClassForAnnotations();
    }


    public void parseClassForAnnotations() {
        for (Class aClass: this.annotadedClasses) {
            if (aClass.isAnnotationPresent(Component.class)) {
                Annotation classAnnotations[] = aClass.getAnnotations(); //annotations de la clase
                System.out.println("Class annotations: " + aClass.getSimpleName());
                for (Annotation annotation : classAnnotations) {
                    System.out.println(annotation.annotationType().getSimpleName());
                }
                Field fields[] = aClass.getDeclaredFields(); //atributos de la clase
                System.out.println("Class attributes " + aClass.getSimpleName());
                for (Field field : fields) {
                    Annotation fieldAnnotations[] = field.getAnnotations(); //annotations de los atributos
                    if (field.isAnnotationPresent(Component.class)) { //agregar a lista de dependencias
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
                Method methods[] = aClass.getDeclaredMethods(); //metodos de la clase
                for (Method method : methods) {
                    if (method.getName().contains("set")){
                        System.out.println(method.getName());
                    }
                    Annotation methodAnnotations[] = method.getDeclaredAnnotations(); //annotations de los metodos
                    System.out.println("Method annotations of " + method.getName() + " are: ");
                    if (methodAnnotations.length > 0) {
                        for (Annotation annotation : methodAnnotations) {
                            System.out.println(annotation.annotationType().getSimpleName());
                        }
                    }
                }
            }
            else{
                System.out.println("Class "+aClass.getSimpleName()+" does not have Component annotation needed to scan class annotations");
            }
        }

    }


    public Object createBean() {
        return null;
    }
}
