FROM eclipse-temurin:18-jdk

WORKDIR /app

COPY .mvn/ .mvn
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
RUN ./mvnw -q -ntp -B -pl back-end-common -am dependency:go-offline
COPY ../../back-end-common back-end-common
RUN ./mvnw -q -ntp -B -pl back-end-common install

RUN ./mvnw -q -ntp -B -pl back-end-practice -am dependency:go-offline
COPY ../../back-end-practice back-end-practice
RUN ./mvnw -q -ntp -B -pl back-end-common,back-end-practice package

CMD ["java", "-jar", "back-end-practice/back-end-practice.jar"]
