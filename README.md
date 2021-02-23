Steps of Execution
-----------------------
 
Follow the below steps to compile and run the session reporting module:

 1) Move to path of module where it is downloaded as:

 cd session-reporting
 

 2) Compile source code and put the compiled java files under target folder as below:

javac -d bin .\src\com\bt\assignment\controller\SessionReporting.java .\src\com\bt\assignment\service\SessionReportingService.java
 
 
 3) Run the compiled source code using log file as:

 java -classpath bin com.bt.assignment.controller.SessionReporting application.log
 
