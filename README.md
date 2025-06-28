# commerce
>This project is a **backend base application** that aims for over 80% test coverage and adheres to clean code principles.</br>
It integrates KakaoPay to implement payment functionality and is designed to be extensible for future backend integration and development.

>이 프로젝트는 테스트 커버리지 80% 이상을 목표로 클린 코드를 지향하는 **백엔드 베이스 애플리케이션**입니다.</br>
KakaoPay 서비스를 연동하여 결제 기능을 구현하며, 향후 다양한 백엔드 통합 개발에 확장 가능하도록 설계되었습니다.

<img src="https://img.shields.io/badge/coverage-100%25-blue"> <img src="https://img.shields.io/badge/branches-100%25-green">


</br>

## 주요 특징
- 테스트 중심 개발: JUnit 기반의 단위 테스트를 통해 안정성과 유지보수성을 확보

- 모듈화된 구조: 향후 기능 확장 및 서비스 통합에 용이하도록 구성

- KakaoPay 연동: 실사용 API 기반의 결제 서비스 구현

</br>

## 기술 스택
| <img src="https://img.shields.io/badge/Java_17-007396?style=for-the-badge&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/spring boot_3.4.4-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"> <img src="https://img.shields.io/badge/gradle_8.13-02303A?style=for-the-badge&logo=gradle&logoColor=white"> <img src="https://img.shields.io/badge/mysql_8.0.29-4479A1?style=for-the-badge&logo=mysql&logoColor=white"> <img src="https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white"> <img src="https://img.shields.io/badge/JaCoCo_0.8.8-F7941E?style=for-the-badge">
  | :--- | 


</br>

## 성능 요구사항

</br>

## API 문서
- API 명세는 'doc/commerce-api.yaml' 파일에 정의되어 있습니다.
- 해당 파일은 [Swagger Editor](https://editor.swagger.io/) 도구로 열람 가능합니다.

</br>

## 빌드 및 테스트 방법 
```
# 프로젝트 빌드
./gradlew clean build

# 프로젝트 테스트 실행
./gradlew clean test

# 테스트 커버리지 리포트 확인
./gradlew clean testCoverage
```
- 전체 빌드 및 테스트 수행 </br>
- 테스트 결과: build/test-results/test/ </br>
- 커버리지 리포트: build/reports/jacoco/test/html/index.html
  - 브라우저로 위 index.html 파일을 열면 커버리지 시각화 리포트를 확인할 수 있습니다.

</br>

## 향후 확장 계획
- 프론트엔드 연동: React 또는 Vue 등과의 REST API 기반 연동

- 배포 자동화: CI/CD 및 클라우드 환경 배포 고려 (Docker, GitHub Actions 등)

- 고급 캐싱 기능: Redis를 활용한 세션 관리, 분산 락, Pub/Sub 등의 적용

- 비동기 메시징 시스템: RabbitMQ 또는 Kafka를 통한 비동기 처리 및 마이크로서비스 간 통신 구조 도입

- 인증/인가 모듈 추가: Spring Security, JWT 기반 인증 시스템 구현

- 모니터링 및 로깅: Prometheus + Grafana를 통한 모니터링, ELK 스택을 활용한 로그 수집 및 분석

- 멀티 모듈 구조화: 기능별 모듈 분리로 유지보수성과 확장성 강화

</br>

## 참고 자료
