import javax.swing.*;
import java.awt.*;

public class GameView {
    public int WINDOW_WIDTH = 2000;
    public int WINDOW_HIGHT = 500;
    public int LEFT_NUMBER = 12;
    public int DIST = 10;
    public float DIST_SCALE = (float) 1.95;
    public int NODE_DISTANCE = 60;
    public int NODE_DIMENTIONS = 40;
    public int LINE_WIDTH = 2;


    MyFrame gameFrame;

    public GameView(){
        this.gameFrame = new MyFrame(WINDOW_WIDTH,WINDOW_HIGHT,LEFT_NUMBER,DIST,DIST_SCALE,NODE_DISTANCE,NODE_DIMENTIONS,LINE_WIDTH);
    }

    public void refreshGameView(RedBlackTree redBlackTree){
        gameFrame.refreshPanel(redBlackTree);
    }
}

class MyFrame extends JFrame{
    GamePanel gamePanel;

    public int WINDOW_WIDTH;
    public int WINDOW_HIGHT;
    public int LEFT_NUMBER;
    public int DIST;
    public float DIST_SCALE;
    public int NODE_DISTANCE;
    public int NODE_DIMENTIONS;
    public int LINE_WIDTH;

    public MyFrame(int WINDOW_WIDTH, int WINDOW_HIGHT, int LEFT_NUMBER, int DIST, float DIST_SCALE, int NODE_DISTANCE, int NODE_DIMENTIONS, int LINE_WIDTH){
        this.WINDOW_HIGHT = WINDOW_HIGHT;
        this.WINDOW_WIDTH = WINDOW_WIDTH;
        this.LEFT_NUMBER = LEFT_NUMBER;
        this.DIST = DIST;
        this.DIST_SCALE = DIST_SCALE;
        this.NODE_DISTANCE = NODE_DISTANCE;
        this.NODE_DIMENTIONS = NODE_DIMENTIONS;
        this.LINE_WIDTH = LINE_WIDTH;

        this.setSize(WINDOW_WIDTH,WINDOW_HIGHT);
        this.setLayout(null);

        this.setTitle("RedBlackTree");
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void refreshPanel(RedBlackTree redBlackTree){
        redBlackTree.refreshHights();
        if(this.gamePanel != null){
            this.remove(this.gamePanel);
        }

        this.gamePanel = new GamePanel(WINDOW_WIDTH,WINDOW_HIGHT);
        constructTreeView(this.gamePanel,redBlackTree);

        this.gamePanel.revalidate();
        this.gamePanel.repaint();

        this.add(this.gamePanel);
        this.revalidate();
        this.repaint();
    }

    public void constructTreeView(GamePanel gamePanel, RedBlackTree redBlackTree) {
        int h = 0;
        inOrder(gamePanel,redBlackTree.root,LEFT_NUMBER,DIST, h);
    }

    private void inOrder(GamePanel backGround, Node node, float leftNumber, float dist, int h) {
        if(node == null){
            return;
        }
        addNode(backGround,node,leftNumber,h );
        h = h +1;
        dist = dist/DIST_SCALE;
        Node p = node.leftChild;
        float leftNumber1 = leftNumber - dist;
        inOrder(backGround,p,leftNumber1,dist,h);

        p = node.rightChild;
        float leftNumber2 = leftNumber + dist ;
        inOrder(backGround,p,leftNumber2,dist,h);
    }

    public void addNode(JPanel backGround, Node node,float leftNumber,int y){
        JPanel nodePanel = new JPanel();
        nodePanel.setLayout(null);
        node.x = (int) (NODE_DISTANCE*leftNumber);
        node.y = NODE_DISTANCE*y;
        JLabel nodeLabel = new JLabel();
        nodeLabel.setText(Integer.toString(node.value) + "," + node.hight + "," + node.player );
        nodeLabel.setVisible(true);
        nodeLabel.setBounds(0,0,NODE_DIMENTIONS,NODE_DIMENTIONS);
        nodePanel.add(nodeLabel);

        if(node.color == "black"){
            nodePanel.setBackground(Color.BLACK);
            nodePanel.setForeground(Color.WHITE);
            nodeLabel.setForeground(Color.WHITE);
        }
        else {
            nodePanel.setBackground(Color.red);
        }

        nodePanel.setBounds(node.x,node.y,NODE_DIMENTIONS,NODE_DIMENTIONS);
        nodePanel.setVisible(true);

        backGround.add(nodePanel);

        connectNode(backGround,node);

        backGround.revalidate();
        backGround.repaint();
    }


    private void connectNode(JPanel backGround, Node node) {
        if(node.parent != null){

            JPanel j1 = new JPanel();
            //System.out.println("node.x: " + (int) ((int) (node.x) + (int) (10)));
            //System.out.println("node.y: " + (int)((int)node.y - (int) 5));
            j1.setBounds(node.x, node.y - (int)((NODE_DISTANCE-NODE_DIMENTIONS)/2), LINE_WIDTH,(int)((NODE_DISTANCE-NODE_DIMENTIONS)/2));
            j1.setBackground(Color.YELLOW);
            j1.setLayout(null);
            j1.setVisible(true);

            JPanel j2 = new JPanel();
            j2.setBounds(node.parent.x, (int)(node.parent.y+(NODE_DIMENTIONS)), LINE_WIDTH,(int)((NODE_DISTANCE-NODE_DIMENTIONS)/2));
            j2.setBackground(Color.YELLOW);
            j2.setLayout(null);
            j2.setVisible(true);

            JPanel j3 = new JPanel();
            j3.setBounds(Math.min(node.x,node.parent.x),node.y-(int)((NODE_DISTANCE-NODE_DIMENTIONS)/2),Math.abs(node.x - node.parent.x),LINE_WIDTH );
            j3.setBackground(Color.YELLOW);
            j3.setLayout(null);
            j3.setVisible(true);

            backGround.add(j1);
            backGround.add(j2);
            backGround.add(j3);

        }
    }
}

class GamePanel extends JPanel{
    public GamePanel(int WINDOW_WIDTH,int WINDOW_HIGHT){
        this.setBounds(0,0,WINDOW_WIDTH,WINDOW_HIGHT);
        this.setBackground(Color.CYAN);
        this.setLayout(null);
        this.setVisible(true);
    }

}

