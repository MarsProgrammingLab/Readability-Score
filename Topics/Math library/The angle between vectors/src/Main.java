import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        // put your code here
        Scanner scan = new Scanner(System.in);
        int x1, x2, y1, y2;

        x1 = scan.nextInt();
        x2 = scan.nextInt();
        y1 = scan.nextInt();
        y2 = scan.nextInt();

        double val = Math.abs(Math.atan2(x2, x1) - Math.atan2(y2, y1));
        System.out.println(Math.toDegrees(val));
    }
}