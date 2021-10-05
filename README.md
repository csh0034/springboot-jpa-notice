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