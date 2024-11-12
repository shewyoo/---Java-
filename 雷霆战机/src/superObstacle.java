import java.awt.*;
import java.util.Random;

public class superObstacle {
    int x,y;
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    private static final int YSPEED = 1;
    private static final int XSPEED = 5;
    Random r = new Random();//使障碍物可以随机改变方向的移动
    enum Direction{
        RD,D,LD
    }
    private Obstacle.Direction[] dir = Obstacle.Direction.values();
    ThunderFighter tf;
    private boolean live = true;
    private int life = 100;
    private BloodBar bb = new BloodBar();

    private class BloodBar{//血条
        public void draw(Graphics g){
            Color c = g.getColor();
            g.setColor(Color.red);
            g.drawRect(x,y-7,WIDTH,5);
            int w = WIDTH*life/100;
            g.fillRect(x,y-7,w,5);
            g.setColor(c);
        }
    }

    public superObstacle(int x,int y,ThunderFighter tf){
        super();
        this.x = x;
        this.y = y;
        this.tf = tf;
    }
    public void draw(Graphics g){
        if(!live){
           tf.superobstacle.remove(this);
            return;
        }
        Color c = g.getColor();
        g.setColor(Color.green);
        g.fillOval(x,y,WIDTH,HEIGHT);
        g.setColor(c);
        bb.draw(g);
        move();
    }
    private void move(){
        y+=YSPEED;
        int m = r.nextInt(3);
        Obstacle.Direction dirs = dir[m];
        switch (dirs){
            case RD:
                x += XSPEED;
                y += YSPEED;
                break;
            case D:
                y += XSPEED;
                break;
            case LD:
                x -= XSPEED;
                y += YSPEED;
                break;
        }
        //越界消失，保证超级障碍物在界面消失后，会产生新的。
        if(y>tf.GAME_HEIGHT){
            live = false;
        }
    }
    //碰撞检测
    public Rectangle getRect(){
        return new Rectangle(x,y,WIDTH,HEIGHT);
    }
    public void setLive(boolean live){
        this.live = live;
    }
    public boolean getLive(){
        return live;
    }
    public boolean hitFighter(Fighter f){
        if(this.getRect().intersects(f.getRect())&&f.getLive()&&this.getLive()){
            f.setLive(false);
            return true;
        }
        return false;
    }
    public void setLife(int life){
        this.life = life;
    }
    public int getLife(){
        return life;
    }
}
