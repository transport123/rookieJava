package JavaBase;


public class Child extends Father{
//����Ĺ��캯��һ������ø���Ĺ��캯��,��һ���ǵ�һ��,����Ĺ��캯��ִ�е�Ҫ�����������е�������;
//�����������,��������ʵ������ʽ��������Ĺ��캯���е�����һ��super();
//���Ե����Ǹ����ඨ���˴������Ĺ��캯��ȴû�пչ��캯��ʱ,���������޷��������Զ�����super,���Ǿͱ����ֶ��Լ����ô�������super(args);
//������ʽ��super����this��䶼Ҫ������ڹ��캯����Ϊ��һ��,�����������޷�ͬʱ����ʽ����;��������пյĹ��캯����ô���ǲ�����ʾ����super���ʱ����ʹ��this
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
        //super.id = 1; �޷�����
    }

}
