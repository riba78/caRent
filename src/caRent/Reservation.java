package caRent;

import java.util.Date;

public class Reservation {
    private int reservationID;
    private int userID;
    private int carID;
    private Date startDate;
    private Date endDate;
    private double totalCost;
    private String reservationType; // "rental" or "purchase"
    
    /**
     * Constructor for Reservation.
     * 
     * @param reservationID The reservation ID.
     * @param userID The ID of the user who made the reservation.
     * @param carID The ID of the car being reserved.
     * @param startDate The start date of the reservation.
     * @param endDate The end date of the reservation.
     * @param totalCost The total cost of the reservation.
     * @param reservationType The type ("rental" or "purchase").
     */
    public Reservation(int reservationID, int userID, int carID, Date startDate, Date endDate, double totalCost, String reservationType) {
        this.reservationID = reservationID;
        this.userID = userID;
        this.carID = carID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalCost = totalCost;
        this.reservationType = reservationType;
    }
    
    // Getters and setters
    public int getReservationID() {
        return reservationID;
    }
    public void setReservationID(int reservationID) {
        this.reservationID = reservationID;
    }
    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {
        this.userID = userID;
    }
    public int getCarID() {
        return carID;
    }
    public void setCarID(int carID) {
        this.carID = carID;
    }
    public Date getStartDate() {
        return startDate;
    }
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    public double getTotalCost() {
        return totalCost;
    }
    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }
    public String getReservationType() {
        return reservationType;
    }
    public void setReservationType(String reservationType) {
        this.reservationType = reservationType;
    }
    
    /**
     * Calculates the total cost.
     * For rental: cost = (number of days) * dailyPrice.
     * For purchase: cost = purchasePrice.
     * 
     * @param dailyPrice The cost per day (for rental).
     * @param purchasePrice The fixed purchase price (for purchase).
     * @return The calculated total cost.
     */
    public double calculateCost(double dailyPrice, double purchasePrice) {
        if ("rental".equalsIgnoreCase(reservationType)) {
            // If the user mistakenly picks an end date before the start date, treat as invalid
            if (endDate.before(startDate)) {
                throw new IllegalArgumentException("End date is before start date. Invalid reservation dates.");
            }
            
            // Calculate difference in days (inclusive)
            long diffInMillis = endDate.getTime() - startDate.getTime();
            long diffDays = (diffInMillis / (1000 * 60 * 60 * 24)) + 1;
            return diffDays * dailyPrice;
        } else if ("purchase".equalsIgnoreCase(reservationType)) {
            return purchasePrice;
        }
        return totalCost;
    }
    
    public void confirmReservation() {
        System.out.println("Reservation " + reservationID + " (" + reservationType + ") has been confirmed");
    }
    
    public void sendReminder() {
        System.out.println("Reminder: Your " + reservationType + " reservation " + reservationID + " is scheduled soon");
    }
    
    @Override
    public String toString() {
        return "Reservation [reservationID=" + reservationID + ", userID=" + userID + ", carID=" + carID +
               ", startDate=" + startDate + ", endDate=" + endDate + ", totalCost=" + totalCost +
               ", reservationType=" + reservationType + "]";
    }
}
