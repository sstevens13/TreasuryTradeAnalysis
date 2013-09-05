package mediator;

import pipesAndFilters.PipesAndFiltersFacade;
import pipesAndFilters.TradeData;
import trade.PricingEngine;

public interface Mediator {
	public boolean getPriceUpdate();
	public void setEOF();
	public void registerPricingEngine(PricingEngine pricingEngine);
	public void registerFilterFacade(PipesAndFiltersFacade filterFacade); 
	public void addTradeDataMsg(TradeData tradeData);
}
