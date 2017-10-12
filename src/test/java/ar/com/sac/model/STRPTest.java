package ar.com.sac.model;

import ar.com.sac.model.formulas.StandardDeviationPercentage;
import org.junit.Assert;
import org.junit.Test;

public class STRPTest {
   @Test
   public void strpTest() {
      StandardDeviationPercentage sdp = new StandardDeviationPercentage( 14, UtilTest.getQuotes() );
      Assert.assertEquals( "3.124224862140212000127803548821248114109039306640625", sdp.calculate().toString());
   }
   
}
