package pipesAndFilters;

import java.util.concurrent.LinkedBlockingQueue;

public class PipeImpl implements Pipe {
	protected Filter sourceFilter;
	protected Filter sinkFilter;
	protected LinkedBlockingQueue<TradeData> tdQueue = new LinkedBlockingQueue<TradeData>();
	
	/*
	 * on setup the source and sink filters are linked  
	 */
	public PipeImpl(Filter sourceFilter, Filter sinkFilter) {
		this.sourceFilter = sourceFilter;
		this.sourceFilter.setSink(this); 
		this.sinkFilter = sinkFilter;
		this.sinkFilter.setSource(this);
	}
	
	public TradeData pull() {
		try {
			if(tdQueue.isEmpty()) {
				sourceFilter.pull();
				return tdQueue.take();
			} else {
				return tdQueue.take();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return new TradeDataNull();
	}
	
	/*
	 * the pipes and filters setup is pull, but when I pull
	 * i want the previous filter to process a set amount of data
	 */
	public void push(TradeData tradeData) {
		tdQueue.add(tradeData);
	}
	
}
