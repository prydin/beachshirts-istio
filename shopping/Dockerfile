FROM akodali/java

COPY target/shopping-0.9.0-SNAPSHOT.jar /shopping/shopping.jar

COPY app-docker.yaml /shopping/app.yaml
COPY applicationTags.yaml /shopping/applicationTags.yaml
COPY wfReportingConfig.yaml /wfReportingConfig.yaml

WORKDIR /
ENTRYPOINT java -jar ./shopping/shopping.jar server ./shopping/app.yaml

