# TODO login into hub.docker.com and accept the argument
# base image is based on RHEL 7.7
FROM store/oracle/jdk:11

# (Optional) If china mainland, set timezone using code blow
ENV TimeZone=Asia/Shanghai   
RUN ln -snf /usr/share/zoneinfo/$TimeZone /etc/localtime && echo $TimeZone > /etc/timezone

# (Optional) If you want to debug in container, these commands may gives you a help
RUN yum -y install curl wget telnet unzip net-tools && yum clean all
