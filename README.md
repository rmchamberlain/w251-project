#Topic Modeling with Enron Email Dataset       
#####Team Members: Ryan Chamberlain, Arthur Mak, James Route, Sayantan Satpati
#####Project Detail: W251 Final Project, Summer 2015

The purpose of this project is to perform Topic Modeling on the Enron email dataset as a proof-of-concept of the utility of Topic Modeling in categorizing unstructured text data from a real world email collection. 

##Key Technologies
####SoftLayer Cloud
We used SoftLayer as our cloud infrastructure so we could spin up and down disposable virtual machines on demand with variable resources.  Using a cloud infrastructure allowed us to use variable resources at different stages of the project and to confirm that our infrastructure could be reliably rebuilt from scratch.
####Swift Object Storage
Since we were using disposable virtual machines for working storage and processing, we needed an efficient permanent storage location for the raw and intermediate data.  We used SoftLayer’s Swift Object Storage because it was cost-efficient, reliable, and had programmatic API access.
####Fabric
When using disposable cloud resources, it is essential to have an automated procedure for setting up the architecture.  We used Fabric for automated application deployment.
####Hadoop Distributed File System (HDFS)
In order to process data in a cluster, the data needs to be accessible from all of the compute nodes.  We accomplished this using HDFS, which distributes the data across the local storage of all of the nodes in the cluster.
####Hadoop
Before using LDA to perform Topic Modeling the emails needed to be pre-processed to remove irrelevant information, perform some basic natural language processing, and to convert to a file format appropriate for processing with LDA.  This was accomplished with basic Map/Reduce jobs in Hadoop.
####Spark
The LDA processing was performed in Spark using the MLLib library.  The data was read in from and written out to HDFS.


## Code Execution
#### load\_enron\_data.py
Transfers data from Amazon data set into SoftLayer object storage (Swift).  Swift credentials are grabbed from environment variables.
