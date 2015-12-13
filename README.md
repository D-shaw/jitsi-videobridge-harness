#Jitsi Videobridge Harness

Jitsi Videobridge Harness consists of creating a Java application which creates and connects a JitMeet.org video conference. It is called the JITSI hammer and is a torture test tool for the JITSI web application. The application starts creating fake participants and streaming audio and video for them. This tool has several basic functions, such as creating Jitsi bridge, switching  protocols between TCP/IP and UDP, adding fake participants, streaming the video and the audio content. Also the tool will report the results such as the CPU usage, the video and audio packet loss, and the fake users added to a call in the front end. In this project, our target audience is the software testing engineer in Jitsi, and according to that, we have to do our best to make the tool simple and efficient.


##Use Cases
- Varying the number of users participating in a single conference call
- Testing for packet loss rate for TCP/IP and UDP protocols
- CPU and memory usage monitored on the server during a call
- Output of logs for the parameters with which a call session is initiated

##How to Use
###Configure EC2 Instance

First you should configure server to run Jitsi Videobridge. This can either be on a local machine or on remote cloud service. We recommend cloud service since it should be more reliable.
So first follow Jitsi Videobridge quick-install instructions to install Web server and XMPP server on EC2 instance. Now the Jitsi Videobridge should be running correctly.
Note that you have to configure two remote server, one for TCP/IP protocol and the other for UDP protocol.

On both server you should install Node.js and MongoDB in order to run the server monitor website. 

https://nodejs.org/en/
https://www.mongodb.com/lp/download/mongodb-enterprise

The Server Monitor Website code is in the folder 
```
/Website_Dashboard
```

The Amazon EC2 instructions are here:

http://docs.aws.amazon.com/AWSEC2/latest/UserGuide/concepts.html

The Manual installation for Jitsi Videobridge instructions are here:

https://github.com/jitsi/jitsi-meet/blob/master/doc/manual-install.md

The Automatic installation for Jitsi Videobridge instructions are here:

https://github.com/jitsi/jitsi-meet/blob/master/doc/quick-install.md


###Run Local Java Application

The Java Application source codes are in folder 
```
/Jitsi Harness
```
Run this to start the application.
```
/Jitsi Harness/src/org/jitsi/hammer/NetworkMonitor.java
```

After starting the program, the user can directly click the button “apply” using the default value provided by the application. Or to start a customized test, the user can set the parameter as in the figures shown below.
In the test, the audio file is located at: 
```
../resources/narwhals-audio.rtpdump
```
and the video file is located at
```
../resources/narwhals- video.rtpdump
```
Then the application will open system default browser and go to the fake room. 

While the jitsi video conference with fake users are running, you are able to monitor the CPU condition by clicking on button “Server Monitor” on our Java application.
Also you can install Linux package Sysstat and configure it to allow monitor mode. Then with command 
```
$ sar -timeInterval totalDataAmount
```
you should be able to see real-time rolling CPU usage.

