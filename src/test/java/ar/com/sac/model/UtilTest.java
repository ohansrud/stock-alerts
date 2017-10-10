package ar.com.sac.model;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class UtilTest {
   
 //desc order, APBR.BA from 04 ene 2017 to 10 nov 2016
   private static double[] closePrices = {86.45,85.2,79.9,82.2,82.2,81.2,79,78.9,78.7,78.1,79.95,78.5,77.9,81,82.6,83.55,87.35,87.9,
                       86.35,86.6,84.2,
                       85.35,
                       84.25,86.65,78.1,80.25,82.05,81.45,82,79.75,76.25,75.75,77.5,79.2,
                       75.2,72.65,77.3,85};
   private static double[] highPrices = {86.85,86.6,82,82.2,82.9,81.3,79.4,79.5,
                                         79.9,
                                         79.7,
                                         80.3,
                                         79.05,
                                         81,
                                         83,
                                         82.7,
                                         87,
                                         88.7,
                                         89.6,
                                          86.9,
                                          87,
                                          86.80,
                                          85.35,
                                          87.50,
                                          86.7,
                                          80.25,
                                          81.45,
                                          82.50,
                                          82.4,
                                          82.3,
                                          79.90,
                                          77,
                                          80,
                                          79,
                                          79.9,
                                          76,
                                          76.5,
                                          84.4,
                                          85.30};
   private static double[] lowPrices = {85.35,82.9,79.65,82.2,80.3,79,77.7,76,
                                        77.95,
                                        77.65,
                                        78.4,
                                        77.5,
                                        77.1,
                                        80.7,
                                        81.3,
                                        83.35,
                                        86.7,
                                        86.35,
                                         85,
                                         83.5,
                                         83.65,
                                         82.5,
                                         83.25,
                                         83.5,
                                         76.6,
                                         79.40,
                                         81.9,
                                         80,
                                         79.5,
                                         78.35,
                                         75,
                                         75.50,
                                         76.5,
                                         77.3,
                                         71,
                                         72.25,
                                         77, 
                                         81.5};
   
   //TODO COMPLETE WITH REAL VOLUME VALUES
   private static long[] volume = {4,3,2,0,29,28,27,26,23,22,21,20,19,16,15,14,13,12,
                                  7,6,5,2,1,30,29,25,24,23,22,21,18,17,16,15,
                                  14,11,10,9};
   
   private static int[] year = {2017,2017,2017,2016,2016,2016,2016,2016,2016,2016,2016,2016,2016,2016,2016,2016,2016,2016,
                                        2016,2016,2016,2016,2016,2016,2016,2016,2016,2016,2016,2016,2016,2016,2016,2016,
                                        2016,2016,2016,2016};
   
   private static int[] month = {0,0,0,0,11,11,11,11,11,11,11,11,11,11,11,11,11,11,
                                 11,11,11,11,11,10,10,10,10,10,10,10,10,10,10,10,
                                 10,10,10,10};
   private static int[] day = {4,3,2,30,29,28,27,26,23,22,21,20,19,16,15,14,13,12,
                                 7,6,5,2,1,30,29,25,24,23,22,21,18,17,16,15,
                                 14,11,10, 9};

   
   
   private static List<Quote> quotes = new ArrayList<Quote>();
   
   public static List<Quote> getQuotes(){
      if(quotes.size() > 0){
         return quotes;
      }
      
      generateQuotes();
      return quotes;
   }
   
   private static void generateQuotes(){
      Quote quote;
      Calendar calendar;
      for(int i = 0; i < closePrices.length; i++){
         quote = new Quote();
         calendar = Calendar.getInstance();
         calendar.set( year[i],month[i],day[i] );
         quote.setId( new QuoteId("APBR.BA", calendar) );
         quote.setClose( new BigDecimal(closePrices[i]) );
         quote.setHigh( new BigDecimal(highPrices[i]) );
         quote.setLow( new BigDecimal(lowPrices[i]) );
         quote.setVolume( volume[i] );
         quotes.add( quote );
      }
      //printQuotes();
   }

   @SuppressWarnings("unused")
   private static void printQuotes() {
      DecimalFormat df = new DecimalFormat("#.00"); 
      SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
      System.out.println( "DATE         HIGH  -  LOW  -  CLOSE" );
      for (Quote q: quotes){
         System.out.println( format.format(q.getDate().getTime()) + " - " + df.format( q.getHigh() ) + " - " + df.format( q.getLow() ) + " - " + df.format( q.getClose() ) );
      }
   }

}
