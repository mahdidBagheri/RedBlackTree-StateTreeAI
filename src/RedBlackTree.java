import Exceptions.InvalidEntry;

public class RedBlackTree {
    private static final int MAX_HIGHT = 7;
    Node root;


    public void INSERT(int number1, int player) throws InvalidEntry {

        Node searchNode = searchValue(number1);

        if(number1 > 32 || number1 <1){
            //visiualizeTree.visualize();
            throw new InvalidEntry("number out of order");
        }

        if ( searchNode != null && searchNode.value != number1) {
            // ERROR number already chosen and increased
            //visiualizeTree.visualize();
            throw new InvalidEntry("number already chosen and increased");
        }

        else if ( searchNode != null && searchNode.value == number1 && searchNode.player == player) {
            // INCREASE the number
            RBdelete(searchNode);
            refreshHights();
            //visiualizeTree.addTree(this );

            number1 += 32;
            RBinsert(number1, player);
            refreshHights();
            //visiualizeTree.addTree(this);
        }

        else if (searchNode != null && searchNode.player != player) {
            // ERROR number does not belong to this player
            //visiualizeTree.visualize();
            throw new InvalidEntry("number does not belong to this player");
        }

        else if (searchNode == null) {
            // INSERT number
            RBinsert(number1, player);
            refreshHights();
            //visiualizeTree.addTree(this);

        }


    }

    public boolean checkEndGame() {
        refreshHights();
        if(root.hight >= MAX_HIGHT){
            return true;
        }

        if(isUsedAllNumbers()){
            return true;
        }

        return false;
    }

    private boolean isUsedAllNumbers() {
        int N = 0;
        N = countNodes(this.root, N);
        if(N >= 32){
            return true;
        }
        else {
            return false;
        }
    }

    public int countNodes(Node node, int N) {
        if(node == null){
            return N;
        }

        int N1 = countNodes(node.leftChild, N);
        int N2 = countNodes(node.rightChild, N);
        N = N1 + N2 + 1;
        return N;
    }

    public void refreshHights() {
        int h = 0;
        if(this.root != null) {
            h = calcHight(this.root, h);
        }

        }

    private int calcHight(Node node, int h) {
        if(node == null){
            return h;
        }
        int h1 = calcHight(node.rightChild,h);
        int h2 = calcHight(node.leftChild,h);

        node.hight = Math.max(h1,h2) + 1;
        return node.hight;


    }

    public int RBinsert(int x, int player){
        Node x_node = new Node(x, player);
        treeInsert(x_node);
        while (x_node != this.root && x_node.parent.color.equals("red") && x_node.parent.parent != null){
            if(x_node.parent == x_node.parent.parent.leftChild){
                Node y = x_node.parent.parent.rightChild;
                if(y != null && y.color.equals("red")){
                    x_node.parent.color = "black";
                    y.color = "black";
                    x_node.parent.parent.color = "red";
                    x_node = x_node.parent.parent;
                }
                else if((y == null || y.color.equals("black")) && x_node == x_node.parent.rightChild) {
                    x_node = x_node.parent;
                    leftRotate(x_node);
                    x_node.parent.color = "black";
                    x_node.parent.parent.color = "red";
                    rightRotate(x_node.parent.parent);
                }
                else if((y == null || y.color.equals("black")) && x_node == x_node.parent.leftChild){
                    x_node.parent.parent.color = "red";
                    x_node.parent.color = "black";
                    rightRotate(x_node.parent.parent);

                }
            }
            else {
                Node y = x_node.parent.parent.leftChild;
                if(y != null && y.color.equals("red")){
                    x_node.parent.color = "black";
                    y.color = "black";
                    x_node.parent.parent.color = "red";
                    x_node = x_node.parent.parent;
                }
                else if((y == null || y.color.equals("black")) && x_node == x_node.parent.leftChild) {
                    x_node = x_node.parent;
                    rightRotate(x_node);
                    x_node.parent.color = "black";
                    x_node.parent.parent.color = "red";
                    leftRotate(x_node.parent.parent);
                }
                else if((y == null || y.color.equals("black")) && x_node == x_node.parent.rightChild){
                    x_node.parent.parent.color = "red";
                    x_node.parent.color = "black";
                    leftRotate(x_node.parent.parent);
                }
            }
        }
        this.root.color = "black";

        return 1;
    }

    public void leftRotate(Node x){
        Node y = x.rightChild;

        x.rightChild = y.leftChild;


        if(y.leftChild != null){
            y.leftChild.parent = x;
        }
        y.parent = x.parent;
        if(x.parent == null){
            this.root = y;
        }
        else if(x == x.parent.leftChild){
            x.parent.leftChild = y;
        }
        else {
            x.parent.rightChild = y;
        }
        y.leftChild = x;
        x.parent = y;
    }

