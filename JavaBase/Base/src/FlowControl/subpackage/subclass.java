package FlowControl.subpackage;

//����ʹ��static�ؼ��ֵ��뾲̬������̬��Ա
import static JavaBase.DefaultInterface.TYPE_C;
public class subclass {
    private int id;


    public static class innerclass{
        void changeid()
        {
            subclass subclass = new subclass();
            subclass.id=10;// can access
        }
    }
}
