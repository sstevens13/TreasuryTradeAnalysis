package trade;

import java.io.FileWriter;
import java.io.IOException;

public class PositionManager {
	private static PositionManager instance = null;
	private static FileWriter recordTrades;
	private static int position = 0;
	private static double dailyPNL = 0; // profit and loss
	private static int monthlyPNL = 0;
	private static int currentDay = 0;
	
	//constructor is private: no instances can be created with the new keyword
	private PositionManager() {}

	/*
	 * returns the only existing instance of PositionManager if it exists
	 * else initialize a single instance and return it
	 */
	public static PositionManager getInstance() {
		if (instance == null) {
			synchronized (PositionManager.class) {
				if (instance == null) {
					instance = new PositionManager();
					try {
						recordTrades = new FileWriter("trades.dat");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} 
		return instance;
	}
	
	/*
	 * Updates position as well as records details of the trade. 
	 */
	public void update(int tradeQty, double znPrice, double zfPrice, double time) {
		position = position + tradeQty;
		double deltaPNL = tradeQty * (zfPrice - znPrice);  //TODO - debugging
		dailyPNL = dailyPNL + tradeQty * (zfPrice - znPrice);
		
		try {
			if (tradeQty > 0) {
				recordTrades.write("Time:      " + time + "\n");
				recordTrades.write("DeltaPNL:  " + deltaPNL + "\n");
				recordTrades.write("Position:  " + position + "\n");
				recordTrades.write("Bought ZN: " + tradeQty * znPrice + "\n");
				recordTrades.write("Sold ZF:   " + -1 * tradeQty * zfPrice + "\n\n");
			} else {
				recordTrades.write("Time:      " + time + "\n");
				recordTrades.write("DeltaPNL:  " + deltaPNL + "\n");
				recordTrades.write("Position:  " + position + "\n");
				recordTrades.write("Sold ZN:   " + tradeQty * znPrice + "\n");
				recordTrades.write("Bought ZF: " + -1 * tradeQty * zfPrice + "\n\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * on shutdown, close (and flush) file.
	 */
	public void closeFile() {
		try {
			recordTrades.write("\n\n_________________________________________________________\n");
			recordTrades.write("Final PNL: " + monthlyPNL + "\n");
			recordTrades.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * records trade details
	 */
	public void logTradeDetails() {
		if (currentDay != 0) { // if 0, no data yet
			int daysProfit = (int) (dailyPNL * 1000);
			dailyPNL = 0;
			monthlyPNL = monthlyPNL + daysProfit;

			System.out.println("The profit for day "+ currentDay +" was: " + daysProfit);
			System.out.println("The profit for the month up to and including today is: " + monthlyPNL + "\n");
			try {
				recordTrades.write("The profit for day "+ currentDay +" was: " + daysProfit + "\n");
				recordTrades.write("The profit for the month up to and including today is: " + monthlyPNL + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * Mediator uses this method to tell position manager to log a new day
	 */
	public void newDay(int currentDay) {
		PositionManager.currentDay = currentDay;
		try {
			recordTrades.write("\n\n_________________________________________________________\n");
			recordTrades.write("Day of month: " + currentDay + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * returns current position
	 */
	public int getPosition() {
		return position;
	}
	
	public void recordStrategy(int strategy, int riskiness) {
		try {
			if (strategy == 2) {
				recordTrades.write("Using Martingale Strategy");
			} else {
				recordTrades.write("Using Basic Strategy with Riskiness value of " + riskiness);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
