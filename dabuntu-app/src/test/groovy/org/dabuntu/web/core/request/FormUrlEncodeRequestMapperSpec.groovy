package org.dabuntu.web.core.request

import lombok.ToString
import org.dabuntu.util.mapper.NameBasedObjectMapper
import spock.lang.Specification

import javax.servlet.http.HttpServletRequest

/**
 * @author ubuntu 2016/12/04.
 */
class FormUrlEncodeRequestMapperSpec extends Specification {
    class Target {
        @Override
        public String toString() {
            return "Target{" +
                    "string='" + string + '\'' +
                    ", strings=" + strings +
                    ", inner=" + inner +
                    ", elements=" + elements +
                    '}';
        }

        class Inner {
            Integer age
            @Override
            public String toString() {
                return "Inner{" +
                        "age=" + age +
                        '}';
            }
        }
        class InnerElement {
            boolean check
            Integer index
            @Override
            public String toString() {
                return "InnerElement{" +
                        "check=" + check +
                        ", index=" + index +
                        '}';
            }
        }
        String string
        List<String> strings
        Inner inner
        List<InnerElement> elements

    }
    def "test"() {

        setup:
        def request = [getParameterMap: { ->
            def map = new HashMap<String, String[]>()

            map.put('string', ['hoge'] as String[])
            map.put('strings[0]', ['hello'] as String[])
            map.put('strings[1]', ['world'] as String[])
            map.put('elements[0].check', ['true'] as String[])
            map.put('elements[0].index', ['1'] as String[])
            map.put('elements[1].check', ['false'] as String[])
            map.put('elements[1].index', ['2'] as String[])
            map.put('inner.age', ['100'] as String[])

            return map
        }] as HttpServletRequest

        def mapper = new FormUrlEncodeRequestMapper()
        mapper.objectMapper = new NameBasedObjectMapper();
        def mapped = mapper.map(request, Target.class)
        println mapped
    }
}