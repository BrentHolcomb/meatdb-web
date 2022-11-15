package com.holcomb.meatdbweb.biz.service;

import com.holcomb.meatdbweb.biz.model.MeatTransaction;
import com.holcomb.meatdbweb.biz.model.MeatType;
import com.holcomb.meatdbweb.data.SupplyRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Log4j2
public class MeatTransactionService {

    private final SupplyRepository supplyRepository;

    public MeatTransactionService(SupplyRepository supplyRepository) {
        this.supplyRepository = supplyRepository;
    }

    // saves entry to DB without duplicating
    public MeatTransaction save(MeatTransaction meatTransaction) {
        // Replace old entry if matching date & type are found
        // Check for matching entry
        Long matchingId = idOfMatchingEntryDateAndType(meatTransaction);
        if (matchingId != null) {
            // Remove DB entry
            try {
                supplyRepository.deleteById(meatTransaction.getId());
            } catch (Exception e) {
                log.info("No transaction to delete");
            }
            // Re-use the removed entry's ID
            meatTransaction.setId(matchingId);
        }
        supplyRepository.save(meatTransaction);
        return meatTransaction;
    }

    public void deleteAllById(Iterable<Long> ids) {
        supplyRepository.deleteAllById(ids);
    }

    public Optional<MeatTransaction> findById(Long id) {
        return supplyRepository.findById(id);
    }

    // finds the ID of DB entries matching the current "meat transaction"
    private Long idOfMatchingEntryDateAndType(MeatTransaction meatTransaction) {
        List<MeatTransaction> matchingDBEntries = StreamSupport.stream(supplyRepository.findAll().spliterator(), false)
                .filter(dbe -> meatTransaction.getType() == dbe.getType())
                .filter(dbe -> meatTransaction.getDateOfTransaction().equals(dbe.getDateOfTransaction()))
                .collect(Collectors.toList());
        if (matchingDBEntries.isEmpty()) {
            return null;
        }
        return matchingDBEntries.get(0).getId();
    }

    // gets entries from date
    public Iterable<MeatTransaction> getDateEntries(int displayYear, int displayMonth) {
        Set<MeatTransaction> meatTransactions = supplyRepository.getEntriesOfDate(String.valueOf(displayYear), String.valueOf(displayMonth));
        return meatTransactions;
    }

    // gets entries from "meat type" and date
    public Iterable<MeatTransaction> getMeatTypeAndDateEntries(MeatType meatType, int displayYear, int displayMonth) {
        Set<MeatTransaction> meatTransactions = supplyRepository.getEntriesOfMeatTypeAndDate(meatType.toString(), String.valueOf(displayYear), String.valueOf(displayMonth));
        return meatTransactions;
    }

    // gets entries from DB and combines into summed entries
    public Iterable<MeatTransaction> getDatePageCombinedTransactions(int displayYear, int displayMonth) {
        // group entries by date
        Map<LocalDate, List<MeatTransaction>> dateGroupedMeatTransactions = StreamSupport.stream(getDateEntries(displayYear, displayMonth).spliterator(), false)
                .collect(Collectors.groupingBy(e -> e.getDateOfTransaction()));
        // make a list of grouped entries
        List<List<MeatTransaction>> listOfGroupedTransactions = new ArrayList<>(dateGroupedMeatTransactions.values());
        // merge each group into a totalled entry
        Set<MeatTransaction> combinedTransactions = listOfGroupedTransactions.stream()
                .map(MeatTransactionService::getCombinedTransactions)
                .collect(Collectors.toSet());
        return sortTransactionsByDate(combinedTransactions);
    }

    // orders entries by date
    public static Iterable<MeatTransaction> sortTransactionsByDate(Iterable<MeatTransaction> meatTransactions) {
        List<MeatTransaction> dateFilteredEntries = StreamSupport.stream(meatTransactions.spliterator(), false)
                .sorted(Comparator.comparing(MeatTransaction::getDateOfTransaction))
                .collect(Collectors.toList());
        return dateFilteredEntries;
    }

    // combines transactions into a summed up transaction
    public static MeatTransaction getCombinedTransactions(List<MeatTransaction> meatTransactions) {
        LocalDate dateOfTransaction;
        if (meatTransactions.size() == 0) {
            dateOfTransaction = null;
        } else {
            dateOfTransaction = meatTransactions.get(0).getDateOfTransaction();
        }
        MeatTransaction combinedTransaction = new MeatTransaction(null,
                dateOfTransaction,
                null,
                meatTransactions.stream().map(MeatTransaction::getSold).reduce(BigDecimal.ZERO, BigDecimal::add),
                meatTransactions.stream().map(MeatTransaction::getWaste).reduce(BigDecimal.ZERO, BigDecimal::add),
                meatTransactions.stream().map(MeatTransaction::getPurchase).reduce(BigDecimal.ZERO, BigDecimal::add),
                meatTransactions.stream().map(MeatTransaction::getSales).reduce(BigDecimal.ZERO, BigDecimal::add),
                meatTransactions.stream().map(MeatTransaction::getExpense).reduce(BigDecimal.ZERO, BigDecimal::add));
        return combinedTransaction;
    }
}
