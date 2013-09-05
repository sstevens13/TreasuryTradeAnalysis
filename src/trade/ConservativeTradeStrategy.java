package trade;

/*
 * only reduces position, does not add to position
 */
public class ConservativeTradeStrategy implements TradeStrategy {
	private PositionManager positionMngr = PositionManager.getInstance();
	
	
	/*
	 * Only tries to unwind (trade out of) positions when our pricing model allows
	 * it.
	 */
	public boolean updateSpreadPrice(PriceData data) {
		
		int position = positionMngr.getPosition();
		
		// spread is in terms of 10year(zn) - 5year(zf)
		// only decreases position
		if (data.spreadBid > data.spreadSMA) {
			if (position > 0) { //get out of long position ==> sell spread 
				positionMngr.update(-1*position, data.znBid, data.zfAsk, data.time);
				return true;
			}
		} else if (data.spreadAsk < data.spreadSMA) {
			if (position < 0) { //get out of short position ==> buy spread
				positionMngr.update(-1*position, data.znAsk, data.zfBid, data.time);
				return true;
			}
		}

		return false;
	}
	
}
