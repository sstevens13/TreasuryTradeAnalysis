package pipesAndFilters;

import mediator.Mediator;

public class PipesAndFiltersFacade {
	private RawDataFilter fiveRaw;
	private FormatDataFilter fiveFormatted;
	private Pipe fiveRtoF;
	private RawDataFilter tenRaw;
	private FormatDataFilter tenFormatted;
	private Pipe tenRtoF;
	private TimeSorterFilter timeSorter; 
	private Pipe zfu11FtoS;
	private Pipe znu11FtoS;
	
	/*
	 * The Pipes and Filters setup is complicated, so I used the facade pattern to
	 * hide the setup behind a simple interface.
	 * 
	 * The interface provides one method for pulling from the pipe.
	 */
	public PipesAndFiltersFacade(String fiveYearFile, String fiveYearContractName, 
			String tenYearFile, String tenYearContractName, Mediator mediator) {
	
		// initialize 5 year pipes/filters
		fiveRaw = new RawDataFilter(fiveYearFile);
		fiveFormatted = new FormatDataFilter(fiveYearContractName);
		fiveRtoF = new PipeImpl(fiveRaw, fiveFormatted);  

		// initialize 10 year pipes/filters
		tenRaw = new RawDataFilter(tenYearFile);
		tenFormatted = new FormatDataFilter(tenYearContractName);
		tenRtoF = new PipeImpl(tenRaw, tenFormatted);  
		
		// link the final two pipes to the filter that sorts and organizes
		// prices by time
		timeSorter = new TimeSorterFilter(mediator);
		zfu11FtoS = new PipeImpl(fiveFormatted, timeSorter);
		znu11FtoS = new PipeImpl(tenFormatted, timeSorter);
		
		mediator.registerFilterFacade(this);
	}
	
	public void pull() {
		timeSorter.pull();
	}
	
	
	
}
