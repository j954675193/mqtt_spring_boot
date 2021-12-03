# 关于MQTT



### 1.概述

- **MQTT（Message Queuing Telemetry Transport，消息队列遥测传输协议）**，是一种基于发布/订阅（publish/subscribe）模式的“轻量级”通讯协议，该协议构建与TCP/IP协议上，由IBM在1999年发布。
- MQTT最大优点在于，**用极少的代码和有限的带宽，为连接远程设备提供实时可靠的消息服务**。作为一种**低开销/低带宽占用的即时通讯协议**，使其在物联网、小型设备、移动应用等方面有比较广泛的应用。



### 2.特点

- MQTT是一个基于客户端-服务器的消息发布/订阅传输协议。
- MQTT协议是轻量、简单、开放和易于实现的，这些特点使它使用范围非常广泛。在很多情况下，包括受限的环境中，如：机器与机器（M2M）通信和物联网（IoT）。
- 对负载内容屏蔽的消息传递。比如，在HTTP协议中，需要声明消息的内容格式；而在MQTT中对负载内容进行屏蔽，不关心用户传输什么，是什么格式。
- 使用TCP/IP提供网络连接。
  - 主流MQTT是基于TCP连接进行数据推送，也有基于UDP的版本，MQTT-SN

- 三种消息发布质量（QoS）：
  - QoS 0（至多一次）：这一级别消息会发生丢失或重复
  - QoS 1（最少一次）：QoS 1承诺将消息至少传送一次给订阅者。为防止丢包，消息会发生重复情况
  - QoS2 （只有一次）：保证消息仅传送到目的地一次。采用四次交互，在QoS 1基础上，额外使用了pubrel、pubcomp包能用及时清理msg与msgId，防止消息重发，避免磁盘保存过多msg导致数据过大，实现了msgId可重用。详见：https://blog.csdn.net/reinforce___/article/details/118995889

- 使用Last Will和Testament特性通知有关各方客户端异常中断的机制。
  - Last Will：即遗言机制，用于通知同一主题下的其他设备发送遗言的设备已经断开了连接。
  - Testament：遗嘱机制，功能类似于Last Will。




### 3.MQTT协议实现方式

实现MQTT协议需要客户端和服务器端通讯完成，在通讯过程中，MQTT协议中有三种身份：发布者（Publish）、代理（Broker）（服务器）、订阅者（Subscribe）。其中，消息的发布者和订阅者都是客户端，消息代理是服务器，消息发布者可以同时是订阅者。

MQTT传输的消息分为：主题（Topic）和负载（payload）两部分：

- （1）Topic，可以理解为消息的类型，订阅者订阅（Subscribe）后，就会收到该主题的消息内容（payload）；
- （2）payload，可以理解为消息的内容，是指订阅者具体要使用的内容。



### 4.MQTT的通信模型

MQTT通信通过发布/订阅来实现，**订阅和发布又是基于主题的（Topic）的**。发布方和订阅方通过这种方式进行解耦，互相没有直接连接，它们需要一个中间方。在MQTT里面，我们称之为**Broker，用来进行消息的存储和转发**。

![微信图片_20211118155353](D:\study_notes\study_resources\notes\img\微信图片_20211118155353.png)

1. 发布方连接到broker；
2. 订阅方连接到broker，并订阅主题Topic1；
3. 发布方发给broker一条消息，主题为Topic1；
4. broker收到发布方的消息，发现订阅方订阅了Topic1，然后将消息转发给订阅方；
5. 订阅方从broker接收该消息；

MQTT通过订阅与发布模型对消息的发布方和订阅方进行解耦后，发布方在发布消息时并不需要订阅方也连接到broker，只要订阅方之前订阅过相应主题，那么它在连接到broker之后就可以收到发布方在它离线期间发布的消息（retained）。



该模型中，有两组身份需要区别：

- 一组是发布方publisher和订阅方subscriber
- 另一组是发送方sender和接收方receiver



#### Publisher和Subscriber

publisher和subscriber是相对于Topic来说的身份，如果一个client向某个Topic发布消息，那么这个client就是publisher；如果一个client订阅了某个Topic，那么它就是subscriber。



#### Sender和Receiver

sender和receiver是相对于消息传输方向的身份。当publisher向Broker发送消息时，那么此时publisher是sender，Broker是receiver；当Broker转发消息给subscriber时，此时Broker是sender，subscriber是receiver。



### 5.MQTT Client

Publisher 和 Subscriber 都属于 Client，Pushlisher 或者 Subscriber 只取决于该 Client 当前的状态——是在发布消息还是在订阅消息。当然，一个 Client 可以同时是 Publisher 和 Subscriber。client的范围很广，任何终端、嵌入式设备、服务器只要运行了MQTT的库或者代码，都可以称为MQTT Client。MQTT Client库很多语言都有实现，可以在这个网址中找到：https://github.com/mqtt/mqtt.org/wiki/libraries



### 6.MQTT Broker

MQTT Broker负责接收Publisher的消息，并发送给相应的Subscriber，是整个MQTT 订阅/发布的核心。现在很多云服务器提供商都有提供MQTT 服务，比如阿里云、腾讯云等。当然我们自己也可以搭建一个MQTT Broker，可以使用 mosquitto来搭建自己的MQTT Broker。http://mosquitto.org/



### 7.MQTT协议中的订阅、主题、会话

**一、订阅（Subscription）**

订阅包含主题筛选器（Topic Filter）和最大服务质量（QoS）。订阅会与一个会话（Session）关联。一个会话可以包含多个订阅。每一个会话中的每个订阅都有一个不同的主题筛选器。

**二、会话（Session）**

每个客户端与服务器建立连接后就是一个会话，客户端和服务器之间有状态交互。会话存在于一个网络之间，也可能在客户端和服务器之间跨越多个连续的网络连接。

**三、主题名（Topic Name）**

连接到一个应用程序消息的标签，该标签与服务器的订阅相匹配。服务器会将消息发送给订阅所匹配标签的每个客户端。

**四、主题筛选器（Topic Filter）**

一个对主题名通配符筛选器，在订阅表达式中使用，表示订阅所匹配到的多个主题。

**五、负载（Payload）**

消息订阅者所具体接收的内容。



### 8.MQTT协议数据包

- MQTT 协议数据包的消息格式为：**固定头|可变头|消息体**

  由下面三个部分组成：

  - 固定头（Fixed header）：**存在于所有的MQTT数据包中**，用于表示数据包类型及对应标志、数据包大小等；
  - 可变头（Variable header）：存在于部分类型的MQTT数据包中，具体内容是由相应类型的数据包决定的这些类型的包有：PUBLISH (QoS > 0)、PUBACK、PUBREC、PUBREL、PUBCOMP、SUBSCRIBE、SUBACK、UNSUBSCRIBE、UNSUBACK；
  - 消息体（Payload）：存在于部分的MQTT数据包中，存储消息的具体数据。



# Spring Boot实现MQTT客户端

- mqttconsumer(subsriber)
- mqttprovider(publisher)
