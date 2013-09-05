package mediator;

import java.io.IOException;

import pipesAndFilters.PipesAndFiltersFacade;
import trade.PricingEngine;

/*
 * for analyzing the M11 contracts (M11 = June 2011)
 * larger files, slower to run, raw files too large to open in an editor
 */
public class RunAnalysis2 {

	public static void main(String[] args) throws IOException, InterruptedException {

		PriceQueueMediator mediator = new PriceQueueMediator();

		PipesAndFiltersFacade sortedData = new PipesAndFiltersFacade("ZFM11_20110501_20110531.ts", 
				"ZFM11", "ZNM11_20110501_20110531.ts", "ZNM11", mediator);
		
		PricingEngine pricingEngine = new PricingEngine(mediator);
 		
		boolean run = true;
		while (run) {
			run = mediator.getPriceUpdate();
		}
		
	}
}
