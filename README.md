# Getting Started

### Technologies

* JAVA 11 with SpringBoot 2.6.1
* Gradle, Docker, docker-compose, Git

### How run application

* Run the command to build docker image: `gradle bootBuildImage`
* Run the command to starts services: `docker-compose up`
* Or run one command `gradle bootBuildImage && docker-compose up`

### How test application

* HTTP Post Request in endpoint (e.g. Postman): `http://localhost:8081/order`
  * Request-body example:

```json
{
  "products": [
    {
      "id": 1,
      "quantity": 2
    },
    {
      "id": 2,
      "quantity": 1
    }
  ]
}
``` 

* Response-body example:

```json
{
    "total_amount": 124125,
    "total_amount_with_discount": 122610,
    "total_discount": 1515,
    "products": [
        {
            "id": 1,
            "quantity": 2,
            "unit_amount": 15157,
            "total_amount": 30314,
            "discount": 1515,
            "is_gift": false
        },
        {
            "id": 2,
            "quantity": 1,
            "unit_amount": 93811,
            "total_amount": 93811,
            "discount": 0,
            "is_gift": false
        }
    ]
}
```
