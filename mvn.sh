#!bin/bash
mvn install; rm lab-server/target/serverrun.jar; mv lab-server/target/lab-server-1.0-SNAPSHOT-jar-with-dependencies.jar lab-server/target/serverrun.jar;rm lab-client/target/clientrun.jar; mv lab-client/target/lab-client-1.0-SNAPSHOT-jar-with-dependencies.jar lab-client/target/clientrun.jar