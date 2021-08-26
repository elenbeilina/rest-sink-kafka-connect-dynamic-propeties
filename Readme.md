[![Quality gate](https://sonarcloud.io/api/project_badges/quality_gate?project=elenbeilina_rest-sink-kafka-connect-dynamic-propeties)](https://sonarcloud.io/dashboard?id=elenbeilina_rest-sink-kafka-connect-dynamic-propeties)

# Kafka Connect Rest API Sink

A kafka connect sink connector for pulling data from kafka and pushing it to multiple endpoints.

> #### Important notes:
> - source: Kafka Topic
> - target: Multiple rest API endpoints
> - can be used for interaction with only one api per connector
> - which endpoint to use depends on identifier property
> - if needed new features(identifier locations, auth types or http methods) can be added through configuration enums

<a name="build-project"></a>
### Build project

```
$ mvn clean package
```

------------------------

### Configuration

#### In `sink.json` there are several configurable parameters:

- **topics** - topics from where records will be pulled.
- **max.retries** - number of times to retry request in case of failure.
- **interval** - interval between requests in milliseconds.
- **api.base.uri** - defines api's base uri for connector interaction.


- **identifier.location** - location of identifier. All implemented locations are in `IdentifierLocation.class`.
- **identification.key** - name of the key field for identification. \
  For example for getting identifier value from object body - you need to specify field name(identification.key).
- **identifier.values** - identifiers, based on them, we decide which api to call and the process of
  calling.
- **identifier.{IDENTIFIER_VALUE}.url** - url to call for one of identifiers.
- **identifier.{IDENTIFIER_VALUE}.http.method** - http methods for one of identifiers. \
  All implemented methods can be found in `HttpMethod.class`


>  **{IDENTIFIER_VALUE} must have down case with '-' instead of ' '** \
>  For example, identifier value: DC COMICS, {IDENTIFIER_VALUE}:dc-comics.
>
>  ```
>    "identifier.values": "DC Comics,Marvel Comics",
>
>    "identifier.dc-comics.uri": "221872d9c0e1dac6d2485cd021bb7598.m.pipedream.net",
>   "identifier.dc-comics.http.method": "POST",
>
>    "identifier.marvel-comics.uri": "en50n8wsvkak1uu.m.pipedream.net",
>    "identifier.marvel-comics.http.method": "POST"
>  ```


- **auth.type** - filed for specifying authorization for api. All implemented methods can be found in `AuthType.class`
- **auth.url** - authentication uri for accessing all api methods.
- **auth.headers** - headers that are needed for authentication.
- **auth.query.params** - query params that are needed for authentication.

------------------------

### Test environment

All test environment can be found in `localTest` folder:

- `docker-compose.yml` with all needed environment such as:
    - kafka
    - zookeeper
    - schema-register
    - kafka-connect
    - control-center


- in `data` folder there is `test-data-dc.txt` and `test-data-marvel.txt` files with test data for kafkacat


- `produce-data.sh` file that pushes test data to kafka via kafkacat. \
  Kafkacat was chosen to implement this task, because for test case we needed to have headers in kafka records.


- `create-connector.sh` file that creates rest-sink-kafka-connector


- `sink.json` file that holds configuration for salesforce-platform-events-kafka-connector

---

### Testing process

There are several steps for full testing process of rest-sink-kafka-connector:

1. [Build connectors project](#build-project)
2. Go to `localTest` folder:

  ```
  cd localTest
  ```

3. Run docker environment:

  ```
  docker-compose up -d
  ```

4. Monitor environment via **control-center** util all clusters will be ready:

  ```
  http://localhost:9021/clusters
  ```

5. Create rest-sink-kafka-connector:

  ```
  sh create-connector.sh
  ```

6. Push test data to kafka:

  ```
  sh produce-data.sh
  ```

After successful creation connector will start pulling test data form kafka and pushing it to different apis based on
identifier specified in `sink.json`. In our test case all marvel publishers will be pushed
to https://en50n8wsvkak1uu.m.pipedream.net
and all dc to https://221872d9c0e1dac6d2485cd021bb7598.m.pipedream.net .