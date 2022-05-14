# 블로그V1 - 스프링부트 - with mustache

## 황재민
- Blog : https://blog.naver.com/woals990605
- Github : https://github.com/woals990605


## 📽️ 시연영상 [(유튜브 링크)](https://www.youtube.com/watch?v=hfRyNDgZvFg "시연영상")

## 프로젝트 관련 공부 내용 블로그 정리
https://blog.naver.com/woals990605/222679281204


## ⚒️ 기술스택

### Backend
|<img src = "https://blog.kakaocdn.net/dn/cKtAuQ/btrAIO5fzCU/NVWnU8UlhL93kq81Ve87uK/img.png" width="150" height="150" />|
|:--:|
|SpringBoot  |

### Frontend
|<img src = "https://blog.kakaocdn.net/dn/cj5mLL/btrAJSMQt43/yfpTni01hZgrvKHmUdVjk1/img.png" width="150" height="150" />|<img src = "https://blog.kakaocdn.net/dn/eG2w1k/btrAD5NJ1dy/YwmkEkygLgmKevkYNgWiPk/img.png" width="150" height="150" />|<img src = "https://blog.kakaocdn.net/dn/dJtW2R/btrAIfhLlRL/cTJDpEZlRWh9m9QczAkGqK/img.png" width="150" height="150" />|<img src = "https://blog.kakaocdn.net/dn/biJtm8/btrAGfWUCEm/wLv8P9GuJP55PI0AWxOyS1/img.png" width="150" height="150" />|<img src = "https://blog.kakaocdn.net/dn/m3Phc/btrAGgBsKbm/FNYpkhIrVweUUEH4h5tsWK/img.png" width="150" height="150" />|
|:--:|:--:|:--:|:--:|:--:|
|HTML5|CSS|jQuery|Bootstrap|JavaScript|

### 형상관리 도구
|<img src = "https://blog.kakaocdn.net/dn/eyjfrN/btrAKvXV0RA/zkyytdkZy7ESd85knYRDq1/img.png" width="150" height="150" />|<img src = "https://blog.kakaocdn.net/dn/mEK9t/btrAHjxWZX3/iEGILm2rWSrOKsfilmPUA1/img.png" width="150" height="150" />|
|:--:|:--:|
|Git|Github|

### 데이터베이스
|<img src = "https://blog.kakaocdn.net/dn/5J8iY/btrAHiseB54/P1Pkgmigz1aANiQLg0Aip0/img.png" width="150" height="150" />|
|:--:|
|MariaDB|

### 의존성
```java
implementation 'org.springframework.boot:spring-boot-starter-data-jpa' // jpa
implementation 'org.springframework.boot:spring-boot-starter-mustache' // mustache
implementation 'org.springframework.boot:spring-boot-starter-web' // spring web (mvc)
compileOnly 'org.projectlombok:lombok' // lombok
developmentOnly 'org.springframework.boot:spring-boot-devtools' // devtools
runtimeOnly 'org.mariadb.jdbc:mariadb-java-client' // mariaDB
annotationProcessor 'org.projectlombok:lombok'
testImplementation 'org.springframework.boot:spring-boot-starter-test'
```

## 기능 설명



### 👨‍💼유저 관련 기능

- 회원가입 시 유저 네임 중복 체크 (Ajax) 기능
- 로그인 시 쿠키에 username remember 기능
- 로그아웃 기능
- 이메일 변경 기능
- 비밀번호 변경 기능
- 유저네임은 유니크 해서 변경 불가능




### 📄 게시글 관련 기능

- 글 쓰기 (Summernote 라이브러리 사용)
- 글 목록보기 (페이징, 최신 순 정렬)
- 글 상세보기 (게시글 수정, 삭제 권한 체크)
- 글 삭제하기
- 글 수정하기




# 모델링 연관관계

![ERD](https://blog.kakaocdn.net/dn/D17sy/btrBSGyfKyi/DhCcfSPDQ63mCcf3Z9adMK/img.png)