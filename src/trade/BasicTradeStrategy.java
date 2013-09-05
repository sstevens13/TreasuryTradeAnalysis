package trade;

/*
 * main trading strategy. buys and sells depending on 
 * whether the spreadBid or spreadAsk is crossing the spreadSMA
 */
public class BasicTradeStrategy implements TradeStrategy {
	// .00078125 = 1/128th which is 1/2 tick in ZN and 1 tick in ZF
	// current spreadAvg has to be at least 4 ticks away from the 4 hour SMA
	private double howAggressive;
	private double minimumTickSize = .0078125;
	private PositionManager positionMngr = PositionManager.getInstance();

	/*
	 * This is the main strategy for the trading day.
	 * the higher the number, the less risky/less aggressive the strategy
	 */
	public BasicTradeStrategy(int riskiness) {
		howAggressive = minimumTickSize*(riskiness + 2);
	}
	
	/*
	 * Takes a range of data inputs and then trades when the spread price 
	 * deviates from the 3 hour SMA, by an amount determined by variables
	 * howAggressive and the currentPosition size which is obtained from
	 * the singleton, PositionManager
	 */
	public boolean updateSpreadPrice(PriceData data) {
		
		int position = positionMngr.getPosition();
		
		// spread is in terms of 10year(zn) - 5year(zf)
		if (data.spreadBid > data.spreadSMA) {
			if (position > 0) { //get out of long position ==> sell spread 
				positionMngr.update(-1*position, data.znBid, data.zfAsk, data.time);
				return true;
			} else if (data.spreadBid - data.spreadSMA > (-1 * position * howAggressive)) { 
				// increase short position ==> sell spread
				positionMngr.update(-1, data.znBid, data.zfAsk, data.time);
				return true;
			}
		} else if (data.spreadAsk < data.spreadSMA) {
			if (position < 0) { //get out of short position ==> buy spread
				positionMngr.update(-1*position, data.znAsk, data.zfBid, data.time);
				return true;
			} else if (data.spreadSMA - data.spreadAsk > (position * howAggressive)) {
				// increase long position ==> buy spread
				positionMngr.update(1, data.znAsk, data.zfBid, data.time);
				return true;
			}
		}

		return false;
	}
	
}
