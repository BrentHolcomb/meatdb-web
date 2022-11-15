package com.holcomb.meatdbweb.web.controller;

import com.holcomb.meatdbweb.biz.model.MeatTransaction;
import com.holcomb.meatdbweb.biz.model.MeatType;
import com.holcomb.meatdbweb.data.SupplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/update")
public class DatabaseUpdateController {
    private SupplyRepository supplyRepository;

    @Autowired
    public void setSupplyRepository(SupplyRepository supplyRepository) {
        this.supplyRepository = supplyRepository;
    }

    @GetMapping
    public String showUpdatePage() {
        return "databaseUpdate";
    }

    // updates database with missing entries from 3 months prior
    @PostMapping(params = "update=true")
    public String updateDatabase(Model model) {
        saveGeneratedMeatTransaction();
        model.addAttribute("updateMsg", "The database has been updated.");
        return "databaseUpdate";
    }

    // deletes all entries in database
    @PostMapping(params = "empty=true")
    public String emptyDatabase(Model model) {
        supplyRepository.deleteAll();
        model.addAttribute("emptyMsg", "The database has been emptied.");
        return "databaseUpdate";
    }

    private void saveGeneratedMeatTransaction() {
        // generate and save entries for previous 3 months and this month
        for (int i = 4; i > 0; i--) {
            YearMonth currentMonth = YearMonth.now().minusMonths(i - 1);
            int monthInt = currentMonth.get(ChronoField.MONTH_OF_YEAR);
            int yearInt = currentMonth.get(ChronoField.YEAR);

            // get entries for current iteration month
            Set<MeatTransaction> entries = supplyRepository.getEntriesOfDate(String.valueOf(yearInt), String.valueOf(monthInt));

            // to prevent making future entries, sets this month's days count to today
            int daysCount;
            if (i == 1) {
                daysCount = LocalDate.now().getDayOfMonth();
            } else {
                daysCount = currentMonth.lengthOfMonth();
            }
            supplyRepository.saveAll(generateMonthMeatTransactions(currentMonth, daysCount, entries));
        }
    }

    // makes missing entries for a given month and amount of days
    private List<MeatTransaction> generateMonthMeatTransactions(YearMonth currentMonth, int daysCount, Set<MeatTransaction> entries) {
        // list to hold newly added entries for the month
        List<MeatTransaction> meatTransactions = new ArrayList<>();

        // for each day of the month, find the existing "meat types" used in entries
        for (int d = 1; d < daysCount + 1; d++) {
            LocalDate currentDay = LocalDate.parse(currentMonth.toString().concat(String.format("-%02d", d)));
            Set<MeatType> existingMeatTypes = entries.stream()
                    .filter(e -> e.getDateOfTransaction().equals(currentDay))
                    .map(MeatTransaction::getType)
                    .collect(Collectors.toSet());

            // generate new entries for missing "meat types" for the current iteration day
            if (!existingMeatTypes.contains(MeatType.REGULAR)) {
                meatTransactions.add(getAutoMeatTransaction(currentDay, MeatType.REGULAR));
            }
            if (!existingMeatTypes.contains(MeatType.SPICY)) {
                meatTransactions.add(getAutoMeatTransaction(currentDay, MeatType.SPICY));
            }
            if (!existingMeatTypes.contains(MeatType.GRILLED)) {
                meatTransactions.add(getAutoMeatTransaction(currentDay, MeatType.GRILLED));
            }
        }
        return meatTransactions;
    }

    // creates generated entries using set ranges
    private MeatTransaction getAutoMeatTransaction(LocalDate dateOfTransaction, MeatType meatType) {
        return new MeatTransaction(null,
                dateOfTransaction,
                meatType,
                getRandomBigDecimal(20, 30),
                getRandomBigDecimal(4, 7),
                getRandomBigDecimal(25, 40),
                getRandomBigDecimal(75, 200),
                getRandomBigDecimal(75, 140));
    }

    // returns a random number between the min and max with 2 decimal places
    private BigDecimal getRandomBigDecimal(int min, int max) {
        BigDecimal bigMin = new BigDecimal(min);
        BigDecimal bigMax = new BigDecimal(max);
        BigDecimal bigRandom = new BigDecimal(Math.random());
        return bigRandom
                .multiply(bigMax.subtract(bigMin))
                .add(bigMin)
                .setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }
}
