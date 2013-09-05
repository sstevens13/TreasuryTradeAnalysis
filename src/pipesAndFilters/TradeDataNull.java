package pipesAndFilters;

public class TradeDataNull extends TradeData {

	/*
	 * hides the TradeData constructor 
	 */
	private TradeDataNull(String contract, String date, Double time, String strPrice,
			String type, Integer quantity) {
		super(contract, date, time, strPrice, type, quantity);
	}

	/*
	 * used to communicate End Of File to downstream filters
	 */
	public TradeDataNull() {
		super("null", "null", null, "null", "null", null);
	}
	
}
