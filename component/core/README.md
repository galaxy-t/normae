# core

    针对本项目贴合实际业务需要的核心代码包
    
## exception

    全局异常控制
    
1. GlobalException

    > 全局自定义异常 , 手动抛出的异常只能使用该异常 , 其中的 code 必须要从 GlobalExceptionCode 中得到

2. GlobalExceptionCode

    > 全局异常状态码 , 全局自定义异常及返回信息必须包含其中一个枚举 , 全局的异常 code 必须维护到该枚举中

## thread
    
    跟线程有关的对象均维护到该目录下
    
1. CurrentUser

    > 当前访问用户对象 , 每一个 request 请求都属于一个线程 , 发起当次 request 请求的对象的信息会维护到 CurrentUser 中

2. GlobalThreadFactory

    > 全局线程工厂 , 创建多线程必须要依赖线程池 , 在使用线程池的时候必须要求提供线程工厂对象 , 以便轻松的维护全部的线程

## util

### json
    
    Json 有关的工具类均维护到该目录下

1. GsonUtil
    
    > Gson 工具类 , Gson 在新的版本其对象是线程安全的 , 所以该类维护了一个私有的 Gson 对象 , 以便做到复用

## wrapper

1. GlobalResponseWrapper

    > 全局返回值包装类 , 所有的 http 返回值必须为该对象 , 要返回的状态信息和内容都要维护到该类的对象中 , 其中 code 必须要从 GlobalExceptionCode 中得到

