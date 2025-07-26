# 1. Java 런타임이 포함된 베이스 이미지 사용
FROM openjdk:17-jdk-slim

# 2. JAR 파일 복사
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# 3. 포트 오픈 (Spring Boot 기본 8080)
EXPOSE 8080

# 4. 앱 실행
ENTRYPOINT ["java", "-jar", "/app.jar"]