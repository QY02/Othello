package com.rt.othello.ai.evaluate;

public class ReversedEvaluator extends Evaluator{
    private int[][] weights = {
            {100, 25, 25, 25, 25, 25, 25, 100},
            {25, 10, 6, 6, 6, 6, 10, 25},
            {25, 6, 6, 4, 4, 6, 6, 25},
            {25, 6, 4, 0, 0, 4, 6, 25},
            {25, 6, 4, 0, 0, 4, 6, 25},
            {25, 6, 6, 4, 4, 6, 6, 25},
            {25, 10, 6, 6, 6, 6, 10, 25},
            {100, 25, 25, 25, 25, 25, 25, 100}};
    public int evaluate(int[][] chessBoardData, int currentPlayer) {
        int score = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (chessBoardData[i][j] == currentPlayer) {
                    score -= weights[i][j];
                }
            }
        }
        return score;
    }
}
