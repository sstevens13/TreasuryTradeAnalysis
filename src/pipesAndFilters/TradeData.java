package pipesAndFilters;

/*
 * this class is used to create TradeData objects which encapsulate all pertinent 
 * data contained in a single line of data pulled from the input files
 */
public class TradeData {
	private String contract;
	private String date;
	// of the form "1425" where first two chars = hr, last two chars = min
	private String strPrice;
	private String type; //bid or ask
	private Integer quantity = null; //size of bid or ask
	private Integer day = null;
	private Double price = null;
	private Double time = null;
	
	public TradeData(String contract, String date, Double time, String strPrice, String type, Integer quantity) {
		this.contract = contract; 
		this.date = date;
		this.time = time;
		this.strPrice = strPrice;
		this.type = type;
		this.quantity = quantity;
	}
	
	public String toString() {
		if (price != null) {
			return contract +" "+ date +" "+ time +" "+ type +" "+ quantity + " " + price;
		} else {
			return contract +" "+ date +" "+ time +" "+ strPrice +" "+ type +" "+ quantity;
		}
	}

	public String getContract() {
		return contract;
	}

	public String getDate() {
		return date;
	}

	public String getStrPrice() {
		return strPrice;
	}
	
	public String getType() {
		return type;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}

	public Double getTime() {
		return time;
	}

	public void addSubMinute() {
		time += .5;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
		

}
