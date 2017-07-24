package org.carbon.util.mapper.cast;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author Shota Oda 2017/03/20.
 */
public interface StringAnyCaster<TO> extends Caster<String, TO> {
    StringAnyCaster<String> identity = source -> source;
    StringAnyCaster<Integer> intCaster = Integer::parseInt;
    StringAnyCaster<Long> longCaster = Long::parseLong;
    StringAnyCaster<Float> floatCaster = Float::parseFloat;
    StringAnyCaster<Double> doubleCaster = Double::parseDouble;
    StringAnyCaster<Byte> byteCaster = Byte::parseByte;
    StringAnyCaster<Short> shortCaster = Short::parseShort;
    StringAnyCaster<Boolean> boolCaster = Boolean::parseBoolean;
    StringAnyCaster<Character> charCaster = s -> s.charAt(0);
    StringAnyCaster<LocalDate> localDateCaster = s -> LocalDate.parse(s, DateTimeFormatter.ISO_DATE);
    StringAnyCaster<LocalDateTime> localDateTimeCaster = s -> LocalDateTime.parse(s, DateTimeFormatter.ISO_DATE_TIME);
}
