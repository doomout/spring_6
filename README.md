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
3. DI 컨테이너
   - 외부에서 처리를 담당하는 스프링의 기능
   - 대량의 객체 생성이나 인젝션을 간결한 코드로 처리해주는 기능
   - Bean: DI 컨테이너가 관리하는 객체
   - Bean 정의: Bean을 정의하는 정보
   - 설정 정보(configuration): DI 컨테이너로 불러올 정보
   - 애플리케이션 컨텍스트: DI 컨테이너의 다른 이름
   - Bean 정의를 작성하는 대표적인 방법 3가지(스테리오타입 애너테이션, @Bean 메서드, <bean>태그)
   