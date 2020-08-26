# 指定基础镜像，必须为第一个命令
# 格式
#   FROM <image>
#   FROM <image>:<tag>
#   FROM <image>@<digest>
# tag或digest是可选的，如果不使用这两个值时，会使用latest版本的基础镜像
FROM anapsix/alpine-java:8_server-jre_unlimited

# MAINTAINER: 维护者信息
MAINTAINER zhouqi

# 指定于外界交互的端口 , host模式该配置不生效
# EXPOSE 8081

# 用于指定持久化目录
# VOLUME /tmp

# 将本地文件添加到容器中，tar类型文件会自动解压(网络压缩资源不会被解压)，可以访问网络资源，类似wget
ADD /target/*.jar app.jar

#时间差8小时
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

# 配置容器，使其可执行化
#-Xmx300m:堆内存最大300M，-Xms300m：堆内存最小300M,-Xmn100m:新生代区域100m，建议在堆内存大小的1/3或1/4
#-XX:SurvivorRatio=8 新生代区域中，eden区域和Survivor区域占比8:10。
#-XX:MetaspaceSize 元数据空间 默认是20.8M。永久区域数据大小首次到达256M或触发FullGC
#-XX:MaxMetaspaceSize=256m元数据空间最大容量，限制于机器内存，默认没有限制
ENV JAVA_OPTS="\
    -server \
    -Xmx256M \
    -Xms128M \
    -Xmn100M \
    -XX:SurvivorRatio=8 \
    -XX:MetaspaceSize=256M \
    -XX:MaxMetaspaceSize=512M \
    -Dfile.encoding=utf-8"
ENTRYPOINT java ${JAVA_OPTS} -jar /app.jar

# -d -v /root/app/upload:/data -e TZ="Asia/Shanghai" --privileged=true
# --net=host

