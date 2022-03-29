FROM openjdk:11
EXPOSE 8083
ADD target/spring-service-user.jar user.jar
ENTRYPOINT ["java","-jar","user.jar"]