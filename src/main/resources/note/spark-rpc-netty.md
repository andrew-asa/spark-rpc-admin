``TransportChannelHandler``
```
netty相关的handler
也仅仅是一个中转站而已真正有意义的是
TransportResponseHandler
和
TransportRequestHandler
```

### TransportRequestHandler
## StreamManager
`` NettyStramManager ``
````
用于管理文件,jar的传输
````
+ 什么时候才会进行添加文件、jar包
+ 为什么没有文件结构
+ 问什么目录需要单独拿出来
+ 所有数据都是放在一个文件夹里面而已，或者是有服务器端自己
进行创建然后进行管理的？但是客户端又是怎样才能
知道这些文件的id呢？
