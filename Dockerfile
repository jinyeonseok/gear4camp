# OpenJDK 17 기반 이미지 사용
FROM openjdk:17-jdk-alpine

# 작업 디렉토리 설정
WORKDIR /app

# Gradle Wrapper 권한 부여
COPY gradlew ./
COPY gradle gradle
RUN chmod +x ./gradlew

# Gradle을 미리 다운로드 + 캐싱
RUN ./gradlew --version

# 소스 코드 복사
COPY . .

# 빌드된 JAR 파일 복사
COPY build/libs/*.jar app.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]