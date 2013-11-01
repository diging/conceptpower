import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;

public class SearchViaf {

/**
* @param args
*/
public static void main(String[] args) {

  StringBuffer buffer = new StringBuffer();
   java.net.URL url;
   String result;
   String line;
   String response = "";
   URLConnection connection;

   try {
      url = new URL( "http://localhost:8080/searchblox/api/rest/add" );
      connection = url.openConnection(  );
      connection.setDoOutput( true );
      OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream(), "UTF-8"); 
      result = new String( buffer.toString().getBytes(), "UTF-8" );
      System.out.println( "\n *INDEX XML * \n\n" + result + "\n\n");
      wr.write( result );
      wr.flush();
      wr.close();

       BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
       while ((line = rd.readLine()) != null) {
           response += line;
       }
       wr.close();
       rd.close();

       System.out.println( "\n *INDEX RESPONSE XML * \n\n" + response + "\n\n");            

  }
  catch(Exception e){
     System.out.println("Errors...");
  }
}
}