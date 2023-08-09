package com.rt.othello.ai.searcher;

import com.rt.othello.ai.evaluate.Evaluator;

import java.util.ArrayList;
import java.util.Collections;

public class Minimax {
    private Evaluator evaluator;
    private int maxDepth;

    public Minimax(Evaluator evaluator, int maxDepth) {
        this.evaluator = evaluator;
        this.maxDepth = maxDepth;
    }

    public int[] search(int[][] chessBoardData, int currentPlayer, int parentDepth, int alpha, int beta) {
        int maxScore = Integer.MIN_VALUE;
        int maxScoreIndex = 0;
        Node node = new Node();
        node.depth = parentDepth + 1;
        node.chessBoardData = chessBoardData;
        node.alpha = alpha;
        node.beta = beta;
        int currentNodePlayer = currentPlayer;
        if (node.depth % 2 != 0) {
            currentNodePlayer = -currentPlayer;
        }
        node.positions = findValidPositions(chessBoardData, currentNodePlayer);
        if ((node.depth < maxDepth) && (!node.positions.isEmpty())) {
            for (int i = 0; (i < node.positions.size()) && (alpha < beta); i++) {
                int childScore = search(flip(node.chessBoardData, currentNodePlayer, node.positions.get(i)), currentPlayer, node.depth, node.alpha, node.beta)[0];
                if (childScore > maxScore) {
                    maxScore = childScore;
                    maxScoreIndex = i;
                }
                if (node.depth % 2 == 0) {
                    if (childScore > node.alpha) {
                        node.alpha = childScore;
                    }
                }
                else {
                    if (childScore < node.beta) {
                        node.beta = childScore;
                    }
                }
            }
            if (node.depth == 0) {
                return node.positions.get(maxScoreIndex);
            }
            else {
                if (node.depth % 2 == 0) {
                    return new int[]{node.alpha};
                }
                else {
                    return new int[]{node.beta};
                }
            }
        }
        else {
            return new int[]{evaluator.evaluate(chessBoardData, currentPlayer)};
        }
    }

    static class Node {
        int depth;
        int[][] chessBoardData;
        ArrayList<int[]> positions = new ArrayList<>();
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
    }

    public ArrayList<int[]> findValidPositions(int[][] chessBoardData, int currentPlayer) {
        ArrayList<int[]> positions = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                int[][] directions = new int[8][];
                directions[0] = new int[]{0,1};//right
                directions[1] = new int[]{0,-1};//left
                directions[2] = new int[]{1,0};//down
                directions[3] = new int[]{-1,0};//up
                directions[4] = new int[]{1,1};//down and right
                directions[5] = new int[]{-1,-1};//up and left
                directions[6] = new int[]{1,-1};//down and left
                directions[7] = new int[]{-1,1};//up and right
                if(chessBoardData[i][j] == 0 ){
                    for (int k = 0; k < 8; k++) { //test 8 directions
                        int[] weizhi = new int[]{i,j};
                        while ((weizhi[0]<8)&&(weizhi[0]>=0)&&(weizhi[1]<8)&&(weizhi[1]>=0)){
                            weizhi[0] += directions[k][0];
                            weizhi[1] += directions[k][1];
                            if((weizhi[0]>=8)||(weizhi[0]<0)||(weizhi[1]>=8)||(weizhi[1]<0)){
                                break;
                            }
                            else if(chessBoardData[weizhi[0]][weizhi[1]] == 0){
                                break;
                            }
                            else if((chessBoardData[weizhi[0]][weizhi[1]] != 0)&&(chessBoardData[weizhi[0]][weizhi[1]] != currentPlayer)){
                                continue;
                            }
                            else if((chessBoardData[weizhi[0]][weizhi[1]] == currentPlayer)&&((weizhi[0]!=(i+directions[k][0]))||(weizhi[1]!=(j+directions[k][1])))){
                                if(positions.size() != 0){
                                    if((positions.get(positions.size()-1)[0] != i)||(positions.get(positions.size()-1)[1] != j)){
                                        positions.add(new int[]{i,j});
                                    }
                                }
                                else if(positions.size() == 0){
                                    positions.add(new int[]{i,j});
                                }
                                break;
                            }
                            else if((chessBoardData[weizhi[0]][weizhi[1]] == currentPlayer)&&(weizhi[0]==(i+directions[k][0]))&&(weizhi[1]==(j+directions[k][1]))){
                                break;
                            }
                        }
                    }
                }
            }
        }
        Collections.shuffle(positions);
        return positions;
    }

    public int[][] flip(int[][] chessBoardData, int currentPlayer, int[] position) {
        int[][] outputChessBoardData = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                outputChessBoardData[i][j] = chessBoardData[i][j];
            }
        }
        outputChessBoardData[position[0]][position[1]] = currentPlayer;
        int[][] directions = new int[8][];
        directions[0] = new int[]{0,1};//right
        directions[1] = new int[]{0,-1};//left
        directions[2] = new int[]{1,0};//down
        directions[3] = new int[]{-1,0};//up
        directions[4] = new int[]{1,1};//down and right
        directions[5] = new int[]{-1,-1};//up and left
        directions[6] = new int[]{1,-1};//down and left
        directions[7] = new int[]{-1,1};//up and right
        int[] direction_save = new int[2];
        int[] weizhi_save = new int[2];
        for (int i = 0; i < 8; i++) { //test 8 directions
            int[] weizhi = new int[]{position[0], position[1]};
            while ((weizhi[0]<8)&&(weizhi[0]>=0)&&(weizhi[1]<8)&&(weizhi[1]>=0)){
                weizhi[0] += directions[i][0];
                weizhi[1] += directions[i][1];
                if((weizhi[0]>=8)||(weizhi[0]<0)||(weizhi[1]>=8)||(weizhi[1]<0)){
                    break;
                }
                else if(chessBoardData[weizhi[0]][weizhi[1]] == 0){
                    break;
                }
                else if((chessBoardData[weizhi[0]][weizhi[1]] != 0)&&(chessBoardData[weizhi[0]][weizhi[1]] != currentPlayer)){
                    continue;
                }
                else if((chessBoardData[weizhi[0]][weizhi[1]] == currentPlayer)&&((weizhi[0]!=(position[0]+directions[i][0]))||(weizhi[1]!=(position[1]+directions[i][1])))){
                    direction_save = new int[]{directions[i][0],directions[i][1]};
                    weizhi_save = new int[]{weizhi[0],weizhi[1]};
                    int[] weizhi1 = new int[]{position[0]+direction_save[0],position[1]+direction_save[1]};
                    while(!((weizhi1[0] == weizhi_save[0])&&(weizhi1[1] == weizhi_save[1]))){
                        outputChessBoardData[weizhi1[0]][weizhi1[1]] = currentPlayer;
                        weizhi1[0] += direction_save[0];
                        weizhi1[1] += direction_save[1];
                    }
                    break;
                }
                else if((chessBoardData[weizhi[0]][weizhi[1]] == currentPlayer)&&(weizhi[0]==(position[0]+directions[i][0]))&&(weizhi[1]==(position[1]+directions[i][1]))){
                    break;
                }
            }
        }
        return outputChessBoardData;
    }
}