    public void rightRotate(Node y){
        Node x = y.leftChild;
        y.leftChild = x.rightChild;
        if(x.rightChild != null){
            x.rightChild.parent = y;
        }
        x.parent = y.parent;
        if(y.parent == null){
            this.root = x;
        }
        else if(y == y.parent.leftChild){
            y.parent.leftChild = x;
        }
        else {
            y.parent.rightChild = x;
        }
        x.rightChild = y;
        y.parent = x;
    }

    public void treeInsert(Node x){
        if(this.root == null){

            this.root = x;
            return;
        }
        else {
            Node iterNode = this.root;
            Boolean isInserted = false;

            while (!isInserted){
                if(iterNode.leftChild != null && x.value < iterNode.value){
                    iterNode = iterNode.leftChild;
                    continue;
                }
                else if(iterNode.rightChild != null && x.value > iterNode.value){
                    iterNode = iterNode.rightChild;
                    continue;
                }
                else if(iterNode.leftChild == null && x.value < iterNode.value){
                    iterNode.putLeftChild(x);
                    isInserted = true;
                    return;
                }
                else if(iterNode.rightChild == null && x.value > iterNode.value){
                    iterNode.putRightChild(x);
                    isInserted = true;
                    return;
                }
            }

        }
    }

    public Node RBdelete(Node z){
        if(z == this.root && countNodes(root,0) == 1){
            root = null;
            return z;
        }
        Node y;
        Node x = null;
        if(z.leftChild == null || z.rightChild == null){
            y = z;
        }
        else {
            y = treeSuccessor(z);
        }

        if(y.leftChild != null){
            x = y.leftChild;
        }
        else if(y.rightChild != null) {
            x = y.rightChild;
        }
        if(x == null) {
            x = new Node(true);
            y.rightChild = x;
            x.parent = y;
        }

        x.parent = y.parent;
        if(y.parent == null && x.isNil == false){
            this.root = x;
        }
        else if(y == y.parent.leftChild){
            y.parent.leftChild = x;
        }
        else {
            y.parent.rightChild = x;
        }

        if(y != z){
            z.value = y.value;
            z.player = y.player;
        }

        Node x_copy = x;

        if(y.color.equals("black")){
            RBdeleteFixup(x);
        }

        if(x_copy.isNil){
            if(x_copy.parent.leftChild == x_copy){
                x_copy.parent.leftChild = null;
            }
            else if(x_copy.parent.rightChild == x_copy){
                x_copy.parent.rightChild = null;
            }
        }
        return y;
    }

    private void RBdeleteFixup(Node x) {
        Node w;
        while (x != this.root && x.color.equals("black")){
            if(x == x.parent.leftChild){
                w = x.parent.rightChild;
                if(w == null){
                    x = x.parent;
                    continue;
                }
                if(w.color.equals("red")){
                    w.color = "black";
                    x.parent.color = "red";
                    leftRotate(x.parent);
                    w = x.parent.rightChild;
                }
                if((w.rightChild == null || w.rightChild.color.equals("black")) && (w.leftChild == null || w.leftChild.color.equals("black"))){
                    w.color = "red";
                    x = x.parent;
                }
                else if(w.leftChild != null && w.leftChild.color.equals("red") && (w.rightChild == null || w.rightChild.color.equals("black"))){

                    w.leftChild.color = "black";
                    w.color = "black";
                    rightRotate(w);
                    w = x.parent.rightChild;
                    w.color = x.parent.color;
                    x.parent.color = "black";
                    leftRotate(x.parent);
                    break;
                }
                else if(w.rightChild == null || w.rightChild.color.equals("black")){

                    w.leftChild.color = "black";
                    w.color = "red";
                    rightRotate(w);
                    w = x.parent.rightChild;
                    w.color = x.parent.color;
                    x.parent.color = "black";
                    leftRotate(x.parent);
                    x = this.root;
                }
                else if(x.color.equals("black") && w.color.equals("black") && (w.rightChild != null && w.rightChild.color.equals("red"))){
                    w.color = x.parent.color;
                    x.parent.color = "black";
                    w.rightChild.color = "black";
                    leftRotate(x.parent);
                    return;
                }
            }
            else if(x == x.parent.rightChild) {
                w = x.parent.leftChild;
                if(w == null){
                    x = x.parent;
                    continue;
                }
                if(w.color.equals("red")){
                    w.color = "black";
                    x.parent.color = "red";
                    rightRotate(x.parent);
                    w = x.parent.leftChild;
                }
                if((w.rightChild == null || w.rightChild.color.equals("black")) && (w.leftChild == null || w.leftChild.color.equals("black"))){
                    w.color = "red";
                    x = x.parent;
                }
                else if(w.rightChild != null && w.rightChild.color.equals("red") && (w.leftChild == null || w.leftChild.color.equals("black"))){

                    w.rightChild.color = "black";
                    w.color = "black";
                    leftRotate(w);
                    w = x.parent.leftChild;
                    w.color = x.parent.color;
                    x.parent.color = "black";
                    rightRotate(x.parent);
                    break;
                }
                else if((w.leftChild == null || w.leftChild.color.equals("black"))){

                    w.rightChild.color = "black";

                    w.color = "red";
                    leftRotate(w);
                    w = x.parent.leftChild;
                    w.color = x.parent.color;
                    x.parent.color = "black";
                    rightRotate(x.parent);
                    x = this.root;
                }
                else if(x.color.equals("black") && w.color.equals("black") && (w.leftChild != null && w.leftChild.color.equals("red"))){
                    w.color = x.parent.color;
                    x.parent.color = "black";
                    w.leftChild.color = "black";
                    rightRotate(x.parent);
                    return;
                }
            }
        }
        x.color = "black";
    }

