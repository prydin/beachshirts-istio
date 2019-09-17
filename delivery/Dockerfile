FROM akodali/java

COPY target/delivery-0.9.0-SNAPSHOT.jar /delivery/delivery.jar

COPY applicationTags.yaml /delivery/applicationTags.yaml
COPY wfReportingConfig.yaml /wfReportingConfig.yaml
COPY ./src/main/resources/application.yml /delivery/application.yml

WORKDIR /
ENTRYPOINT java -jar ./delivery/delivery.jar server --spring.config.location=/delivery/application.yml

