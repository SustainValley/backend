package com.likelion.hackathon.repository;

import com.likelion.hackathon.entity.Reservation;
import com.likelion.hackathon.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);

    @Query("""
    SELECT r
    FROM Reservation r
    JOIN r.cafe c
    JOIN c.businessInfo b
    WHERE b.user.id = :ownerId
""")
    List<Reservation> findAllByOwnerId(@Param("ownerId") Long ownerId);

    @Query("""
    SELECT r
    FROM Reservation r
    WHERE r.cafe.businessInfo.user.id = :userId
      AND r.cafe.businessInfo.user.type = :userType
      AND r.date = :date
""")
    List<Reservation> findReservationsForOwnerByDate(
            @Param("userId") Long userId,
            @Param("userType") UserType userType,
            @Param("date") LocalDate date
    );

    Optional<Reservation> findByIdAndUserId(Long reservationId, Long userId);

}
