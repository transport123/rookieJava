# Maven 管理机制

## 依赖管理

1，根据依赖链会将所有的间接依赖都下载下来，不需要每一个都设置

2，**依赖关系**

complie (默认) 编译时需要 compile默认也是runtime的

runtime 编译时不需要，但运行时需要

provided 编译时需要，但运行时由jdk或服务器提供

test 编译test时需要用到(工程下有一个test文件夹是做测试用的，和main文件夹同级)

（思考一下，jar包在编译期间内部其实已经是.class文件了，说明.class也能帮助编译器在编译时通过package申明找到完整的类名,能起到和.java同样的作用)

本质上就是因为编译期间和运行加载期间都有一个 找类的过程，这些依赖关系就是定义了在不同阶段是否需要到 该jar的classpath中去找类 https://segmentfault.com/q/1010000043340482

其中一句话很有意思：**你声明Runtime依赖的时候在编译中是不会将依赖引入ClassPath的，但是打包的时候会** 这表明我们打包的应用在运行加载阶段会去该jar包的classpath寻找相关的类，而provided应该打包时也不会引入该classpath，比如servlet-api，我们编写的并不能算作一个完整的应用，程序运行模块如服务器 是别人编写的，在别人的服务器（JVM）启动时就已经加载了对应的class，并不由我们来指定

## 生命周期

lifecycle包含phase，phase包含goal；

mvn指令后接的是phase，它会自动匹配该phase对应的lifecycle，并从该生命周期开始运行到此phase结束；

如果我们没有在pom中配置这些phase，那么大部分phase其实都是空；

常用的phase：clean compile test package

通常情况下我们都执行phase默认绑定的goal，不需要手动去指定

## 插件

事实上maven本身并不知道phase阶段到底要如何执行，真正的执行是插件控制的，比如compiler插件实现了compile的具体功能，maven只是通过compile这个phase去找到compiler插件并执行其中的goal   compiler:compile

要使用插件就必须得在pom文件中引入插件，插件也由三元id确定唯一性 通过定义phase属性使得插件和phase对应起来；goals内部申明具体的goal。

如果插件还需要具体配置就在execution的configuration中申明



## 模块管理

最需要理解的是 <packaging>标签内的pom与jar的区别，pom表示该模块不包含java源文件，只是一个辅助项目结构的模块，而jar表示该模块是需要真实输出的模块；

子模块可以通过<parent>标签引入 指定父模块pom的内容，避免重复编写相同的内容，父模块可以使用相对路径来指明文件位置。

最顶层的pom文件需要使用<modules>来引入所有模块，且它的packaging属性必须为pom。我在换成jar之后使用mvn命令直接报错

 Aggregator projects require 'pom' as packaging.

因为模块项目 是将一个大型项目 拆分成多个子模块，最外层本身并不再是一个模块。

## mvnw的使用

其实就是一个wrapper，很容易理解。只不过安装起来不像gradlew一样idea集成，需要用命令行安装，每个项目使用自己单独版本的maven版本。

**groupid aitifactid和 包路径没有任何关系，类的包路径如com.soft.xx还是要在src/java文件中自己去定义**