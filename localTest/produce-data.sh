docker run --tty \
           --network localtest_kafka-connect \
           -v /Users/aqua-len/IdeaProjects/rest-sink-kafka-connect-dynamic-propeties/localTest/data/test-data-dc.txt:/test-data-dc.txt\
           -v /Users/aqua-len/IdeaProjects/rest-sink-kafka-connect-dynamic-propeties/localTest/data/test-data-marvel.txt:/test-data-marvel.txt\
           confluentinc/cp-kafkacat \
           bash -c "cat /test-data-dc.txt | kafkacat  \
           -b broker:29092 \
            -P -t main \
            -H publisher='DC Comics'

            cat /test-data-marvel.txt | kafkacat  \
           -b broker:29092 \
            -P -t main \
            -H publisher='Marvel Comics'"
