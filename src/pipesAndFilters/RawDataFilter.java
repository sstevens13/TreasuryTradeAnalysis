package pipesAndFilters;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/*
 * converts each line of data that is a bid or ask update into a TradeData object
 */
public class RawDataFilter extends Filter {
	BufferedReader fileReader;
	String lineOfData;
	private Boolean bPush, bEOF = false;

	public RawDataFilter(String fileName) {
		 try {
			fileReader = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * strips superfluous info and converts each bid or ask update
	 * into an object 
	 */
	public void pull() {
		try {
			bPush = false;
			while (!bPush && !bEOF) {
				if((lineOfData = fileReader.readLine()) == null) {
					fileReader.close();
					bEOF = true;
					bPush = true;
					sinkPipe.push(new TradeDataNull());
				} else {
					String[] dataArray = lineOfData.split(" "); 
					if (dataArray.length == 9 && dataArray[1].length() == 8 && dataArray[2].equals("0") &&
							(dataArray[5].equals("A") || dataArray[5].equals("B") || dataArray[5].equals("S"))) {
						TradeData tradeData = new TradeData(dataArray[0], dataArray[1].substring(4), (double) Integer.parseInt(dataArray[3]),
								dataArray[4], dataArray[5], Integer.parseInt(dataArray[8]));
						tradeData.setDay(Integer.parseInt(dataArray[1].substring(6)));
						bPush = true;
						sinkPipe.push(tradeData);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
