package ar.com.sac.services;

import ar.com.sac.model.Alert;
import ar.com.sac.model.Notification;
import ar.com.sac.model.operations.Operator;
import ar.com.sac.services.dao.AlertDAO;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@EnableScheduling
@PropertySource("classpath:application.properties")
@Transactional
public class AlertService {
   
   @Autowired
   private IStockService stockService;
   @Autowired
   private EmailService emailService;
   @Autowired
   private ExpressionService expressionService;
   
   @Autowired
   private AlertDAO alertDAO;
   
   @Value("${alerts.application.host}")
   private String host;
   
   @Value("${mail.subject}")
   private String emailSubject;
   
   @Value("${mail.salutation}")
   private String emailSalutation;
   
   @Transactional(readOnly = true)
   public List<Alert> getAlerts(boolean onlyActive){
      return alertDAO.getAlerts( onlyActive );
   }
   
   @Scheduled(cron = "${alerts.process.cron}")
   public void processAlertsJob(){
      System.out.println( "Processing Alerts JOB: " + new Date() );
      processAlerts();
   }
   
   public List<Notification> processAlerts() {
      return processAlerts( getAlerts( true ));
   }
   
   public List<Notification> processAlerts( List<Alert> alerts ){
      List<Notification> notifications = new ArrayList<Notification>();
      StringBuilder sb = new StringBuilder();
      String[] stocks = new String[]{"AAPL", "TSLA"};

      String[] stocks2 = new String[] {"ASC.OL","APCL.OL","AFG.OL","AGA.OL","AKA.OL","AKER.OL","AKPS.OL","AKSO.OL",
              "AKVA.OL","AMSC.OL","ABT.OL","ARCHER.OL","AFK.OL","ASETEK.OL","ATEA.OL","AURLPG.OL","AUSS.OL","AVANCE.OL","AVM.OL","AWDR.OL","BAKKA.OL","BEL.OL",
              "BERGEN.OL","BIONOR.OL","BIOTEC.OL","BIRD.OL","BLO.OL","BON.OL","BRG.OL","BWLPG.OL","BWO.OL","BMA.OL","CECON.OL","DAT.OL","DESSC.OL",
              "DETNOR.OL","DNB.OL","DNO.OL","DOF.OL","DOLP.OL","EAM.OL","ECHEM.OL","EKO.OL","EMGS.OL","ELT.OL","EMAS.OL","ENTRA.OL","EVRY.OL","FAR.OL","FLNG.OL",
              "FOE.OL","FRO.OL","FUNCOM.OL","GRO.OL","GJF.OL","GOGL.OL","GOD.OL","GSF.OL","HNB.OL","HFISK.OL","HAVI.OL","HEX.OL","HBC.OL","HLNG.OL","IDEX.OL",
              "IOX.OL","ITX.OL","IMSK.OL","JIN.OL","KOA.OL","KOG.OL","KVAER.OL","LSG.OL","MHG.OL","NAPA.OL","NATTO.OL","NAVA.OL","NEL.OL","NEXT.OL","NMG.OL",
              "NIO.OL","NOM.OL","NOD.OL","NSG.OL","NHY.OL","NOF.OL","NRS.OL","NAS.OL","NOR.OL","NPRO.OL","NTS.OL","OCY.OL","ODL.OL","ODF.OL","ODFB.OL","OLT.OL",
              "OPERA.OL","ORK.OL","PEN.OL","PCIB.OL","PGS.OL","PDR.OL","PHO.OL","PLCS.OL","POL.OL","PRS.OL","PROS.OL","PROTCT.OL","PSI.OL","QFR.OL","QEC.OL","RAKP.OL",
              "REC.OL","RECSOL.OL","RENO.OL","REPANT.OL","RGT.OL","RCL.OL","SALM.OL","SCI.OL","SSHIP.OL","SSO.OL","SCH.OL",
              "SDRL.OL","SBO.OL","SENDEX.OL","SER.OL","SEVDR.OL","SEVAN.OL","SIOFF.OL","SKI.OL","SOFF.OL","SOLV.OL","SONG.OL","SRBANK.OL","SPU.OL","STL.OL","SNI.OL",
              "STB.OL","STORM.OL","SUBC.OL","TIL.OL","TEL.OL","TELIO.OL","TGS.OL","SSC.OL","THIN.OL","TOM.OL","TTS.OL","VARDIA.OL","VEI.OL","VIZ.OL","WEIFA.OL","WRL.OL",
              "WBULK.OL","WWASA.OL","WWI.OL","WWIB.OL","XXL.OL","YAR.OL","ZAL.OL","ZONC.OL"};

      List<Alert> newAlerts = new ArrayList<Alert>();

      for(String stock: stocks) {
         for(Alert alert : alerts) {
            Alert newAlert = alert.copy();
            newAlert.setSymbol(stock);
            String exp = alert.getExpression();
            String newExp = exp.replaceAll("\\[SYMBOL]", stock);
            newAlert.setExpression(newExp);
            newAlerts.add(newAlert);
         }
      }

      for(Alert alert : newAlerts){
         processAlert(alert, notifications);
      }

      Alert alert;
      int i = 1;
      for(Notification notification: notifications){
         alert = notification.getAlert();
         if(!alert.getSendEmail()){
            continue;
         }
         sb.append( "<BR>-------------------------- " + i + " --------------------------<BR>" );
         sb.append( getEmailBody(alert) );
         i++;
      }
      
      if(sb.length()>0){
         sb.append( emailSalutation );
         try {
            emailService.generateAndSendEmail( emailSubject, sb.toString() );
         } catch (Exception e) {
            e.printStackTrace();
         }
      }
      return notifications;
   }
   
