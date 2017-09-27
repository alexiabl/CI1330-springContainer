package cr.ac.ucr.ecci.ci1330.IoC;

import cr.ac.ucr.ecci.ci1330.IoC.XMLBased.XMLBeanFactory;


public class Main {
    public static void main(String[] args) {
        String path = "beans.xml";
        AbstractBeanFactory abstractBeanFactory= new XMLBeanFactory(path);

        System.out.println("beanHashMap.size()= "+abstractBeanFactory.beanHashMap.size());

        Persona persona= (Persona) abstractBeanFactory.getBean("Persona");
        System.out.println(persona.getNombre());

        testeReflection testeReflection= (cr.ac.ucr.ecci.ci1330.IoC.testeReflection) abstractBeanFactory.getBean("teste");
        System.out.println(testeReflection.getA().getNombre());
        System.out.println(testeReflection.getEst().getName());
      //  System.out.println(testeReflection.);
/*
        testeReflection.setNum(9);
        System.out.println(testeReflection.getP().getName());
        testeReflection testeReflection2= (cr.ac.ucr.ecci.ci1330.IoC.testeReflection) abstractBeanFactory.getBean("teste");
        System.out.println(testeReflection2.getNum());
        testeReflection.setNum(3);
        System.out.println(testeReflection.getNum());
*/
        //errores: al constructor no se le puede poner mas de un parametro
    }
}