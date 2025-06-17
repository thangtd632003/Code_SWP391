/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

/**
 *
 * @author thang
 */
import java.math.BigDecimal;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Booking {
    private int id;
    private int travelerId;
    private int tourId;
    private int numPeople;
    private String contactInfo;
    private BookingStatus status;
    private Timestamp createdAt;
    private Timestamp updatedAt;
     private String tourName;
    private BigDecimal tourPrice;
    private int tourDays;
    private String tourLanguage;
    private String tourItinerary;

public String getTourItinerary() {
    return tourItinerary;
}

public void setTourItinerary(String tourItinerary) {
    this.tourItinerary = tourItinerary;
}
 public String getTourName() {
        return tourName;
    }

    public void setTourName(String tourName) {
        this.tourName = tourName;
    }

    public BigDecimal getTourPrice() {
        return tourPrice;
    }

    public void setTourPrice(BigDecimal tourPrice) {
        this.tourPrice = tourPrice;
    }

    public int getTourDays() {
        return tourDays;
    }

    public void setTourDays(int tourDays) {
        this.tourDays = tourDays;
    }

    public String getTourLanguage() {
        return tourLanguage;
    }

    public void setTourLanguage(String tourLanguage) {
        this.tourLanguage = tourLanguage;
    }
private Date departureDate;
    private List<Review> review = new ArrayList<>();

    public Booking() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }




    public int getTravelerId() {
        return travelerId;
    }


    public void setTravelerId(int travelerId) {
        this.travelerId = travelerId;
    }

    public int getTourId() {
        return tourId;
    }

    public void setTourId(int tourId) {
        this.tourId = tourId;
    }

    public int getNumPeople() {
        return numPeople;
    }

    public void setNumPeople(int numPeople) {
        this.numPeople = numPeople;
    }

    public List<Review> getReview() { return review; }
    public void setReview(List<Review> review) { this.review = review; }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }



    public void setDepartureDate(Date departureDate) {
        this.departureDate = departureDate;
    }

    public Date getDepartureDate() {
        return departureDate;
    }
}