   private String getEmailBody( Alert alert ){
      StringBuilder sb = new StringBuilder();
      sb.append( alert.getName() );
      sb.append( "<BR>" );
      sb.append( alert.getDescription() );
      sb.append( "<BR>" );
      sb.append( "Expression: " + alert.getExpression().replaceAll( ">", "&gt;" ).replaceAll( "<", "&lt;" ) );
      sb.append( "<BR><BR>" );
      sb.append( "<a href=\"" + generateLink(alert.getId()) +  "/deactivate\" target=\"_blank\">Deactivate this alert</a>" );
      sb.append( "<BR>" );
      if(alert.getOpposedAlertId() != null && !alert.getOpposedAlertId().isEmpty()){
         sb.append( "<a href=\"" + generateLink(alert.getOpposedAlertId()) +  "/activate\" target=\"_blank\">Activate opposed alert " + alert.getOpposedAlertId() + "</a>" );
         sb.append( "<BR>" );
      }
      sb.append( "<a href=\"http://finance.yahoo.com/chart/" + alert.getSymbol() + "\" target=\"_blank\">See the chart on Yahoo Finance</a>" );
      sb.append( "<BR>" );
      return sb.toString();
   }
   
   private String generateLink( String alertId ) {
      String link = host;
      if(!link.endsWith( "/" )){
         link += "/";
      }
      link += "alerts/";
      return link + alertId ;
   }

   private void processAlert( Alert alert, List<Notification> notifications ) {
      try {
         Operator operator = expressionService.parseExpression( alert.getExpression(), stockService );
         if(operator.evaluate()){
            Notification notification = new Notification();
            notification.setCreationDate( new Date() );
            notification.setAlert( alert );
            notifications.add( notification  );
         }
      } catch (Exception e) {
         Notification notification = new Notification();
         notification.setCreationDate( new Date() );
         Alert exceptionAlert = new Alert();
         exceptionAlert.setSendEmail( true );
         exceptionAlert.setActive( true );
         exceptionAlert.setId( "exceptionAlertFor" + alert.getId() );
         exceptionAlert.setDescription( "An Exception ocurred processing Alert: " + alert.getId() + "\n" + e.getMessage() + "\n" +  stackTraceToString(e) );
         exceptionAlert.setName( "Exception Alert For " + alert.getId() );
         exceptionAlert.setExpression( alert.getExpression() );
         exceptionAlert.setSymbol( alert.getSymbol() );
         exceptionAlert.setOpposedAlertId( alert.getOpposedAlertId() );
         notification.setAlert( exceptionAlert );
         notifications.add( notification  );
      }
   }
   
   @Transactional
   public void saveOrUpdateAlert( Alert newAlert ) {
      normalizeId( newAlert );
      alertDAO.update( newAlert );
   }

   @Transactional
   public void saveAlert( Alert newAlert ) {
      normalizeId( newAlert );
      if( getAlertById( newAlert.getId() ) != null ){
         throw new RuntimeException( "Already exist an alert with id: " + newAlert.getId() );
      }
      alertDAO.persist( newAlert );
   }
   
   @Transactional
   public void deleteAlertById( String alertId ) {
      Alert alertToDelete = alertDAO.findById( alertId );
      if( alertToDelete != null ){
         alertDAO.remove( alertToDelete );
      }
   }

   @Transactional
   public void updateAlert( Alert alert ) {
      normalizeId( alert );
      if( getAlertById( alert.getId() ) == null ){
         throw new RuntimeException( "It doesn't exist an alert with id: " + alert.getId() );
      }
      alertDAO.update( alert );
   }

   @Transactional( readOnly = true )
   public Alert getAlertById( String alertId ) {
      return alertDAO.findById( alertId );
   }
   
   @Transactional
   public void activateAlert( String alertId ) {
      changeActive(alertId, true);
   }
   
   @Transactional
   public void deactivateAlert( String alertId ) {
      changeActive(alertId, false);
   }

   private void changeActive( String alertId, boolean value ) {
      Alert alert = getAlertById( alertId );
      if(alert == null){
         return;
      }
      alert.setActive( value );
      alertDAO.update( alert );
   }

   public List<Alert> getAlertsBySymbol( String symbol ) {
      return alertDAO.getAlertsBySymbol(symbol);
   }
   
   private void normalizeId( Alert alert ) {
      if(alert.getId().contains( " " )){
         alert.setId( toCamelCase( alert.getId() ) );
      }
   }
   
   private String toCamelCase(final String init) {
      if (init==null)
          return null;
      
      final StringBuilder ret = new StringBuilder(init.length());
      String word;
      String[] split = init.split(" ");
      for ( int i=0 ; i < split.length ; i++ ) {
         word = split[i];
         if (word.isEmpty()) {
            continue;
         }
         if( i == 0 ){
            ret.append( word.toLowerCase() );
         }else{
            ret.append(word.substring(0, 1).toUpperCase());
            ret.append(word.substring(1).toLowerCase());
         }
      }

      return ret.toString();
  }
   
   /**
    * This is a "Utils" method
    * @param t
    * @return
    */
   private String stackTraceToString( Throwable t ){
      StringWriter writer = new StringWriter();
      PrintWriter printWriter = new PrintWriter( writer );
      t.printStackTrace( printWriter );
      printWriter.flush();
      return writer.toString();
   }

}
