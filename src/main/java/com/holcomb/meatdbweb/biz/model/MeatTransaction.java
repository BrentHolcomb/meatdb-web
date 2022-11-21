package com.holcomb.meatdbweb.biz.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MeatTransaction {
    @Id
    @GeneratedValue
    private Long id;

    @PastOrPresent(message = "The selected date cannot be in the future")
    @NotNull(message = "A date must be selected")
    private LocalDate dateOfTransaction;

    @NotNull(message = "The meat type is missing")
    @Enumerated(EnumType.STRING)
    private MeatType type;

    @PositiveOrZero(message = "Pounds of meat sold must be positive")
    @NotNull(message = "Pounds of meat sold must be entered")
    @Digits(integer=5, fraction=3, message = "Pounds must be less than 100,000 lb and have no more than 3 decimal places")
    private BigDecimal sold;

    @PositiveOrZero(message = "Pounds of meat waste must be positive")
    @NotNull(message = "Pounds of meat wasted must be entered")
    @Digits(integer=5, fraction=3, message = "Pounds must be less than 100,000 lb and have no more than 3 decimal places")
    private BigDecimal waste;

    @PositiveOrZero(message = "Pounds of meat purchased must be positive")
    @NotNull(message = "Pounds of meat purchased must be entered")
    @Digits(integer=5, fraction=3, message = "Pounds must be less than 100,000 lb and have no more than 3 decimal places")
    private BigDecimal purchase;

    @PositiveOrZero(message = "Dollar sales for sold meat must be positive")
    @NotNull(message = "Dollar sales for sold meat must be entered")
    @Digits(integer=7, fraction=2, message = "Dollar value must be less than $10,000,000 and have no more than 2 decimal places")
    private BigDecimal sales;

    @PositiveOrZero(message = "Dollar expense for purchased meat must be positive")
    @NotNull(message = "Dollar expense for purchased meat must be entered")
    @Digits(integer=7, fraction=2, message = "Dollar value must be less than $10,000,000 and have no more than 2 decimal places")
    private BigDecimal expense;
}
