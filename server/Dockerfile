FROM openjdk:8-jre-alpine

ADD *.tar.gz /opt/server
WORKDIR /opt/server
ENTRYPOINT ["java", "-cp", "*:lib/*", "unknow.kyhtanil.server.GameWorld"]