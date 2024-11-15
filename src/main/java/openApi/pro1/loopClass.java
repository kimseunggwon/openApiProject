package openApi.pro1;

public class loopClass {

    public static void main(String[] args) {

        int count = 0;
        count = count + 1;
        System.out.println("현재 숫자는 : " + count);

        count = count + 1;
        System.out.println("현재 숫자는 : " + count);

        count = count + 1;
        System.out.println("현재 숫자는 : " + count);

        whileDo();
        System.out.println("============");
        whileDo2();
    }

    public static void whileDo() {
        int count = 0;

        while (count < 3) {
            count++;
            System.out.println("현재 숫자는 : " + count);
        }
    }

    public static void whileDo1() {
        int sum = 0;
        int i = 1;
        int endNum = 3;

        while ( i <= endNum){
            sum = sum + i;
            System.out.println("i = " + i + " sum =" +sum);
            i++;
        }
        //i = 1 , sum =1
        //i = 2 , sum =3
        //i = 3 , sum =6
    }

    public static void whileDo2() {
        int i = 10;
        do {
            System.out.println("현재숫자는 : " + i);
            i++;
        } while (i < 3);

        for (int k =1; k <= 10; k++ ){
            System.out.println(k);
        }
    }

    public static void whileDo3() {

        int count = 1;

        while (count <= 10) {
            System.out.println(count);
            count++;
        }

        for (int k =0; k <= 10; k++ ){
            System.out.println(k);
        }

    }

}
