package ar.com.sac.model.formulas;

import ar.com.sac.model.Quote;

import java.math.BigDecimal;
import java.util.List;


public class MACDCross extends AbstractFormula {

   private int slowPeriod; //12
   private int fastPeriod; //26
   private int signalPeriod; //9

   public MACDCross(int fastPeriod, int slowPeriod, int signalPeriod, List<Quote> quotes){
      super( quotes );
      this.fastPeriod = fastPeriod;
      this.slowPeriod = slowPeriod;
      this.signalPeriod = signalPeriod;
   }

   @Override
   public BigDecimal calculateValue() {
      MACD macd = new MACD(fastPeriod, slowPeriod, quotes);
      MACDSignalLine macdSignalLine = new MACDSignalLine(fastPeriod, slowPeriod, signalPeriod, quotes);
      MACDHistogram macdHistogram = new MACDHistogram(fastPeriod, slowPeriod, signalPeriod, quotes);


      BigDecimal currentValue = macdHistogram.calculateValue();
      BigDecimal pastValue = macdHistogram.calculateHistoricValue(1);


      BigDecimal zero = new BigDecimal(0);

      BigDecimal signal = new BigDecimal(0);

      if (pastValue.compareTo(zero) == 1) {
         System.out.println("MACD Over Signal");
         if (currentValue.compareTo(zero) == -1) {
            System.out.println("Signal crossing over");
            signal = new BigDecimal(1);
         }
      }
      else if (pastValue.compareTo(zero) == -1) {
         System.out.println("Signal over MACD");
         if (currentValue.compareTo(zero) == 1) {
            System.out.println("Signal crossing under");
            signal = new BigDecimal(-1);
         }
      }
      if(signal.compareTo(new BigDecimal(0)) != 0){
         int d = 1;
      }
      return signal;
   }

   @Override
   public String getKeySufix(){
      return String.valueOf( slowPeriod ) + "-" + String.valueOf( fastPeriod ) + "-"  + String.valueOf( signalPeriod );
   }

}
