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
import java.util.List;

public class Tour {
    private int id;
    private int guideId;
    private String name;
    private String description;
    private String itinerary;
    private BigDecimal price;
    private int maxPeoplePerBooking;
    private Timestamp createdAt;
    private Timestamp updatedAt;
private int days; // số ngày của tour
    private String language; // ngôn ngữ hướng dẫn
    private Status status; // 'active' hoặc 'deactive'
    private List<Booking> bookings = new ArrayList<>();


    public Tour() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getGuideId() {
        return guideId;
    }

    public void setGuideId(int guideId) {
        this.guideId = guideId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getItinerary() {
        return itinerary;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getMaxPeoplePerBooking() {
        return maxPeoplePerBooking;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setItinerary(String itinerary) {
        this.itinerary = itinerary;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setMaxPeoplePerBooking(int maxPeoplePerBooking) {
        this.maxPeoplePerBooking = maxPeoplePerBooking;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getDays() {
        return days;
    }

    public String getLanguage() {
        return language;
    }

    public Status getStatus() {
        return status;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    
    }
