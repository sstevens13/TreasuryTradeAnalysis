package pipesAndFilters;

import java.io.IOException;

import mediator.PriceQueueMediator;

/*
 * was used for testing pipes and filters 
 */
public class TestPipesAndFilters {
	
	public static void main(String[] args) throws IOException {
		
		RawDataFilter zfu11Raw = new RawDataFilter("ZFU11_20110501_20110531.ts");
		FormatDataFilter zfu11Formatted = new FormatDataFilter("ZFU11");
		Pipe zfu11RtoF = new PipeImpl(zfu11Raw, zfu11Formatted);  
		
		RawDataFilter znu11Raw = new RawDataFilter("ZNU11_20110501_20110531.ts");
		FormatDataFilter znu11Formatted = new FormatDataFilter("ZNU11");
		Pipe znu11RtoF = new PipeImpl(znu11Raw, znu11Formatted);  
		
		PriceQueueMediator mediator = new PriceQueueMediator();

		TimeSorterFilter timeSorter = new TimeSorterFilter(mediator);
		Pipe zfu11FtoS = new PipeImpl(zfu11Formatted, timeSorter);
		Pipe znu11FtoS = new PipeImpl(znu11Formatted, timeSorter);
		timeSorter.setSource(zfu11FtoS);
		timeSorter.setSource(znu11FtoS);
			
		for (int i = 0; i < 200000; i++) {
			timeSorter.pull();
		}
		
	}
	
	

}
