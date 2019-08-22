转载于 [ vue 生命周期-钩子函数执行过程主要变量的初始化次序](https://www.jianshu.com/p/0d50ea1cef93?utm_source=oschina-app)  
![ vue 生命周期示例图(侵删)](https://cn.vuejs.org/images/lifecycle.png)  

  
html示例： 
```
<div id="container">
  <button @click="changeMsg()">change</button>
  <span>{{msg}}</span>
 </div>
```
js示例：  
```
var vm = new Vue({
  el:'#container', //这里定义的是挂载点，根据它去寻找挂载位置
  data:{
    msg:'TigerChain'
  },
  beforeCreate(){
    console.group("%c%s","color:red",'beforeCreate--实例创建前状态')
    console.log("%c%s","color:blue","el  :"+this.$el)
    console.log("%c%s","color:blue","data  :"+this.$data)
    console.log("%c%s","color:blue","message  :"+this.msg)
  },
  created() {
    console.group("%c%s","color:red",'created--实例创建完成状态')
    console.log("%c%s","color:blue","el  :"+this.$el)
    console.log("%c%s","color:blue","data  :"+this.$data)
    console.log("%c%s","color:blue","message  :"+this.msg)
  },
  beforeMount() {
    console.group("%c%s","color:red",'beforeMount--挂载之前的状态')
    console.log("%c%s","color:blue","el  :"+this.$el)
    console.log(this.$el);
    console.log("%c%s","color:blue","data  :"+this.$data)
    console.log("%c%s","color:blue","message  :"+this.msg)
  },
  mounted() {
    console.group("%c%s","color:red",'mounted--已经挂载的状态')
    console.log("%c%s","color:blue","el  :"+this.$el)
    console.log(this.$el);
    console.log("%c%s","color:blue","data  :"+this.$data)
    console.log("%c%s","color:blue","message  :"+this.msg)
  },
  beforeUpdate(){
    console.group("%c%s","color:red",'beforeUpdate--数据更新前的状态')
    console.log("%c%s","color:blue","el  :"+this.$el.innerHTML)
    console.log(this.$el);
    console.log("%c%s","color:blue","data  :"+this.$data)
    console.log("%c%s","color:blue","message  :"+this.msg)
    console.log("%c%s","color:green","真实的 DOM 结构:"+document.getElementById('container').innerHTML)
  },
  updated() {
    console.group("%c%s","color:red",'updated--数据更新完成时状态')
    console.log("%c%s","color:blue","el  :"+this.$el.innerHTML)
    console.log(this.$el);
    console.log("%c%s","color:blue","data  :"+this.$data)
    console.log("%c%s","color:blue","message  :"+this.msg)
    console.log("%c%s","color:green","真实的 DOM 结构:"+document.getElementById('container').innerHTML)
  },
  activated() {
    console.group("%c%s","color:red",'activated-- keep-alive 组件激活时调用')
    console.log("%c%s","color:blue","el  :"+this.$el)
    console.log(this.$el);
    console.log("%c%s","color:blue","data  :"+this.$data)
    console.log("%c%s","color:blue","message  :"+this.msg)
  },
  deactivated(){
    console.group("%c%s","color:red",'deactivated-- keep-alive 停用时调用')
    console.log("%c%s","color:blue","el  :"+this.$el)
    console.log(this.$el);
    console.log("%c%s","color:blue","data  :"+this.$data)
    console.log("%c%s","color:blue","message  :"+this.msg)
  },
  beforeDestroy() {
    console.group("%c%s","color:red",'beforeDestroy-- vue实例销毁前的状态')
    console.log("%c%s","color:blue","el  :"+this.$el)
    console.log(this.$el);
    console.log("%c%s","color:blue","data  :"+this.$data)
    console.log("%c%s","color:blue","message  :"+this.msg)
  },
  destroyed() {
    console.group("%c%s","color:red",'destroyed-- vue实例销毁完成时调用')
    console.log("%c%s","color:blue","el  :"+this.$el)
    console.log(this.$el);
    console.log("%c%s","color:blue","data  :"+this.$data)
    console.log("%c%s","color:blue","message  :"+this.msg)
  },
  methods: {
    changeMsg() {
      this.msg = 'TigerChain111'
    }
  }
```
##### 代码解释：
```
 1. 生命周期钩子的 this 上下文指向调用它的 Vue 实例,这里是 vm 实例变量
 2. this.msg 指的是 Vue 实例的数据属性对象中的 msg 元素
 3. this.$data  指的是 Vue 实例的整个数据属性对象
 4. this.$el === vm.$el === document.getElementById('container')
 5. 实例中的 data 里的 数据对象 和 method 方法里的 具体方法 都是直接绑定在 vue 的实例上的
```

钩子函数顺序：  
1. beforeCreate  
+ vue 实例创建之前，没有实例，el,data,msg 都没有定义 
2. created  
- 实例创建成功，data,msg 定义 ，el 尚未定义  
3. beforeMount
+ 挂载之前，el 定义，但是其中的数据还只是占位符，未替换为真实的数据  
4. mounted  
- 挂载，替换 beforeMount 中的占位符，并做更多得转换  
5. beforeUpdate  
+ 响应式的数据更新后，视图会进行重渲染，这里虚拟 dom（vm.$el）改变，但是真实的 dom（页面的值）尚未改变  
6. updated  
- 真实 dom 改变  
7. beforeDestroy  
+ Vue 实例是完全可以使用的  
8. destroyed  
- Vue 实例就会解除所有绑定，所有事件被移除，子组件被销毁
  
其他问题：
1. render 函数的优先级 > template 模版 > outerhtml,也就验证上面生命周期图
2. 实例被创建时 data 中存在的属性才是响应式的，这些数据改变，视图会重新渲染，创建之后添加的数据属性改变，不会重新渲染视图
