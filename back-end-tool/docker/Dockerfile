FROM eclipse-temurin:18-jdk

WORKDIR /app

COPY .mvn .mvn
COPY mvnw mvnw
COPY pom.xml pom.xml
COPY back-end-common/pom.xml back-end-common/pom.xml
COPY back-end-practice/pom.xml back-end-practice/pom.xml
COPY back-end-tool/pom.xml back-end-tool/pom.xml

# -q: quiet mode, only show error messages
# -ntp: option to skip the test phase
# -B: batch mode, suppress interactive prompts
# -pl: projects to build specified as a comma-delimited list of simple project identifiers
# -am: build only the dependencies of the specified projects, including transitive dependencies.
# The "go-offline" option ensures that Maven will use the artifacts in the local repository, even if they are not available in the remote repository.
RUN chmod +x mvnw
RUN ./mvnw -pl back-end-common -am dependency:go-offline
COPY back-end-common back-end-common
RUN ./mvnw -pl back-end-common install

RUN ./mvnw -pl back-end-tool -am dependency:go-offline
COPY back-end-tool back-end-tool
RUN ./mvnw -pl back-end-common,back-end-tool package

CMD ["java", "-jar", "back-end-tool/target/back-end-tool.jar"]
