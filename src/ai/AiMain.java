package ai;

import ai.searcher.Minimax;
import view.offlineMode.OfflineModeFrame;

public class AiMain implements Runnable{

    private OfflineModeFrame offlineModeFrame;
    private int blackPlayerType;
    private int whitePlayerType;
    private Minimax blackPlayerAi;
    private Minimax whitePlayerAi;

    public AiMain(OfflineModeFrame offlineModeFrame, int blackPlayerType, int whitePlayerType, Minimax blackPlayerAi, Minimax whitePlayerAi) {
        this.offlineModeFrame = offlineModeFrame;
        this.blackPlayerType = blackPlayerType;
        this.whitePlayerType = whitePlayerType;
        this.blackPlayerAi = blackPlayerAi;
        this.whitePlayerAi = whitePlayerAi;
    }
    @Override
    public void run() {
        synchronized (this) {
            while (true) {
                this.offlineModeFrame.getChessBoardPanel().aiMove();
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
