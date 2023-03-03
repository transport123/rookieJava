package JavaBase;

public class Constructor {

    //static 声明的代码块会在其Class对象生成时期调用，也就是类加载时期；
    //其余代码块的本质是一样的，不管是赋值或者别的语句，都会被拼接到外界调用的构造函数的最前部，所以构造函数内部的赋值会覆盖这些外部语句
    //在构造函数内部内部可以通过调用 this()语句来传递调用构造函数,但是this()语句必须放在第一句，且不可以存在循环的构造函数调用


    private int id;
    private String name;

    static {
        System.out.println("static1");

    }

    public Constructor() {
        this(0, "null");


        System.out.println("default constructor top");


        System.out.println("default constructor bottom");

    }


    static {
        System.out.println("static2");

    }

    public Constructor(int id, String name) {

        System.out.println("argument constructor top");

        this.id = id;
        this.name = name;

        System.out.println("argument constructor bottom");

    }


    {

        id = 100;
        System.out.printf("id:%d", id);
        id++;

    }
}
