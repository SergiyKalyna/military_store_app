FROM openjdk:21-slim

ENV MAX_RAM_PERCENTAGE=80 \
  ADDITIONAL_JAVA_FLAGS="" \
  JAVA_ARGS="" \
  DEBUG_ARGS="" \
  APPLICATION_ARGS=""

ADD ./application/build/libs/application.jar /app/application.jar

WORKDIR /app

ENTRYPOINT java \
  -XX:+UseContainerSupport \
  -XX:MaxRAMPercentage=$MAX_RAM_PERCENTAGE \
  $ADDITIONAL_JAVA_FLAGS \
  $JAVA_ARGS \
  $DEBUG_ARGS \
  -jar ./*.jar \
  $APPLICATION_ARGS

