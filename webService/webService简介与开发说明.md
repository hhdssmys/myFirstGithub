[原理理解](https://www.cnblogs.com/pony1223/p/7847323.html)  
[实战代码](https://blog.csdn.net/yhahaha_/article/details/81395397)  

#### 一、 WebService的本质
&emsp;&emsp;一句话：WebService是一种跨语言和跨平台的远程调用技术。  
&emsp;&emsp;所谓跨编程语言和跨操作平台，就是说服务端程序采用java编写，客户端程序则可以采用其他编程语言编写，反之亦然！跨操作系统平台则是指服务端程序和客户端程序可以在不同的操作系统上运行。  
&emsp;&emsp;所谓远程调用，就是一台计算机a上 的一个程序可以调用到另外一台计算机b上的一个对象的方法，譬如，银联提供给商场的pos刷卡系统，商场的POS机转账调用的转账方法的代码其实是跑在银 行服务器上。再比如，amazon，天气预报系统，淘宝网，校内网，百度等把自己的系统服务以webservice服务的形式暴露出来，让第三方网站和程 序可以调用这些服务功能，这样扩展了自己系统的市场占有率，往大的概念上吹，就是所谓的SOA应用。  
&emsp;&emsp;其实可以从多个角度来理解 WebService，从表面上看，WebService就是一个应用程序向外界暴露出一个能通过Web进行调用的API，也就是说能用编程的方法通过 Web来调用这个应用程序。我们把调用这个WebService的应用程序叫做客户端，而把提供这个WebService的应用程序叫做服务端。从深层次 看，WebService是建立可互操作的分布式应用程序的新平台，是一个平台，是一套标准。它定义了应用程序如何在Web上实现互操作性，你可以用任何 你喜欢的语言，在任何你喜欢的平台上写Web service ，只要我们可以通过Web service标准对这些服务进行查询和访问。  
&emsp;&emsp;WebService平台需要一套协议来实现分布式应用程序的创建。任何平台都有它的数据表示方法和类型系统。要实现互操作性，WebService平台 必须提供一套标准的类型系统，用于沟通不同平台、编程语言和组件模型中的不同类型系统。Web service平台必须提供一种标准来描述 Web service，让客户可以得到足够的信息来调用这个Web service。最后，我们还必须有一种方法来对这个Web service进行远 程调用,这种方法实际是一种远程过程调用协议(RPC)。为了达到互操作性，这种RPC协议还必须与平台和编程语言无关。  
  
#### 二、WebService的技术基础  
&emsp;&emsp;XML+XSD,SOAP和WSDL就是构成WebService平台的三大技术。
1. XML+XSD  
&emsp;&emsp;WebService 采用HTTP协议传输数据，采用XML格式封装数据（即XML中说明调用远程服务对象的哪个方法，传递的参数是什么，以及服务对象的 返回结果是什么）。XML是WebService平台中表示数据的格式。除了易于建立和易于分析外，XML主要的优点在于它既是平台无关的，又是厂商无关 的。无关性是比技术优越性更重要的：软件厂商是不会选择一个由竞争对手所发明的技术的。   
&emsp;&emsp;XML解决了数据表示的问题，但它没有定义一套标准的数据类型，更没有说怎么去扩展这套数据类型。例如，整形数到底代表什么？16位，32位，64位？这 些细节对实现互操作性很重要。XML Schema(XSD)就是专门解决这个问题的一套标准。它定义了一套标准的数据类型，并给出了一种语言来扩展这套数据类型。WebService平台就 是用XSD来作为其数据类型系统的。当你用某种语言(如VB.NET或C#)来构造一个Web service时，为了符合WebService标准，所 有你使用的数据类型都必须被转换为XSD类型。你用的工具可能已经自动帮你完成了这个转换，但你很可能会根据你的需要修改一下转换过程。  
2. SOAP  
&emsp;&emsp;WebService通过HTTP协议发送请求和接收结果时，发送的请求内容和结果内容都采用XML格式封装，并增加了一些特定的HTTP消息头，以说明 HTTP消息的内容格式，这些特定的HTTP消息头和XML内容格式就是SOAP协议。SOAP提供了标准的RPC方法来调用Web Service。  
&emsp;&emsp;SOAP协议 = HTTP协议 + XML数据格式  
&emsp;&emsp;SOAP协议定义了SOAP消息的格式，SOAP协议是基于HTTP协议的，SOAP也是基于XML和XSD的，XML是SOAP的数据编码方式。打个比 喻：HTTP就是普通公路，XML就是中间的绿色隔离带和两边的防护栏，SOAP就是普通公路经过加隔离带和防护栏改造过的高速公路。  
3. WSDL  
&emsp;&emsp;好比我们去商店买东西，首先要知道商店里有什么东西可买，然后再来购买，商家的做法就是张贴广告海报。 WebService也一样，WebService客户端要调用一个WebService服务，首先要有知道这个服务的地址在哪，以及这个服务里有什么方 法可以调用，所以，WebService务器端首先要通过一个WSDL文件来说明自己家里有啥服务可以对外调用，服务是什么（服务中有哪些方法，方法接受 的参数是什么，返回值是什么），服务的网络地址用哪个url地址表示，服务通过什么方式来调用。  
&emsp;&emsp;WSDL(Web Services Description Language)就是这样一个基于XML的语言，用于描述Web Service及其函数、参数和返回值。它是WebService客户端和服务器端都 能理解的标准格式。因为是基于XML的，所以WSDL既是机器可阅读的，又是人可阅读的，这将是一个很大的好处。一些最新的开发工具既能根据你的 Web service生成WSDL文档，又能导入WSDL文档，生成调用相应WebService的代理类代码。  
&emsp;&emsp;WSDL 文件保存在Web服务器上，通过一个url地址就可以访问到它。客户端要调用一个WebService服务之前，要知道该服务的WSDL文件的地址。 WebService服务提供商可以通过两种方式来暴露它的WSDL文件地址：1.注册到UDDI服务器，以便被人查找；2.直接告诉给客户端调用者。
#### 三、知识小节  
3.1 WSDL是什么，有什么作用？  
&emsp;&emsp; WSDL 是 web service definition language 的缩写，即 web service 的定义（描述）语言。  
&emsp;&emsp;怎样向别人介绍你的 web service 有什么功能，以及每个函数调用时的参数呢？你可能会自己写一套文档，你甚至可能会口头上告诉需要使用你的web service的人。这些非正式的方法至少都有一个严重的问题：当程序员坐到电脑前，想要使用你的web service的时候，他们的工具（如Visual Studio）无法给他们提供任何帮助，因为这些工具根本就不了解你的web service。  
&emsp;&emsp;解决方法是：用机器能阅读的方式提供一个正式的描述文档。web service描述语言(WSDL)就是这样一个基于XML的语言，描述web service及其函数、参数和返回值。因为是基于XML的，所以WSDL既是机器可阅读的，又是人可阅读的，这将是一个很大的好处。一些最新的开发工具既能根据你的web service生成WSDL文档，又能导入WSDL文档，生成调用相应web ervice的代码。Webservice服务发布之后，通过浏览器访问发布的+?wsdl即可获得wsdl文档。  
3.2 WSDL文档主要有那几部分组成，分别有什么作用？  
&emsp;&emsp;一个WSDL文档的根元素是definitions元素，WSDL文档包含7个重要的元素：types, import, message, portType, operations, binding和service元素。  
&emsp;&emsp;1） definitions元素中一般包括若干个XML命名空间；  
&emsp;&emsp;2） Types元素用作一个容器，定义了自定义的特殊数据类型，在声明消息部分（有效负载）的时候，messages定义使用了types元素中定义的数据类型与元素；  
&emsp;&emsp;3） Import元素可以让当前的文档使用其他WSDL文档中指定命名空间中的定义；  
&emsp;&emsp;4） Message元素描述了Web服务的有效负载。相当于函数调用中的参数和返回值；  
&emsp;&emsp;5） PortType元素定义了Web服务的抽象接口，它可以由一个或者多个operation元素，每个operation元素定义了一个RPC样式或者文档样式的Web服务方法；  
&emsp;&emsp;6） Operation元素要用一个或者多个messages消息来定义它的输入、输出以及错误；  
&emsp;&emsp;7） Binding元素将一个抽象的portType映射到一组具体的协议（SOAP或者HTTP）、消息传递样式（RPC或者document）以及编码样式（literal或者SOAP encoding）；  
&emsp;&emsp;8）Service元素包含一个或者多个Port元素每一个Port元素对应一个不同的Web服务，port将一个URL赋予一个特定的binding，通过location实现。可以使两个或者多个port元素将不同的URL赋给相同的binding。  
3.3说说你知道的webservice框架，他们都有什么特点？  
&emsp;&emsp;Webservice常用框架有JWS、Axis2、XFire以及CXF。下面分别介绍一个这几种WebService框架的基本概念  
&emsp;&emsp;1）JWS是Java语言对WebService服务的一种实现，用来开发和发布服务。而从服务本身的角度来看JWS服务是没有语言界限的。但是Java语言为Java开发者提供便捷发布和调用WebService服务的一种途径。  
&emsp;&emsp;2）Axis2是Apache下的一个重量级WebService框架，准确说它是一个Web Services / SOAP / WSDL 的引擎，是WebService框架的集大成者，它能不但能制作和发布WebService，而且可以生成Java和其他语言版WebService客户端和服务端代码。这是它的优势所在。但是，这也不可避免的导致了Axis2的复杂性，使用过的开发者都知道，它所依赖的包数量和大小都是很惊人的，打包部署发布都比较麻烦，不能很好的与现有应用整合为一体。但是如果你要开发Java之外别的语言客户端，Axis2提供的丰富工具将是你不二的选择。  
&emsp;&emsp;3）XFie是一个高性能的WebService框架，在Java6之前，它的知名度甚至超过了Apache的Axis2，XFire的优点是开发方便，与现有的Web整合很好，可以融为一体，并且开发也很方便。但是对Java之外的语言，没有提供相关的代码工具。XFire后来被Apache收购了，原因是它太优秀了，收购后，随着Java6 JWS的兴起，开源的WebService引擎已经不再被看好，渐渐的都败落了。  
&emsp;&emsp;4) CXF是Apache旗下一个重磅的SOA简易框架，它实现了ESB（企业服务总线）。CXF来自于XFire项目，经过改造后形成的，就像目前的Struts2来自WebWork一样。可以看出XFire的命运会和WebWork的命运一样，最终会淡出人们的视线。CXF不但是一个优秀的Web Services / SOAP / WSDL 引擎，也是一个不错的ESB总线，为SOA的实施提供了一种选择方案，当然他不是最好的，它仅仅实现了SOA架构的一部分。
###### 注：对于Axis2与CXF之间的关系，一个是Axis2出现的时间较早，而CXF的追赶速度快。如何抉择：   
1、如果应用程序需要多语言的支持，Axis2应当是首选了；  
2、如果应用程序是遵循 spring哲学路线的话，Apache CXF是一种更好的选择，特别对嵌入式的Web Services来说；  
3、如果应用程序没有新的特性需要的话，就仍是用原来项目所用的框架，比如 Axis1，XFire，Celtrix或BEA等等厂家自己的Web Services实现，就别劳民伤财了。  
3.4 你的系统中是否有使用到webservice开发，具体是怎么实现的？  
&emsp;&emsp;如果你觉得自己掌握的不够好，对自己不够自信的可以回答为“我的系统中没有使用到webservice的开发，但是我掌握webservice开发的概念和流程”，然后可以给他讲讲相关的概念，也就是上面的这些问题的回答，这样可以绕过这个问题，因为并不是所有的系统都会涉及到webservice的开发。  
&emsp;&emsp;另一种回答即是先给他介绍一种webservice开发框架，比如CXF，然后告诉他你做的是服务端开发还是客户端开发，如果你说你做的事服务端开发，那么你就告诉他怎么定义的webservice，使用了哪些注解，怎么跟spring进行的整合，怎么发布的服务等等；如果你告诉他你做的事客户端的开发，那么你可以告诉他你怎么生成的本地代码，然后又怎么通过本地代码去调用的webservice服务。  
  
四、CXF 的简单实现（服务端+客户端）  
1. 服务端  
##### pom 依赖
```
    <!-- cxf 3剑客 -->
		<dependency>
			<groupId>org.apache.cxf</groupId>
			<artifactId>cxf-rt-frontend-jaxws</artifactId>
			<version>3.1.5</version>
		</dependency>
		<dependency>
			<groupId>org.apache.cxf</groupId>
			<artifactId>cxf-rt-transports-http</artifactId>
			<version>3.1.5</version>
		</dependency>
		<dependency>
			<groupId>org.apache.cxf</groupId>
			<artifactId>cxf-rt-transports-http-jetty</artifactId>
			<version>3.1.5</version>
			<!-- <scope>test</scope> -->
		</dependency>
```
##### 实现代码
```
//创建一个接口，记得加上@WebService注解，表示你要“暴露”的接口（服务类）
@WebService
public interface HelloService {
	public String sayHello(String name);
}
//实现类上可以不添加@Webservice注解
public class HelloServiceImpl implements HelloService{
	@Override
	public String sayHello(String name) {
		return "大家好，我是"+name;
	}
}
```
配置
```
spring.xml

<?xml version="1.0" encoding="UTF-8"?>  
<beans xmlns="http://www.springframework.org/schema/beans"  
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xmlns:context="http://www.springframework.org/schema/context"  
xsi:schemaLocation="http://www.springframework.org/schema/beans
                    http://www.springframework.org/schema/beans/spring-beans-3.0.xsd  
                    http://www.springframework.org/schema/context   
                    http://www.springframework.org/schema/context/spring-context-3.0.xsd">  
 
    <import resource="spring-cxf.xml" />  
 
</beans>


spring-cxf.xml  

<?xml version="1.0" encoding="UTF-8"?>  
<beans xmlns="http://www.springframework.org/schema/beans"  
xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xmlns:context="http://www.springframework.org/schema/context"
xmlns:jaxws="http://cxf.apache.org/jaxws"
xsi:schemaLocation="http://www.springframework.org/schema/beans
                    http://www.springframework.org/schema/beans/spring-beans-3.0.xsd  
                    http://www.springframework.org/schema/context   
                    http://www.springframework.org/schema/context/spring-context-3.0.xsd 
                    http://cxf.apache.org/jaxws  
                    http://cxf.apache.org/schemas/jaxws.xsd
                    " > 
     <!--其中id是自己定义的，implementor是接口实现类，address就是访问的地址 -->
     <!-- 相当于Endpoint.publish("http://localhost:8080/service", newHelloServiceImp()); -->
 
     <jaxws:endpoint id="helloService" implementor="com.gc.cxf.test001.HelloServiceImpl" address="/hello"/>
  
</beans>


web.xml 配置 

<!DOCTYPE web-app PUBLIC "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN" "http://java.sun.com/dtd/web-app_2_3.dtd" >
<web-app>
	<display-name>Archetype Created Web Application</display-name>
  <!-- AOP 容器参数 -->	
  <context-param>
		<param-name>contextConfigLocation</param-name>
		<param-value>classpath:test001/spring.xml</param-value>
	</context-param>
  <!-- AOP 容器 与 tomcat等服务器的启动连接类 -->	
	<listener>
		<listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
	</listener>
  
	<servlet>
		<servlet-name>cxf</servlet-name>
		<servlet-class>org.apache.cxf.transport.servlet.CXFServlet</servlet-class>
		<load-on-startup>1</load-on-startup>
	</servlet>
	<servlet-mapping>
		<servlet-name>cxf</servlet-name>
		<url-pattern>/service/*</url-pattern>
	</servlet-mapping>
  
</web-app>
```
部署至 tomcat ，查看地址 http://localhost:8080/项目名/service 是否成功  

2. 生成客户端
##### 2.1 wsdl2java CXF(bin)提供的根据wsdl生成客户端代码的命令  
在cmd命令中输入：wsdl2java -d 指定代码生成目录 -client webservice的访问地址url  
示例：wsdl2java -d E:\\AllWorkSpace\\MyWork\\TheClient\\src -client http://localhost:8080/Dom4j_AxisDemo/service/hello?wsdl  
注意中间的空格！！！  
具体用法自行百度，这里只对上面的用法做解释：  
-d 指定要产生代码所在目录  
-client 生成客户端测试web service的代码   
##### 2.2 wsimport JDK(bin)提供的生成客户端的命令。  
在cmd命令中输入：wsimport -s 指定代码生成目录 -p 包名 -keep webservice访问地址url   
##### 2.3 利用客户端测试调用
```
public class TestService {
	public static void main(String[] args) {
		HelloService service=new HelloServiceImpService().getHelloServiceImpPort();
		System.out.println(service.sayHello("CXF"));
	}
```
##### 注意：相较于 jdk 转换工具可以指定生成的客户端代码的包名，wsdl2java 在转换过程中默认生产的客户端代码的包路径是按照服务端实现的（或者我还没有看到参数的覆盖写），如果在客户端想实现自己的包路径，必须修改 @WebService中的className为自定义的路径，不然会报错  
```
Caused by: org.apache.cxf.service.factory.ServiceConstructionException
	at org.apache.cxf.jaxb.JAXBDataBinding.initialize(JAXBDataBinding.java:329)
	at org.apache.cxf.service.factory.AbstractServiceFactoryBean.initializeDataBindings(AbstractServiceFactoryBean.java:86)
	at org.apache.cxf.service.factory.ReflectionServiceFactoryBean.buildServiceFromWSDL(ReflectionServiceFactoryBean.java:434)
	at org.apache.cxf.service.factory.ReflectionServiceFactoryBean.initializeServiceModel(ReflectionServiceFactoryBean.java:538)
	at org.apache.cxf.service.factory.ReflectionServiceFactoryBean.create(ReflectionServiceFactoryBean.java:252)
	at org.apache.cxf.jaxws.support.JaxWsServiceFactoryBean.create(JaxWsServiceFactoryBean.java:205)
	at org.apache.cxf.frontend.AbstractWSDLBasedEndpointFactory.createEndpoint(AbstractWSDLBasedEndpointFactory.java:101)
	at org.apache.cxf.frontend.ClientFactoryBean.create(ClientFactoryBean.java:90)
	at org.apache.cxf.frontend.ClientProxyFactoryBean.create(ClientProxyFactoryBean.java:155)
	at org.apache.cxf.jaxws.JaxWsProxyFactoryBean.create(JaxWsProxyFactoryBean.java:156)
	at org.apache.cxf.jaxws.ServiceImpl.createPort(ServiceImpl.java:469)
	at org.jboss.wsf.stack.cxf.client.ProviderImpl$JBossWSServiceImpl.createPort(ProviderImpl.java:484)
	at org.apache.cxf.jaxws.ServiceImpl.getPort(ServiceImpl.java:336)
	... 40 more
Caused by: com.sun.xml.bind.v2.runtime.IllegalAnnotationsException: 6 counts of IllegalAnnotationExceptions
两个类具有相同的 XML 类型名称 "{http://client.ws.logink.org/AuthenService}isTokenValiedResponse"。请使用 @XmlType.name 和 @XmlType.namespace 为类分配不同的名称。
	this problem is related to the following location:
		at com.intelink.logink.authenService.jaxws_asm.IsTokenValiedResponse
	this problem is related to the following location:
		at com.intelink.logink.authenService.IsTokenValiedResponse
		at public com.intelink.logink.authenService.IsTokenValiedResponse com.intelink.logink.authenService.ObjectFactory.createIsTokenValiedResponse()
		at com.intelink.logink.authenService.ObjectFactory
两个类具有相同的 XML 类型名称 "{http://client.ws.logink.org/AuthenService}authenticate"。请使用 @XmlType.name 和 @XmlType.namespace 为类分配不同的名称。
	this problem is related to the following location:
		at com.intelink.logink.authenService.jaxws_asm.Authenticate
	this problem is related to the following location:
		at com.intelink.logink.authenService.Authenticate
		at public javax.xml.bind.JAXBElement com.intelink.logink.authenService.ObjectFactory.createAuthenticate(com.intelink.logink.authenService.Authenticate)
```
































