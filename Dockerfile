# 1. OpenJDK 17 기반 이미지 사용
FROM openjdk:17-jdk-alpine

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. Gradle Wrapper 권한 부여
COPY gradlew .
COPY gradle gradle
RUN chmod +x ./gradlew

# 4. 소스 코드 복사
COPY . .

# 5. Gradle 빌드 수행 (테스트 제외)
RUN ./gradlew clean build -x test

# 6. 빌드된 JAR 파일 복사
COPY build/libs/*.jar app.jar

# 7. 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]