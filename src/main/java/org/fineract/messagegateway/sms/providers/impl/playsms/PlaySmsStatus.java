package org.fineract.messagegateway.sms.providers.impl.playsms;

import org.fineract.messagegateway.sms.util.SmsMessageStatusType;

public class PlaySmsStatus {

  public static SmsMessageStatusType smsStatus(final String twilioStatus) {
    SmsMessageStatusType smsStatus = SmsMessageStatusType.PENDING;
    switch(twilioStatus) {
      case "PENDING":
      case "ACCEPTED":
        smsStatus = SmsMessageStatusType.WAITING_FOR_REPORT ;
        break ;
      case "sent" :
        smsStatus = SmsMessageStatusType.SENT ;
        break ;
      case "DELIVERED":
        smsStatus = SmsMessageStatusType.DELIVERED;
        break ;
      case "UNDELIVERABLE":
      case "EXPIRED" :
      case "REJECTED":
        smsStatus = SmsMessageStatusType.FAILED ;
        break ;
    }
    return smsStatus ;
  }

  public static SmsMessageStatusType smsStatus(final Integer infoBipStatus) {
    SmsMessageStatusType smsStatus = SmsMessageStatusType.PENDING;
    switch(infoBipStatus) {
      case 0:
        smsStatus = SmsMessageStatusType.WAITING_FOR_REPORT ;
      case 1:
        smsStatus = SmsMessageStatusType.SENT ;
        break ;
      case 2:
      case 4:
      case 5:
        smsStatus = SmsMessageStatusType.FAILED;
        break ;
      case 3:
        smsStatus = SmsMessageStatusType.DELIVERED;
        break ;
    }
    return smsStatus ;
  }
}
