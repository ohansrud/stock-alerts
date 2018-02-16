package ar.com.sac.model.formulas;

import ar.com.sac.model.Quote;

import java.math.BigDecimal;
import java.util.List;


public class RSICross extends AbstractFormula {

   private int slowPeriod; //12
   private int fastPeriod; //26
   private int signalPeriod; //9

   public RSICross(int fastPeriod, int slowPeriod, int signalPeriod, List<Quote> quotes){
      super( quotes );
      this.fastPeriod = fastPeriod;
      this.slowPeriod = slowPeriod;
      this.signalPeriod = signalPeriod;
   }

   @Override
   public BigDecimal calculateValue() {
      RelativeStrengthIndex rsi = new RelativeStrengthIndex(14, quotes);

      BigDecimal currentValue = rsi.calculateValue();
      BigDecimal pastValue = rsi.calculateHistoricValue(1);

      BigDecimal lowThreshold = new BigDecimal(30);

      BigDecimal signal = new BigDecimal(0);

      if (pastValue.compareTo(lowThreshold) == -1) {
         System.out.println("RSI Below 30");
         if (currentValue.compareTo(lowThreshold) == 1) {
            System.out.println("RSI Crossed over 30");
            signal = new BigDecimal(1);
         }
      }
      return signal;
   }

   @Override
   public String getKeySufix(){
      return String.valueOf( slowPeriod ) + "-" + String.valueOf( fastPeriod ) + "-"  + String.valueOf( signalPeriod );
   }

}
