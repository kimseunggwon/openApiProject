package openApi.pro1;

public class Pro1 {

    public static void main(String[] args) {
        int num1 = 10;
        int num2 = 20;
        int sum = num1 + num2;
        System.out.println(sum);
        //varEx3();
        //operator();
        OperationEx1();

    }

    public static void varEx3(){

        long num = 10000000000L;
        boolean type = true;
        System.out.println(num);
        System.out.println(type);
    }

    public static void operator() {

        int a = 5;
        int b = 2;

        int sum = a + b;
        System.out.println(" a + b = " + sum);

        String s1 ="string1";
        String s2 ="string2";
        String result = s1 + s2;
        System.out.println(result);

        int num = 20;
        String str = " a + b = ";
        String result4 = str + num;
        System.out.println(result4);

    }

    public void operator4() {

        int sum1 = 1 + 2 * 3 ;
        int sum2 = (1 + 2) * 3;
        System.out.println();
    }

    public void operator5() {

        int a = 1;
        int b = 0;
        b = ++a; // a값을 먼저 증가시키고, 그 결과를 b에 대입
        System.out.println(" a = " + a + ", b = " + b);
    }

    /**
     * `num1` , `num2` , `num3` 라는 이름의 세 개의 `int` 변수를 선언하고, 각각 10, 20, 30으로 초기화하세요.
     * 세 변수의 합을 계산하고, 그 결과를 `sum` 이라는 이름의 `int` 변수에 저장하세요.
     * 세 변수의 평균을 계산하고, 그 결과를 `average` 라는 이름의 `int` 변수에 저장하세요. 평균 계산 시 소수점 이 하의 결과는 버림하세요.
     * `sum` 과 `average` 변수의 값을 출력하세요.
     */
    public static void OperationEx1() {

        int num1 = 10;
        int num2 = 20;
        int num3 = 30;

        int sum = num1 + num2 + num3;
        int average = sum / 3;

        System.out.println(sum); //60
        System.out.println(average); //20

        double var1 = 1.5;
        double var2 = 2.5;
        double var3 = 3.5;

        double sum1 = var1 + var2 + var3;
        double average1 = sum1 / 3;

        // score가 80점 이상이고, 100점 이하면 true 출력, 아니면 false
        int score = 80;
        boolean result = score >= 80 && score <= 100;
        System.out.println(result);

    }

    public void if5() {

        int price = 10000; // 아이템 가격
        int age = 10; // 나이
        int discount = 0;

        if (price >= 10000){
            discount = discount + 1000;
            System.out.println("10000원 이상 구매, 1000원 할인");
        }

        //자바  14 새로운 switch문
        int grade = 2;

        int coupon = switch (grade){
            case 1 -> 1000;
            case 2 -> 2000;
            case 3 -> 3000;
            default -> 500;
        };
        System.out.println("발급받은 쿠폰 " + coupon);

        // 삼항 연산자 테스트
        int number = 10;
        String result = (number % 2 == 0) ? "짝수" : "홀수";
        System.out.println(result); // 짝수

        //
        int age2 = 20;
        String newResult = (age2 >= 18) ? "성인" : " 미성년자";
        System.out.println(newResult); // true 면 : 성인 , false : 미성년자

        // 삼항 연산자와 if-else 비교
        int agge = 20;
        String results;
        if (agge >= 18) {
            results = "성인";
        } else {
            results = "미성년자";
        }
    }

    /**
     *  문제: "학점 계산하기"
     * 학생의 점수를 기반으로 학점을 출력하는 자바 프로그램을 작성하자. 다음과 같은 기준을 따른다.
     * 90점 이상: "A"
     * 80점 이상 90점 미만: "B"
     * 70점 이상 80점 미만: "C"
     * 60점 이상 70점 미만: "D"
     * 60점 미만: "F"
     */
    public void ifScore() {

        int score = 85; // 점수 다양하게 변경 가능 85 . 75 . 65. 55

        if (score >= 90) {
            System.out.println(score + "학점은 A입니다");
        } else if (score >= 80) {
            System.out.println(score + "학점은 B입니다");
        }
    }

    public void doWorking() {

        int a = 10;
        int b = 20;

        int max = ( a > b ) ? a : b;

        System.out.println(" 더 큰 숫자는 " + max + " 입니다.");

    }







}
