package com.holcomb.meatdbweb.data;

import com.holcomb.meatdbweb.biz.model.MeatTransaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SupplyRepository extends CrudRepository<MeatTransaction, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM MEAT_TRANSACTION ORDER BY DATE_OF_TRANSACTION ASC")
    public Set<MeatTransaction> getEntriesInOrder();
    @Query(nativeQuery = true, value = "SELECT * FROM MEAT_TRANSACTION WHERE MONTH(DATE_OF_TRANSACTION)=:MONTH AND YEAR(DATE_OF_TRANSACTION)=:YEAR ORDER BY DATE_OF_TRANSACTION ASC")
    public Set<MeatTransaction> getEntriesOfDate(@Param("YEAR") String displayYear, @Param("MONTH") String displayMonth);
    @Query(nativeQuery = true, value = "SELECT * FROM MEAT_TRANSACTION WHERE TYPE=:TYPE AND MONTH(DATE_OF_TRANSACTION)=:MONTH AND YEAR(DATE_OF_TRANSACTION)=:YEAR ORDER BY DATE_OF_TRANSACTION ASC")
    public Set<MeatTransaction> getEntriesOfMeatTypeAndDate(@Param("TYPE") String meatType, @Param("YEAR") String displayYear, @Param("MONTH") String displayMonth);
}