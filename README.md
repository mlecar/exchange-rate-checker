## Service that checks exchange rates from Euro to USDollar continuously

I am using the public exposed api from https://fixer.io/ to get the latest exchange rates

http://api.fixer.io/latest?base=EUR

### Main services exposed

#### Get the latest exchange rate
curl -X GET http://localhost:8080/exchange/rate/

Response Status: 200
Response Content Type: application/json
Response Message:
```
{
   "rates":{
      "date":"2018-04-06",
      "rate":1.2234
   },
   "from":"EUR",
   "to":"USD"
}
```

#### Get the exchange rates from 2018-04-01 and 2018-04-06
curl -X GET http://localhost:8080/exchange/rate/dateseries/2018-04-01/2018-04-06


Response Status: 200
Response Content Type: application/json
Response Message Example:
```
{
   "rates":[
      {
         "date":"2018-04-01",
         "rate":1.232118
      },
      {
         "date":"2018-04-02",
         "rate":1.228855
      },
      {
         "date":"2018-04-03",
         "rate":1.226083
      },
      {
         "date":"2018-04-04",
         "rate":1.229276
      },
      {
         "date":"2018-04-05",
         "rate":1.222894
      },
      {
         "date":"2018-04-06",
         "rate":1.2234
      }
   ],
   "from":"EUR",
   "to":"USD"
}
```

### Services exposed for testing some features

#### To simplify test, I load some values from a file to get historical values
curl -X PUT http://localhost:8080/exchange/rate/historical/load

Response Status: 201

#### For testing purpose I exposed all entries from the H2 database
curl -X GET http://localhost:8080/exchange/rate/all

Response Status: 200
Response Content Type: application/json
Response Message Example:
```
[
   {
      "id":1,
      "from":"EUR",
      "to":"USD",
      "date":"2018-04-06",
      "lastDateCheck":1523175880652,
      "rate":1.2234
   },
   {
      "id":2,
      "from":"EUR",
      "to":"USD",
      "date":"2018-04-06",
      "lastDateCheck":1523175890892,
      "rate":1.2234
   },
   {
      "id":3,
      "from":"EUR",
      "to":"USD",
      "date":"2018-04-06",
      "lastDateCheck":1523175900804,
      "rate":1.2234
   },
   {
      "id":4,
      "from":"EUR",
      "to":"USD",
      "date":"2018-04-06",
      "lastDateCheck":1523175910532,
      "rate":1.2234
   }
]
```
