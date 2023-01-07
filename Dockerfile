FROM openjdk:11

ENV APP_FILE gcp-proj.jar
ENV APP_HOME /usr/apps

EXPOSE 8080
COPY cloudRun/target/$APP_FILE $APP_HOME/
WORKDIR $APP_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $APP_FILE"]