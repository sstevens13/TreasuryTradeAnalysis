package mediator;

import java.util.Vector;

import pipesAndFilters.TradeData;

public class PriceQueue {
	private Vector<TradeData> vectorQueue = new Vector<TradeData>();
	
	public boolean addPriceMsg(TradeData tradeData) {
		return vectorQueue.add(tradeData);
	}
	
	//*************************************************************************
	// removes first element of queue and returns it, returns null if empty
	//*************************************************************************
	public TradeData popPriceMsg() {
		if (isEmpty() || vectorQueue == null) return null;
		return vectorQueue.remove(0);
	}
	
	//*************************************************************************
	// returns true if queue is empty
	//*************************************************************************
	public boolean isEmpty() {
		if (vectorQueue.size() > 0) {
			return false;
		}
		return true;
	}
	
	//*************************************************************************
	// returns first element without removing it, returns null if empty
	//*************************************************************************
	public TradeData peek() {
		if (isEmpty()) return null;
		return vectorQueue.elementAt(0);		
	}

}
