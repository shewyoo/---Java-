import java.awt.*;
import java.util.Random;

public class Buff {
    int x, y;
    private static final int WIDTH = 10;
    private static final int HEIGHT = 10;
    private static final int YSPEED = 5;
    ThunderFighter tf;
    private boolean live = true;
    Random r = new Random();//随机产生三种颜色BUFF，有不同效果
    int m = r.nextInt(2);

    public Buff(int x,int y,ThunderFighter tf){
        super();
        this.x = x;
        this.y = y;
        this.tf = tf;
    }
    public void draw(Graphics g){
        if(!live){
            tf.buff.remove(this);
            return;
        }
        Color c = g.getColor();
        if(m == 0)g.setColor(Color.magenta);
        if(m == 1)g.setColor(Color.blue);
        g.fillRect(x,y,WIDTH,HEIGHT);
        g.setColor(c);
        move();
    }
    void move(){
        y+=YSPEED;
        if(y>tf.GAME_HEIGHT){
            live = false;
        }
    }
    public int buffType(){
    return m;
    }
    public Rectangle getRect(){//碰撞检测
        return new Rectangle(x,y,WIDTH,HEIGHT);
    }
    public void setLive(boolean live){
        this.live = live;
    }
    public boolean getLive(){
        return live;
    }
}
