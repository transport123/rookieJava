package JavaBase;

public interface DefaultInterface {

    //interface也可以定义静态方法与变量
    //静态变量默认的就是final类型,不可以被修改

    public static final int TYPE_C = 1;

    default void defaultServie() {
        System.out.println("java8 可以定义默认方法了!");
    }

    //之前确实没在接口内定义静态方法
    static void dosth() {

    }

    void inheritService();


    //注意一下内部接口,因为接口是无法实例化的,所以其默认为静态,和静态内部类是一个意思
//理解内部接口的设计初衷,是属于外部接口的一个抽象“属性”. 实现了外部接口的类大概率会有一个 实现了内部接口的成员变量
    interface inheritInterface extends DefaultInterface {
        void innerService();
    }
//一个类可以同时实现子接口和父接口(其实就是最终实现了子接口),但不能同时出现相同的接口
}

