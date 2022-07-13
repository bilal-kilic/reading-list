FROM gradle:jdk18-alpine as cache
RUN mkdir -p /home/gradle/cache_home
ENV GRADLE_USER_HOME /home/gradle/cache_home
COPY build.gradle.kts gradle.properties /home/gradle/java-code/
COPY src/main/resources/lib/kompendium-core-1.7.0-SNAPSHOT.jar /home/gradle/java-code/src/main/resources/lib/
WORKDIR /home/gradle/java-code
RUN gradle clean build fatJar

FROM gradle:jdk18-alpine as builder
COPY --from=cache /home/gradle/cache_home /home/gradle/.gradle
COPY .. /usr/src/java-code/
WORKDIR /usr/src/java-code
RUN gradle build fatJar

FROM eclipse-temurin:18.0.1_10-jre-centos7
ARG APP_PORT
EXPOSE $APP_PORT
USER root
COPY --from=builder /usr/src/java-code/support /support/
ENV LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/support/linux/x86_64/libicu/
ENV LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/support/linux/x86_64/libz/
ENV LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/support/linux/x86_64/libc++/
WORKDIR /usr/src/java-app
COPY --from=builder /usr/src/java-code/build/libs/reading-list-0.0.1-all.jar ./app.jar
ENTRYPOINT ["java","-cp", "app.jar", "bilalkilic.com.ApplicationKt"]