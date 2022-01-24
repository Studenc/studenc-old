FROM openjdk:8-jdk
EXPOSE 6969:6969
RUN mkdir /app
COPY ./build/install/docker/ /app/
WORKDIR /app/bin
CMD ["./docker"]