package org.carbon.util.mapper

import spock.lang.Specification

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * @author Shota Oda 2016/12/01.
 */
class NameBasedObjectMapperSpec extends Specification {
    class Form {
        class Inner1 {
            class Inner2 {
                Integer integer
                Long longf
                Character character
                String string
                LocalDateTime localDateTime = LocalDateTime.now()
            }
            Inner2 inner2
            Pojo pojo
            Integer integer
            Long longf
            Character character
            String string
            LocalDateTime localDateTime
        }
        Inner1 inner
        Integer integer
        Long longf
        Character character
        String string
        LocalDateTime localDateTime
        List<Pojo> pojos
        List<String> strings
        List<Inner1> inners
    }

    class Pojo {
        String string;
    }

    def createPojo(value) {
        def map = new HashMap()
        map.put('string', value)
        return map
    }

    def createInner(now) {
        def map = new HashMap()
        map.put('integer', '1000')
        map.put('string', 'inner string')
        map.put('longf', '987654321')
        map.put('character', 'c')
        map.put('localDateTime', now)
        return map
    }

    def "TestMap"() {
        setup:
        def formatter = DateTimeFormatter.ISO_DATE_TIME
        def now = LocalDateTime.now()
        def nowString = formatter.format(now)
        def map = new HashMap<>();

        def inner = createInner(nowString)
        inner.put('pojo', createPojo("inner pojo"))
        inner.put('inner2', createInner(nowString))

        def pojos = Arrays.asList(createPojo("hoge"), createPojo("fuga"), createPojo("piyo"))
        def strings = Arrays.asList("admin", "system", "power")
        def inners = Arrays.asList(createInner(nowString), createInner(nowString), createInner(nowString))

        // top level field
        map.put("integer", '100')
        map.put("longf", '123456789')
        map.put("character", 'c')
        map.put("string", "sssss")
        map.put("localDateTime", now)
        // list
        map.put("pojos", pojos)
        map.put("strings", strings)
        // inner
        map.put("inner", inner)
        // inner list
        map.put("inners", inners)

        def mapper = new KeyValueMapper()
        def form = mapper.mapAndConstruct(map, Form.class)

        expect:
        // top level field
        form.integer == 100
        form.longf == 123456789L
        Character c = 'c'
        form.character == c
        form.string == 'sssss'
        form.localDateTime == now

        form.strings.get(0) == 'admin'
        form.strings.get(1) == 'system'
        form.strings.get(2) == 'power'

        form.pojos.get(0).string == 'hoge'
        form.pojos.get(1).string == 'fuga'
        form.pojos.get(2).string == 'piyo'

        def formInner = form.inner
        formInner.integer == 1000
        formInner.string == 'inner string'
        formInner.longf == 987654321L
        formInner.character == c
        formInner.localDateTime == now
        formInner.pojo.string == 'inner pojo'

        def fInIn = formInner.inner2
        fInIn.integer == 1000
        fInIn.string == 'inner string'
        fInIn.longf == 987654321L
        fInIn.character == c
        fInIn.localDateTime == now
    }
}
