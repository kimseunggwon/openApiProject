package openApi.pro1;

import java.util.Scanner;

public class Scope {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("문자열을 입력하세요");



        System.out.println("hello \n");
        System.out.println("ww \n");
    }

    public void scope1() {
        int m = 10; // m 생존 시작

        if (true) {
            int x = 20; // x 생존 시작
            System.out.println("if m = " + m); // 블록 내부에서 블록 외부는 접근 가능
            System.out.println("if x = " + x);
        } // x 생존 종료
        //System.out.println("if x = " + x); // x 죽음
    }

    public static void scan() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("문자열을 입력하세요");

        String str = scanner.nextLine(); // 입력을 String 으로 가져온다
        System.out.println("입력한 문자열 : " + str);
    }

    public static void score() {

        int[] students; // 배열 번수 선언
        students = new int[5]; // 배열 생성 0 ~ 4

        students[0] = 10;
        students[1] = 20;
        students[3] = 30;
        students[4] = 40;
    }

    public void exid() {

        int[] student = {90,80,70,60,50};

        int total = 0;
        for (int i =0; i < student.length; i++){
             total += student[i];
        }

        double average = (double) total / student.length;
        System.out.println("점수 총합 : " + total);
        System.out.println("점수 평균 : " + average);
    }

    public void exid1() {

        int[] number = new int[5];
        for (int a=0;a <= number.length; a++){

        }


    }





}
