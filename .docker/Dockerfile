FROM openjdk:17-oracle as build
WORKDIR /workspace/app

COPY ../mvnw .
COPY ../.mvn .mvn
COPY ../pom.xml .
COPY ../src src

RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../currency-service-0.0.1-SNAPSHOT-spring-boot.jar; jar -xf ../currency-service-0.0.1-SNAPSHOT.jar)

FROM openjdk:17-oracle
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build /workspace/app/target/classes /target/classes
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.currencyservice.CurrencyApplication"]
