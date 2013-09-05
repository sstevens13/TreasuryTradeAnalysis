package pipesAndFilters;

import java.io.FileWriter;
import java.io.IOException;

/*
 * formats data so that the pricing engine only reads valid prices
 */
public class FormatDataFilter extends Filter {
	private FileWriter writer;
	private TradeData firstBid = null;
	private TradeData firstAsk = null;
	private TradeData lastBid = null;
	private TradeData lastAsk = null;
	private int currentDay;
	private double currentTime;
	// initialized for first comparison
	private int lastDay = -1;
	private double lastTime = -1;
	private Boolean bPush, bEOF = false;

	public FormatDataFilter(String dataName) {
		try {
			writer = new FileWriter(dataName+"Formatted.dat");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Pulls all data from RawDataFilter occurring in the same minute. 
	 * Removes all data that does not occur at the beginning or end of 
	 * the minute time period. Then pushes that data to TimeSorter.
	 */
	public void pull() {
		TradeData currentData;
		try {
			bPush = false;
			while (!bPush && !bEOF) {
				currentData = sourcePipe.pull();
				if (currentData instanceof TradeDataNull) {
					sinkPipe.push(currentData);
					writer.close();
					bPush = true;
					bEOF = true;
				} else {
					currentDay = currentData.getDay();
					currentTime = currentData.getTime();
					if (currentTime != lastTime || currentDay != lastDay) {
						pushBidsAsks();
					}
					if (currentData.getType().equals("S")) {
						// RESET VALUES: S = end of trading day
						firstAsk = null;
						firstBid = null;
						lastBid = null;
						lastAsk = null;
					} else if (currentData.getType().equals("A")) {
						if (firstAsk == null) {
							firstAsk = currentData;
							firstAsk.setPrice(normalizePrice(firstAsk.getStrPrice()));
						} else { 
							// keeps throwing away prices that don't occur at (beginning or) end of
							// minute by replacing with most recent last Bid or Ask
							lastAsk = currentData; 
						}
					} else if (currentData.getType().equals("B")) {
						if (firstBid == null) {
							firstBid = currentData;
							firstBid.setPrice(normalizePrice(firstBid.getStrPrice()));
						} else {
							// keeps throwing away prices that don't occur at (beginning or) end of
							// minute by replacing with most recent last Bid or Ask
							lastBid = currentData; 
						} 
					}
					lastDay = currentDay;
					lastTime = currentTime;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * pushes all the formatted/kept data from a minute time period
	 */
	private void pushBidsAsks() throws IOException {
		if (firstBid != null) {
			sinkPipe.push(firstBid);
			writer.write(firstBid +"\n");
			firstBid = null;
			bPush = true;
		}
		if (firstAsk != null) {
			sinkPipe.push(firstAsk);
			writer.write(firstAsk +"\n");
			firstAsk = null;
			bPush = true;
		}
		if (lastBid != null) {
			// only label LastBid before pushing it since most  
			// lastBids will be discarded
			lastBid.addSubMinute();
			lastBid.setPrice(normalizePrice(lastBid.getStrPrice()));
			sinkPipe.push(lastBid);
			writer.write(lastBid +"\n");
			lastBid = null;
		}
		if (lastAsk != null) {
			// only label LastAsk before pushing it since most  
			// lastAsks will be discarded
			lastAsk.addSubMinute();
			lastAsk.setPrice(normalizePrice(lastAsk.getStrPrice()));
			sinkPipe.push(lastAsk);
			writer.write(lastAsk +"\n");
			lastAsk = null;
		}
	}
	
	/*
	 * converts price string to an integer
	 * 
	 * 119162 represents 119 + 16.25/32
	 */
	private double normalizePrice(String strPrice) {
		double price = Integer.parseInt(strPrice.substring(0, 3));
		double fraction = (double) Integer.parseInt(strPrice.substring(3, 5));
		double subFraction = 0.0;
		String strFraction = strPrice.substring(5);
		if (strFraction.equals("2")) {
			subFraction = .25;
		} else if (strFraction.equals("5")) {
			subFraction = .5;
		} else if (strFraction.equals("7")) {
			subFraction = .75; 
		}
		price = price + (fraction + subFraction)/32.0;
		return price;
	}
		
}
