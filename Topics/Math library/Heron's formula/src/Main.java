import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        double a = scan.nextDouble(),
                b = scan.nextDouble(),
                c = scan.nextDouble();

        double p = (a + b + c) / 2;
        double sqrt = Math.sqrt(p * (p - a) * (p - b) * (p - c));

        System.out.println((sqrt));
    }
}