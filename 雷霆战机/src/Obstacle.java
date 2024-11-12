import java.awt.*;
import java.util.List;
import java.util.Random;

public class Obstacle {
    int x,y;
    private static final int WIDTH = 10;
    private static final int HEIGHT = 10;
    private static final int YSPEED = 1;
    private static final int XSPEED = 2;
    Random r = new Random();//使障碍物可以随机改变方向的移动
    enum Direction{
        RD,D,LD
    }
    private Direction[] dir = Direction.values();
    ThunderFighter tf;
    private boolean live = true;

    public Obstacle(int x,int y){
        super();
        this.x = x;
        this.y = y;
    }
    public void draw(Graphics g){
        if(!live){
            return;
        }
        Color c = g.getColor();
        g.setColor(Color.ORANGE);
        g.fillOval(x,y,WIDTH,HEIGHT);
        g.setColor(c);
        move();
    }
    private void move(){
        y+=YSPEED;
        int m = r.nextInt(3);
        Direction dirs = dir[m];
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

}
