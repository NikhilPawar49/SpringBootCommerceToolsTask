FROM eclipse-temurin:21-jre

# Copy Zscaler Root CA
COPY zscaler.cer /tmp/zscaler.cer

# Import cert into JVM truststore
RUN keytool -importcert \
    -trustcacerts \
    -alias zscaler-root-ca \
    -file /tmp/zscaler.cer \
    -keystore $JAVA_HOME/lib/security/cacerts \
    -storepass changeit \
    -noprompt

WORKDIR /app
COPY build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
