package FlowControl;


import java.util.Random;
import java.util.Scanner;

public class PrintControl {



    public void calculateGrade()
    {
        Scanner scanner =new Scanner(System.in);
        System.out.println("�������ϴγɼ�:");
        float a =scanner.nextFloat();

        System.out.println("�����뱾�γɼ�:");
        float b = scanner.nextFloat();
        float output = 100*(b-a)/a;
        System.out.printf("��ĳɼ�������:%.2f%%\n",output);

    }

    // %������ʾ�����ռλ����

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
    //java 12 ʹ�� case "apple"->��д����ȡ����switch�Ĵ�͸��,��ʹ��дbreakҲ����ִ����������
    //ʹ��lamda��yield������ֵ�ķ���



    public void rocksiccorpaper()
    {
        while (true){

        Random r = new Random();
        int num = Math.abs(r.nextInt()%3);
        Scanner scanner =new Scanner(System.in);
        System.out.println("�����: 1 ���� 2 ʯͷ 3 �� 4 �˳�");
        int player = scanner.nextInt()-1;
        if(player>=3)
            break;

        System.out.printf("�ҳ�%s��%s��",transformToString(num),getResult(player,num));
        }

    }

     String transformToString(int type){

        switch (type){
            case 0:
                return "����";
            case 1:
                return "ʯͷ";
            case 2:
                return "��";
            default:return "����";
        }
    }

    String getResult(int player,int ai){
        int result = player -ai;
        switch (result)
        {
            case 0:
                return "ƽ";
            case 1:
            case -2:
                return "Ӯ";
            case -1:
            case 2:
                return "��";
            default:
                return "Ӯ";
        }
    }
}
