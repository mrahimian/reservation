FROM openjdk:21-jdk-oracle

WORKDIR /opt/app

COPY target/reservation-*.jar reservation.jar
COPY --chmod=755 start.sh start.sh

CMD ["./start.sh"]
