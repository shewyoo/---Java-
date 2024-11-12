import org.omg.CORBA.PRIVATE_MEMBER;
import org.w3c.dom.html.HTMLDOMImplementation;

import java.awt.*;
import java.util.List;
public class Missile {
    int x,y;
    public static final int WIDTH = 1;//子弹大小
    public static final int HEIGHT = 20;
    private static final int YSPEED = 10;
    private boolean live = true;
    ThunderFighter tf;

    public Missile(int x,int y,ThunderFighter tf){
        super();
        this.x = x;
        this.y = y;
        this.tf = tf;
    }
    public void draw(Graphics g){
        if(!live){
            tf.missiles.remove(this);
            return;
        }
        Color c = g.getColor();
        g.setColor(Color.black);
        g.fillRect(x,y,WIDTH, HEIGHT);
        g.setColor(c);
        move();
    }
    private void move(){//子弹移动
        y -= YSPEED;
        if(x<0||y<0||x>ThunderFighter.GAME_WIDTH||y>ThunderFighter.GAME_HEIGHT){//阻止子弹越界
            live = false;
        }
    }
    //碰撞检测
    public Rectangle getRect(){
        return new Rectangle(x,y,WIDTH,HEIGHT);
    }
    public boolean hitObstacle(Obstacle o){//子弹攻击障碍物
        if(this.getRect().intersects(o.getRect())&&o.getLive()&&this.live){
            o.setLive(false);
            this.live = false;
            return true;
        }
        return false;
    }
    public boolean hitObstacles(List<Obstacle> o){
        for(int i = 0;i<o.size();i++){
            if(hitObstacle(o.get(i))){
                tf.number++;
                return true;
            }
        }
        return false;
    }
    public boolean hitSuperObstacle(superObstacle so){//子弹攻击超级障碍物
        if(this.getRect().intersects(so.getRect())&&so.getLive()&&this.live){
            this.live = false;
            so.setLife(so.getLife()-20);
            if(so.getLife()>0){
                so.setLive(true);
            }
            else{
                so.setLive(false);
                tf.superMissileNumber++;//消灭超级障碍物后可使用超级子弹次数+1
            }
            return true;
        }
        return false;
    }
    public boolean hitSuperObstacles(List<superObstacle> so){
        for(int i = 0;i<so.size();i++){
            if(hitSuperObstacle(so.get(i))){
                return true;
            }
        }
        return false;
    }
    public void setLive(boolean live){
        this.live = live;
    }
    public boolean getLive(){
        return live;
    }
}
