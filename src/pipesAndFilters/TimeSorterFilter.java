package pipesAndFilters;

import java.io.FileWriter;
import java.io.IOException;

import mediator.Mediator;

public class TimeSorterFilter extends Filter {
	private FileWriter writer;
	private Mediator priceQueueMediator;
	private Pipe pipeFirst = null;
	private Pipe pipeSecond = null;
	private TradeData firstData = null;
	private TradeData secondData = null;

	private int firstDay;
	private double firstTime;
	private int secondDay;
	private double secondTime;
	
	private boolean bEOF = false;
	
	public TimeSorterFilter(Mediator mediator) {
		try {
			writer = new FileWriter("Sorted.dat");
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.priceQueueMediator = mediator;
	}

	public void pull() {
		try {
			if(!bEOF) {
				if (firstData == null) {
					firstData = pipeFirst.pull();
					if (firstData instanceof TradeDataNull) {
						writer.close();
						bEOF = true;
					} else {
						firstDay = firstData.getDay();
						firstTime = firstData.getTime();
					}
				}
				if (secondData == null) {
					secondData = pipeSecond.pull();
					if (secondData instanceof TradeDataNull) {
						bEOF = true;
					} else {
						secondDay = secondData.getDay();
						secondTime = secondData.getTime();
					}
				}
				if (bEOF) {
					priceQueueMediator.addTradeDataMsg(new TradeDataNull());	// indicates EOF to mediator
					System.out.println("End of File"); //TODO debugging
				} else if (firstTime == secondTime && firstDay == secondDay) {
					priceQueueMediator.addTradeDataMsg(secondData);
					priceQueueMediator.addTradeDataMsg(firstData);
					writer.write(firstData + "\n");
					writer.write(secondData + "\n");
					secondData = null;
					firstData = null;
				} else if ( (firstDay < secondDay) || (firstDay == secondDay && firstTime < secondTime)) {
					priceQueueMediator.addTradeDataMsg(firstData);
					writer.write(firstData + "\n");
					firstData = null;
				} else {
					priceQueueMediator.addTradeDataMsg(secondData);
					writer.write(secondData + "\n");
					secondData = null;
				} 
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Overrides abstract class - this filter takes two sources
	 */
	public void setSource(Pipe sourcePipe) {
		if (pipeFirst == null) {
			pipeFirst = sourcePipe;
		} else if (pipeSecond == null) {
			pipeSecond = sourcePipe;
		} else {
			System.err.println("too many source pipes to TimeSorterFilter");
		}
	}

}
