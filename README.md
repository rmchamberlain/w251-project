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
####1. Load data from Amazon public dataset to Swift
Go to [1_get_data](https://github.com/rmchamberlain/w251-project/edit/master/1_get_data). Follow the instructions in get_data.sh to (i) mount the data into a new EC2 instance and (ii) install Python, its packages and Swift onto the EC2 isntance. 

Then, you will need to transfer files from EC2 instance to Swift Obeject Storage. Simply scp the load_enron_data.py file onto the EC2 instance and run it on that EC2 instance with:
``` 
python load_enron_data.py
```
####2. Create a temporary server for first stage cleaning of the data in Swift
Go to [2_preprocess_A](https://github.com/rmchamberlain/w251-project/edit/master/2_preprocess_A). Follow the README instructions in that folder to setup the server. There is a "setup.sh" script to help setup the server quickly. After everything is well setup, you can run the clean script to (i) get the files from the Swift Object Store, (ii) clean it such that each text file represents a email with one word per line, (iii) upload those text files back to the Swift Object Store. The clean script is called "clean.py" and you should run it as such:
```
python clean.py no-attach swift 1 151
#This means that we are cleaning only the emails and ignore the attachments, and that we are selecting folders 1 to 151
```
####

3. Use Fabric to create HDFS
Go to [3_preprocess_B](https://github.com/rmchamberlain/w251-project/edit/master/2_preprocess_A). 


