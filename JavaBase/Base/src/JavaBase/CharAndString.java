package JavaBase;

public class CharAndString {


    public void charUse() {
        //1,一个char占两个字节 保存Unicode字符,所以中英字符都可以存储
        int unicode = 'A';//通过将一个字符赋给int等于将unicode值赋过去
        char unichar = '\u0041';// 通过\\u转译表示一个unicode值 十六进制 4*4 = 16 =两个字节
        char unichar1 = '\'';
        String str = "text\"\\\u0041";//String的转义符号也是\--->text"\A
        System.out.println(str);

        //2,Java13可以使用三个双引号定义String block 不知道为什么idea用不了
        //此idea版本过低,不支持text block

        //String对象为immutable 这是一块很大的内容,今晚可以看一下
    }

    public void stitchChars() {
        int a = 72;
        int b = 105;
        int c = 65281;
        char cc = (char) c;
        // FIXME:
        String s = "" + (char) a + (char) b + (char) c;
        int x = '!';
        System.out.println(s);
        //很诡异 utf编码下 ！ 打不出来,但是数值确实又是65281,可能String和print函数默认做了一些处理
        //换成gbk编码就可以打印出来了
        //核心思路是得把编码值放进char类型的变量中
        //Character.forDigit也是使用强转来实现的,不过它是 '0'+ a 因为这个函数的本意是将9这个数字转换为 '9' 所以通过'0'的编码数加上a来获取a的编码数
    }
}
