[转载于 Jackson介绍 ](https://blog.csdn.net/Master_Shifu_/article/details/84194273)  
如何解析JSON:
尽管有很多种拆分JSON并解析的方法，但是可以归纳成3种:

迭代：对事件流（Jackson称之为Token流）的迭代  
数据绑定：将Json数据绑定到对象  
遍历树：构建一个树结构并使用合适的方法遍历它  

对应的Java的API:  
SAX和Stax。这个提供了一些基本的API来遍历事件流。其中，SAX是主动把event推给你(push)，Stax是让你可以主动遍历这个事件流(pull)。一个是push，一个是pull，但是事件流都是一样的，只是表现方式不同。提供事件callback的SAX，主动遍历event的Stax，还有一个是Stax Cursor API（游标）。
JAXB是数据绑定的标准; 虽然有n + 1个替代品（Jibx，XMLBeans，Castor等等），但它们都是这样做的：将（Java）对象转换为xml，反之亦然，其中一些方便而有效，另一些则不如此。
DOM是“最标准的”API，它定义了树结构; 但是与数据绑定一样，还有多种（更好的）替代方案（XOM，JDOM，DOM4j）。您可以逐节点遍历它，也可以使用XPath。
虽然上面说的都是XML的，但是我们讨论的是JSON啊！事实证明，格式不重要，重要的是这种解析的思想。

对应了Jackson提供的三个类:  
核心包（jackson-core）包含JsonParser和JsonGenerator，它们允许迭代令牌(Jackson喜欢说成令牌token，而不是事件event)。  
ObjectMapper实现了数据绑定功能：JSON和Object之间的相互转换。  
TreeMapper是把JSON字符串构造成一个树，其中包含了节点(JsonNode)和子节点(JsonNode)。  

和Jackson 1.x的区别:  
使用Maven作为构建工具，而不是Ant  
注释划分到一个单独的包  
包改成了com.fasterxml.jackson.core(原来是 org.codehaus.jackson)  
