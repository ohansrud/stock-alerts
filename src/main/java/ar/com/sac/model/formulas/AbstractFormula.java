package ar.com.sac.model.formulas;

import ar.com.sac.model.Quote;
import java.math.BigDecimal;
import java.util.List;

public abstract class AbstractFormula implements Formula {
   protected List<Quote> quotes;
   
   public AbstractFormula( List<Quote> quotes ){
      this.quotes = quotes;
   }
   
   @Override
   public BigDecimal calculate() {
      BigDecimal value = FormulasCacheValues.getInstance().getFromCache( getKey() );
      if ( value == null ){
         value = calculateValue();
         FormulasCacheValues.getInstance().addToCache( getKey(), value );
      }
      return value;
   }
   
   public abstract BigDecimal calculateValue();

   @Override
   public String getKey(){
      StringBuilder key = new StringBuilder();
      if(quotes.size()>0){
         Quote q = quotes.get( 0 );
         key.append( q.getSymbol() );
         key.append( String.valueOf( q.getDate().getTimeInMillis() ));
         key.append( "-" );
      }
      key.append( getClass().getSimpleName() );
      key.append( getKeySufix() );
      return key.toString();
   }

   public String getKeySufix(){
      return "";
   }
}
