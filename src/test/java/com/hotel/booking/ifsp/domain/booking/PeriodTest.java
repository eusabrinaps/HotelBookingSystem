package com.hotel.booking.ifsp.domain.booking;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@Tag("UnitTest")
@Tag("TDD")
class PeriodTest {

    @Test
    @DisplayName("Should throw IllegalArgumentException when check-out equals check-in")
    void shouldThrowWhenCheckOutEqualsCheckIn() {
        LocalDate date = LocalDate.now().plusDays(1);

        assertThatThrownBy(() -> new Period(date, date))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when check-out is before check-in")
    void shouldThrowWhenCheckOutIsBeforeCheckIn() {
        LocalDate checkIn = LocalDate.now().plusDays(3);
        LocalDate checkOut = LocalDate.now().plusDays(1);

        assertThatThrownBy(() -> new Period(checkIn, checkOut))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should create period successfully when check-out is after check-in")
    void shouldCreatePeriodSuccessfullyWhenDatesAreValid() {
        LocalDate checkIn = LocalDate.now().plusDays(1);
        LocalDate checkOut = LocalDate.now().plusDays(2);

        assertThatCode(() -> new Period(checkIn, checkOut))
                .doesNotThrowAnyException();
    }

    @Test
    @Tag("Structural")
    @Tag("UnitTest")
    @DisplayName("Should return number of days between check-in and check-out")
    void shouldReturnNumberOfDaysBetweenCheckInAndCheckOut() {
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = checkIn.plusDays(5);
        Period period = new Period(checkIn, checkOut);

        assertThat(period.numberOfDays()).isEqualTo(5);
    }

    @Test
    @Tag("Structural")
    @Tag("UnitTest")
    @DisplayName("Should return true when periods overlap")
    void shouldReturnTrueWhenPeriodsOverlap() {
        LocalDate checkIn1 = LocalDate.now().plusDays(1);
        LocalDate checkOut1 = LocalDate.now().plusDays(5);
        Period period1 = new Period(checkIn1, checkOut1);

        LocalDate checkIn2 = LocalDate.now().plusDays(3);
        LocalDate checkOut2 = LocalDate.now().plusDays(7);
        Period period2 = new Period(checkIn2, checkOut2);

        assertThat(period1.overlapsWith(period2)).isTrue();
    }

    @Test
    @Tag("Structural")
    @Tag("UnitTest")
    @DisplayName("Should return false when check-in is on or after other check-out")
    void shouldReturnFalseWhenCheckInIsOnOrAfterOtherCheckOut() {
        LocalDate today = LocalDate.now();
        Period period1 = new Period(today.plusDays(10), today.plusDays(12));
        Period period2 = new Period(today.plusDays(1), today.plusDays(9));

        assertThat(period1.overlapsWith(period2)).isFalse();
    }
}
