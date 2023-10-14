package com.example.BookingService.util;

import com.example.BookingService.dto.BookingDTO;
import com.example.BookingService.dto.TransactionDTO;
import com.example.BookingService.model.BookingInfoEntity;
import com.example.BookingService.model.TransactionDetailsEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Random;

public class BookingUtil {

    public static boolean validateModeOfPayment(TransactionDTO transactionDTO) {
        String paymentMode = transactionDTO.getPaymentMode();
        if("UPI".equalsIgnoreCase(paymentMode) || "CARD".equalsIgnoreCase(paymentMode)){
            return true;
        }
        return false;
    }

    public static BookingInfoEntity convertDTOtoEntity(BookingDTO bookingDTO){
        BookingInfoEntity bookingInfoEntity = new BookingInfoEntity();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        bookingInfoEntity.setFromDate(LocalDate.parse(bookingDTO.getFromDate(), formatter));
        bookingInfoEntity.setToDate(LocalDate.parse(bookingDTO.getToDate(),
                formatter));
        bookingInfoEntity.setAadharNumber(bookingDTO.getAadharNumber());
        bookingInfoEntity.setNumOfRooms(bookingDTO.getNumOfRooms());
        return bookingInfoEntity;
    }

    public static BookingDTO convertEntitytoDTO(BookingInfoEntity bookingInfoEntity){
        BookingDTO bookingDTO = new BookingDTO();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        bookingDTO.setFromDate(bookingInfoEntity.getFromDate().toString());
        bookingDTO.setToDate(bookingInfoEntity.getToDate().toString());
        bookingDTO.setAadharNumber(bookingInfoEntity.getAadharNumber());
        bookingDTO.setNumOfRooms(bookingInfoEntity.getNumOfRooms());
        return bookingDTO;
    }

    public static TransactionDetailsEntity convertDTOtoEntity(TransactionDTO transactionDTO){
        TransactionDetailsEntity transactionDetailsEntity = new TransactionDetailsEntity();
        transactionDetailsEntity.setPaymentMode(transactionDTO.getPaymentMode());
        transactionDetailsEntity.setBookingId(transactionDTO.getBookingId());
        transactionDetailsEntity.setCardNumber(transactionDTO.getCardNumber());
        transactionDetailsEntity.setUpiId(transactionDTO.getUpiId());
        return transactionDetailsEntity;
    }

    public static ArrayList<String> getRoomNumber(int count){
        Random rand = new Random();
        int upperBound = 100;
        ArrayList<String>numberList = new ArrayList<String>();

        for (int i=0; i<count; i++){
            numberList.add(String.valueOf(rand.nextInt(upperBound)));
        }
        return numberList;
    }

    public static int calculateRoomPrice(int numOfRooms, int count){
        return 1000*numOfRooms*(count);
    }
}
