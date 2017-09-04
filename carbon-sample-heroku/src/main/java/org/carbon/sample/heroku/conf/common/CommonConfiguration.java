package org.carbon.sample.heroku.conf.common;

import org.apache.commons.text.RandomStringGenerator;
import org.carbon.component.annotation.Component;
import org.carbon.component.annotation.Configuration;

import static org.apache.commons.text.CharacterPredicates.DIGITS;
import static org.apache.commons.text.CharacterPredicates.LETTERS;

/**
 * @author Shota Oda 2017/07/30.
 */
@Configuration
public class CommonConfiguration {

    @Component
    public RandomStringGenerator randomStringGenerator() {
        return new RandomStringGenerator.Builder()
                .withinRange('0', 'z')
                .filteredBy(LETTERS, DIGITS)
                .build();
    }
}
