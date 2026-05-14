package com.hotel.booking.ifsp.domain.guest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

@Tag("UnitTest")
@Tag("TDD")
class CPFTest {

    @Test
    @DisplayName("Should throw IllegalArgumentException when CPF is invalid")
    void shouldThrowWhenCPFIsInvalid() {
        assertThatThrownBy(() -> new CPF("000.000.000-00"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when CPF has wrong format")
    void shouldThrowWhenCPFHasWrongFormat() {
        assertThatThrownBy(() -> new CPF("123.456.789-00"))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should throw NullPointerException when CPF is null")
    void shouldThrowWhenCPFIsNull() {
        assertThatThrownBy(() -> new CPF(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Should create CPF successfully when value is valid")
    void shouldCreateCPFSuccessfullyWhenValueIsValid() {
        assertThatCode(() -> new CPF("529.982.247-25"))
                .doesNotThrowAnyException();
    }

    @Test
    @Tag("Structural")
    @Tag("UnitTest")
    @DisplayName("Should throw when IllegalArgumentException CPF length is invalid")
    void shouldThrowWhenCpfLengthIsInvalid() {
        assertThatThrownBy(() -> new CPF("123"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid CPF: 123");
    }

    @Test
    @Tag("Structural")
    @Tag("UnitTest")
    @DisplayName("Should throw IllegalArgumentException when first check digit is invalid")
    void shouldThrowWhenFirstCheckDigitIsInvalid() {
        assertThatThrownBy(() -> new CPF("52998224735"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid CPF: 52998224735");
    }

    @Test
    @Tag("Structural")
    @Tag("UnitTest")
    @DisplayName("Should accept CPF when second check digit resets to zero")
    void shouldAcceptCpfWhenSecondCheckDigitResetsToZero() {
        String validValue = "10000000280";
        CPF cpf = new CPF(validValue);

        assertThat(cpf.value()).isEqualTo(validValue);
    }

    @Test
    @Tag("Structural")
    @Tag("UnitTest")
    @DisplayName("Should return value on toString")
    void shouldReturnValueOnToString() {
        String validValue = "52998224725";
        CPF cpf = new CPF(validValue);

        assertThat(cpf.toString()).isEqualTo(validValue);
    }

    @Test
    @Tag("Mutation")
    @Tag("UnitTest")
    @DisplayName("Should accept CPF when first check digit resets to zero")
    void shouldAcceptCpfWhenFirstCheckDigitResetsToZero() {
        String validValue = "10000000108";
        CPF cpf = new CPF(validValue);

        assertThat(cpf.toString()).isEqualTo(validValue);
    }
}
