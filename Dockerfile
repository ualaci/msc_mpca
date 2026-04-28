FROM sbtscala/scala-sbt:eclipse-temurin-jammy-21.0.2_13_1.9.9_3.3.3

WORKDIR /app

COPY build.sbt ./
COPY project ./project

RUN sbt update

COPY . .

RUN sbt compile

ENTRYPOINT ["sbt"]
CMD ["run"]
