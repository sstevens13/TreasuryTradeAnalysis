package trade;
/*
 * This is the Martingale Strategy which doubles size on each successive
 * trade. 
 * the higher the number, the less risky/aggressive the strategy
 */
public class MartingaleTradeStrategy implements TradeStrategy {
	// .03125 = 1/32nd which is 2 ticks in ZN and 4 ticks in ZF
	// current spreadAvg has to be at least 4 ticks away from the 4 hour SMA
	private double howAggressive = .03125; 
	private PositionManager positionMngr = PositionManager.getInstance();
	
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
				positionMngr.update(-1*position,  data.znBid, data.zfAsk, data.time);
				return true;
			} else if (data.spreadBid - data.spreadSMA > (-1 * position * howAggressive)) { 
				// increase short position ==> sell spread
				if (position == 0) {
					positionMngr.update(-1,  data.znBid, data.zfAsk, data.time);
				} else {
					positionMngr.update(2*position,  data.znBid, data.zfAsk, data.time);
				}
				return true;
			}
		} else if (data.spreadAsk < data.spreadSMA) {
			if (position < 0) { //get out of short position ==> buy spread
				positionMngr.update(-1*position,  data.znBid, data.zfAsk, data.time);
				return true;
			} else if (data.spreadSMA - data.spreadAsk > (position * howAggressive)) {
				// increase long position ==> buy spread
				if (position == 0) {
					positionMngr.update(1,  data.znBid, data.zfAsk, data.time);
				} else {
					positionMngr.update(2*position, data.znBid, data.zfAsk, data.time);
				}
				return true;
			}
		}

		return false;
	}
	
}
