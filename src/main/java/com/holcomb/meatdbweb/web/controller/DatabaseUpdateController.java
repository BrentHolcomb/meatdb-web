package com.holcomb.meatdbweb.web.controller;

import com.holcomb.meatdbweb.biz.model.MeatTransaction;
import com.holcomb.meatdbweb.biz.model.MeatType;
import com.holcomb.meatdbweb.biz.service.DatabaseUpdateService;
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
    private DatabaseUpdateService databaseUpdateService;

    @Autowired
    public void setSupplyRepository(SupplyRepository supplyRepository, DatabaseUpdateService databaseUpdateService) {
        this.supplyRepository = supplyRepository;
        this.databaseUpdateService = databaseUpdateService;
    }

    @GetMapping
    public String showUpdatePage() {
        return "databaseUpdate";
    }

    // updates database with missing entries from 3 months prior
    @PostMapping(params = "update=true")
    public String updateDatabase(Model model) {
        databaseUpdateService.saveGeneratedMeatTransaction();
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
}
