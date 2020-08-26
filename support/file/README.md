# 文件服务

    项目标识: file
    默认端口: 8084

    提供文件上传接口
    本服务基于 MinIO 文件服务器及 JavaSDK 开发完成
    
## MinIO

    一个较为轻量级的分布式文件服务器系统
    可以做到上传一张图片 , 然后返回一个 url 然后可以直接在浏览器打开这个图片
    可以做到上传各种类型的文件并提供下载
    可以做到上传大文件(比如好几个G)
    
    当前代码开发测试版本 MinIO-VERSION: 2020-08-16T18:39:38Z
                     JavaSDK-VERSION: 7.1.0
    
### 安装

    去官网下载一个叫做 minio 的可执行文件 , 作者下载的 Linux 版本 , 运行在 CentOS 7 上
    安装规范 , 创建目录 /usr/local/minio 将 minio 移动到该文件夹下
    启动这里提供了一个脚本 , minio.sh , 将其放在 /usr/local/minio 目录下
    启动: ./minio.sh start
    停止: ./minio.sh stop
    状态: ./minio.sh status
    具体需要使用到的命令可以自行参考 minio.sh 文件
    文件服务器默认端口为 9000
    
#### MinIO 操作脚本 [minio.sh](minio.sh)
    
### 操作

    启动好文件服务器后访问 http://ip:9000 默认端口为 9000 , 若修改过端口需要将地址中的端口修改成正确的端口
    访问该地址会打开文件服务器管理界面 , 可以自行手动创建 bucket 
    文件需要上传到 bucket , 所以文件服务器中至少要存在一个 bucket , 名称自定设置
    文件上传到文件服务器上之后文件的名称为上传时候文件的名称 , 通过 JavaSDK 上传的时候已经设置文件的名称为 uuid , 其实用 MD5 这种摘要签名会更合理一点0
    注: 上传之后的图片如果想直接在浏览器直接打开 , 如现有一个 bucket 的名称为 aaa , 为 aaa 上传了一张 b.jpg 的图片 , 那么我们希望通过地址 http://ip:9000/aaa/b.jpg 可以直接打开图片
        此时默认是打不开这张图片的 , 它会直接跳转到管理界面 , 然后说找不到 b.jpg 这个 bucket , 需要首先设置 aaa 这个 bucket , 点击 Edit policy , 添加前缀为 "*" 后面改为 Read and Write
        这样才能直接打开图片
   
## 使用

    该项目不需要数据库 , 仅作为调用方与文件服务器的中间方来使用
    仅提供一个接口 com.galaxyt.normae.file.web.UploadController.upload(@RequestPart("file") MultipartFile file) 用于文件上传
    返回值中会包含 文件服务器地址(http://ip:port/bucketName),上传上来的文件的原名称(带后缀),文件存放在文件服务器上的名称(带后缀),文件的扩展名(如: ".jpg"),文件上传之后的访问地址(如: http://ip:port/bucketName/aaa.jpg)
    参数使用 @RequestPart 标记 , 以用于 Feign 接口调用
    若需要下载接口需要开发人员自行开发