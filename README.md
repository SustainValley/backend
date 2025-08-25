## MOCA 

---
#### 파일구조
```angular2html
.
├── Dockerfile
├── README.md
├── build
├── build.gradle
├── gradle
│   └── wrapper
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── gradlew
├── gradlew.bat
├── settings.gradle
├── src
│   ├── main
|   |    ├── java/com/likelion/hackathon/
│   |    |     ├── apiPayload/
│   |    |     ├── config/
│   |    |     ├── controller/
│   |    |     ├── converter/
│   |    |     ├── dto/
│   |    |     ├── entity/
│   |    |     ├── redis/
│   |    |     ├── repository/
│   |    |     ├── security/jwt/
│   |    |     ├── service/
│   |    |     └── HackathonApplication.java
│   |    └── resources
│   |           ├── application-database.yml
│   |           └── application.yml
│   └── test/java/com/likelion/hackathon
└── .gitattributes
└── .gitignore
```

#### 코드 컨벤션
| Commit Type | 설명 |
|-------------|------|
| `feat`      | 기능 추가 |
| `fix`       | 버그 수정 |
| `refactor`  | 리팩토링 |
| `docs`      | 문서 수정 |
| `test`      | 테스트 또는 테스트 코드 추가 |
| `style`     | 코드 포맷팅, 세미콜론 누락, 코드 의미에 영향 없는 변경 |
| `build`     | 빌드 시스템 수정, 외부 종속 라이브러리 수정 (gradle, npm 등) |
| `rename`    | 파일명 또는 폴더명 수정 |
| `remove`    | 코드 또는 파일 삭제 |
| `chore`     | 그 외 자잘한 수정 |
