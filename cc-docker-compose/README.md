# Introduction
This example shows how to start Compliance Checker docker images using docker compose. This is a very basic example with the default configurations, its only purpose is to quickly get you started.

# Prerequisites
The followings need to be installed on your system:
- Docker
- Docker compose
- mongo:3.6 docker image (to install execute: `docker pull mongo:3.6`)

You also need to install the Compliance Checker docker images. See instructions [here](https://chemaxon.com/products/compliance-checker/download)

# How to run
- Clone the repository
- Open the docker-compose.yml, edit the CHEMAXON_LICENSE_URL environment variable for each service to point to the location where license file for Compliance Checker is available.
- Execute command: `docker-compose up -d`

# Access the system
- To access the web UI navigate to: http://localhost:8080/cc-web
- To access the API endpoints navigate to: http://localhost:8066/cc-api

# How to stop
- Execute command: `docker-compose down`

# Configure
To configure Compliance Checker edit config file: ./cc-config/config/application.properties. Documentation on configuration options can be found [here](https://docs.chemaxon.com/Configuring_Compliance_Checker.html)