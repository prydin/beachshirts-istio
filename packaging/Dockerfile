FROM akodali/java

COPY target/packaging-0.9.0-SNAPSHOT.jar /packaging/packaging.jar

COPY app.yaml /packaging/app.yaml
COPY applicationTags.yaml /packaging/applicationTags.yaml
COPY wfReportingConfig.yaml /wfReportingConfig.yaml

WORKDIR /
ENTRYPOINT java -jar ./packaging/packaging.jar ./packaging/app.yaml

