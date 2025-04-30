FROM openjdk:21
WORKDIR /var/lib/jenkins/workspace/jars
ADD target/springboot-conf-admin-server-0.0.1-SNAPSHOT.jar springboot-conf-admin-server.jar
COPY target/springboot-conf-admin-server-0.0.1-SNAPSHOT.jar springboot-conf-admin-server-0.0.1-SNAPSHOT.jar
EXPOSE 8763
ENTRYPOINT ["java", "-jar", "springboot-conf-admin-server-0.0.1-SNAPSHOT.jar"]