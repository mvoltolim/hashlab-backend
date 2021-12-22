package com.hash.hashlab.utils;

import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;

@Component
public class DateProvider {

    public boolean isBlackFriday() {
        return LocalDate.now().equals(LocalDate.now().withMonth(Month.NOVEMBER.getValue()).with(TemporalAdjusters.lastInMonth(DayOfWeek.FRIDAY)));
    }

}
