# Compliance Checker File API Usage Example

## Introduction
This project is a Java SpringBoot application example on how to use [ChemAxon Compliance Checker](https://chemaxon.com/products/compliance-checker) file integration API to check big amount of structures read from a relational database and writing back the results. Focus was made on showing what services need to be invoked in what order to execute checks, and how to retrieve the generated reports. The results written back to the database are very simple, they only tell whether the structures are controlled, not controlled or if any kind of error happened during checking. Further details about he results can be extracted from the Segment class.
To make the application easy to execute and test a h2 database file is included in the project (h2db/nci10Ksmiles-h2db.mv.db) with 10.000 structures. The size of the included database is quite small but the application is able to deal with lot larger datasets (has been tested with a database containing up to 7 million structures).
The code is not robust enough for production use, possible runtime failures (e.g. failing service calls) are not handled properly.

## Workflow
1. Reading molecules from the embedded H2 database and creating configurable (`file.mols.count`) sized batch files in CSV format. (Creating several files are only needed in case of a huge dataset. Uploading one file with hundreds of thousands of structures can produce such a big JSON report that can not fit into the server's memory.)
2. Submitting batches one after each other for checking to the Compliance Checker batch check service.
3. Program waits for the check of a batch to finish. When it is finished and the results are available then a report is downloaded and results are written back to the H2 database.
4. Batches are processed sequentially one after the other, when the result of a batch has been written to the database, the next batch is produced, submitted for check and the results are persisted.
5. Optionally stored data (which can be used to generate a new report without rechecking the molecules) can be removed from the compliance checker persistence by configuring the parameter `check.cleanup.enabled` to `true` (default setup). With this setup MongoDB will require less memory.

## Prerequisites
* JDK8 or higher installed
* H2 DB file with molecules (path has to be cofigured in the `application.properties` file)
* ChemAxon Compliance Checker services installed and running (for help, please refer to our [documentation](https://docs.chemaxon.com/display/docs/Compliance+Checker)):
    * cc-webservice
    * cc-bigdata
    * cc-config
    * cc-eureka

## Usage
1. Build jar for the application:
```
./gradlew ccbootjar
```
2. Copy the content of the `build/libs` folder to the desired location.
2. Configure application by setting properties in the `application.properties` file under the `config` folder.
3. Run the application:
```
java -jar <path>/cc-file-api-client-0.1.0.jar
```

