package lab2;

import java.util.Scanner;

public class Playground {
    private ABSearch ab;
    private State table;
    private int now = 1;
    public Playground()
    {
        table = new State();
        ab = new ABSearch(-1);//For AI
    }


    public void checkWinner()
    {
        int b = 0, a = 0;
        for (int i = 1; i <= 8; i++)
            for (int j = 1; j <= 8; j++)
                if (table.bigTable[i][j] == -1)
                    a++;
                else if (table.bigTable[i][j] == 1)
                    b++;
        if (b > a)
            System.out.println("Black: "+ b + ", White: " + a + ". User wins!");
        else if (a > b)
            System.out.println("Black: "+ b + ", White: " + a + ". AI wins!");
        else
            System.out.println("Black: "+ b + ", White: " + a + ". Draw!");
    }

    public int[] askUser() {
        int[] inp = new int[2];
        Scanner sc = new Scanner(System.in);
        System.out.print("Input the x position: ");
        inp[0] = sc.nextInt();
        System.out.print("Input the y position: ");
        inp[1] = sc.nextInt();
        return inp;
    }

    public void play()
    {
        int step1, count1 = 0;
        int[] userMove;
        int x=0;
        int y=0;
        System.out.println("User: Black. AI: White.");
        while (true)
        {
            if (now == 1)// user move
            {

                State userState = new State(table);
                userState.printState();
                int[] userChoice = userState.nextSteps(1);
                System.out.print("You can choose:");
                for (int i=1; i<=userChoice[0]; i++) {
//                    System.out.print((userChoice[i]+1) + " ");
                    int stepX = userChoice[i]%8 + 1;
                    int stepY = userChoice[i]/8 + 1;
                    System.out.print("("+stepX+", "+stepY+") ");
                }

                System.out.println();

                if (userChoice[0] == 0) {
                    System.out.println("Nothing! Pass!");
                    step1 = -1;
                } else {
                    userMove = askUser();
                    x = userMove[0];
                    y = userMove[1];
                    step1 = (x-1) * 8 + y-1;
                }

                while (!userState.checkStep(y,x,1)) {//If the move is illegal
                    if (userChoice[0] == 0) {
                        break;
                    }
                    System.out.println("Illegal move!");
                    userMove = askUser();
                    x = userMove[0];
                    y = userMove[1];
                    userState = new State(table);
                }

                userState.set(y, x, 1);
                userState.printState();
                System.out.println();
                if (step1 >= 0)
                {
                    table = new State(userState);
                    count1 = 0;
                }
                else
                {
                    if (count1 > 0 || State.isTerminal(table.getTable()))//
                    {
                        checkWinner();
                        break;
                    }
                    count1++;
                }
                now = -1;
            }
            else if (now == -1)
            {
                ab.table = new State(table);
                step1 = ab.move();
                //System.out.printf("ab: %d\n", step1);
//                ab.table.printState();
//                System.out.println();
                if (step1 >= 0)
                {
                    table = new State(ab.table);
                    count1 = 0;
                }
                else
                {
                    if (count1 > 0 || State.isTerminal(table.getTable()))
                    {
                        checkWinner();
                        break;
                    }
                    count1++;
                }
                now = 1;
            }
        }
    }
}
