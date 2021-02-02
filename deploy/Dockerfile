# build
FROM maven:3.6.3-adoptopenjdk-11 as builder
WORKDIR /usr/src/app
COPY . .
RUN mvn clean package --settings ./deploy/.m2/settings.xml


FROM adoptopenjdk/openjdk11:jre-11.0.9.1_1 AS final
WORKDIR /opt/app
EXPOSE 80
COPY --from=builder /usr/src/app/automation-app/target/automation-app-*.war automation.war
ENTRYPOINT ["java", "-jar", "automation.war", "--spring.profiles.active=${ASPNETCORE_ENVIRONMENT}"]
