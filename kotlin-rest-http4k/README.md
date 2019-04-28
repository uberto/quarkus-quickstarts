# Quarkus demo: Kotlin conversion of RESTEasy example


TODO:
 * static page from file for native
 * error page
 * tests
 * persistance
    

to start in dev mode with hotreplacement:

mvn quarkus:dev


to start it without hotreplacement:

java -jar target/kotlin-rest-http4k-1.0-SNAPSHOT-runner.jar 


to create a native binary (graalvm must be installed and pointed by GRAALVM_HOME and JAVA_HOME)

mvn clean package -D native -DskipTests

https://quarkus.io/guides/building-native-image-guide.html




## See the demo in your browser

Navigate to:

<http://localhost:8080/index.html>

Have fun, and join the team of contributors!
