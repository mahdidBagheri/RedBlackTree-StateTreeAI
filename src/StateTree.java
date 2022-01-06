import Exceptions.InvalidEntry;

import java.util.LinkedList;

public class StateTree {
    State root;
    LinkedList<State> statesList = new LinkedList();
    //LinkedList<State> statesToBeProcessedList = new LinkedList();

    public StateTree(RedBlackTree redBlackTree){
        this.root = new State(redBlackTree,0,1,0);
        this.root = new State(redBlackTree.Copy(),0,1,0);
        //statesToBeProcessedList.add(this.root);

    }

    public void createStateTree(){

        //nextStep(root,2);
        //processQueue();
        BFS(root);
    }

    private void BFS(State state1) {
        if(state1.redBlackTree.root == null && state1.redBlackTree.checkEndGame()){
            return;
        }
        if(state1.stepsAhead > 3){
            return;
        }

        for (int i = 1; i < 33; i++) {
            int player;
            if(state1.player == 1){
                player = 2;
            }
            else  if(state1.player == 2){
                player = 1;
            }
            else {
                player = 1;
            }

            try {
                State state = new State(state1.redBlackTree.Copy(), i, player, state1.stepsAhead+1);
                state.redBlackTree.INSERT(i,player);
                state.calcWinner();
                state1.addState(state);
            }catch (InvalidEntry e){
                continue;
            }
        }

        for (State subState:state1.nextSteps){
            BFS(subState);
        }

    }

    /*
    private void processQueue() {
        while (!statesToBeProcessedList.isEmpty()) {
            State state1 = statesToBeProcessedList.remove(0);
            statesList.add(state1);
            //System.out.println(state1.redBlackTree.countNodes(state1.redBlackTree.root,0));
            if(  state1.redBlackTree.root != null && state1.redBlackTree.checkEndGame()){
                continue;
            }
            if(state1.stepsAhead > 3){
                continue;
            }

            for (int i = 1; i < 33; i++) {
                int player;
                if (state1.player == 1) {
                    player = 2;
                } else if(state1.player == 2) {
                    player = 1;
                }
                else {
                    player = 1;
                }

                try {
                    State state = new State(state1.redBlackTree.Copy(), i, player, state1.stepsAhead+1);
                    state.redBlackTree.INSERT(i, player);
                    state.calcWinner();
                    state1.addState(state);
                    statesToBeProcessedList.add(state);
                }catch (InvalidEntry e){
                    continue;
                }
            }
        }
    }

     */

    private void nextStep(State state1, int player) {
        if(state1.redBlackTree.root != null) {
            state1.calcWinner();
        }

        for (int i = 1; i < 33; i++) {

            try {
                if(state1.redBlackTree.root != null && state1.redBlackTree.checkEndGame()){
                    continue;
                }

                if(player == 1){
                    player = 2;
                }
                else {
                    player = 2;
                }


                State state = new State(state1.redBlackTree.Copy(),i,player,state1.stepsAhead+1);
                state.redBlackTree.INSERT(i,player);
                state.calcWinner();
                state1.addState(state);


            }catch (InvalidEntry e){
                continue;
            }
        }
        for (State state:state1.nextSteps){
            nextStep(state,state.player);
        }
    }


    public State chooseState() {
        return bestBranch(this.root);
    }

    private State bestBranch(State state){


        int utility = 0;
        long worstWorstScore = 1000000000;
        long bestBestScore = -1000000000;
        long sumSumScore = 0;
        long bestWorstScore = -1000000000;
        long countAllStates = 0;

        long bestMean = -1000000000;
        long worstMean = 1000000000;

        for (State subState:state.nextSteps){
            LinkedList<State> subStateList = new LinkedList();

            subStateList = leaves(subState,subStateList);
            float winCounter = 0;
            long bestScore = 0;
            long worstScore = 0;


            for (State leafState:subStateList){
                countAllStates++;
                if(leafState.scores.player1Score - leafState.scores.player2Score > 0){
                    winCounter++;
                }
                if(bestScore < leafState.scores.player1Score - leafState.scores.player2Score ){
                    bestScore = leafState.scores.player1Score - leafState.scores.player2Score;
                }
                if(worstScore > leafState.scores.player1Score - leafState.scores.player2Score){
                    worstScore = leafState.scores.player1Score - leafState.scores.player2Score;
                }
                if(worstWorstScore > leafState.scores.player1Score - leafState.scores.player2Score){
                    worstWorstScore = leafState.scores.player1Score - leafState.scores.player2Score;
                }
                if(bestBestScore < leafState.scores.player1Score - leafState.scores.player2Score){
                    bestBestScore = leafState.scores.player1Score - leafState.scores.player2Score;
                }
                subState.winScore += leafState.scores.player1Score - leafState.scores.player2Score;
                sumSumScore += leafState.scores.player1Score - leafState.scores.player2Score;
                if(bestWorstScore < worstScore){
                    bestWorstScore = worstScore;
                }
            }
            subState.winProbability = winCounter/subStateList.size();
            subState.meanScore = subState.winScore / subStateList.size();
            subState.worstScore = worstScore;
            subState.bestScore = bestScore;

            if(bestMean < subState.meanScore){
                bestMean = subState.meanScore;
            }

            if(worstMean > subState.meanScore){
                worstMean = subState.meanScore;
            }

        }


        for (State subState:state.nextSteps){
            double normalizedMean;
            double normalizedWorst = 0;

            subState.normalizedWorst = (double)(subState.worstScore - worstWorstScore) / (double)(bestWorstScore - worstWorstScore);

            subState.normalizedMean =  (double)(subState.meanScore - worstMean) /(double)(bestMean - worstMean);

            subState.utility = subState.winProbability + subState.normalizedMean + subState.normalizedWorst;

        }

        double MAX_UTILITY = 0;
        State bestState = state.nextSteps.get(0);
        for (State subState:state.nextSteps){
            if(MAX_UTILITY < subState.utility){
                bestState = subState;
                MAX_UTILITY = subState.utility;
            }
        }
        System.out.println("AI win probability: "+ String.format("%.3f", bestState.winProbability) );
        System.out.println("AI worst case utility: "+ String.format("%.3f", bestState.normalizedWorst) );
        System.out.println("AI mean utility: "+ String.format("%.3f", bestState.normalizedMean) );
        System.out.println("AI utility: " + String.format("%.3f", bestState.utility)  );
        System.out.println("chosen int: " + bestState.lastInt);
        return bestState;

    }

    private LinkedList<State> leaves(State state,LinkedList subStatesList) {
        if(state.nextSteps.size() == 0){
            subStatesList.add(state);
            return subStatesList;
        }
        else {
            for (State subState1:state.nextSteps){
                subStatesList = leaves(subState1,subStatesList);
            }
        }
        return subStatesList;
    }


}

    class State{
    State parent;
    int lastInt;
    int player;
    int stepsAhead;

    long winScore = 0;
    double winProbability = 0;
    long meanScore = 0;
    long worstScore = 0;
    long bestScore = 0;
    double utility = 0;

    double normalizedWorst;
    double normalizedMean;



    RedBlackTree redBlackTree;
    Scores scores;
    LinkedList<State> nextSteps = new LinkedList();
    public State(RedBlackTree redBlackTree, int i, int player, int stepsAhead){
        this.lastInt = i;
        this.redBlackTree = redBlackTree;
        this.player = player;
        this.stepsAhead = stepsAhead;
    }

    public void calcWinner(){
        this.scores = this.redBlackTree.calcWinner(redBlackTree.root, new Scores());
    }

    public void addState(State state){
        this.nextSteps.add(state);
        state.parent = this;
    }
}
