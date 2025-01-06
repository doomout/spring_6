## 환경 설정
IDE: IntelliJ IDEA 2024.3.1.1 (Community Edition)  
JDK: openjdk version "17.0.12"  
스프링 부트: 3.2.3  
스프링 프레임워크: 6.1.4  
스프링 시큐리티: 6.2.2  
Thymeleaf: 3.1.2  
JUnit: 5.10.2  

## 스프링 요약
1. 역할 분리
   - Service: 비즈니스 로직 처리만 담당.
   - Repository: 데이터베이스 작업만 담당.

2. 의존성 주입  
   - Service 클래스가 특정 Repository 구현체에 의존하지 않도록 설계했습니다.
   - 다양한 Repository 구현체(Mock 객체, 다른 DB 구현체 등)를 사용할 수 있어 유연합니다.