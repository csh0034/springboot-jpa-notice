# spring boot + jpa + security
공지사항 웹 어플리케이션

## 개발 환경
![Generic badge](https://img.shields.io/badge/spring--boot-2.5.5-brightgreen.svg)
![Generic badge](https://img.shields.io/badge/spring--data--jpa-2.5.5-blueviolet.svg)
![Generic badge](https://img.shields.io/badge/querydsl-4.4.0-bbd.svg)  
![Generic badge](https://img.shields.io/badge/h2-1.4.200-blue.svg)
![Generic badge](https://img.shields.io/badge/jdk-11-orange.svg)
![Generic badge](https://img.shields.io/badge/Gradle-7.1.1-yellowgreen.svg)
![Generic badge](https://img.shields.io/badge/intelij-2021.2.2-purple.svg) 

## 요구사항
- 사용자는 텍스트로 된 공지를 추가할 수 있다.
- 사용자는 공지를 수정/삭제할 수 있다.
- 사용자는 공지 목록을 조회할 수 있다.
- 조회시 제목, 작성일, 작성자, 최종 수정일, 내용이 조회 가능하다.
- 목록은 페이징 기능이 있다.
- 공지 등록시 여러개의 첨부 파일을 등록할 수 있다 (선택)
- 로그인 기능(선택)


## 실행 방법
```shell
$ ./gradlew clean bootRun
```

## 테스트 방법
```shell
$ ./gradlew clean test
```

## 빌드 및 jar 실행 방법
```shell
$ ./gradlew clean build
$ java -jar build/libs/springboot-jpa-notice-0.0.1-SNAPSHOT.jar
```

## RestDocs
[http://localhost:8080/docs/index.html](http://localhost:8080/docs/index.html)
> executable jar 실행시에만 접근 가능