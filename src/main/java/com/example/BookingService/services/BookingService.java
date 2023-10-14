package com.example.BookingService.services;

import com.example.BookingService.dto.TransactionDTO;
import com.example.BookingService.model.BookingInfoEntity;

import java.util.ArrayList;

public interface BookingService{

    public BookingInfoEntity saveBooking(BookingInfoEntity booking);

    public BookingInfoEntity getBookingBasedOnBookingId(int id);

    public BookingInfoEntity upBookingInfoEntity(BookingInfoEntity bookingInfoEntity);

    int saveTransaction(TransactionDTO transactionDTO);

    public BookingInfoEntity getBookingById(int id);
}
