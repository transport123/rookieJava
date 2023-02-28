package FlowControl.subpackage;

//可以使用static关键字导入静态方法或静态成员
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
