class Problem {
    public static void main(String[] args) {
        String[] operators = new String[]{"+", "-", "*"};
        int i = Integer.parseInt(args[1]);
        int j = Integer.parseInt(args[2]);
        int ans = 0;

        String operator = args[0];
        if (operator.equals("+")) {
            ans = i + j;
            System.out.println(ans);
        } else if (operator.equals("*")) {
            ans = i * j;
            System.out.println(ans);
        } else if (operator.equals("-")) {
            ans = i - j;
            System.out.println(ans);
        } else {
            System.out.println("Unknown operator");
        }
    }
}