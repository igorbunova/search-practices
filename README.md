# search-practices

This repository contains asynchronous examples of the java code integration to implement CRUD on Solr and Elasticsearch.

## search-common
Contains common interfaces for operations and example model class

## search-es
Elasticsearch implementation

## search-solr
Solr implementation

## search-data-storage
Example of asynchronous reader from the postgresql database.

## search-cli
small command line interface example to execude CRUD operations <br/>
to setup you need:
* postgresql 9.5
* Elasticsearch 5.x or/and Solr 6.x
* create any database and run [init.sql](https://github.com/igorbunova/search-practices/blob/master/source-data-storage/src/main/resources/db/init_script.sql) <br/>
Note: it contains postgresql copy command execution to populate data, the best way is running locally with psql cli.
* change 
[ds.conf](https://github.com/igorbunova/search-practices/blob/master/search-cli/src/main/resources/ds.conf) <br/>
[es.conf](https://github.com/igorbunova/search-practices/blob/master/search-cli/src/main/resources/es.conf) <br/>
[solr.conf](https://github.com/igorbunova/search-practices/blob/master/search-cli/src/main/resources/solr.conf) <br/>
regarding your environment
* mvn clean install
* unzip search-cli/target/<name>-distr.zip ./<folder>
* cd ./<folder>/bin
* ./example.sh output all supported commands and options 

