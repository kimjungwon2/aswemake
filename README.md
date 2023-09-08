# :pushpin: 애즈위메이크 기술 과제

## 1. 사용 기술 스택
 - Spring Boot 2.7.14
 - JPA
 - MySQL 5.7
 - Java 11

## 2. 실행 방법
### 2.1. MySQL
![image](https://github.com/kimjungwon2/aswemake/assets/40010165/68b18332-8711-429b-a80b-76c8eaa5ecb3)
application.yml 파일의 username과 password를 mysql의 계정에 맞게끔 설정해야 합니다.

![image](https://github.com/kimjungwon2/aswemake/assets/40010165/904c2183-0412-45bf-be5f-de75925c7483)
</br>
'aswe' 데이터베이스를 생성해줍니다. ```create database aswe;```

### 2.2. H2 Database(테스터용)
![image](https://github.com/kimjungwon2/aswemake/assets/40010165/77217c53-606f-4909-8e60-944e56a0ada3)
</br>
H2 콘솔을 열고, JDBC URL을 jdbc:h2:tcp://localhost/~/order로 설정합니다.

## 3. ERD
![image](https://github.com/kimjungwon2/aswemake/assets/40010165/9523e5dd-ec2d-46fa-85cc-5f665a68d639)
