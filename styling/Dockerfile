FROM akodali/java

COPY target/styling-0.9.0-SNAPSHOT.jar /styling/styling.jar

COPY app-docker.yaml /styling/app.yaml
COPY applicationTags.yaml /styling/applicationTags.yaml
COPY wfReportingConfig.yaml /wfReportingConfig.yaml

WORKDIR /
ENTRYPOINT java -jar ./styling/styling.jar server ./styling/app.yaml

