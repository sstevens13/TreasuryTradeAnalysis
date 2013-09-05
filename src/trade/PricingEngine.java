package trade;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import mediator.Mediator;

import pipesAndFilters.TradeData;

public class PricingEngine {
	private boolean bEndTradeDay = true;
	private double spreadAvg;
	//Simple Moving Averages, could try CumulativeMA and EMA
	private SimpleMovingAverage spreadSMA;
	private PriceData currentData;
	private FileWriter writerSpread;
	private TradeStrategy aggressiveStrategy;
	private TradeStrategy conservativeStrategy = new ConservativeTradeStrategy();
	private TradeStrategy flatStrategy = new FlatTradeStrategy();
	private int currentDay;
	
	public PricingEngine(Mediator mediator) {
		// 2 periods per minute ==> 720 = 6 hour SMA
		spreadSMA = new SimpleMovingAverage(720); 
		currentData = new PriceData();
		try {
			writerSpread = new FileWriter("spreads.dat");
		} catch (IOException e) {
			e.printStackTrace();
		}
		mediator.registerPricingEngine(this);
		setAggressiveStrategy();
	}
	
	/*
	 * this is how the Mediator updates the pricing engine
	 */
	public void update(TradeData tradeData) {
		currentDay = tradeData.getDay();
		
		currentData.time = tradeData.getTime();
		
		if (currentData.time > 1300 && currentData.time < 1800) {
			endDay();
		} else if (tradeData.getContract().contains("ZN")) {
			if (tradeData.getType().equals("B")) {
				currentData.znBid = tradeData.getPrice();
			} else {
				currentData.znAsk = tradeData.getPrice();
			}
		} else if (tradeData.getContract().contains("ZF")) {
			if (tradeData.getType().equals("B")) {
				currentData.zfBid = tradeData.getPrice();
			} else {
				currentData.zfAsk = tradeData.getPrice();
			}
		}
	}
	
	/*
	 * this runs the strategies at the end of every update cycle
	 */
	public void processData() {
		calculateSpread();
		if (currentData.time < 45) {
			beginDay();
		} else if (currentData.time > 45 && currentData.time <= 1015) {
			if (currentData.time <= 700){
				aggressiveStrategy.updateSpreadPrice(currentData);
			} else if (currentData.time > 700 && currentData.time <= 945) {
				conservativeStrategy.updateSpreadPrice(currentData);
			} else if (currentData.time > 945 && currentData.time <= 1015) {
				flatStrategy.updateSpreadPrice(currentData);
			}
		}
		try {
			writerSpread.write(currentDay +" "+ currentData.time +" "+ spreadSMA.getAvg() +" "+ spreadAvg +"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * calculates spread and updates SMA
	 * Spread is in terms of ten year: SpreadBid B = Ten Bid - Five Ask // A = Ten Ask - Five Bid 
	 */
	public void calculateSpread() {
		if (currentData.znBid != 0 && currentData.znAsk != 0 && currentData.zfBid != 0 && currentData.zfAsk != 0) {
			currentData.spreadBid = currentData.znBid - currentData.zfAsk;
			currentData.spreadAsk = currentData.znAsk - currentData.zfBid;
			spreadAvg = (currentData.spreadAsk + currentData.spreadBid)/2;
			spreadSMA.newNum(spreadAvg);
			currentData.spreadSMA = spreadSMA.getAvg();
		}
	}
	
	/*
	 *  used to monitor when trading day is over
	 */
	private void endDay() {
		if (!bEndTradeDay) {
			bEndTradeDay = true;
			currentData.znAsk = 0;
			currentData.znBid = 0;
			currentData.zfAsk = 0;
			currentData.zfBid = 0;
			spreadSMA.reset();
			PositionManager.getInstance().logTradeDetails();
		}
	}
	
	/*
	 * used to monitor beginning of trade day and make some daily 
	 * variable changes 
	 */
	private void beginDay() {
		if (bEndTradeDay) {
			bEndTradeDay = false;
			PositionManager.getInstance().newDay(currentDay);
		}	
	}
	
	private void setAggressiveStrategy() {
		System.out.println("You can trade the Basic Strategy(1) or the Martingale Strategy (2). \n" +
				"Martingale doubles it's trade size as it's position increases. \n" +
				"Basic Strategy always uses the same size trade, but offers you the option to \n" +
				"trade aggressively or conservatively");
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));		
		try {
			String key = input.readLine();
			switch (key) {
			case "2":
				aggressiveStrategy = new MartingaleTradeStrategy();
				PositionManager.getInstance().recordStrategy(2, 0);
				System.out.println("Martingale Strategy Picked");
				break;
			default:
				System.out.println("Basic Strategy Picked, enter value between 0 and 4");
				System.out.println("Most aggressive is 0, most conservative is 4. Default is aggressive (0)");
				key = input.readLine();
				switch (key) {
				case "0":
					aggressiveStrategy = new BasicTradeStrategy(0);
					PositionManager.getInstance().recordStrategy(0, 0);
					break;
				case "1":
					aggressiveStrategy = new BasicTradeStrategy(1);
					PositionManager.getInstance().recordStrategy(0, 1);
					break;
				case "2":
					aggressiveStrategy = new BasicTradeStrategy(2);
					PositionManager.getInstance().recordStrategy(0, 2);
					break;
				case "3":
					aggressiveStrategy = new BasicTradeStrategy(3);
					PositionManager.getInstance().recordStrategy(0, 3);
					break;
				case "4":
					aggressiveStrategy = new BasicTradeStrategy(4);
					PositionManager.getInstance().recordStrategy(0, 4);
					break;
				default:
					aggressiveStrategy = new BasicTradeStrategy(0);
					PositionManager.getInstance().recordStrategy(0, 0);
					break;
				}

				break;
			}
			

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
