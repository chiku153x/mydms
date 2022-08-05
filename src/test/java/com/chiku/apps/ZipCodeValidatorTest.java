package com.chiku.apps;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class ZipCodeValidatorTest {

    ZipCodeValidator validator = new ZipCodeValidator();

    @Test
    public void canValidateValidGermanZipCode() {
        assertThat(validator.isGermanZipCode("71245"), is(true));
    }

    @Test
    public void canValidateInvalidGermanZipCode() {
        assertThat(validator.isGermanZipCode("0815"), is(false));
    }

}
