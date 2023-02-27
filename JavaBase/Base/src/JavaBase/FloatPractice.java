package JavaBase;


public class FloatPractice {
    //1,浮点类型只能做加减乘除类的数值形运算,不可以做位运算与移位运算
    //2,由于浮点数也是用2进制表示 如0.5 0.25 等 所以有的小数它无法真正的表达 如0.1 这也就造成了小数的精度问题 即误差
    //所以在判断两个浮点数是否相等会使用Math.abs(a-b) 看这个值是否小于一个很小的值(依据具体精度情况）
    //3，类型提升 当乘除法操作数中有一个浮点数时,该位置的运算结果会自动提升为浮点数,如果都是整数类型则不会提升
    //4,浮点数在除以0时不会报错,会返回NaN 与 + - Infinity
    public void FloatPractice()
    {
        //2
        float a = 0.1f;
        float b = 1.0f-0.9f;

        System.out.println(b);

        if(Math.abs(a-b)<0.0001f)
        {
            System.out.println("equal");
        }

        //3
        a=0.5f+1/0.5f;
        System.out.println(a);
        a=0.5f+3/2;
        System.out.println(a);

    }
    //ax2+bx+c=0求根公式
    //-b+-sqrt(b*b-4ac)/2a
    public void caculateFloat(){
        double a = 1.0;
        double b = 3.0;
        double c = -4.0;
        double sqrt= Math.sqrt(b*b - 4*a*c);
        double r1 = (-b + sqrt)/(2*a);
        double r2 = (-b - sqrt)/(2*a);
        System.out.println(r1);
        System.out.println(r2);

    }
}
