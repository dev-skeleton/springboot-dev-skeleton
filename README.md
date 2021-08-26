# springboot-dev-skeleton

## Environment

+ IDEA 2021.2
+ gradle

## Usage

### Prepare MySQL database
+ Install MySQL database server using docker or binary
+ Create database named skeleton: `create database skeleton default charset=utf8mb4 collate utf8mb4_general_ci;`
+ Create database user: `grant all privileges on skeleton.* to skeleton@'%' identified by 'password4skeleton';`;

### Start application
+ IDE `com.example.skeleton.SkeletonApplication` to start application
+ Visit [Swagger UI](http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config)

### Change to your own

+ settings.gradle => change project name to yours
+ build.gradle => change group of `'com.example'` to yours
+ Use IDE like IDEA's *[Refactor -> Rename]* to rename package from `com.example.skeleton` to yours
+ Modify yaml config at `src/main/resources` to set your database, swagger etc.

## Git commit format

- `feat` Add new features
- `fix` bugfix
- `style` code style, no logic change
- `perf` performance/optimize
- `refactor` refactor
- `revert` revert commit
- `test` test
- `docs` document
- `chore` dependencies or skeleton changes
- `workflow` workflow improvement
- `ci` continue integration
- `types` type define
- `wip` work in progress

## TIPS

### maven
+ Current gradle need maven repo or proxy using https，[reference](https://support.sonatype.com/hc/en-us/articles/360041287334)