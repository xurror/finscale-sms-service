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
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Service(value= "PlaySMS")
public class PlaySmsMessageProvider extends SMSProvider {

  private static final Logger logger = LoggerFactory.getLogger(TwilioMessageProvider.class);

  private HashMap<String, RestTemplate> restClients = new HashMap<>() ;

  private final String callBackUrl ;

  private final StringBuilder builder ;

  @Autowired
  PlaySmsMessageProvider(final HostConfig hostConfig) {
    builder = new StringBuilder() ;
    callBackUrl = String.format("%s://%s:%d/playsms/report/", hostConfig.getProtocol(),  hostConfig.getHostName(), hostConfig.getPort());
    logger.info("Registering call back to playsms:"+callBackUrl);
  }


  @Override
  public void sendMessage(SMSBridge smsBridgeConfig, SMSMessage message) throws MessageGatewayException {

  }

  /**
   *  @Todo need to implement the token function
   *
   */

//  public RestTemplate get(SMSBridge smsBridge) {
//    logger.debug("Creating a new playSMSClient...");
//    String providerAccountId = smsBridge.getConfigValue(MessageGatewayConstants.PROVIDER_ACCOUNT_ID);
//    String providerAuthToken = smsBridge.getConfigValue(MessageGatewayConstants.PROVIDER_AUTH_TOKEN);
//  }

}
