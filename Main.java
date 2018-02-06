package lab2;

public class Main {

    public static void main(String[] args) {
	// write your code here
//        int[] ans = new int[64];
//        for (int i = 0; i < 64; i++)
//            ans[i] = 0;
//        ans[27] = -1;
//        ans[28] = 1;
//        ans[35] = 1;
//        ans[36] = -1;
//
//        State test = new State(ans);
//        test.printState();
//        System.out.println();
//        test.set(4,3,1);
//        test.printState();
//        System.out.println();
//        int[] avilStep = test.nextSteps(-1);
//        for (int i=0; i<=avilStep.length-1; i++) {
//            System.out.print(avilStep[i] + " ");
//        }
//
//        System.out.println();
//        test.set(3,3,-1);
//        test.printState();
//
//        System.out.println();
//
//       int[][] table = new int[9][];
//        for (int i = 0; i <= 8; i++)
//        {
//            table[i] = new int[9];
//            for (int j = 1; j <= 8; j++)
//                table[i][j] = (i-1) * 8 + j-1;
//        }
//        for (int i=1; i<table.length; i++) {
//            for (int j = 1; j < table[1].length; j++) {
//                System.out.print(table[i][j] + " ");
//            }
//            System.out.println();
//        }
//       ABSearch abSearch = new ABSearch(1);
//       abSearch.move();
        Playground game = new Playground();
        game.play();
    }
}
