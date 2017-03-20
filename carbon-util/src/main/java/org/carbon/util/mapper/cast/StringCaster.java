package org.carbon.util.mapper.cast;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Shota Oda 2017/03/20.
 */
public interface StringCaster<TO> extends Caster<String, TO> {
    StringCaster<String> identity = source -> source;
    StringCaster<Integer> intCaster = Integer::parseInt;
    StringCaster<Long> longCaster = Long::parseLong;
    StringCaster<Float> floatCaster = Float::parseFloat;
    StringCaster<Double> doubleCaster = Double::parseDouble;
    StringCaster<Byte> byteCaster = Byte::parseByte;
    StringCaster<Short> shortCaster = Short::parseShort;
    StringCaster<Boolean> boolCaster = Boolean::parseBoolean;
    StringCaster<Character> charCaster = s -> s.charAt(0);
    StringCaster<LocalDate> localDateCaster = s -> LocalDate.parse(s, DateTimeFormatter.ISO_DATE);
    StringCaster<LocalDateTime> localDateTimeCaster = s -> LocalDateTime.parse(s, DateTimeFormatter.ISO_DATE_TIME);
}
