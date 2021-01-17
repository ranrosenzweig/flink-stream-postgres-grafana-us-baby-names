# US Baby Names

A demo of a simple exactly-once processing with Flink Streaming pipeline.
[data sets source (Kaggle)](https://www.kaggle.com/kaggle/us-baby-names#).<br/>
Data.gov releases two datasets that are helplful for this: one at the national level and another at the state level. Note that only names with at least 5 babies born in the same year (/ state) are included in this dataset for privacy.<br/>
<br/>
The data exposed with several views over the dashboard: 
![Alt text](images/dashboard.png?raw=true "Title")


## Stack
[Apache Flink™](http://flink.apache.org)<br/>
[postgresql](https://www.postgresql.org)<br/>
[Docker Compose](https://docs.docker.com/compose) <br/>
[Grafana](https://grafana.com/) <br/>

## Usage

Startup the environment:

```sh
docker-compose up --build
```

Build Flink job:

```sh
mvn clean package -DskipTests
```

Copy and execute the job within Flink cluster with arguments:<br/>
arg 1 [mandatory]: path/to/StateNames.csv<br/>
arg 2 [mandatory]: path/to/NationalNames.csv<br/>
arg 3 [optional]:  truncateTables<br/>
```sh
flink run ./my/path/to/flink/job/flink-stream-postgres-grafana-us-baby-names-1.0-SNAPSHOT.jar /path/to/StateNames.csv /path/to/NationalNames.csv TRUNCATETables
```

You can check the job status in the [Flink UI](http://localhost:8081). (default username:admin/password:admin)

Once it's running, check dashboard in the [Grafana Dashboard](http://localhost:3000).

Stop the environment:

```sh
docker-compose down
```