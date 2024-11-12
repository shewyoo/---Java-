import org.omg.CORBA.PRIVATE_MEMBER;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;

public class Fighter {
    int x,y;
    private static final int WIDTH = 30;//飞机大小
    private static final int HEIGHT = 10;
    private static final int XSPEED = 10;//移动速度
    private static final int YSPEED = 10;
    private boolean bL = false,bU = false,bD = false,bR = false;
    enum Direction{
        L,LU,U,RU,R,RD,D,LD,STOP//表示移动的八个方向
    }
    private Direction dir = Direction.STOP;
    ThunderFighter tf;
    private boolean live = true;
    int m;

    public Fighter(int x,int y,ThunderFighter tf){
        super();
        this.x = x;
        this.y = y;
        this.tf = tf;
    }
    public void draw(Graphics g){
        if(!live){
            return;
        }
        Color c = g.getColor();
        g.setColor(Color.red);
        g.fillOval(x,y,WIDTH,HEIGHT);
        g.setColor(c);
        g.drawLine(x+Fighter.WIDTH/2,y+Fighter.HEIGHT/2,x+Fighter.WIDTH/2,y-10);
        g.drawLine(x,y,x,y+HEIGHT);
        g.drawLine(x+WIDTH,y,x+WIDTH,y+HEIGHT);
        move();
    }
    public void move() {
        switch (dir) {
            case L:
                x -= XSPEED;
                break;
            case LU:
                x -= XSPEED;
                y -= YSPEED;
                break;
            case U:
                y -= XSPEED;
                break;
            case RU:
                x += XSPEED;
                y -= YSPEED;
                break;
            case R:
                x += XSPEED;
                break;
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
            case STOP:
                break;
        }
        //阻止飞机越界
        if(x<0) x = 0;
        if(y<30) y = 30;
        if(x+Fighter.WIDTH>ThunderFighter.GAME_WIDTH) x = ThunderFighter.GAME_WIDTH-Fighter.WIDTH;
        if(y+Fighter.HEIGHT>ThunderFighter.GAME_HEIGHT) y = ThunderFighter.GAME_HEIGHT-Fighter.HEIGHT;
        if(tf.protectTime>=0){
            tf.p = new protect(x,y,this);
        }
    }
    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();
        switch (key){
            case KeyEvent.VK_A:bL=true;break;
            case KeyEvent.VK_D:bR=true;break;
            case KeyEvent.VK_W:bU=true;break;
            case KeyEvent.VK_S:bD=true;break;
        }
        locateDirection();
    }
    public void keyReleased(KeyEvent e){
        int key = e.getKeyCode();
        switch (key){
            case KeyEvent.VK_A:bL=false;break;
            case KeyEvent.VK_D:bR=false;break;
            case KeyEvent.VK_W:bU=false;break;
            case KeyEvent.VK_S:bD=false;break;
            case KeyEvent.VK_J:if(live)fire();break;
            case KeyEvent.VK_K:if(live&&tf.superMissileNumber!=0){//需要超级子弹使用次数
                superFire1();
                tf.superMissileNumber--;
            }break;
            case KeyEvent.VK_L:if(!live){//复活删除所有障碍物并且飞机在原点出生
                live = true;
                tf.obstacles.clear();
                x = 400;y = 550;
            }break;
            case KeyEvent.VK_1:tf.yourMode = 0;break;
            case KeyEvent.VK_2:tf.yourMode = 1;break;
            case KeyEvent.VK_3:tf.yourMode = 2;break;
            case KeyEvent.VK_4:tf.yourMode = 3;break;
        }
        locateDirection();
    }
    private void locateDirection() {
        if(bL&&!bU&&!bR&&!bD)dir = Direction.L;
        else if(bL&&bU&&!bR&&!bD)dir = Direction.LU;
        else if(!bL&&bU&&!bR&&!bD)dir = Direction.U;
        else if(!bL&&bU&&bR&&!bD)dir = Direction.RU;
        else if(!bL&&!bU&&bR&&!bD)dir = Direction.R;
        else if(!bL&&!bU&&bR&&bD)dir = Direction.RD;
        else if(!bL&&!bU&&!bR&&bD)dir = Direction.D;
        else if(bL&&!bU&&!bR&&bD)dir = Direction.LD;
        else if(!bL&&!bU&&!bR&&!bD)dir = Direction.STOP;
    }
    private Missile fire(){
        int x = this.x+Fighter.WIDTH/2-Missile.WIDTH/2;//子弹相对飞机发出的位置
        int y = this.y+Fighter.HEIGHT/2-Missile.HEIGHT/2;
        Missile m = new Missile(x,y,tf);
        tf.missiles.add(m);
        return m;
    }
    private void superFire1(){
        int y =this.y+Fighter.HEIGHT/2-Missile.HEIGHT/2;
        for(int i = 0;i<800;i+=18){//超级子弹的间隔
            int x = i;
            Missile m = new Missile(x,y,tf);
            tf.missiles.add(m);
        }
    }
    private void superFire2(int m){//拾取BUFF获得的效果
        if(m == 0){//清屏
            tf.obstacles.clear();
        }
        if(m == 1){//生成屏障，保护飞机
            tf.p = new protect(x,y,this);
            tf.p.setLive(true);
            tf.protectTime = 150;
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
    public boolean eatBuff(Buff b){
        if(this.getRect().intersects(b.getRect())&&this.live&&b.getLive()){
            b.setLive(false);
            return true;
        }
        return false;
    }
    public boolean eatBuffs(List<Buff> b){
        for(int i = 0;i<b.size();i++)
            if(eatBuff(b.get(i))){
                 m = b.get(i).buffType();//拾取BUFF得到BUFF种类
                superFire2(m);//拾取后立刻调用超级子弹
                return true;
            }
        return false;
    }


}
