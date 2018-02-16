package ar.com.sac.model.formulas;

import ar.com.sac.model.Quote;
import java.math.BigDecimal;
import java.util.List;


public class MACD extends AbstractFormula {
   
   private int slowPeriod; //12
   private int fastPeriod; //26

   public MACD( int fastPeriod, int slowPeriod, List<Quote> quotes ){
      super( quotes );
      this.fastPeriod = fastPeriod;
      this.slowPeriod = slowPeriod;
   }

   @Override
   public BigDecimal calculateValue() {
      if(fastPeriod >= slowPeriod){
         return new BigDecimal(0d);
      }
      BigDecimal emaFast = new ExponentialMovingAverage( fastPeriod, quotes ).calculate();
      BigDecimal emaSlow = new ExponentialMovingAverage( slowPeriod, quotes ).calculate();
      return emaFast.subtract( emaSlow );
   }

   public BigDecimal calculateHistoricValue(Integer skip) {
      List<Quote> subList = quotes.subList(skip, quotes.size());

      if(fastPeriod >= slowPeriod){
         return new BigDecimal(0d);
      }
      BigDecimal emaFast = new ExponentialMovingAverage( fastPeriod, subList ).calculate();
      BigDecimal emaSlow = new ExponentialMovingAverage( slowPeriod, subList ).calculate();
      return emaFast.subtract( emaSlow );
   }
   
   @Override
   public String getKeySufix(){
      return String.valueOf( slowPeriod ) + "-" + String.valueOf( fastPeriod );
   }

}
