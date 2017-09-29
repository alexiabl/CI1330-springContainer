package cr.ac.ucr.ecci.ci1330.IoC;

import cr.ac.ucr.ecci.ci1330.IoC.Bean.Bean;

import java.util.HashMap;

/**
 * Created by majo_ on 22/9/2017.
 */
public interface BeanFactoryContainer {

        Bean createBean(HashMap<String, Object> beanInformation);

        Object getBean(String id);

        void destroyBean(String id);

}
