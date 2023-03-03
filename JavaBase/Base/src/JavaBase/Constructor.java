package JavaBase;

public class Constructor {

    //static �����Ĵ���������Class��������ʱ�ڵ��ã�Ҳ���������ʱ�ڣ�
    //��������ı�����һ���ģ������Ǹ�ֵ���߱����䣬���ᱻƴ�ӵ������õĹ��캯������ǰ�������Թ��캯���ڲ��ĸ�ֵ�Ḳ����Щ�ⲿ���
    //�ڹ��캯���ڲ��ڲ�����ͨ������ this()��������ݵ��ù��캯��,����this()��������ڵ�һ�䣬�Ҳ����Դ���ѭ���Ĺ��캯������


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
