# db-unloader

The db-unloader enables unloading a specified list of tables from a relational database into cloud storage. 

Currently it supports MySQL and PostgreSQL as source database and AWS S3 and ADLS Gen2 as target storage. 

## Getting Started
Use terraform to create the necessary cloud resources. Create a docker container and push to artifactory. Then use
the helm charts to deploy to kubernetes of choice. Make sure the docker registry credential is 
available as a secret to be able to pull from private repository. 

List the tables to be unloaded into the `application.conf` file, along with the name of the target bucket. Currently 
it supports `csv` file types, more types will be added in future. Output files are categorized by table names. Logs are available inside the `log` folder.