lazy val root = (project in file(".")).
  settings(
	name := "spray-server",
	version := "0.1",
	scalaVersion := "2.11.5",
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-encoding", "utf8"),
    libraryDependencies ++= Seq(
    	"com.typesafe.akka"				%%	"akka-actor"		%	"2.3.9",
    	"com.typesafe.akka"      		%% 	"akka-slf4j"        % 	"2.3.9",
	  	"io.spray"						%% 	"spray-can"         %	"1.3.2",
	  	"io.spray"						%%	"spray-client"      %	"1.3.2",
	  	"io.spray"						%%	"spray-routing" 	%	"1.3.2",
	  	"org.json4s" 					%% 	"json4s-native" 	% 	"3.2.11",
	  	"org.json4s" 					%% 	"json4s-jackson" 	% 	"3.2.11",
	  	"org.apache.httpcomponents" 	% 	"httpclient"	 	% 	"4.4",
    	"commons-logging" 				%	"commons-logging" 	% 	"1.1.1"
    )
  )

