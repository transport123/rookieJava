package FlowControl;


import java.util.Random;
import java.util.Scanner;

public class PrintControl {



    public void calculateGrade()
    {
        Scanner scanner =new Scanner(System.in);
        System.out.println("请输入上次成绩:");
        float a =scanner.nextFloat();

        System.out.println("请输入本次成绩:");
        float b = scanner.nextFloat();
        float output = 100*(b-a)/a;
        System.out.printf("你的成绩提升了:%.2f%%\n",output);

    }

    // %用来表示输出的占位符号

    public void switchandbreak(){
        int arg = 2;
        switch (arg)
        {
            case 2:
                System.out.println("2");
            case 3:
                System.out.println("3");
                break;


            default:
                System.out.println("default");
                break;

        }
    }
    //java 12 使用 case "apple"->的写法来取消了switch的穿透性,即使不写break也不会执行下面的语句
    //使用lamda和yield来进行值的返回



    public void rocksiccorpaper()
    {
        while (true){

        Random r = new Random();
        int num = Math.abs(r.nextInt()%3);
        Scanner scanner =new Scanner(System.in);
        System.out.println("请出招: 1 剪刀 2 石头 3 布 4 退出");
        int player = scanner.nextInt()-1;
        if(player>=3)
            break;

        System.out.printf("我出%s你%s了",transformToString(num),getResult(player,num));
        }

    }

     String transformToString(int type){

        switch (type){
            case 0:
                return "剪刀";
            case 1:
                return "石头";
            case 2:
                return "布";
            default:return "剪刀";
        }
    }

    String getResult(int player,int ai){
        int result = player -ai;
        switch (result)
        {
            case 0:
                return "平";
            case 1:
            case -2:
                return "赢";
            case -1:
            case 2:
                return "输";
            default:
                return "赢";
        }
    }
}
