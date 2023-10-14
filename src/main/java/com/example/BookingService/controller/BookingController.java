package com.example.BookingService.controller;

import com.example.BookingService.dto.BookingDTO;
import com.example.BookingService.dto.TransactionDTO;
import com.example.BookingService.model.BookingInfoEntity;
import com.example.BookingService.services.BookingService;
import com.example.BookingService.util.BookingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value="/hotel")
public class BookingController {

    @Autowired
    BookingService bookingService;

    @GetMapping(value = "/booking/{id}")
    public ResponseEntity getBokingById(@PathVariable int id){
        BookingInfoEntity dbBookingInfo = bookingService.getBookingById(id);
        BookingDTO dbBookingDTO = BookingUtil.convertEntitytoDTO(dbBookingInfo);
        return new ResponseEntity(dbBookingDTO, HttpStatus.OK);
    }

    @PostMapping(value = "/booking", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity bookRoom(@RequestBody BookingDTO bookingDTO) {

        BookingInfoEntity inputBookingInfoEntity = BookingUtil.convertDTOtoEntity(bookingDTO);
        ArrayList<String> getListOfRoom = BookingUtil.getRoomNumber(inputBookingInfoEntity.getNumOfRooms());
        int count_no_of_days = (int) ChronoUnit.DAYS.between(inputBookingInfoEntity.getFromDate(), inputBookingInfoEntity.getToDate());
        int totalRoomPrice = BookingUtil.calculateRoomPrice(inputBookingInfoEntity.getNumOfRooms(), count_no_of_days);
        inputBookingInfoEntity.setRoomNumbers(getListOfRoom);
        inputBookingInfoEntity.setRoomPrice(totalRoomPrice);
        inputBookingInfoEntity.setBookedOn(LocalDateTime.now());

        BookingInfoEntity savedBookingInfo = bookingService.saveBooking(inputBookingInfoEntity);
        return new ResponseEntity(savedBookingInfo, HttpStatus.CREATED);
    }

    @PostMapping(value = "/booking/{id}/transaction", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity makePayment(@RequestBody TransactionDTO transactionDTO, @PathVariable(name = "id") int id) {
        if(!BookingUtil.validateModeOfPayment(transactionDTO)){
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("message", "Invalid Mode of Payment");
            responseMap.put("code",400);
            return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
        }

        // Save Transaction id to BookingEntityInfo based on the bookingId
        // Get Booking First based on Id
        BookingInfoEntity dbBookingInfoEntity = bookingService.getBookingBasedOnBookingId(id);

        if(dbBookingInfoEntity == null){
            Map<String, Object> responseMap = new HashMap<>();
            responseMap.put("message", "Invalid Booking Id");
            responseMap.put("code",400);
            return new ResponseEntity(responseMap, HttpStatus.BAD_REQUEST);
        }

        // Call API "/payment/transaction" Endpoint 1 of PaymentService
        // get Transaction Id from Payment Service API
        int transactionId = bookingService.saveTransaction(transactionDTO);

        // Save/Update Booking after that
        dbBookingInfoEntity.setTransactionId(transactionId);
        BookingInfoEntity savedBookingInfo = bookingService.upBookingInfoEntity(dbBookingInfoEntity);

        // Send Response
        return new ResponseEntity(savedBookingInfo, HttpStatus.CREATED);
    }

}
