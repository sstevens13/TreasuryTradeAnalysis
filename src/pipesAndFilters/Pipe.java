package pipesAndFilters;

/*
 * To construct my pipe interface, I consulted this code: 
 * https://code.google.com/p/pipesandfilters/source/browse/trunk/pipesandfilters/src/pipes/and/filters/?r=20
 */
public interface Pipe {
	public TradeData pull();
	
	/*
	 * the pipes and filters setup is pull, but when I pull i want the previous
	 * filter to push at least one TradeData object to it's sink
	 */
	public void push(TradeData tradeData);

}
