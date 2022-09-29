# currency-service

This service converts monetary amounts, in the following currencies, using an external API (https://v6.exchangerate-api.com):

- Euro
- Dollar
- Pound 
- Lira 
- Yen

The service accepts and returns JsonStrings via rabbit-mq.

**Rabbit-MQ**

- Exchange name: "kbeTopicExchange"
  - Queue name for calls: "currencyCallQueue
    - Routing key name for calls: "currencyService.call"
  - Queue name (for the receiver to listen for) for responses: "currencyResponsesQueue"
    - Routing key name for responses: "currencyService.response"
