package com.holcomb.meatdbweb.web.controller;

import com.holcomb.meatdbweb.biz.model.MeatTransaction;
import com.holcomb.meatdbweb.biz.model.MeatType;
import com.holcomb.meatdbweb.biz.service.MeatTransactionService;
import com.holcomb.meatdbweb.data.SupplyRepository;
import com.holcomb.meatdbweb.web.formatter.BigDecimalFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.YearMonth;
import java.time.chrono.ChronoLocalDate;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Controller
@RequestMapping("/supply")
public class MeatSupplyController {

    private SupplyRepository supplyRepository;
    private MeatTransactionService meatTransactionService;

    @Autowired
    public MeatSupplyController(SupplyRepository supplyRepository, MeatTransactionService meatTransactionService) {
        this.supplyRepository = supplyRepository;
        this.meatTransactionService = meatTransactionService;
    }

    @ModelAttribute("formatter")
    public BigDecimalFormatter getFormatter() {
        return new BigDecimalFormatter();
    }

    @ModelAttribute
    public MeatTransaction getMeatTransaction() {
        return new MeatTransaction();
    }

    // Returns supply.html template with default model setup
    @GetMapping
    public String showSupplyPage(Model model) {
        modelSetupAndTransactionTypeSet(null, "#F5F5F5", model, null);
        return "supply";
    }

    @GetMapping("/regular")
    public String setMeatTypeRegular(Model model, MeatTransaction meatTransaction) {
        modelSetupAndTransactionTypeSet(MeatType.REGULAR, "#B0E0E6", model, meatTransaction);
        return "supply";
    }

    @GetMapping("/spicy")
    public String setMeatTypeSpicy(Model model, MeatTransaction meatTransaction) {
        modelSetupAndTransactionTypeSet(MeatType.SPICY, "#FA8072", model, meatTransaction);
        return "supply";
    }

    @GetMapping("/grilled")
    public String setMeatTypeGrilled(Model model, MeatTransaction meatTransaction) {
        modelSetupAndTransactionTypeSet(MeatType.GRILLED, "#FFE4B5", model, meatTransaction);
        return "supply";
    }

    // Submit "Meat Entry" Form
    @PostMapping
    public String saveTransaction(Model model, @Valid MeatTransaction meatTransaction, Errors errors) {
        if (!errors.hasErrors()) {
            meatTransactionService.save(meatTransaction);
            return returnPageFromMeatTransaction(model, meatTransaction, true);
        }
        // displays user errors from validation
        return returnPageFromMeatTransaction(model, meatTransaction, false);
    }

    // "Table Selections" Delete
    @PostMapping(params = "delete=true")
    public String deleteTransaction(@RequestParam("selections") Optional<List<Long>> selections, Model model, MeatTransaction meatTransaction) {
        if (selections.isPresent()) {
            model.addAttribute("displayDate", YearMonth.from(meatTransactionService.findById(selections.get().get(0)).get().getDateOfTransaction()));
            meatTransactionService.deleteAllById(selections.get());
        }
        return returnPageFromMeatTransaction(model, meatTransaction, false);
    }

    // 1st "Table Selection" Edit
    @PostMapping(params = "edit=true")
    public String editTransaction(@RequestParam("selections") Optional<List<Long>> selections, Model model , MeatTransaction meatTransaction) {
        if (selections.isPresent()) {
            MeatTransaction editMeatTransaction = meatTransactionService.findById(selections.get().get(0)).get();
            model.addAttribute("meatTransaction", editMeatTransaction);
            model.addAttribute("displayDate", YearMonth.from(editMeatTransaction.getDateOfTransaction()));
            return returnPageFromMeatTransaction(model, editMeatTransaction, false);
        }
        return returnPageFromMeatTransaction(model, meatTransaction, false);
    }

    // sets the date range
    @PostMapping(params = "chosenDate=true")
    public String setDisplayDate(@RequestParam("displayDate") YearMonth displayDate, Model model , MeatTransaction meatTransaction) {
        model.addAttribute("displayDate", displayDate);
        return returnPageFromMeatTransaction(model, meatTransaction, false);
    }

    private void modelSetupAndTransactionTypeSet(MeatType meatType, String color, Model model, MeatTransaction meatTransaction) {
        if (model.getAttribute("displayDate") == null) {
            model.addAttribute("displayDate", YearMonth.now());
        }
        YearMonth displayDate = YearMonth.parse(model.getAttribute("displayDate").toString());
        int displayMonth = displayDate.get(ChronoField.MONTH_OF_YEAR);
        int displayYear = displayDate.get(ChronoField.YEAR);

        // use chosen month to get either combined transaction or "meat type" specific
        Iterable<MeatTransaction> meatTransactions;
        if (meatType == null) {
            meatTransactions = meatTransactionService.getDatePageCombinedTransactions(displayYear, displayMonth);
        } else {
            meatTransactions = meatTransactionService.getMeatTypeAndDateEntries(meatType, displayYear, displayMonth);
        }

        model.addAttribute("meatType", meatType);
        model.addAttribute("meatColor", color);
        model.addAttribute("meatEntries", meatTransactions);
        model.addAttribute("totalledEntry", meatTransactionService.getCombinedTransactions(StreamSupport.stream(meatTransactions.spliterator(), false).collect(Collectors.toList())));
        // sets form's "meat type" to the selected type
        if (meatTransaction != null) {
            meatTransaction.setType(meatType);
        }
    }

    // Uses current "meat transaction" to return the corresponding page
    private String returnPageFromMeatTransaction(Model model, MeatTransaction meatTransaction, Boolean redirect) {
        if (meatTransaction.getType() == null) {
            return showSupplyPage(model);
        }
        if (redirect) {
            switch(meatTransaction.getType()) {
                case REGULAR:
                    return "redirect:supply/regular";
                case SPICY:
                    return "redirect:supply/spicy";
                case GRILLED:
                    return "redirect:supply/grilled";
                default:
                    return "redirect:supply";
            }
        }
        switch(meatTransaction.getType()) {
            case REGULAR:
                return setMeatTypeRegular(model, meatTransaction);
            case SPICY:
                return setMeatTypeSpicy(model, meatTransaction);
            case GRILLED:
                return setMeatTypeGrilled(model, meatTransaction);
            default:
                return "supply";
        }
    }
}
