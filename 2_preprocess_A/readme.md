###Setup server to read files from Swift Object Store
Provision server [REMEMBER to change key name]
```
slcli vs create --hostname=preprocessor2 --domain=test.net --cpu 1 --memory 12288 -o UBUNTU_LATEST --datacenter=dal05 --billing=hourly --key atm
```
SCP scripts and SSH into the newly provisioned server [CHANGE IP address]
```
scp setup.sh root@192.155.194.138:
scp clean.py root@192.155.194.138:
ssh root@192.155.194.138
```
Once ssh inside the server, run below script to setup:
```
sh setup.sh
```
Test the script
```
swift download w251-enron /enron/edrm-enron-v2/edrm-enron-v2_allen-p_pst.zip
```
If download of file is successful, your server is ready to rock with swift! Otherwise, you may need to enter "source .bashrc" if it doesn't recognize your swift credentials

###Fix Python library file - zipfile.py
Modify zipfile.py core python file such that it will allow unzipping files that span multiple disks. Otherwise, an error related to "span multiple disks" will occur for reading some of the zipped files. 
```
vi /usr/lib/python2.7/zipfile.py
```
Once inside the zipfile.py, commment the following lines:
```
if diskno != 0 or disks != 1:
    raise BadZipFile("zipfiles that span multiple disks are not supported")
```

###Running the clean.py script
2 options for running the python script (recommend to choose Option 2):
######Option 1: just clean the data and save everything in "/emails" folder in the server
```
python clean.py no-attach local 1 151
```
######Option 2: in additon to job in Option 1, also upload output files to SWIFT OBJECT STORAGE (much slower)
```
python clean.py no-attach swift 1 151

```
After using option 2, check whether files uploaded properly inside the container "w251-enron" and folder "emails" inside the Swift Object Store:
```
swift list w251-enron emails
```
>>>>>>> 0da67ed5f2384af078f0bce8b6bdb7a66568b2f5
