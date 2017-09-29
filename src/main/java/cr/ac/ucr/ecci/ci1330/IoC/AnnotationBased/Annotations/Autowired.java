package cr.ac.ucr.ecci.ci1330.IoC.AnnotationBased.Annotations;

import cr.ac.ucr.ecci.ci1330.IoC.AutowiringMode;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by alexiaborchgrevink on 9/24/17.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {

    AutowiringMode autowiringMode() default AutowiringMode.BYTYPE;





}
