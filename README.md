# Jitsi-Hammer-Harness
I.	What we will do (The big picture)?
Implement a tool, connecting to Videobridge and generating maybe hundreds of video streams to run pressure test on the Videobridge. 

II.	Who will use this application?
The target user of this tool is Jitsi Videobridge developer and tester, for they need performance and scalability report of the Videobridge for the server deploy and maintenance. 

III.	What this tool should do?
The tool will first create a video conference and connect to Videobridge, then the user can set parameter for different test scenario:
1.	Create chat room(group), add fake participants
2.	Choose default or open audio/video content for streaming
3.	Select the protocol: TCP/IP or UDP
Then the application should monitor the Videobridge and network metrics, such as:
1.	The CPU usage for M room, N users per room.
2.	How the CPU usage grows when more user join(/drop) the conference
3.	The total amount of data received by Videobridge and bit rate.
4.	The total amount of data sent to Videobridge and bit rate.
5.	Package lost percentage of audio/video
This test tool will generate a user-friendly report for these metrics and present in dashboard. 

IV.	What technology we will use to implement?
Using Java to create the application, and call public API of Jitsi (or Jitsi/Videobridge) to simulate the user join/drop scenario to get the network metrics during the session. We will deploy and run this tool on cloud providers, such as AWS.  
For project development and management, we use agile software development (sprint). For the daily scrum and communication between team members, we use Slack.com. For source code version control, we use gitHub. 
