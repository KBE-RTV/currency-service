# currency-service

This service converts monerary amounts, in the following currencies, using an external API (https://v6.exchangerate-api.com):

- Euro
- Dollar
- Pound 
- Lira 
- Yen

The service accepts and returns JsonStrings via rabbit-mq:

**Example for input**:

- {"id":0,"currencyToConvertFrom":"Euro","currencyToConvertTo":"Dollar","amountList":[80.0,70.0]}

**Example for output**:

- {"id":0,"currencyToConvertFrom":"Euro","currencyToConvertTo":"Dollar","amountList":[82.048,71.792]}

**Rabbit-MQ**

- Exchange name: "kbe_topic_exchange" 
  - Queue name for calls: "currency_call_queue"
    - Routing key name for calls: "currencyService.call"
  - Queue name (for the receiver to listen for) for responses: "currency_responses_queue"
    - Routing key name for responses: "currencyService.response"
