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

    private List<TourSchedule> schedules = new ArrayList<>();

    public Tour() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getGuideId() { return guideId; }
    public void setGuideId(int guideId) { this.guideId = guideId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getItinerary() { return itinerary; }
    public void setItinerary(String itinerary) { this.itinerary = itinerary; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public int getMaxPeoplePerBooking() { return maxPeoplePerBooking; }
    public void setMaxPeoplePerBooking(int maxPeoplePerBooking) { this.maxPeoplePerBooking = maxPeoplePerBooking; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public List<TourSchedule> getSchedules() { return schedules; }
    public void setSchedules(List<TourSchedule> schedules) { this.schedules = schedules; }
}