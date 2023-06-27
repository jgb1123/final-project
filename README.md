# final-project
## 프로젝트 목표
* 배달 앱 서비스를 구현해 보며 공부한 내용을 복습하기 위함입니다.
* 기능 구현 뿐만 아니라 테스트 코드에 익숙해지기 위해 노력하며, 단위 테스트를 최대한 많이 작성하여 안정적인 앱을 만드는 것이 목표입니다.
* CI/CD 및 무중단 배포를 구현하여 간편한 배포가 가능하도록 합니다.
* OAuth2를 적용하여 간편한 소셜로그인이 가능하도록 합니다.

## 기술 스택
![기술스택](https://github.com/jgb1123/final-project/assets/104135638/747ec90e-4522-461d-9323-5a6269c84c12)

## 서버 구조도
![서버 구조도](https://github.com/jgb1123/final-project/assets/104135638/11a46abb-4d0d-4af8-bc69-e54f3d5e939f)


## 프로젝트 중점사항
* Spring REST Docs를 이용한 REST API 문서화
* 단위테스트 작성
* AWS EC2, AWS RDS를 활용하여 배포
* Github Actions, AWS CodeDeploy를 통한 CI/CD 구현
* Nginx의 Reverse proxy를 활용해 무중단 배포 구현
* OAuth2 및 JWT를 적용하여 로그인 시스템 구현
* Member의 Role(ADMIN, USER, SELLER)과 Spring Security를 활용한 접근 권한 설정
* 입력 데이터에 대한 유효성 검증
* Querydsl을 활용한 검색 동적쿼리 적용
* AOP 설정을 활용한 글로벌 예외처리
* 무조건적인 연관관계는 지양
  * Item과 Order의 연관관계를 끊어 상품 정보를 수정해도 주문 내역이 수정되지 않도록 구현
  * ADMIN만이 Store를 생성할 수 있고, 각 Store에서는 ADMIN이 생성 시 지정한 SELLER의 MemberId만 갖고 있도록 구현

## DataBase ERD
![스키마](https://github.com/jgb1123/final-project/assets/104135638/28440cfd-eed3-4013-b867-26f2298bb2fe)
