## Citus-spike

### Multinode setup
Clone the repositiory on the master and each worker nodes.

#### 1. Start workers on each worker node
```sh
./gradlew startWorker
```

It will install `Docker` if necessary and run `PostgreSQL` with `Citus` and 
`jchem-psql` in a `Docker` container.
To have the `PostgreSQL` port on other port than the default 5432, use

```sh
./gradlew startWorker -Pport=<port numer>
```

#### 2. Start master on the master node
- Fill out the `docker/citus-master/pg_worker_list.conf` file with the hostnames and
`PostgreSQL` ports of the worker nodes. An example of its syntax is given in the
`docker/citus-master/pg_worker_list.conf.sample` file.
- Run the `startMaster` task to install `Docker` (if necessary), the master `PostgreSQL` and `jchem-psql` services:

```sh
./gradlew startMaster
```

To have the `PostgreSQL` port on other port than the default 5432, use

```sh
./gradlew startMaster -Pport=<port numer>
```

### Single node setup
For a single node setup start the workers on the same node as the master with a different
port number for each of them and give localhost and the used ports in the 
`pg_worker_list.conf` configuration file.


### Usage
#### 1. If docker container is not running then start it on master and workers

```sh
docker start citus-master
```

```sh
docker start citus-worker-<port number>
```

#### 2. Login to master docker container on master node 
```sh
docker exec -it citus-master bash
```

#### 3. Run `psql` as `postgres` user
```sh
psql -U postgres
```

### Results

Below are summarized the results of comparing runtimes of substructure search with three queries on a distributed table and a non-distributed table of 1 million SMILES structures. Distribution is on 4 worker nodes. Queries with low network raffic perform much better on the distributed table. For example, the five membered ring has few hits, thus the network traffic is low even if selecting molecules, but many ABAS has to be performed, thus distribution is a gain.

The 'hack' is trying to minimalize the network traffic by selecting id's from the distributed table and selecting molecules from the non-distributed table by the id's. The implemented `pgpsql` function is:

```sh
CREATE OR REPLACE FUNCTION sss(query text) RETURNS TABLE(mol molecule('sample')) AS $$ 
BEGIN
	DROP TABLE IF EXISTS sss_tmp;
	EXECUTE 'CREATE TEMPORARY TABLE sss_tmp AS (SELECT id FROM mol_table WHERE ' || QUOTE_LITERAL(query) || ' |<| mol)';
	RETURN QUERY SELECT m.mol FROM mol_table_single m, sss_tmp WHERE m.id = sss_tmp.id;
END;
$$ LANGUAGE plpgsql;    
```

And the query is:

```sh
SELECT sss('<SMILES>');
```

#### Hit and screen counts 

|  Molecule  | Hit count | Screen count |
|:---------: | --------: | -----------: |
|**c1ccccc1**|   791179  |    813800    |
|**c1cccc1** |	  674  |    826869    |
|**NN**      |    75557  |    311740    | 


#### Search times 

|   Query      |    SELECT    |  Simple table | Distributed table | Hack |
|:-----------: | -----------: | ------------: | ----------------: | ---: |
|**c1ccccc1**  | **count(\*)**|     22160     |        5356       |      |
|              | **id**  	  |     21499     |        5730       |      |
|    	       | **mol**      |     22844     |      204866       | 8330 |
|**c1cccc1**   | **count(\*)**|     27614     |        6954       |      |
|       	   | **id**	   	  |     26557     |        6615       |      |
|    	       | **mol**	  |     26072     |        7241       | 7307 |
|**NN**		   | **count(\*)**|      5382     |        1469       |      |
|       	   | **id**	   	  |      5265     |        1592	      |      | 
|              | **mol**	  |      5947     |       21801       | 2265 |


## JChem PostgreSQL Cartridge on a distributed PostgreSQL Citus database

### Setup
#### 1. Install PostgreSQL and Citus on your master and worker nodes and create the Citus extension on each node (master and workers) as described in the [Citus documentation](https://www.citusdata.com/docs/citus/5.1/installation/production.html).

