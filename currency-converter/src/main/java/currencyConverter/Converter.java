package currencyConverter;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Scanner;

import org.json.JSONObject;

/**
 * Servlet implementation class Converter
 */
public class Converter extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		HashMap<Integer, String> codeForCurrency = new HashMap<Integer, String>();
		
		codeForCurrency.put(1,"USD");
        codeForCurrency.put(2,"ZAR");
        codeForCurrency.put(3,"BDT");
        codeForCurrency.put(4,"PKR");
        codeForCurrency.put(5,"INR");

        //Integer from,to;

        String fromCurrency, toCurrency;
        double currAmount;
        
        Scanner sc = new Scanner(System.in);
		
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		response.setContentType("text/html");
		PrintWriter out = null;
		try
		{
		  out = response.getWriter();
		  int from = Integer.parseInt(request.getParameter("t1")); 
		  int to = Integer.parseInt(request.getParameter("t2"));
		  
		  out.println("<center>");
		  while(from < 2 || from > 5){
	            out.println("Please select a valid currency (2-5)");
	            out.println("2:ZAR (South African rand) \t 3:BDT (Bangladeshi taka) \t 4.PKR (Pakistani rupee) \t 5.INR (Indian rupee) ");
	            from =sc.nextInt();
	        }
	        fromCurrency = codeForCurrency.get(from);

	       
	        to = sc.nextInt();
	        
	        while(to != 1){
	            System.out.println("Please select a valid currency (1)");
	            System.out.println("1.USD (United States Dollar)");
	            to =sc.nextInt();
	        }
	        toCurrency = codeForCurrency.get(to);
	        
	        currAmount=sc.nextFloat();
	        
	    HttpRequest(fromCurrency,toCurrency,currAmount);
		}
		catch(Exception e)
		{
			out.println("Error: " + e.getMessage());
		}
		finally 
		{
			out.println("<br><br>");
			out.println("To refresh page <a href=index.html> Click Here </a>");
			out.println("</center");
			
		}

     }
	
	
	private static void HttpRequest(String fromCurrency, String toCurrency, double currAmount) throws IOException {
     
		ServletResponse response = null;
		Object out = response.getWriter();
		
        String pattern = "##.##";
        DecimalFormat f = new DecimalFormat(pattern);
        try {
            String apikey = "YOUR-APIKEY";
            String url = "https://freecurrencyapi.net/api/v2/latest";
            URL urlForGetRequest = new URL(url);
            String readLine = null;
            HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
            conection.setRequestMethod("GET");
            int responseCode = conection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));
                StringBuffer ressponse = new StringBuffer();
                while ((readLine = in.readLine()) != null) {
                    ressponse.append(readLine);
                }
                in.close();
                //System.out.println(response.toString());
                ((PrintWriter) out).println("\n");
                JSONObject object = new JSONObject(ressponse.toString());
                Double exRate = object.getJSONObject("data").getDouble(fromCurrency);
                ((PrintWriter) out).println(object.getJSONObject("data"));
                ((PrintWriter) out).println(exRate); //keep for debugging
                ((PrintWriter) out).println("\n");
                ((PrintWriter) out).println(f.format(currAmount)  + fromCurrency + " = " + f.format(currAmount/exRate)  + toCurrency);
            } else {
                throw new Exception("Error in API Call");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
	
}

