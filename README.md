# springboot-dev-skeleton

## Environment

+ IDEA 2021.2
+ gradle

## Usage

### Prepare MySQL database
+ Install MySQL database server using docker or binary
+ create database named skeleton: `create database skeleton default charset=utf8mb4 collate utf8mb4_general_ci;`
+ create database user: `grant all privileges on skeleton.* to skeleton@'%' identified by 'password4skeleton';`;

### Start application
+ IDE `com.example.skeleton.SkeletonApplication` to start application
+ Visit [Swagger UI](http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config)

## TIPS

### maven
+ Current gradle need maven repo or proxy using https，[reference](https://support.sonatype.com/hc/en-us/articles/360041287334)