    private Node treeSuccessor(Node r) {
        if(r.rightChild != null){
            return BST_Minimum(r.rightChild);
        }
        Node y = r.parent;
        while (y != null && r == y.rightChild){
            r = y;
            y = y.parent;
        }
        return y;
    }

    private Node BST_Minimum(Node r) {
        while (r.leftChild != null){
            r = r.leftChild;
        }
        return r;
    }

    public Node searchValue(long value){
        Node iterNode = this.root;
        while (iterNode != null){
            if(iterNode.value == value){
                return iterNode;
            }
            if(value > iterNode.value){
                iterNode = iterNode.rightChild;
            }
            else {
                iterNode = iterNode.leftChild;
            }
        }
        iterNode = this.root;
        value = value + 32;
        while (iterNode != null){
            if(iterNode.value == value){
                return iterNode;
            }
            if(value > iterNode.value){
                iterNode = iterNode.rightChild;
            }
            else {
                iterNode = iterNode.leftChild;
            }
        }
        return null;
    }


    public String defineExeMsg(int state){
        if(state == 0){
            return "0";
        }
        else {
            // state = 1

            Scores scores = new Scores();
            refreshHights();
            scores = calcWinner(this.root, scores);
            if(scores.player1Score < scores.player2Score){
                return "YOU ARE AHEAD! " + "YOUR SCORE:" + -scores.player1Score + " " + "AI SCORE:" + -scores.player2Score;
            }
            else if(scores.player1Score > scores.player2Score){
                return "AI IS AHEAD! " + "YOUR SCORE:" + -scores.player1Score + " " + "AI SCORE:" + -scores.player2Score;
            }
            else {
                return "EQUAL " + "YOUR SCORE:" + -scores.player1Score + " " + "AI SCORE:" + -scores.player2Score;
            }
        }
    }

    public Scores calcWinner(Node node, Scores scores1) {
        if (node == null){
            return scores1;
        }
        Scores scores = new Scores();
        scores.player1Score = scores1.player1Score;
        scores.player2Score = scores1.player2Score;
        scores.hight = scores1.hight;

        Scores leftScores = calcWinner(node.leftChild, scores);
        Scores rightScores = calcWinner(node.rightChild,scores);

        Scores resScores = new Scores();
        resScores.player1Score = leftScores.player1Score + rightScores.player1Score;
        resScores.player2Score = leftScores.player2Score + rightScores.player2Score;
        resScores.hight = Math.max(leftScores.hight,rightScores.hight) + 1;
        if(node.color.equals("red")){
            if(node.player == 1){
                resScores.player1Score += resScores.hight * node.value;
            }
            else {
                resScores.player2Score += resScores.hight * node.value;
            }
        }
        node.hight = scores.hight;

        return resScores;
    }

    public RedBlackTree Copy(){
        RedBlackTree redBlackTree_copy = new RedBlackTree();
        redBlackTree_copy.root = NodeCopy(root, null);
        return redBlackTree_copy;
    }

    public Node NodeCopy(Node node, Node parentNodeCopy){
        if(node == null){
            return null;
        }
        Node node_copy = new Node(node.value,node.player);
        node_copy.color = node.color;
        if(parentNodeCopy != null){
            node_copy.parent = parentNodeCopy;
        }
        node_copy.leftChild = NodeCopy(node.leftChild, node_copy);
        node_copy.rightChild = NodeCopy(node.rightChild, node_copy);

        return node_copy;
    }

}

class Scores{
    int player1Score = 0;
    int player2Score = 0;
    int hight = 0;
    int MaxHight = 0;
}

class Node{
    int value;
    int player;
    Node leftChild;
    Node rightChild;
    Node parent;
    String color;
    boolean isNil = false;
    int hight;

    int x = 0;
    int y = 0;

    public Node(int value, int player){
        this.value = value;
        this.player = player;
        this.color = "red";

    }
    public Node(boolean isNil){
        this.isNil = true;
        this.color = "black";
    }

    public void putLeftChild(Node x){
        x.parent = this;
        this.leftChild = x;

    }

    public void putRightChild(Node x){
        x.parent = this;
        this.rightChild = x;

    }



}
