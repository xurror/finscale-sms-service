# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements. See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership. The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License. You may obtain a copy of the License at
#
# http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied. See the License for the
# specific language governing permissions and limitations
# under the License.
#
FROM openjdk:11 AS builder

RUN apt-get update -qq && apt-get install -y wget

COPY . sms
WORKDIR /sms

RUN ./gradlew --no-daemon -q  -x test bootJar

WORKDIR /sms/target
RUN jar -xf /sms/build/libs/message-gateway.jar

# https://issues.apache.org/jira/browse/LEGAL-462
# https://issues.apache.org/jira/browse/FINERACT-762
# We include an alternative JDBC driver (which is faster, but not allowed to be default in Apache distribution)
# allowing implementations to switch the driver used by changing start-up parameters (for both tenants and each tenant DB)
# The commented out lines in the docker-compose.yml illustrate how to do this.
WORKDIR /sms/target/BOOT-INF/lib
RUN wget -q https://repo1.maven.org/maven2/mysql/mysql-connector-java/8.0.20/mysql-connector-java-8.0.20.jar

# =========================================

FROM gcr.io/distroless/java:11 as sms

COPY --from=builder /sms/target/BOOT-INF/lib /app/lib
COPY --from=builder /sms/target/META-INF /app/META-INF
COPY --from=builder /sms/target/BOOT-INF/classes /app

ENTRYPOINT ["java", "-cp", "app:app/lib/*", "org.fineract.messagegateway.MessageGateway"]
