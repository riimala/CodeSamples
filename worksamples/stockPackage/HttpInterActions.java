package stockPackage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.json.*;

import org.json.JSONException;

//import org.json.simple.parser.*;


public class HttpInterActions {

//https://stackoverflow.com/questions/2591098/how-to-parse-json-in-java
	
	public ArrayList<String> stockDataList = new ArrayList<>();
	
	public ArrayList<String> FetchRequest(String stock) throws IOException, InterruptedException{
		String pSymbol = "";
		String pName = "";
		double pScore = 0;
		BufferedReader reader = null;
		String line;
		StringBuffer responseContent = new StringBuffer();
		//JSONParser jsonParser = new JSONParser();
		
		
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://apidojo-yahoo-finance-v1.p.rapidapi.com/market/v2/get-quotes?region=US&symbols=" + stock )) //"AMD%2CIBM%2CAAPL"))
				.header("x-rapidapi-key", "4177a9055emsheec3ed52c7200fap1dad8cjsnf4da14b400f3")
				.header("x-rapidapi-host", "apidojo-yahoo-finance-v1.p.rapidapi.com")
				.method("GET", HttpRequest.BodyPublishers.noBody())
				.build();
		HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
		//crashing..
		System.out.println(response.body());

		if ( !response.body().contains("regularMarketOpen") )
		{
			StockFetcher.infoBox("Stock doesn't exist in US-market", "Stock Finder");
			return null;
		}
		
		try {
			if (response.statusCode() == 200) {
				JSONObject obj = new JSONObject(response.body());
				//String pageName = obj.getJSONObject("pageInfo").getString("pageName");
				JSONObject quoteResponse = obj.getJSONObject("quoteResponse");
				JSONArray arr = quoteResponse.getJSONArray("result");
				
				for (int i = 0; i < arr.length(); i++)
				{
					String symbol = arr.getJSONObject(i).getString("symbol");
					double open = arr.getJSONObject(i).getDouble("regularMarketOpen");
				    double bid = arr.getJSONObject(i).getDouble("bid");
				    double ask = arr.getJSONObject(i).getDouble("ask");				    
				    double fPE = arr.getJSONObject(i).getDouble("forwardPE");
					System.out.println("Stock " + symbol + ", PE: " + fPE + ", open: " + open + ", bid: " +bid+ ", ask: " +ask);					
					
					//PopulateData(symbol, open, bid, ask, fPE);
					
					stockDataList.add(symbol);
					stockDataList.add(Double.toString(open));
					stockDataList.add(Double.toString(bid));
					stockDataList.add(Double.toString(ask));
					stockDataList.add(Double.toString(fPE));

					return stockDataList;
				}
						
				//sF.SetData(stockDataList);
				//System.out.println("Stock data: \nStock " + pName + "\nSymbol: " + pSymbol + "\nScore: " + pScore);				
			}

			
		} catch (JSONException j) {
			System.err.println(j.getMessage());
		}
	return null;
	}
	
	void parseArray(JSONObject a) {
		JSONObject b = (JSONObject) a.get("result");
		System.out.println("result");
	}
	
	ArrayList<String> PopulateData(String symbol, double open, double bid, double ask, double fPE ){		
		ArrayList<String> stockList = new ArrayList<>();
		stockList.add(symbol);
		stockList.add(String.valueOf(open));
		stockList.add(String.valueOf(bid));
		stockList.add(String.valueOf(ask));
		stockList.add(String.valueOf(fPE));
		
		/*
		StockFetcher s = new StockFetcher();		
		String[] columnNames = {"Symbol", "Open", "Bid", "Ask", "Forward PE"};
		//DefaultTableModel model = new DefaultTableModel();
		s.model.setColumnIdentifiers(columnNames);
		Object [] data = {symbol, open, bid, ask, fPE};		
		s.model.addRow(data);
		s.table.setModel(s.model);
		*/	
		return stockList;
		}

	
	public ArrayList<String> stockData(){
		return this.stockDataList;
	}
}
