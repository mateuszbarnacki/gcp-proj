FROM openjdk:17-alpine

ENV APP_FILE gcp-proj.jar
ENV APP_HOME /usr/apps

EXPOSE 9009
COPY target/$APP_FILE $APP_HOME/
WORKDIR $APP_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $APP_FILE"]