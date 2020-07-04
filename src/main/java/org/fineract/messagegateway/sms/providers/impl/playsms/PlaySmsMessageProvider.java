package org.fineract.messagegateway.sms.providers.impl.playsms;


import com.twilio.http.TwilioRestClient;
import org.fineract.messagegateway.MessageGateway;
import org.fineract.messagegateway.configuration.HostConfig;
import org.fineract.messagegateway.constants.MessageGatewayConstants;
import org.fineract.messagegateway.exception.MessageGatewayException;
import org.fineract.messagegateway.sms.domain.SMSBridge;
import org.fineract.messagegateway.sms.domain.SMSMessage;
import org.fineract.messagegateway.sms.providers.SMSProvider;
import org.fineract.messagegateway.sms.providers.impl.twilio.TwilioMessageProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.print.attribute.standard.Destination;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Service(value= "PlaySMS")
public class PlaySmsMessageProvider extends SMSProvider {

  private static final Logger logger = LoggerFactory.getLogger(TwilioMessageProvider.class);

  private  RestTemplate restClients = new RestTemplate();

  private final String callBackUrl ;

  private final StringBuilder builder ;

  private static final String BASE_URI = "http://52.172.158.105/playsms/index.php?app=ws";

  @Autowired
  PlaySmsMessageProvider(final HostConfig hostConfig) {
    builder = new StringBuilder() ;
    callBackUrl = String.format("%s://%s:%d/playsms/report/", hostConfig.getProtocol(),  hostConfig.getHostName(), hostConfig.getPort());
    logger.info("Registering call back to playsms:"+callBackUrl);
  }


  @Override
  public void sendMessage(SMSBridge smsBridgeConfig, SMSMessage message) throws MessageGatewayException, URISyntaxException {
    String statusCallback = callBackUrl+message.getId();
    String smsClient = get(smsBridgeConfig);

    builder.setLength(0);
      builder.append(smsBridgeConfig.getCountryCode());
      builder.append(message.getMobileNumber());
      String mobile = builder.toString();

      logger.info("Sending SMS to " + mobile + "....");
    // Add message code to the sms client
      // Before we
      String newSms = null;
      try{
        newSms = smsClient + "&op=pv&to=" + mobile + "&msg=" + URLEncoder.encode(message.getMessage(), "UTF-8") + "&format=json";
      } catch (UnsupportedEncodingException e) {
        logger.error(e.getMessage());
      }
      URI uri = new URI(newSms);
      ResponseEntity<MessageHelper> responseEntity = restClients.getForEntity(uri.toASCIIString(),MessageHelper.class);
      responseEntity.getBody();
      message.setExternalId(responseEntity.getBody().getData().get(0).getSmslogId());
      message.setDeliveryStatus(PlaySmsStatus.smsStatus(3).getValue());
      logger.debug("PlaySmSMessageProvider.sendMessage():" + responseEntity.getBody().getData().get(0).getStatus());
  }

  /**
   *  @Todo need to implement the token function
   *
   */

  public String get(SMSBridge smsBridge) {
    logger.debug("Creating a new playSMSClient...");
    String providerAccountId = smsBridge.getConfigValue(MessageGatewayConstants.PROVIDER_ACCOUNT_ID);
    String providerAuthToken = smsBridge.getConfigValue(MessageGatewayConstants.PROVIDER_AUTH_TOKEN);
    // build the url
    return BASE_URI + "&u="  + providerAccountId + "&h=" + providerAuthToken;
  }



}
