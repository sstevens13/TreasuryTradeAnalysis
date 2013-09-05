package mediator;

import pipesAndFilters.PipesAndFiltersFacade;
import pipesAndFilters.TradeData;
import pipesAndFilters.TradeDataNull;
import trade.PositionManager;
import trade.PricingEngine;

public class PriceQueueMediator implements Mediator {
	private PricingEngine pricingEngine;
	private PriceQueue msgQueue = new PriceQueue();
	private boolean bEOF = false;
	private PipesAndFiltersFacade filterFacade;
	
	private TradeData currentTradeData = null;
	private int currentDay = -1;
	private double currentTime = -1;
	
	/*
	 *  pushes all prices occurring at the same day & time
	 *  returns false when there is no more data
	 */
	public boolean getPriceUpdate() {
		if (currentDay == -1) {
			currentTradeData = nextTradeData(); //initialization
		}
		currentDay = currentTradeData.getDay();
		currentTime = currentTradeData.getTime();
		// The TradeData that didn't match Day & Time the last time getPriceUpdate() was
		// called will be sent plus any other TradeData that matches the Day & Time
		while ((currentTradeData.getTime() == currentTime) && (currentTradeData.getDay() == currentDay)) {
			pricingEngine.update(currentTradeData);
			currentTradeData = nextTradeData();
			if (currentTradeData instanceof TradeDataNull) {
				PositionManager.getInstance().closeFile();
				return false;
			}
		}
		pricingEngine.processData();
		return true;
	}

	public void setEOF() {
		bEOF = true;
	}

	public void registerPricingEngine(PricingEngine pricingEngine) {
		this.pricingEngine = pricingEngine;
	}

	public void registerFilterFacade(PipesAndFiltersFacade filterFacade) {
		this.filterFacade = filterFacade;
	}

	public void addTradeDataMsg(TradeData tradeData) {
		msgQueue.addPriceMsg(tradeData);
	}
	
	private TradeData nextTradeData() {
		if(msgQueue.isEmpty() && !bEOF) {
			for (int i = 0; i < 1000; i++) {
				filterFacade.pull();
			}
		} 
		if (bEOF) {
			return null;
		}
		return msgQueue.popPriceMsg();  
	}
	
}
