[在Java中怎样把数组转换为ArrayList]( https://www.cnblogs.com/liushaobo/p/4423102.html)

最普遍也是被最多人接受的答案如下：  

1 
ArrayList<Element> arrayList = new ArrayList<Element>(Arrays.asList(array));  
  
首先，我们来看下ArrayList的构造方法的文档。   
      ArrayList(Collection < ? extends E > c) : 构造一个包含特定容器的元素的列表，并且根据容器迭代器的顺序返回。     
所以构造方法所做的事情如下：   
  1.将容器c转换为一个数组   
  2.将数组拷贝到ArrayList中称为”elementData”的数组中     
  ArrayList的构造方法的源码如下：   
  ```
      public ArrayList(Collection<? extends E> c) {
             elementData = c.toArray();
             size = elementData.length;
             if (elementData.getClass() != Object[].class)
                   elementData = Arrays.copyOf(elementData, size, Object[].class);
      }
   ```
