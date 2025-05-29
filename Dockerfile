# OpenJDK 17 기반 이미지 사용
FROM openjdk:17-jdk-alpine

# 작업 디렉토리 설정
WORKDIR /app

# Gradle Wrapper 복사 및 실행 권한 부여
COPY gradlew ./gradlew
COPY gradle ./gradle
RUN chmod +x ./gradlew

# 의존성 캐시를 위한 Gradle 설정 파일 복사
COPY build.gradle settings.gradle ./

# 의존성 미리 다운 (캐싱 목적)
RUN ./gradlew dependencies --no-daemon || true

# 전체 소스 코드 복사
COPY . .

# 빌드 및 테스트 수행 → .jar 파일 생성됨
# RUN ./gradlew build --no-daemon --stacktrace --warning-mode all

# ci에서 docker exec로 테스트하기에 Dockerfile에서 test 제외
RUN ./gradlew build -x test --no-daemon --stacktrace --warning-mode all

# 애플리케이션 실행 (이미 위에서 .jar 생성됨)
ENTRYPOINT ["java", "-jar", "build/libs/app.jar"]
