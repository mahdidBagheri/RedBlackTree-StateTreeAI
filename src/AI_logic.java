public class AI_logic {
    StateTree stateTree;

    public int getInput(RedBlackTree redBlackTree) {
        this.stateTree = new StateTree(redBlackTree);
        stateTree.createStateTree();
        State state = stateTree.chooseState();

        return state.lastInt;
    }
}

