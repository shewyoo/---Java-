import javax.net.ssl.KeyManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ThunderFighter extends JFrame {
    public static final int GAME_WIDTH = 800;
    public static final int GAME_HEIGHT = 600;
    public int number = 0;//消除的障碍物数量
    private String[] score = {"菜鸟","入门","一般","高手","王牌飞行员"};//得分等级
    private String yourScore;
    private String[] mode = {"初级","中级","高级","地狱"};//难度等级:1为初级...
    public int yourMode;
    public int superMissileNumber;//超级子弹可使用次数
    public int protectTime;//无敌保护时间

    Fighter myFighter = new Fighter(400,550,this);
    List<Missile> missiles = new ArrayList<Missile>();//存放子弹集合
    List<Obstacle> obstacles = new ArrayList<Obstacle>();//存放障碍物集合
    List<superObstacle> superobstacle = new ArrayList<superObstacle>();//存放超级子弹集合
    List<Buff> buff = new ArrayList<Buff>();//存放Buff集合
    protect p = null;//无敌保护

    public static void main(String[] args){
        ThunderFighter tf = new ThunderFighter();
        tf.lauchFrame();
    }
    private void lauchFrame(){
        setLocation(300,50);
        setSize(GAME_WIDTH,GAME_HEIGHT);
        setTitle("雷霆战机");
        setResizable(false);
        getContentPane().setBackground(Color.white);
        getContentPane().setVisible(true);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        new Thread(new ObstacleThread()).start();//障碍物生成线程
        new Thread(new PaintThread()).start();//页面刷新线程
        addKeyListener(new KeyMonitor());
    }
    public void paint(Graphics g){
        super.paint(g);
        myFighter.draw(g);
        myFighter.eatBuffs(buff);
        if(p!=null&&protectTime>0){
            p.draw(g);
            p.protectFighters(obstacles);
        }
        g.drawString("子弹数量："+missiles.size(),10,50);
        for(int i = 0;i<missiles.size();i++){
            Missile m = missiles.get(i);
            m.hitObstacles(obstacles);
            m.hitSuperObstacles(superobstacle);
            m.draw(g);
        }
        g.drawString("已消除的障碍物数量："+number,10,70);
        for(int i = 0;i<obstacles.size();i++){
            Obstacle o = obstacles.get(i);
            o.hitFighter(myFighter);
            o.draw(g);
        }
        //等级划分
        switch (number/100){
            case 0:yourScore = score[0];break;
            case 1:
            case 2:yourScore = score[1];break;
            case 3:
                case 4:yourScore = score[2];break;
            case 5:
            case 6:yourScore = score[3];break;
            case 7:
            case 8:
            case 9:
            case 10:yourScore = score[4];break;
        }
        g.drawString("你的战机水平："+yourScore,10,90);
        g.drawString("当前游戏难度："+mode[yourMode],10,110);
        for(int i = 0;i<superobstacle.size();i++){
            superObstacle so = superobstacle.get(i);
            so.hitFighter(myFighter);
            so.draw(g);
        }
        g.drawString("可使用超级子弹次数："+superMissileNumber,10,130);
        for(int i = 0;i<buff.size();i++){
            Buff b = buff.get(i);
            b.draw(g);
        }
        if(protectTime!=0){
            g.drawString("无敌时间剩余："+protectTime/10,10,150);
        }
    }

    private class ObstacleThread implements Runnable{//生成障碍物和BUFF线程
        @Override
        public void run() {
            while(true){
                if(yourMode == 0){//游戏难度，通过改变i的数量，改变生成的数量，但是800个位置不变
                    for(int i = 0;i<=800;i+=100){
                        Random r= new Random();//使生成的障碍物y随机
                        Obstacle o = new Obstacle(i,r.nextInt(50));
                        obstacles.add(o);
                        if(r.nextInt(30)>28&&superobstacle.size()<1){//三十分之一的概率生成一个超级障碍物
                           superObstacle so= new superObstacle(r.nextInt(800),0,ThunderFighter.this);//内部类调用外部类
                           superobstacle.add(so);
                        }
                        if(r.nextInt(30)>28&&buff.size()<1){//生成BUFF
                            Buff b = new Buff(r.nextInt(800),0,ThunderFighter.this);
                            buff.add(b);
                        }
                    }
                }
                if(yourMode == 1){
                    for(int i = 0;i<=800;i+=60){
                        Random r= new Random();//使生成的障碍物y随机
                        Obstacle o = new Obstacle(i,r.nextInt(50));
                        obstacles.add(o);
                        if(r.nextInt(20)>18&&superobstacle.size()<2){//二十分之一的概率生成一个超级障碍物
                            superObstacle so= new superObstacle(r.nextInt(800),0,ThunderFighter.this);
                            superobstacle.add(so);
                        }
                        if(r.nextInt(20)>18&&buff.size()<2){//生成BUFF
                            Buff b = new Buff(r.nextInt(800),0,ThunderFighter.this);
                            buff.add(b);
                        }
                    }
                }
                if(yourMode == 2){
                    for(int i = 0;i<=800;i+=30){
                        Random r= new Random();//使生成的障碍物y随机
                        Obstacle o = new Obstacle(i,r.nextInt(50));
                        obstacles.add(o);
                        if(r.nextInt(15)>13&&superobstacle.size()<3){//十五分之一的概率生成一个超级障碍物
                            superObstacle so= new superObstacle(r.nextInt(800),0,ThunderFighter.this);
                            superobstacle.add(so);
                        }
                        if(r.nextInt(15)>13&&buff.size()<3){//生成BUFF
                            Buff b = new Buff(r.nextInt(800),0,ThunderFighter.this);
                            buff.add(b);
                        }
                    }
                }
                if(yourMode == 3){
                    for(int i = 0;i<=800;i+=10){
                        Random r= new Random();//使生成的障碍物y随机
                        Obstacle o = new Obstacle(i,r.nextInt(50));
                        obstacles.add(o);
                        if(r.nextInt(10)>8&&superobstacle.size()<4){//十分之一的概率生成一个超级障碍物
                            superObstacle so= new superObstacle(r.nextInt(800),0,ThunderFighter.this);
                            superobstacle.add(so);
                        }
                        if(r.nextInt(10)>8&&buff.size()<4){//生成BUFF
                            Buff b = new Buff(r.nextInt(800),0,ThunderFighter.this);
                            buff.add(b);
                        }
                    }

                }
                try {
                    Thread.sleep(2000);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
    private class PaintThread implements Runnable{//刷新页面线程
        public void run(){
            while (true){
                repaint();
                if(protectTime>0)protectTime--;
                try {
                    Thread.sleep(100);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public class KeyMonitor extends KeyAdapter{
        public void keyPressed(KeyEvent e){
            super.keyPressed(e);
            myFighter.keyPressed(e);
        }
        public void keyReleased(KeyEvent e){
            super.keyPressed(e);
            myFighter.keyReleased(e);
        }
    }
}
