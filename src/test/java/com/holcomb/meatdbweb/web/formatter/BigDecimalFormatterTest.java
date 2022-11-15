package com.holcomb.meatdbweb.web.formatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;

class BigDecimalFormatterTest {
    private BigDecimalFormatter formatter;

    @BeforeEach
    void setUp() {
        formatter = new BigDecimalFormatter();
    }

    @Test
    public void canPrintMoneyWithLeadingDollarSign() {
        BigDecimal money = new BigDecimal("500");
        String formattedMoney = formatter.printMoney(money);
//        System.out.println(formattedMoney);
        assertTrue(formattedMoney.startsWith("$"));
    }

    @Test
    public void canPrintMoneyAndCommasAndTrailing00() {
        BigDecimal money = new BigDecimal("5000");
        String formattedMoney = formatter.printMoney(money);
//        System.out.println(formattedMoney);
        assertEquals("$5,000.00", formattedMoney);
    }

    @Test
    public void canParseMoney() throws ParseException {
        String formattedMoney = "$15,003.45";
        BigDecimal money = formatter.parseMoney(formattedMoney);
        assertEquals(new BigDecimal("15003.45"), money);
    }

    @Test
    public void returnZeroIfCannotParseMoney() throws ParseException {
        String formattedMoney = "don't parse this";
        BigDecimal money = formatter.parseMoney(formattedMoney);
        assertEquals(null, money);
    }

    @Test
    public void canAddTrailingLb() {
        String formattedPounds = formatter.printPounds(new BigDecimal("15.4"));
        assertEquals("15.4 lb", formattedPounds);
    }

    @Test
    public void canRemoveNonNumberCharacters() {
        String formattedPounds = "23.2 lb";
        assertEquals("23.2", formatter.parsePounds(formattedPounds).toString());
    }

    @Test
    public void canRemoveOtherSuffixCharacters() {
        String formattedPounds = "22 Pounds";
        assertEquals("22", formatter.parsePounds(formattedPounds).toString());
    }

    @Test
    public void canRemoveSuffixWithNoSpace() {
        String formattedPounds = "163.3supplyPounds";
        assertEquals("163.3", formatter.parsePounds(formattedPounds).toString());
    }

    @Test
    public void canReturnWithoutSuffix() {
        String formattedPounds = "23.2";
        assertEquals("23.2", formatter.parsePounds(formattedPounds).toString());
    }

    @Test
    public void returnsNullIfDoesNotStartWithDigit() {
        String formattedPounds = "Non-Digit23.2";
        assertNull(formatter.parsePounds(formattedPounds));
    }
}