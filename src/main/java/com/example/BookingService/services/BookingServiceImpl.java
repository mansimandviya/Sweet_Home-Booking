package com.example.BookingService.services;

import com.example.BookingService.dao.BookingDAO;
import com.example.BookingService.dto.TransactionDTO;
import com.example.BookingService.model.BookingInfoEntity;
import com.example.BookingService.model.TransactionDetailsEntity;
import com.example.BookingService.util.BookingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class BookingServiceImpl implements BookingService{

    @Autowired
    private BookingDAO bookingDAO;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public BookingInfoEntity saveBooking(BookingInfoEntity booking){
        return bookingDAO.save(booking);
    }

    @Override
    public BookingInfoEntity getBookingBasedOnBookingId(int id){
        return bookingDAO.getById(id);
    }

    @Override
    public BookingInfoEntity upBookingInfoEntity(BookingInfoEntity bookingInfoEntity){
        BookingInfoEntity storedEntity = getBookingBasedOnBookingId(bookingInfoEntity.getBookingId());

        storedEntity.setTransactionId(bookingInfoEntity.getTransactionId());
        storedEntity.setNumOfRooms(bookingInfoEntity.getNumOfRooms());
        storedEntity.setAadharNumber(bookingInfoEntity.getAadharNumber());
        storedEntity.setToDate(bookingInfoEntity.getToDate());
        storedEntity.setBookedOn(bookingInfoEntity.getBookedOn());
        storedEntity.setFromDate(bookingInfoEntity.getFromDate());
        storedEntity.setToDate(bookingInfoEntity.getToDate());
        storedEntity.setRoomPrice(bookingInfoEntity.getRoomPrice());
        storedEntity.setRoomNumbers(bookingInfoEntity.getRoomNumbers());

        return bookingDAO.save(storedEntity);
    }

    @Override
    public int saveTransaction(TransactionDTO transactionDTO) {

        // Call API "/payment/transaction" Endpoint 1 of PaymentService
        // get Transaction Id from Payment Service API

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        TransactionDetailsEntity transactionDetailsEntity = BookingUtil.convertDTOtoEntity(transactionDTO);
        HttpEntity<TransactionDetailsEntity> httpEntity = new HttpEntity<>(transactionDetailsEntity, headers);
        String uri = "http://localhost:9191/payment/transaction";
        Integer val = restTemplate.postForObject(uri, httpEntity, Integer.class);

        return val;
    }

    @Override
    public BookingInfoEntity getBookingById(int id) {
        return bookingDAO.getById(id);
    }

}
