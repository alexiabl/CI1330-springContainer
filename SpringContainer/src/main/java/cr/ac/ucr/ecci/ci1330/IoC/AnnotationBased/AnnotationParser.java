package cr.ac.ucr.ecci.ci1330.IoC.AnnotationBased;

import cr.ac.ucr.ecci.ci1330.IoC.AnnotationBased.Annotations.*;
import cr.ac.ucr.ecci.ci1330.IoC.AutowiringMode;
import cr.ac.ucr.ecci.ci1330.IoC.Bean.Dependency;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by majo_ on 22/9/2017.
 */
public class AnnotationParser {

    private Class theClass;
    private AnnotationBeanFactory annotationBeanFactory;
    private HashMap<String, Object> annotationBeanContent;


    public AnnotationParser(Class aClass, AnnotationBeanFactory annotationBeanFactory) {
                this.annotationBeanFactory = annotationBeanFactory;
                this.annotationBeanContent = new HashMap<>();
                this.theClass=aClass;
                this.parseClassForAnnotations();
    }

    public HashMap<String, Object> getAnnotationBeanContent(){
        return this.annotationBeanContent;
    }




    public void parseClassForAnnotations() {
            if (this.theClass.isAnnotationPresent(Component.class)) {
                String autowiringMode ="";
                String scopeType="";
                if (this.theClass.isAnnotationPresent(Scope.class)){
                    Scope scopeAnnotation=(Scope) this.theClass.getAnnotation(Scope.class);
                    scopeType = scopeAnnotation.scopeType().name();
                }
                else if (this.theClass.isAnnotationPresent(Lazy.class)){
                    annotationBeanContent.put("lazy",true);
                }
                Constructor constructors[] = this.theClass.getConstructors();
                for (Constructor constructor: constructors) {
                    if (constructor.isAnnotationPresent(Autowired.class)){
                        Autowired wiringAnnotation=(Autowired)constructor.getAnnotation(Autowired.class);
                        autowiringMode = wiringAnnotation.autowiringMode().name();
                    }
                    else if (!this.theClass.isAnnotationPresent(Scope.class) && constructor.isAnnotationPresent(Scope.class)){
                        Scope scopeAnnotation = (Scope) constructor.getAnnotation(Scope.class);
                        scopeType = scopeAnnotation.scopeType().name();
                    }
                }
                Component component = (Component) this.theClass.getAnnotation(Component.class);
                String key = component.id(); //bean id
                ArrayList<Dependency> setterDependencies = this.scanClassSetterDependencies(key);
                ArrayList<Dependency> constructorDependencies = this.scanClassConstructorDependencies(key);
                Method methods[] = this.theClass.getDeclaredMethods();
                String initMethod="";
                String destroyMethod = "";
                for (Method method: methods){
                    if (method.isAnnotationPresent(PostConstruct.class)){
                        initMethod = method.getName();
                    }
                    else if (method.isAnnotationPresent(PreDestruct.class)){
                        destroyMethod = method.getName();
                    }
                }
                this.annotationBeanContent.put("id",this.theClass.getSimpleName());
                this.annotationBeanContent.put("className",this.theClass.getName());
                this.annotationBeanContent.put("initMethod", initMethod);
                this.annotationBeanContent.put("destructMethod",destroyMethod);
                this.annotationBeanContent.put("autowiringMode",autowiringMode);
                this.annotationBeanContent.put("scopeType",scopeType);
                this.annotationBeanContent.put("constructorDependencies",constructorDependencies);
                this.annotationBeanContent.put("setterDependencies", setterDependencies);
                this.annotationBeanFactory.createBean(annotationBeanContent);
                System.out.println(annotationBeanContent.get("id"));
                this.annotationBeanFactory.getAnnotationsContent().put(this.theClass.getSimpleName(), this.annotationBeanContent);
            }
            else {
                System.out.println("La clase " + this.theClass.getSimpleName() + " no tiene el Component annotation necesitado para poder escanearla");
            }
    }

    public ArrayList<Dependency> scanClassSetterDependencies(String beanId){
        Field fields[] = this.theClass.getDeclaredFields(); //atributos de la clase
        Method methods[] = this.theClass.getDeclaredMethods();
        ArrayList<Dependency> setterDependencies = new ArrayList<>();
        for (Field field : fields) { //revisar los atributos de la clase
            if (field.isAnnotationPresent(Autowired.class)) { //setter dependencies
                Dependency dependency = new Dependency();
                Autowired autowired = field.getAnnotation(Autowired.class);
                if (autowired.autowiringMode().equals(AutowiringMode.BYNAME)) {
                    dependency.setReference(field.getName());
                    dependency.setAutowiringMode(AutowiringMode.BYNAME);
                    dependency.setBeanID(beanId);
                    dependency.setName(field.getName());
                }
                else if (field.getAnnotation(Autowired.class).autowiringMode().equals(AutowiringMode.BYTYPE)){
                    dependency.setReference(field.getType().getTypeName());
                    dependency.setAutowiringMode(AutowiringMode.BYTYPE);
                    dependency.setBeanID(beanId);
                }
                setterDependencies.add(dependency);
            }
        }
        for (Method method : methods){
            if (method.isAnnotationPresent(Autowired.class)) { //setter dependencies
                Dependency dependency = new Dependency();
                Autowired autowired = method.getAnnotation(Autowired.class);
                if (autowired.autowiringMode().equals(AutowiringMode.BYNAME)) {
                    dependency.setReference(method.getName());
                    dependency.setAutowiringMode(AutowiringMode.BYNAME);
                    dependency.setBeanID(beanId);
                    dependency.setName(method.getName());
                }
                else if (method.getAnnotation(Autowired.class).autowiringMode().equals(AutowiringMode.BYTYPE)){
                    dependency.setReference(method.getGenericReturnType().getTypeName());
                    dependency.setAutowiringMode(AutowiringMode.BYTYPE);
                    dependency.setBeanID(beanId);
                }
                setterDependencies.add(dependency);
            }
        }
        return setterDependencies;
    }

    public ArrayList<Dependency> scanClassConstructorDependencies(String beanId){
        ArrayList<Dependency> constructorDependencies = new ArrayList<>();
        Constructor constructors[] = this.theClass.getConstructors(); //constructores de la clase
        for (Constructor constructor : constructors){
            if (constructor.isAnnotationPresent(Autowired.class)){ //si el constructor esta autowired
                Autowired autowiredConstructor = (Autowired)constructor.getAnnotation(Autowired.class);
                Dependency dependency = new Dependency();
                if (autowiredConstructor.autowiringMode().equals(AutowiringMode.BYNAME)) {
                    dependency.setReference(constructor.getName());
                    dependency.setAutowiringMode(AutowiringMode.BYNAME);
                    dependency.setBeanID(beanId);
                    dependency.setName(constructor.getName());
                }
                else if (autowiredConstructor.autowiringMode().equals(AutowiringMode.BYTYPE)){
                    dependency.setReference(constructor.getDeclaringClass().getSimpleName());
                    dependency.setAutowiringMode(AutowiringMode.BYTYPE);
                    dependency.setBeanID(beanId);
                }
                constructorDependencies.add(dependency);
            }
        }
        return constructorDependencies;
    }


}
