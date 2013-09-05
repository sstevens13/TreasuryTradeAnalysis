package trade;

public interface TradeStrategy {

//	public boolean updateSpreadPrice(double spreadSMA, double spreadAvg, double znBid, double znAsk, double zfBid, double zfAsk, double time);
//	public boolean updateSpreadPrice(double askSMA, double spreadAsk, double bidSMA, double spreadBid, double znBid, double znAsk, double zfBid, double zfAsk, double time);
	public boolean updateSpreadPrice(PriceData data);
	
}
