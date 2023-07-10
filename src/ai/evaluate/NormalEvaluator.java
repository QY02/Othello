package ai.evaluate;

public class NormalEvaluator extends Evaluator{
    private int[][] weights = {
            {100, -10, 8, 6, 6, 8, -10, 100},
            {-10, -25, -4, -4, -4, -4, -25, -10},
            {8, -4, 6, 4, 4, 6, -4, 8},
            {6, -4, 4, 0, 0, 4, -4, 6},
            {6, -4, 4, 0, 0, 4, -4, 6},
            {8, -4, 6, 4, 4, 6, -4, 8},
            {-10, -25, -4, -4, -4, -4, -25, -10},
            {100, -10, 8, 6, 6, 8, -10, 100}};
    public int evaluate(int[][] chessBoardData, int currentPlayer) {
        int score = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (chessBoardData[i][j] == currentPlayer) {
                    score += weights[i][j];
                }
                else if (chessBoardData[i][j] == -currentPlayer) {
                    score -= weights[i][j];
                }
            }
        }
        return score;
    }
}
