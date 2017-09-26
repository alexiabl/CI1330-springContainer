package cr.ac.ucr.ecci.ci1330.SpringContainer.AnnotationBased;

import cr.ac.ucr.ecci.ci1330.SpringContainer.ScopeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by alexiaborchgrevink on 9/24/17.
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface Scope {

    ScopeType scopeType() default ScopeType.SINGLETON; //default



}
