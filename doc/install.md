### Installation 

1. Ubuntu 
3. Docker Installtion 
4. Post Install Setup 


## Ubuntu 

1. Make sure you have mysql DB where you need to create a DB ``messagegateway``

2. Install JAVA JDK (Open JDK 8 above)

3. Then 
``./gradlew bootRun``

## Docker Installation

You need to have docker engine and docker compose installed in your system 

``docker compose up -d``

Above command will run a service in detached mode with both mysql

## Post Install Setup 

You might have to run these from curl

1. Make a tenant  here you will recieve the token generated you have to pass the tenant value from fineract


```curl
curl --location --request POST 'localhost:9191/tenants' \
--header 'Content-Type: application/json' \
--header 'Cookie: i18next=us' \
--data-raw '{
"tenantId":"tenant",
"description": "Some description"
}'
```

2.  SMS Bridges , you might have to provide the PlaySMS username `Provider_Account_Id` and for the  `Provider_Auth_Token`  provide api key from play Sms 

```curl 
curl --location --request GET 'localhost:9191/smsbridges' \
--header 'Fineract-Platform-TenantId: {tenant}' \
--header 'Content-Type: application/json' \
--header 'Cookie: i18next=us' \
--data-raw '{
"phoneNo": "+12345678901",
"providerName": "Play SMS",
"providerDescription": "Play SMS Provider",
"providerKey":"Playsms",
"countryCode":"+91",
"bridgeConfigurations": [
{
"configName":"Provider_Account_Id",
"configValue":"Ankit"
},
{
"configName":"Provider_Auth_Token",
"configValue":"315994744cc2402047a6ea80210c9fb6"
}
]
}
'
```

3. Send SMS provide the App Key you got in first step

```curl 
curl --location --request POST 'localhost:9191/sms' \
--header 'Fineract-Platform-TenantId: default' \
--header 'Fineract-Tenant-App-Key: 9c12e77b-f0a5-4aba-8941-ac80f8a617c0' \
--header 'Content-Type: application/json' \
--header 'Cookie: i18next=us' \
--data-raw '[{
"internalId":"55",
"mobileNumber":"+919767901992",
"message":"Hello from Staging",
"providerId":"2"
}]'
```

Happy Sending SMS

