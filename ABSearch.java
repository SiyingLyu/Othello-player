package lab2;

public class ABSearch {
    private double[] value = new double[65];
    public State table;
    public int hold;
    public ABSearch(int h)
    {
        hold = h;
        for (int i = 1; i <= 64; i++)
            value[i] = 0;
        value[1] = 100; value[2] = -20; value[3] = 20; value[4] = 5; value[5] = 5; value[6] = 20; value[7] = -20; value[8] = 100;
        value[9] = -20; value[10] = -40; value[11] = -5; value[12] = -5; value[13] = -5; value[14] = -5; value[15] = -40; value[16] = -20;
        value[17] = 20; value[18] = -5; value[19] = 15; value[20] = 3; value[21] = 3; value[22] = 15; value[23] = -5; value[24] = 20;
        value[25] = 5; value[26] = -5; value[27] = 3; value[28] = 3; value[29] = 3; value[30] = 3; value[31] = -5; value[32] = 5;
        value[33] = 5; value[34] = -5; value[35] = 3; value[36] = 3; value[37] = 3; value[38] = 3; value[39] = -5; value[40] = 5;
        value[41] = 20; value[42] = -5; value[43] = 15; value[44] = 3; value[45] = 3; value[46] = 15; value[47] = -5; value[48] = 20;
        value[49] = -20; value[50] = -40; value[51] = -5; value[52] = -5; value[53] = -5; value[54] = -5; value[55] = -40; value[56] = -20;
        value[57] = 100; value[58] = -20; value[59] = 20; value[60] = 5; value[61] = 5; value[62] = 20; value[63] = -20; value[64] = 100;
    }

    private double max(double a, double b)
    {
        if (a >= b)
            return a;
        return b;
    }

    private double min(double a, double b)
    {
        if (a <= b)
            return a;
        return b;
    }

    private double AlphaBeta(int s, int[] pt, double a, double b, int h)//当前再下一步可能的位置s，当前棋盘pt
    {
        int[] moves = new int[65];
        pt = State.nextState(pt, s, h);//把s放进去以后棋盘的样子，这个是要evaluate的
        moves = State.searchNext(pt, h);//新state的下一步的可能位置
        if (moves[0] == 0)//新state的下一步没有可移动的了，算的就是叶子节点的utility
        {
            double sum = 0;
            int hands = State.calcHand(pt);//计算新state的棋子数量
            for (int i = 0; i < 64; i++)
            {
                if (pt[i] == hold)
                    sum += value[i] + Math.pow(1.2, Math.max(35, hands) - 34) * 0.09;//35 branching factor, effecitive nodes number
                else if (pt[i] == -hold)
                    sum -= value[i] + Math.pow(1.2, Math.max(35, hands) - 34) * 0.09;
            }
            if (State.isTerminal(pt)) sum *= 50;
            return sum;
        }
        if (h == hold)
        {
            for (int i = 1; i <= moves[0]; i++)
            {
                a = max(a, AlphaBeta(moves[i], pt, a, b, -h));
                if (b <= a)
                    break;
                return a;
            }
        }
        else
        {
            for (int i = 1; i <= moves[0]; i++)
            {
                b = min(b, AlphaBeta(moves[i], pt, a, b, -h));
                if (b <= a)
                    break;
                return b;
            }
        }
        return 0.0;
    }

    public int move() {
        int[] steps = new int[65];
        int step = 1;
        double max = Integer.MIN_VALUE, temp;
        int[] s = new int[64];
        int hands = State.calcHand(table.getTable());
        steps = table.nextSteps(hold);//下一步hold可以落子的所有地方，放在长度为64数组里面
        int[] nexts = new int[65];
        if (steps[0] > 0) {
            if (steps[0] == 1)
                step = steps[1];//没得选的情况下，就只有这一个
            else {
                for (int i = 1; i <= 8; i++)
                    for (int j = 1; j <= 8; j++)
                        s[(i - 1) * 8 + j - 1] = table.bigTable[i][j];//把当前的棋盘放到s里面
                for (int i = 1; i <= steps[0]; i++) {
                    temp = AlphaBeta(steps[i], s, Integer.MIN_VALUE, Integer.MAX_VALUE, hold);
                    if (temp > max) {//pruning for minimizer
                        max = temp;
                        nexts[0] = 1;
                        nexts[1] = steps[i];
                    } else if (temp == max) {// no pruning, copy the possible move from steps[] to nexts[]
                        nexts[0]++;
                        nexts[nexts[0]] = steps[i];
                    }
                }
                if (nexts[0] == 1)
                    step = nexts[1];//如果next[]里面只有一种可能性，就这么做移动
                else
                    step = nexts[(int) (Math.random() * nexts[0]) + 1];// otherwise, 在后面的步骤中任意选一个
            }
            table.set(step / 8 + 1, step % 8 + 1, hold);
            return step;
        }
        return -1;
    }
}
