package cr.ac.ucr.ecci.ci1330.IoC.AnnotationBased;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by alexiaborchgrevink on 9/24/17.
 */


@Retention(RetentionPolicy.RUNTIME)
public @interface Component {

   String value();

}
