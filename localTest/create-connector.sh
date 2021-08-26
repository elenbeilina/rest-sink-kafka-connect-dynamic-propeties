printf " -> Try to a connect delete if exists \n\n"
curl \
  --request DELETE \
  --header "Content-Type: application/json" \
  http://localhost:8083/connectors/rest-sink-kafka-connect-dynamic-properties

printf "\n\n -> Create sink connector \n\n"
curl \
  --request POST \
  --header "Content-Type: application/json" \
  --data @sink.json \
  http://localhost:8083/connectors