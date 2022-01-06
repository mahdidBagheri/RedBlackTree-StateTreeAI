import Exceptions.InvalidEntry;

public class main {

    public static void main(String[] args) {
        GameView gameView = new GameView();
        RedBlackTree redBlackTree = new RedBlackTree();
        Utils utils = new Utils();
        AI_logic ai_logic = new AI_logic();

        int N1;
        int N2;

        while (true){
            gameView.refreshGameView(redBlackTree);

            N1 = utils.getInput();
            try{
                redBlackTree.INSERT(N1,1);
            } catch (InvalidEntry e1){
                e1.printStackTrace();
                continue;
            }
            gameView.refreshGameView(redBlackTree);
            if(redBlackTree.checkEndGame()){
                String exeMsg = redBlackTree.defineExeMsg(1);
                System.out.println(exeMsg);
                utils.getInput();
                System.exit(0);
            }


            N2 = ai_logic.getInput(redBlackTree.Copy());
            System.out.println(N2);
            try{
                redBlackTree.INSERT(N2,2);
            } catch (InvalidEntry e1){
                e1.printStackTrace();
                continue;
            }
            gameView.refreshGameView(redBlackTree);
            if(redBlackTree.checkEndGame()){
                String exeMsg = redBlackTree.defineExeMsg(1);
                System.out.println(exeMsg);
                System.out.println("Game End. Enter any key ...");
                utils.getInput();

                System.exit(0);
            }

            String exeMsg = redBlackTree.defineExeMsg(1);
            System.out.println(exeMsg);


        }
    }


}
