FROM akodali/java

COPY target/printing-0.9.0-SNAPSHOT.jar /printing/printing.jar

COPY app.yaml /printing/app.yaml
COPY applicationTags.yaml /printing/applicationTags.yaml
COPY wfReportingConfig.yaml /wfReportingConfig.yaml

WORKDIR /
ENTRYPOINT java -jar ./printing/printing.jar ./printing/app.yaml

