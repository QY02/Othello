package com.rt.othello.ai;

import com.rt.othello.ai.evaluate.Evaluator;
import com.rt.othello.ai.evaluate.NormalEvaluator;
import com.rt.othello.ai.searcher.Minimax;

import java.util.Arrays;

public class MinimaxTest {
    public static void main(String[] args) {
        int[][] chessBoardData = {
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, -1, 1, -1, 0, 0},
                {0, 0, 0, 1, 1, 0, 0, 0},
                {0, 0, 0, 1, 1, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0}};
        Evaluator evaluator = new NormalEvaluator();
        Minimax minimax = new Minimax(evaluator, 10);
        System.out.println(Arrays.toString(minimax.search(chessBoardData, -1, -1, Integer.MIN_VALUE, Integer.MAX_VALUE)));
    }
}
