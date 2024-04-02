package com.example.MuseumTicketing.Repo;

import com.example.MuseumTicketing.Model.PublicDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PublicDetailsRepo extends JpaRepository<PublicDetails, Long> {

    Optional<PublicDetails> findBySessionId(String sessionId);

    Optional<PublicDetails> findByMobileNumber(String mobileNumber);

    PublicDetails findByPaymentid(String paymentid);

    boolean existsByPaymentid(String paymentId);

    Optional<PublicDetails> findByOrderId(String orderId);

    Optional<PublicDetails> findByticketId(String ticketId);

    List<PublicDetails> findByVisitDate(LocalDate currentDate);

    List<? extends Object> findByVisitDateBetween(LocalDate startDate, LocalDate endDate);

    default int countVisitorsToDate(LocalDate currentDate) {
        return Optional.ofNullable(countVisitorsToDateOrNull(currentDate)).orElse(0);
    }

    @Query("SELECT SUM(p.numberOfAdults + p.numberOfChildren + p.numberOfSeniors) FROM PublicDetails p WHERE p.visitDate <= :currentDate AND p.visitStatus = true")
    Integer countVisitorsToDateOrNull(LocalDate currentDate);

    @Query("SELECT COUNT(p) FROM PublicDetails p WHERE p.ticketId IS NOT NULL")
    Integer countTotalPublicTicketsWithticketId();

    @Query("SELECT SUM(p.totalPrice) FROM PublicDetails p WHERE p.ticketId IS NOT NULL")
    Double calculateTotalPublicIncome();


    default double safeCalculateTotalPublicIncomeForMonth(int month, int year) {
        Double totalIncome = calculateTotalPublicIncomeForMonth(month, year);
        return totalIncome != null ? totalIncome : 0;
    }
    @Query("SELECT SUM(p.totalPrice) FROM PublicDetails p WHERE MONTH(p.visitDate) = :month AND YEAR(p.visitDate) = :year AND p.ticketId IS NOT NULL")
    Double calculateTotalPublicIncomeForMonth(@Param("month") int month, @Param("year") int year);

}



