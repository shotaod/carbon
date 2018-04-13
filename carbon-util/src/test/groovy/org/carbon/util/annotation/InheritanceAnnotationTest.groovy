package org.carbon.util.annotation

import spock.lang.Specification

import java.lang.annotation.Documented
import java.lang.annotation.Retention
import java.lang.annotation.Target

import static java.lang.annotation.ElementType.ANNOTATION_TYPE
import static java.lang.annotation.RetentionPolicy.RUNTIME

/**
 * @author Shota.Oda 2018/02/23.
 */

// circular annotation reference
@Retention(RUNTIME)
@Target(ANNOTATION_TYPE)
@CircularPartner
@interface Circular {}

@Retention(RUNTIME)
@Target(ANNOTATION_TYPE)
@Circular
@interface CircularPartner {}

@Retention(RUNTIME)
@Target(ANNOTATION_TYPE)
@interface Super {}

@Retention(RUNTIME)
@Target(ANNOTATION_TYPE)
@Documented
@Super
@interface Intermediate {}

@Retention(RUNTIME)
@Target(ANNOTATION_TYPE)
@Circular
@Intermediate
@interface TestedBase {}

@Retention(RUNTIME)
@Target(ANNOTATION_TYPE)
@interface Outsider {}


class InheritanceAnnotationTest extends Specification {

    def "test isAnnotated"() {
        when:
        def inheritanceAnnotation = new InheritanceAnnotation<>(TestedBase.class)
        then:
        inheritanceAnnotation.isAnnotated(TestedBase.class)
        inheritanceAnnotation.isAnnotated(Intermediate.class)
        inheritanceAnnotation.isAnnotated(Super.class)
        inheritanceAnnotation.isAnnotated(Circular.class)
        inheritanceAnnotation.isAnnotated(CircularPartner.class)

        !inheritanceAnnotation.isAnnotated(Outsider.class)
    }
}
