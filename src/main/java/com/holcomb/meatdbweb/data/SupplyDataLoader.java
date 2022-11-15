package com.holcomb.meatdbweb.data;

import com.holcomb.meatdbweb.biz.model.MeatTransaction;
import com.holcomb.meatdbweb.biz.model.MeatType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

//@Component
public class SupplyDataLoader implements ApplicationRunner {
    private SupplyRepository supplyRepository;

    @Autowired
    public SupplyDataLoader(SupplyRepository supplyRepository) {
        this.supplyRepository = supplyRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (supplyRepository.count() == 0) {
            List<MeatTransaction> meatEntries = List.of(
                    new MeatTransaction(null, LocalDate.of(2022, 10, 19), MeatType.REGULAR, BigDecimal.ZERO, new BigDecimal("5"), new BigDecimal("55"), new BigDecimal("130"), BigDecimal.ZERO),
                    new MeatTransaction(null, LocalDate.of(2022, 10, 20), MeatType.SPICY, new BigDecimal("30"), new BigDecimal("3"), new BigDecimal("40"), new BigDecimal("75"), new BigDecimal("199")),
                    new MeatTransaction(null, LocalDate.of(2022, 10, 21), MeatType.GRILLED, new BigDecimal("20"), new BigDecimal("1"), new BigDecimal("20"), new BigDecimal("36"), new BigDecimal("25"))
            );
            supplyRepository.saveAll(meatEntries);
        }
    }
}
