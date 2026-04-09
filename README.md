***


# 🚀 Ultimate Hexagonal Multi-Module Architecture

> **"사이드 프로젝트 할때마다 프로젝트 세팅하는거 귀찮아 죽겠다"**
> 철저하게 격리된 비즈니스 로직, 외부 의존성 0%에 수렴하는 헥사고날(Ports and Adapters) 아키텍처 템플릿입니다.

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Hexagonal Architecture](https://img.shields.io/badge/Hexagonal_Architecture-FF4500?style=for-the-badge)

---

## 📦 전체 멀티모듈 조감도

본 프로젝트는 의존성 방향을 한 방향(Core)으로만 강제하기 위해 아래와 같이 모듈을 분리했습니다.


boilerplate-root
├── 📂 module-core           # [중심] 비즈니스 로직의 심장 (프레임워크 의존성 X)
├── 📂 module-infrastructure # [Outbound] DB, 외부 API, Redis 등 하청업체
├── 📂 module-api            # [Inbound] 클라이언트 요청 처리 (REST API, 진입점)
└── 📂 module-batch          # [Inbound] 스프링 배치 환경


---

## 🏗️ 모듈별 심층 가이드

### 1️⃣ `module-core` (방구석 여포, 핵심 도메인)
스프링(Spring) 의존성조차 최소화한 **순수 Java의 성지**입니다. 비즈니스 로직은 오직 이곳에만 존재합니다.
- `domain`: 순수 비즈니스 모델 (⚠️ **JPA Entity 아님**)
- `application/port/in`: 외부에서 Core로 들어오는 UseCase 인터페이스
- `application/port/out`: Core가 외부에 심부름을 시킬 때 쓰는 인터페이스
- `application/service`: UseCase의 실제 구현체 (핵심 비즈니스 로직)

### 2️⃣ `module-infrastructure` (굽신거리는 하청업체, Outbound)
`module-core`에서 정의한 `port/out` 인터페이스의 실제 구현체들이 모여있는 곳입니다. "DB 저장해 줘", "카카오 API 찔러 줘" 같은 궂은일을 전담합니다.
- `persistence`: JPA Entity, Repository, QueryDSL 및 Core 연동 어댑터
- `external`: 소셜 로그인(OAuth) 등 외부 API 통신 (WebClient/Feign)
- `redis`: 토큰 관리 등 Redis 연동

### 3️⃣ `module-api` (얼굴마담 매니저, Inbound)
사용자의 요청을 최초로 맞이하고, 통행증(DTO)을 검사한 뒤 Core의 `port/in`을 호출하는 실행 주체입니다.
- `presentation`: REST API Controller 및 Web DTO
- `security`: JWT 필터, 인증/인가 로직
- `exception`: Global 예외 처리 (`@RestControllerAdvice`)

---

## 🚨 타협 불가 4대 철칙 (이거 어길 거면 MVC 하세요)

### 🛑 철칙 1. 의존성 화살표는 무조건 "안쪽(Core)"을 향한다.
Core는 세상에 자기 혼자만 존재한다고 믿는 '방구석 여포'여야 합니다.
- **❌ 절대 금지:** `module-core` 안에서 `import com.example.infra...` 호출
- **✅ 정답:** API와 Infra 모듈이 Core를 짝사랑해야 합니다.

### 🛑 철칙 2. JPA `@Entity`는 Core에 얼씬도 할 수 없다.
- Core 구역에는 순수 Java 객체인 도메인(`User`)만 존재합니다.
- Infra 구역에 JPA 전용 엔티티(`UserJpaEntity`)를 두고, DB에서 데이터를 꺼낸 뒤 Core 도메인 객체로 **변환(Mapping)**해서 전달해야 합니다.

### 🛑 철칙 3. 나갈 때는 반드시 Port(인터페이스)를 통과해라.
Core가 비즈니스 로직을 처리하다 DB나 외부 API가 필요하면 직접 호출하지 않습니다.
- **Port:** Core 구역에 뚫어놓은 심부름 목록 (Interface)
- **Adapter:** Infra 구역에서 이를 실제로 수행하는 구현체 (Implementation)

### 🛑 철칙 4. 구역별 "통행증(DTO)"은 절대 선을 넘지 않는다.
Web 계층의 `@RequestBody` DTO를 서비스 단까지 끌고 가지 마세요.
- Controller가 Core를 호출할 때는 Web DTO(`LoginRequest`)를 Core 전용 DTO(`LoginCommand`)로 반드시 변환해서 넘깁니다. 배치(Batch)에서 호출하든 웹(Web)에서 호출하든 Core는 모르게 해야 합니다.

---

## 📝 한눈에 보는 네이밍 매핑표 (Cheat Sheet)

개발하다가 이름 짓기 헷갈리면 이 표를 보십시오. (소셜 로그인 도메인 예시)

| 계층 | 역할 | 헥사고날 네이밍 (예시) | (참고) 레거시 MVC 시절 이름 |
| :--- | :--- | :--- | :--- |
| **API** | HTTP 요청 수신 | `SocialLoginController` | `SocialLoginController` |
| **API** | Web DTO | `SocialLoginRequest` | `SocialLoginRequest` |
| **Core** | **Inbound Port** | `SocialLoginUseCase` | `SocialLoginService` (인터페이스) |
| **Core** | Core DTO (통행증) | `LoginCommand` | `SocialLoginDto` |
| **Core** | **도메인 모델** | `User` | - |
| **Core** | UseCase 구현체 | `SocialLoginService` | `SocialLoginServiceImpl` |
| **Core** | **Outbound Port** | `UserPersistencePort` | `UserRepository` (인터페이스) |
| **Infra** | **Outbound Adapter** | `UserPersistenceAdapter` | `UserRepositoryImpl` |
| **Infra** | JPA 엔티티 | `UserJpaEntity` | `User` (Entity) |
| **Infra** | Spring Data JPA | `UserJpaRepository` | `UserRepository` (extends JpaRepo) |

> 자신이 지금 코딩하고 있는 위치가 **Core 안쪽(주방)**인지, **외부(홀/창고)**인지 항상 의식하세요. Core는 "명령(Interface)"만 내리고, 진짜 궂은일(구현체)은 Infra가 알아서 합니다.
```