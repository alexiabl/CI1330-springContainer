package cr.ac.ucr.ecci.ci1330.IoC.AnnotationBased;


import cr.ac.ucr.ecci.ci1330.IoC.AbstractBeanFactory;
import cr.ac.ucr.ecci.ci1330.IoC.Bean;
import cr.ac.ucr.ecci.ci1330.IoC.ScopeType;

import java.util.List;

public class AnnotationBeanFactory extends AbstractBeanFactory {

    private AnnotationParser annotationParser;


    public AnnotationBeanFactory(Class annotadedClasses) {
        annotationParser = new AnnotationParser(annotadedClasses, this);
    }

    @Override
    public Object getBean(String id) {
        try {
            if (beanHashMap.get(id).getScopeType().equals(ScopeType.PROTOTYPE)) {
                Bean bean = createBean(annotationParser.getAnnotationBeanContent());
                bean.setBeanInstance(injectBeanInstance(bean));
                return bean.getBeanInstance();
            }
            return beanHashMap.get(id).getBeanInstance();
        } catch (NullPointerException e) {
            System.out.println("El id '" + id + "' no identifica nigún bean.");
            return null;
        }
    }

}

