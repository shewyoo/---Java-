import java.awt.*;
import java.util.List;

public class protect {
        int x;int y;
        private  int WIDTH = 80;
        private  int HEIGHT = 80;
        Fighter f;
        private boolean live = false;

        public protect(int x,int y,Fighter f){
            super();
            this.x = x;
            this.y = y;
            this.f = f;
        }
        public void draw(Graphics g){
            if(!true){
                return;
            }
            Color c = g.getColor();
            g.setColor(Color.black);
            g.drawOval(x-25,y-30,WIDTH,HEIGHT);
            g.setColor(c);
        }
        public Rectangle getRect(){
            return new Rectangle(x-25,y-30,WIDTH,HEIGHT);
        }
    public boolean protectFighter(Obstacle o){//无敌保护破坏障碍物
        if(this.getRect().intersects(o.getRect())&&o.getLive()){
            o.setLive(false);
            return true;
        }
        return false;
    }
    public boolean protectFighters(List<Obstacle> o){
        for(int i = 0;i<o.size();i++){
            if(protectFighter(o.get(i))){
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
