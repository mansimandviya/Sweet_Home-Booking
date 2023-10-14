package com.example.BookingService.dao;

import com.example.BookingService.model.BookingInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingDAO extends JpaRepository<BookingInfoEntity, Integer> {
}
