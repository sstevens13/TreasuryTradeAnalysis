package trade;

/*
 * immediately exits position
 */
public class FlatTradeStrategy implements TradeStrategy {
	private PositionManager positionMngr = PositionManager.getInstance();
	
	/*
	 * Trades out of all positions on the first update.
	 */
	public boolean updateSpreadPrice(PriceData data) {
		
		int position = positionMngr.getPosition();
		
		// spread is in terms of 10year(zn) - 5year(zf)
		// only decreases position
		if (position > 0) { //get out of long position ==> sell spread 
			positionMngr.update(-1*position, data.znBid, data.zfAsk, data.time);
			return true;
		} else if (position < 0) { //get out of short position ==> buy spread
			positionMngr.update(-1*position, data.znAsk, data.zfBid, data.time);
			return true;
		} else {
			return false;
		}

	}
	

}
