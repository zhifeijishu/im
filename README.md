# im
im 消息聊天系统，服务器和android客户端，不依赖第三方，编译可直接运行。
数据库和redis集群需自己配置。单机集群可参考https://www.jianshu.com/p/2a10c63eb360
基于netty实现服务器 ,采用lv定长编码，4个字节表示数据长度
数据采用protocol buffer编码
webrtc实现视频聊天，参考了 https://github.com/taxiao213
turnserver和stunserver 采用开源的coturn，网上有比较多的教程。

数据库为mysql

android客户端写的比较水，仅仅是实现了聊天和视频功能。

目前已具备单人文字和视频聊天

如有疑问可联系
erli30_2019@163.com
如长时间无回复，可联系zhifeijishu@163.com
