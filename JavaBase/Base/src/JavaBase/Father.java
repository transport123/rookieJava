package JavaBase;


public class Father {
    private int id;
    protected String name;

    public Father(int id) {
        System.out.println("father id constructor");
    }

    public Father() {
        System.out.println("father empty constructor");

    }

    {
        System.out.println("testFather");
    }
}
