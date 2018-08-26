###
[总体架构](image/netty总体架构.png)
出自netty权威指南

### 知识点
+ [``网络的粘包以及拆包``](./Netty粘包以及拆包.md)
+ [``线程模型``](./Netty线程处理模型.md)
+ [``事件处理模型``](./Netty事件处理模型.md)
+ [``ByteBuf``](./Netty-ByteBuf.md)
+ [``参数``](./Netty参数.md)

### Q&A
+ ``为什么需要放两个EventLoopGroup``
```
Reactor设置模式上变种模式抽象成为两个参数
性能上的设计，其实给使用者提供了更多的选择
```


+ ``对象的传输``
+ ``服务器断事件处理``
+ ``客户端事件发起``
+ ``消息拼接``

### 参考资料

+ [netty权威指南](http://www.java1234.com/a/javabook/javabase/2016/0122/5570.html)
+ [粘包解决方案1](https://blog.csdn.net/u010853261/article/details/55803933)