package JavaBase;

public interface DefaultInterface {

    //interfaceҲ���Զ��徲̬���������
    //��̬����Ĭ�ϵľ���final����,�����Ա��޸�

    public static final int TYPE_C = 1;

    default void defaultServie() {
        System.out.println("java8 ���Զ���Ĭ�Ϸ�����!");
    }

    //֮ǰȷʵû�ڽӿ��ڶ��徲̬����
    static void dosth() {

    }

    void inheritService();


    //ע��һ���ڲ��ӿ�,��Ϊ�ӿ����޷�ʵ������,������Ĭ��Ϊ��̬,�;�̬�ڲ�����һ����˼
//����ڲ��ӿڵ���Ƴ���,�������ⲿ�ӿڵ�һ���������ԡ�. ʵ�����ⲿ�ӿڵ������ʻ���һ�� ʵ�����ڲ��ӿڵĳ�Ա����
    interface inheritInterface extends DefaultInterface {
        void innerService();
    }
//һ�������ͬʱʵ���ӽӿں͸��ӿ�(��ʵ��������ʵ�����ӽӿ�),������ͬʱ������ͬ�Ľӿ�
}

