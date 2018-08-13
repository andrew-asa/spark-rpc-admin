源码翻译中的scala知识点

``函数也可以当成变量来进行定义``
```
val startNettyRpcEnv: Int => (NettyRpcEnv, Int) = { actualPort =>
        nettyEnv.startServer(config.bindAddress, actualPort)
        (nettyEnv, nettyEnv.address.port)
}
```
这种就是定义了一个函数