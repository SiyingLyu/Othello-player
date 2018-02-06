package lab2;

import java.awt.*;
import java.util.Vector;

public class State {
    public int[][] bigTable;
    public int bhand = 2, whand = 2, hand = 4;
    private int[] MoveX = {0,  0, 0, -1,  1, -1, 1, -1, 1};// eight directions
    private int[] MoveY = {0, -1, 1, -1, -1,  0, 0,  1, 1};
    public State()
    {
        bigTable = new int[9][];
        for (int i = 0; i <= 8; i++)
        {
            bigTable[i] = new int[9];
            for (int j = 1; j <= 8; j++)
                bigTable[i][j] = 0;
        }
        bigTable[4][4] = -1;
        bigTable[4][5] = 1;
        bigTable[5][4] = 1;
        bigTable[5][5] = -1;
    }
    public State(int[] s)//s.length = 64, convert state into array
    {
        bigTable = new int[9][9];
        for (int i = 1; i <= 8; i++)
            for (int j = 1; j <= 8; j++)
            {
                bigTable[i][j] = s[(i-1) * 8 + j-1];
                if (bigTable[i][j] == 1)
                {
                    bhand++;
                    hand++;
                }
                else if (bigTable[i][j] == -1)
                {
                    whand++;
                    hand++;
                }
            }
    }
    public State(State b)
    {
        bigTable = new int[9][9];
        for (int i = 1; i <= 8; i++)
            for (int j = 1; j <= 8; j++)
                bigTable[i][j] = b.bigTable[i][j];
        hand = b.hand;
        bhand = b.bhand;
        whand = b.whand;
    }

    public static int calcHand(int[] s)
    {
        int total = 0;
        for (int i = 0; i < 64; i++)
            if (s[i] != 0)
                total++;
        return total;
    }

    public static boolean isTerminal(int[] s)
    {
        State b = new State(s);
        if (State.calcHand(s) == 64) return true;
        int[] a1, b1;
        a1 = b.nextSteps(1);
        if (a1[0] > 0) return false;
        b1 = b.nextSteps(-1);
        if (b1[0] > 0) return false;
        return true;
    }

    public int[] nextSteps(int who)//下一步可以落子的所有地方，放在长度为64数组里面
    {
        int[] ans = new int[65];
        for (int i = 0; i < 65; i++)
            ans[i] = 0;
        for (int i = 1; i <= 8; i++)
            for (int j = 1; j <= 8; j++)
                if (checkStep(i, j, who))
                {
                    //if check是否可以落子
                    ans[0]++;//第一位存储的是可以落子的数量
                    ans[ans[0]] = (i - 1) * 8 + j - 1;//
                }
        return ans;
    }

    public boolean checkStep(int x, int y, int who)
    {
        int x1, y1;
        if ((x < 1) || (x > 8) || (y < 1) || (y > 8)) return false;//不可越界
        if (bigTable[x][y] != 0) return false;//这个位置不能有棋子
        // Check周围的8个是不是要翻子
        for (int i = 1; i <= 8; i++)
        {
            x1 = x + MoveX[i];// Move to new position according to the directions
            y1 = y + MoveY[i];
            if ((x1 < 1) || (x1 > 8) || (y1 < 1) || (y1 > 8)) continue;//check这个子是不是在边界上
            if (bigTable[x1][y1] != -who) continue;//如果不是对方的子
            while (bigTable[x1][y1] == -who) //如果是对方的子，就按着这个方向接着往下走
            {
                x1 += MoveX[i];
                y1 += MoveY[i];
                if (((x1 < 1) || (x1 > 8) || (y1 < 1) || (y1 > 8))) break;
                if (bigTable[x1][y1] == 0) break;//如果这条线上没有自己的子，返回不可以落子
                if (bigTable[x1][y1] == who)
                {
                    return true;//如果这条线上有自己的子，那么就返回可以落子
                }
            }
        }
        return false;
    }

    public void set(int x, int y, int who)//实现最后的棋子翻转
    {
        int x1, y1, count = 0;
        for (int i = 1; i <= 8; i++)
        {
            count = 0;
            x1 = x + MoveX[i];
            y1 = y + MoveY[i];
            count++;
            if ((x1 < 1) || (x1 > 8) || (y1 < 1) || (y1 > 8)) continue;
            if (bigTable[x1][y1] != -who) continue;
            while (bigTable[x1][y1] == -who)
            {
                x1 += MoveX[i];
                y1 += MoveY[i];
                count++;
                if ((x1 < 1) || (x1 > 8) || (y1 < 1) || (y1 > 8)) break;
                if (bigTable[x1][y1] == 0) break;
                if (bigTable[x1][y1] == who)
                {
                    for (int j = 1; j <= count - 1; j++)
                    {
                        bigTable[x + j * MoveX[i]][y + j * MoveY[i]] = who;//实现棋子翻转
                        //System.out.printf("%d, %d; %d, %d\n", x, y, x + j * MoveX[i], y + j * MoveY[i]);
                    }
                    break;
                }
            }
        }
        bigTable[x][y] = who;
        hand++;
        if (who == 1)
            bhand++;
        else if (who == -1)
            whand++;
    }

    public static int[] nextState(int[] s1, int action, int hold)
    {
        State s = new State(s1);
        s.set(action / 8 + 1, action % 8 + 1, hold);
        return s.getTable();
    }

    public int[] getTable()
    {
        int[] ans = new int[64];
        for (int i = 1; i <= 8; i++)
            for (int j = 1; j <= 8; j++)
                ans[(i - 1) * 8 + j - 1] = bigTable[i][j];
        return ans;
    }

    public static int[] searchNext(int[] s1, int hold)
    {
        State s = new State(s1);
        return s.nextSteps(hold);
    }

    public void printState() {
        for (int i=1; i<bigTable.length; i++) {
            for (int j=1; j<bigTable[1].length; j++) {
                if (bigTable[i][j] == 0) {
                    System.out.print(". ");
                } else if (bigTable[i][j] == -1) {
                    System.out.print("W ");
                } else {
                    System.out.print("B ");
                }
//                System.out.print(bigTable[i][j] + " ");
            }
            System.out.println();
        }
    }

}
