package cr.ac.ucr.ecci.ci1330.SpringContainer;


import cr.ac.ucr.ecci.ci1330.SpringContainer.AnnotationBased.AnnotationBeanFactory;
import cr.ac.ucr.ecci.ci1330.SpringContainer.XMLBased.XMLBeanFactory;

public class Main {
    public static void main(String[] args) {

        AnnotationBeanFactory annotationBeanFactory = new AnnotationBeanFactory(Persona.class);


        String path = "beans.xml";
        AbstractBeanFactory abstractBeanFactory= new XMLBeanFactory(path);
        System.out.println("Tamaño del beanHashMap: " + abstractBeanFactory.getContainerSize());





       System.out.println("\n1) Dependency Injection");
        System.out.println("*Se crea una instancia llamada \"testeReflection\", sin uso de bean y se pregunta el valor del atributo Persona:");
        testeReflection testeReflection = new testeReflection();
        if (testeReflection.getPersona() == null) {
            System.out.println("El valor de Persona en testeReflection es null");
        }
        System.out.println("\n-COMPROBACION DEL SETTER:");
        testeReflection testeReflection1 = (testeReflection) abstractBeanFactory.getBean("TR1");
        System.out.println("*Se crea una instancia del bean \"TR1\", llamada \"testeReflection1\"");
        System.out.println("En la definición del bean se hace usa un set con referencia a un bean de tipo Persona, por lo tanto su valor ya no es null:");
        System.out.println("Edad: " + testeReflection1.getPersona().getEdad() + " y nombre: " + testeReflection1.getPersona().getNombre());
        System.out.println("\n-COMPROBACION DEL CONSTRUCTOR:");
        testeReflection testeReflection2 = (testeReflection) abstractBeanFactory.getBean("TR2");
        System.out.println("*Se crea una instancia del bean \"TR2\", llamada \"testeReflection2\"");
        System.out.println("En la definición del bean se usa un constructor con referencia a un bean de tipo Persona, por lo tanto su valor ya no es null:");
        System.out.println("Edad: " + testeReflection2.getPersona().getEdad() + " y nombre: " + testeReflection2.getPersona().getNombre());


        System.out.println("\n2) Lyfe Cycle");
        System.out.println("-COMPROBACION DEL INITIALIZATION:");
        System.out.println("La clase Persona tiene un metodo llamado \"initMethod\", que se llama por defecto cuando se destruye el respectivo bean, que publica un mensaje. Por ejemplo:");
        System.out.println("*Se crea una instancia del bean \"Persona\", llamada \"persona1\"");
        Persona persona1 = (Persona) abstractBeanFactory.getBean("Persona");
        System.out.println("*Se crea una instancia del bean \"PersonaMethods\", llamada \"persona2\". En esta se asigna como \"initMehod\" su \"destructMethod\" y viceversa con su \"initMehod\"");
        System.out.println("Por lo tanto, cuando se construye el bean se publica el mensaje del \"destructMethod\" (ya que se intercambio con \"initMethod\"):");
        Persona persona2 = (Persona) abstractBeanFactory.getBean("PersonaMethods");
        System.out.println("\n-COMPROBACION DEL DESTROY:");
        System.out.println("Tambien tiene un metodo llamado \"destrucMehod\", que se llama por defecto cuando se destruye el respectivo bean, que publica un mensaje. Por ejemplo:");
        System.out.println("*Se destruye el bean de la instancia \"persona1\"");
        abstractBeanFactory.destroyBean("Persona");
        System.out.println("Cuando se destruye el bean de la instancia \"persona2\" se publica se publica el mensaje del \"initMethod\" (ya que se intercambio con \"destructMethod\"):");
        abstractBeanFactory.destroyBean("PersonaMethods");


        System.out.println("\n3)Scope");
        System.out.println("-COMPROBACION DEL SINGLETON:");
        testeReflection testeReflection3 = (testeReflection) abstractBeanFactory.getBean("TR3");
        System.out.println("\n*Se crea una instancia del bean \"TR3\", llamada \"testeReflection3\"");
        System.out.println("testeReflection3.getNum() por defecto tiene: " + testeReflection3.getNum());
        testeReflection3.setNum(6);
        System.out.println("testeReflection3.getNum() luego de setear un valor de 6: " + testeReflection3.getNum());
        testeReflection testeReflection4 =  (testeReflection) abstractBeanFactory.getBean("TR3");
        System.out.println("\n*Se crea una instancia del bean \"TR3\", llamada \"testeReflection4\"");
        System.out.println("testeReflection4.getNum() es de tipo \"singleton\", por lo que por defecto tiene el ultimo valor de \"testeReflection3\": " + testeReflection4.getNum());
        testeReflection4.setNum(7);
        System.out.println("testeReflection4.getNum() luego de setear un valor de 7: " + testeReflection4.getNum());
        System.out.println("testeReflection3.getNum() cambia de valor, pues se cambio en el testeReflection4 y este es de tipo \"singleton\": " + testeReflection3.getNum());

        System.out.println("\n-COMPROBACION DEL PROTOTYPE");
        testeReflection testeReflection5 = (testeReflection) abstractBeanFactory.getBean("TR4");
        System.out.println("\n*Se crea una instancia del bean \"TR4\", llamada \"testeReflection5\"");
        System.out.println("testeReflection5.getNum() es de tipo \"prototype\" y los cambios en los \"singleton\" no le afectan, asi que por defecto tiene: " + testeReflection5.getNum());
        testeReflection5.setNum(6);
        System.out.println("testeReflection5.getNum() luego de setear un valor de 6: " + testeReflection5.getNum());
        testeReflection testeReflection6 =  (testeReflection) abstractBeanFactory.getBean("TR4");
        System.out.println("\n*Se crea una instancia del bean \"TR4\", llamada \"testeReflection6\"");
        System.out.println("testeReflection6.getNum() es de tipo \"prototype\", por lo que por defecto tiene: " + testeReflection6.getNum());
        testeReflection6.setNum(7);
        System.out.println("testeReflection6.getNum() luego de setear un valor de 7: " + testeReflection6.getNum());
        System.out.println("testeReflection5.getNum() no cambia de valor, pues este es de tipo \"prototype\": " + testeReflection5.getNum());


        /*System.out.println("\n4) Autowiring");
        System.out.println("-COMPROBACION DEL By Name:");
        System.out.println("-COMPROBACION DEL By Type:");
        */
    }
}
