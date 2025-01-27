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
4. 프로파일로 설정 전환하기
   - 프로파일: 설정을 그룹화 하는 DI 컨테이너의 기능
   - 프로파일 사용법: @Profile("프로파일 이름") 을 사용할 구상 클래스에 설정한다.
   - 활성화 할 프로파일 지정법
     - 윈도우  
     환경변수에 set SPRING_PROFILES_ACTIVE=프로파일 이름  
     java main 메서드를 가진 클래스 이름  
     - 리눅스
     export SPRING_PROFILES_ACTIVE=프로파일 이름  
     java main 메서드를 가진 클래스 이름
5. MVC 란?
   - Model: 업무의 로직(Service), 업무 데이터 보유(Entity), 데이터베이스 접근(Repository) 
   - View: 화면을 표시하기 위한 HTML 생성(JSP 등의 템플릿 파일)
   - Controller: 요청에서 응답까지의 전체 흐름 제어
   - 스프링 MVC: MVC 패턴에 따라 웹 애플리케이션을 만들 수 있는 스프링의 기능.
                주로 Controller 프로그램을 만들기 위한 기능 제공.  
                Bean으로 관리 됨으로 Service 등의 의존 객체를 인젝션 하여 사용
    
6. Thymeleaf 란?
   - HTML 문법에 따라 템플릿 파일을 작성할 수 있다.
   - HTML 태크의 속성으로 Thymeleaf 의 구문을 삽입하기 때문에 템플릿 파일을 브라우저에서 직접 열어도 레이아웃이 깨지지 않는다.
```html
<tr th:each="prod:${productList}">
    <td><a th:href="@{/catalog/display-details(productId=${prod.id})}"></td>
    <td><span th:text="${prod.price}"></span>원</td>
</tr>
<tr>
    <th>상품명</th><td><span th:text="${product.name}">상품명01</span></td>
</tr>
    <tr>
        <th>가격</th><td><span th:text="${product.price}">300</span>원</td>
    </tr>
<div th:if="${order != null}">주문 ID「<span th:text="${order.id}"></span>」</div>
<a th:href="@{/order/display-form}">주문 양식으로 돌아가기</a>
```
7. Bean Validation 을 이용한 입력 검사
    - 자바 표준 기술인 Bean Validation 으로 입력 검사를 하는 것이 편리하다
    - 입력 검사 규칙을 애너테이션으로 지정하여 사용
    - 주요 애너테이션
      - @NotNull: 값이 null이 아닌지 확인
      - @NotBlank: 문자열이 null,빈문자(""), 공백문자(" ") 가 아닌지 확인
      - @NotEmpty: 문자열이 null,빈문자("")가 아닌지 확인 공백 문자(" ")는 허용,  
                   List나 Map이 null이거나 크기가 0이 아닌지 확인
      - @Max: 지정한 값 이하의 수치인지 확인  
              (예)@Max(10)은 10 이하인지 확인
      - @Min: 지정한 값 이상의 수치인지 확인   
              (예)@Min(10)은 10 이상인지 확인
      - @Size: 문자열 길이나 List, Map의 요소의 수가 최소 및 최대 범위인지 확인   
              (예) @Size(min=5, max=10)는 5이상이고 10 이하인지 확인
      - @Email: 이메일 주소 형식인지 확인
      - @Pattern: 지정한 정규표현에 맞는지 확인
      - @AssertTrue: Boolean 티압인 필드 혹은 메서드의 반환 값이 True인지 확인. 
8. 주요 HTTP 메서드
    - GET: 리소스를 가져온다.
    - POST: 리소스를 새로 등록한다.
    - PUT: 리소스를 갱신한다.
    - DELETE: 리소스를 삭제한다.
9. 상태 코드 첫째 자리 의미
    - 1xx: 처리중이다.
    - 2xx: 정상적으로 처리가 종료되었다.
    - 3xx: 다른 처리의 호출이 필요하다.
    - 4xx: 클라이언트 측의 오류
    - 5xx: 서버 측의 오류
10. 주요 상태 코드
    - 200(ok): 요청이 정상적으로 처리 되었다.
    - 201(Created): 리소스가 정상적으로 추가 되었다.
    - 204(No Content): 정상 처리되어 응답 바디의 데이터가 비어있다.
    - 400(Bad Request): 요청에 문제가 있어 처리할 수 없었다.
    - 401(Unauthorized): 인증이 필요하다.
    - 403(Forbidden): 권한 부족 등으로 접근이 금지되었다.
    - 404(Not Found): 리소스를 찾을 수 없다.
    - 405(Method Not Allowed): 요청된 HTTP 메서드를 웹 서비스가 지원하지 않는다.
    - 409(Conflict): 다른 처리와 충돌해서 처리할 수 없다.
    - 500(Internal Server Error): 서버 측 처리에서 뭔가 오류가 발생 했다.
    - 503(Service Unavailable): 점검 등으로 서버를 이용할 수 없다.
11. 주요 요청 헤더
    - Accept: 응답 받고 싶은 데이터 형식을 지정한다.
    - Authorization: 인증하기 위한 정보를 지정한다.
    - Content-Type: 요청 바디의 데이터 형식을 지정한다.
12. 주요 응답 헤더
    - Content-Type: 응답 바디의 데이터 형식을 지원한다.
    - Location: 새로 등록한 리소스의 URL 을 지정한다.
13. 스프링 시큐리티 접근 가능한 조건을 지정하는 주요 메서드
    - hasRole: 지정한 권한을 사용자가 가지고 있다.
    - hasAnyRole: 지정한 권한 중 하나를 사용자가 갖고 있다.
    - hasAuthority: 사용자가 지정한 권한을 갖고 있다. 권한 이름이 역할이 아닌 경우 사용
    - hasAnyAuthority: 지정한 권한 중 하나를 사용자가 갖고 있다.
    - permitAll: 무조건 접근 가능
    - denyAll: 무조건 접근 불가
    - isAuthenticated: 인증되었다.(권한은 묻지 않는다.)
    - isAnonymous: 인증되지 않았다(인증되어 있으면 접근 불가)
   