#### 2. Follow the [JChem PostgreSQL Cartridge Manual](https://docs.chemaxon.com/display/docs/JChem+PostgreSQL+Cartridge+Manual) to set up the cartridge on each node:
- [Install the ChemAxon `jchem-psql` package on each node.](https://docs.chemaxon.com/display/docs/JChem+PostgreSQL+Cartridge+Manual#JChemPostgreSQLCartridgeManual-InstallationandSetup)
- [Create the ChemAxon and `hstore` extensions on each node.](https://docs.chemaxon.com/display/docs/JChem+PostgreSQL+Cartridge+Manual#JChemPostgreSQLCartridgeManual-Createextensioninadatabase)
- Put your valid ChemAxon license file to the `/etc/chemaxon/` folder on each node.

#### 3. Initialize the jchem-psql service on each node with
```sh
sudo service jchem-psql init
```

#### 4. Start the jchem-psql service on each node with
```sh
sudo service jchem-psql start
```

#### 5. Configure workers on the master node
Create the file `pg_worker_list.conf` in the master node's `PostgreSQL` data 
directory (the directory declared in the `posgresql.conf` file as `data_directory`) and add the 
worker's hostname and `PostgreSQL` port setups to this file, like:

```sh
worker-host1	5432
worker-host2	5421
(more workers)
```
 
### Examples of usage
#### 1. Check worker nodes
```sh
SELECT * FROM master_get_active_worker_nodes();
```

#### 2. Create distributed table with an ID and a molecule column
In the example below the table is created using hash distribution,
it has four shards and has one replica for each shard.

```sh
CREATE TABLE mol_table(id int, mol molecule('sample'));
SELECT master_create_distributed_table('mol_table', 'id', 'hash');
SELECT master_create_worker_shards('mol_table', 4, 1);
```

#### 3. Prepare CSV format SMILES file with ID before each molecule from ordinary SMILES file
In a command line shell: 

```sh
cat -n nci-pubchem_1m_unique.smiles | sed -e 's/^[ \t]*//' | sed -e 's/^[0-9]*/&,/' | sed -e 's/[ \t]*//g' > nci1m_with_id.smiles
```

#### 4. Fill the table with data
The script below cuts the original CSV file to 64 pieces and imports them to the created table parallely.
This script has to be executed from command line as `postgres` user. 

```sh
split -n l/64 nci1m_with_id.smiles chunks/
find chunks/ -type f | xargs -n 1 -P 64 sh -c 'echo $0 `copy_to_distributed_table -C $0 mol_table`'
```

The Citus 5.1 documentation states that the `PostgreSQL` `COPY` command is also soupported and inserts rows into tables parallely
but it failed in our tests.  


#### 5. Use the distributed table as any ordinary table for search, for example: 
```sh
CREATE INDEX mol_table_index ON mol_table USING chemindex(mol);
SELECT id from mol_table WHERE 'c1ccccc1N' |<| mol; 
```

### Exprerienced limitations when using Citus Community version

- Import can be done only with a limited set of PostgreSQL methods. Only a single insert can be performed using `SQL`, bulk insert can be performed with a [command-line tool](https://www.citusdata.com/docs/citus/5.0/performance/scaling_data_ingestion.html#bulk-copy) or `COPY`, described [here](https://www.citusdata.com/docs/citus/5.1/performance/scaling_data_ingestion.html#bulk-copy-100-200k-s), which has a much better performance. 
- No subselects are allowed in a modification statement (e.g. insert, delete, update). 
For example `INSERT INTO table2 SELECT * FROM table1 WHERE 'C' |<| mol` is not supported.
- Only distributed tables can be joint in one `SELECT` statement. A distributed and 
non-distributed table join is not supported.
- Only the `postgres` user can have distributed tables.
- Distributed tables can not be renamed.
- In Citus version 5.0 explain plans are not available, but they are already available in version 5.1. 
- Since the `ChemAxon PostgreSQL Cartridge` does not contain an equality operator for 
`Molecule` type, tables can not be distributed by hashing the `Molecule` type column. Tables containing molecules have to be sharded by another column.
