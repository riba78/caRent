package caRent;

public class Car {
	private int carID;
	private String make; //the year of production
	private String model;
	private String status; //available, rented, sold
	private double dailyPrice; //price for rent
	private double purchasePrice;
	
	//construcotr
	public Car(int carID, String make, String model, String status, double dailyPrice, double purchasePrice) {
		super();
		this.carID = carID;
		this.make = make;
		this.model = model;
		this.status = status;
		this.dailyPrice = dailyPrice;
		this.purchasePrice = purchasePrice;
	}

	public int getCarID() {
		return carID;
	}

	public void setCarID(int carID) {
		this.carID = carID;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getDailyPrice() {
		return dailyPrice;
	}

	public void setDailyPrice(double dailyPrice) {
		this.dailyPrice = dailyPrice;
	}

	public double getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}
	
	//method for status update
	public void updateStatus(String newStatus) {
		this.status = newStatus;
		System.out.println("the Car: " + carID + " status updated to: " + newStatus);
	}

	@Override
	public String toString() {
		return "Car [carID=" + carID + ", make=" + make + ", model=" + model + ", status=" + status + ", dailyPrice="
				+ dailyPrice + ", purchasePrice=" + purchasePrice + "]";
	}
}
