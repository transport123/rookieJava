package JavaBase;


public class Child extends Father{
//子类的构造函数一定会调用父类的构造函数,且一定是第一句,父类的构造函数执行点要比子类代码块中的语句更早;
//由于这个特性,编译器其实总是隐式的在子类的构造函数中调用了一个super();
//所以当我们给父类定义了带参数的构造函数却没有空构造函数时,编译器就无法替我们自动调用super,我们就必须手动自己调用带参数的super(args);
//由于显式的super语句和this语句都要求必须在构造函数体为第一句,所以它们俩无法同时被显式调用;如果父类有空的构造函数那么我们不用显示调用super则此时可以使用this
{
    System.out.println("testChild");
}
    public Child()
    {

        this(10,"");
    }

    public Child(int id,String name)
    {

        //this();
    }

    public void testField()
    {
        this.name = "hello";
        //super.id = 1; 无法访问
    }

}
