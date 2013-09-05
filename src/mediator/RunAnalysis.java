package mediator;

import java.io.IOException;

import pipesAndFilters.PipesAndFiltersFacade;
import trade.PricingEngine;

/*
 * for analyzing the U11 contracts (U11 = September 2011)
 * smaller files, faster to run, and can open raw files in an editor
 */
public class RunAnalysis {

	public static void main(String[] args) throws IOException, InterruptedException {

		PriceQueueMediator mediator = new PriceQueueMediator();

		PipesAndFiltersFacade sortedData = new PipesAndFiltersFacade("ZFU11_20110501_20110531.ts", 
				"ZFU11", "ZNU11_20110501_20110531.ts", "ZNU11", mediator);
		
		PricingEngine pricingEngine = new PricingEngine(mediator);
		
		boolean run = true;
		while (run) {
			run = mediator.getPriceUpdate();
		}
		
	}
}
