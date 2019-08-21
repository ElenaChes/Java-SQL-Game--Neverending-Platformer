/*
 * @author Elena Chestnov
 */
package main_pkg;
    import javax.swing.*;
    import java.awt.Point;
    import java.awt.event.*;

public class Player {
/*  Flags and variables  */     
    private int amount;
    protected int width=Screen1.width;
    protected int height=Screen1.height;
    private int animT=55;   // timers' delay

    protected boolean failed;  
    protected boolean finished;  
    protected boolean jumped;  
    private String CAnim="c0";  // current animation
    private Images [] a=new Images[4];
    private Images [] c=new Images[6];
    private Images [] j=new Images[5];
    private Images [] ce=new Images[3];
    private Images [] cs=new Images[3];
    private Images [] h=new Images[6];
    private Images [] f=new Images[2];
    private Images [] w=new Images[3];
    private Images [] ws=new Images[2];
    private Images [] s=new Images[2];
    private Images [] fw=new Images[4];
    private double PlayerP;    // position
    private double PlayerE;    // elevation
    private int jumpT;  // jump time
/*  Pointers  */     
    private Queue <Tile> list;
    private JLabel player;
    
/*  Builders  */           
    public Player(int amount, JLabel player, Queue list){
        this.amount=amount;
        this.player = player;
        this.list=list;
        CAnim="c0";
        PlayerP=height/3;
        PlayerE=0;
        jumpT=0;
        player.setLocation(width*2/amount,0);
}
    public Player(){}
    
/*  Timers  */ 
    private Timer tm1 = new Timer(animT, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!failed&&!finished){
                    Run();
                }
            }
        });
    private Timer failTimer = new Timer(10, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!failed&&!finished){
                    UpdateElevation();
                    IsFail();
                }
            }
        });
   
/*  Access to timers  */     
    protected void startAll(){
        tm1.start();
        failTimer.start();
    }
    protected void stop1(){
        tm1.stop();
    }
    protected void stopAll(){
        jumpT=3;
        tm1.stop();
        failTimer.stop();
    }
    
/*  Preloads images for less lag  */      
    protected void LoadImg(int width, int height){
        String str;
        this.width=width;
        this.height=height;
        for (int i=0;i<6;i++){
            str= System.getProperty( "user.dir" )+"/res/c"+i+".png";
                c[i]=new Images(str,height/5, height/5,"c"+i);
            str= System.getProperty( "user.dir" )+"/res/h"+i+".png";
                h[i] = new Images(str,height/5, height/5,"h"+i);
        }
        for (int i=0;i<5;i++){
            str= System.getProperty( "user.dir" )+"/res/j"+i+".png";
                j[i] = new Images(str,height/5, height/5,"j"+i);
        }
        for (int i=0;i<4;i++){
            str= System.getProperty( "user.dir" )+"/res/a"+i+".png";
                a[i] = new Images(str,height/5, height/5,"a"+i);
            str= System.getProperty( "user.dir" )+"/res/fw"+i+".png";
                fw[i] = new Images(str,height/5, height/5,"fw"+i);
        }
        for (int i=0;i<3;i++){
            str= System.getProperty( "user.dir" )+"/res/ce"+i+".png";
                ce[i] = new Images(str,height/5, height/5,"ce"+i);
            str= System.getProperty( "user.dir" )+"/res/cs"+i+".png";
                cs[i] = new Images(str,height/5, height/5,"cs"+i);
            str= System.getProperty( "user.dir" )+"/res/w"+i+".png";
                w[i] = new Images(str,height/5, height/5,"w"+i);
        }
        for (int i=0;i<2;i++){
            str= System.getProperty( "user.dir" )+"/res/f"+i+".png";
                f[i] = new Images(str,height/5, height/5,"f"+i);
            str= System.getProperty( "user.dir" )+"/res/ws"+i+".png";
                ws[i] = new Images(str,height/5, height/5,"ws"+i);
            str= System.getProperty( "user.dir" )+"/res/s"+i+".png";
                s[i] = new Images(str,height/5, height/5,"s"+i);
        }
    }
    
    private void UpdateElevation(){
        int elevation =(list.GetItem(3)).getHeight();
        if (PlayerE>(elevation-1)*height*160/1080){
            PlayerE-=height/27;
            if (PlayerE-40<(elevation-1)*height*160/1080)
                PlayerE=(elevation-1)*height*160/1080;
        }
        else if (PlayerE<(elevation-1)*height*160/1080){
            PlayerE+=height/27;
            if (PlayerE+40>(elevation-1)*height*160/1080)
                PlayerE=(elevation-1)*height*160/1080;
        }
    }
    
 /*  Animation  */      
    protected void Start(){
        boolean anim=true;
        ImageIcon temp=new ImageIcon();
        switch (CAnim) {
            case "a0":  CAnim="a1";
                        temp=a[1].getIcon();
                        PlayerP-=height/6;
                        break;
            case "a1":  CAnim="a2";
                        temp=a[2].getIcon();
                        PlayerP-=height/6;
                        break;
            case "a2":  CAnim="a3";
                        temp=a[3].getIcon();
                        PlayerP-=height/6;
                        break;   
            case "a3":  CAnim="c0";
                        temp=c[0].getIcon();
                        PlayerP=0;
                        anim=false;
                        break;  
            default:    CAnim="a0";
                        temp=a[0].getIcon();
                        PlayerP=height/2;
                        break;
        } 
        if (anim){
            Point point = new Point();
            point.setLocation(width*2/amount,height/3-PlayerP);
            player.setLocation(point);
                player.setIcon(temp);
            Timer t = new Timer(animT*2, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Start();
                    }
            });
            t.setRepeats( false );
            t.start();
        }
        else 
            Screen2.startAll();
    }
    
    private void Run(){
        ImageIcon temp=new ImageIcon();
        switch (CAnim) {
            case "c0":  CAnim="c1";
            
                        temp=c[1].getIcon();
                        jumped=false;
                        break;
            case "c1":  CAnim="c2";
            
                        temp=c[2].getIcon();
                        break;
            case "c2":  CAnim="c3";
            
                        temp=c[3].getIcon();
                        break;
            case "c3":  CAnim="c4";
            
                        temp=c[4].getIcon();
                        break;
            case "c4":  CAnim="c5";
            
                        temp=c[5].getIcon();
                        break;
            default:    CAnim="c0"; 
            
                        temp=c[0].getIcon();
                    
                        break;
        }
        if (PlayerP>0){
            PlayerP-=height/27;
            if (PlayerP<height/27)
                PlayerP=0;
        }
        Point point = new Point();
        point.setLocation(width*2/amount,height/3-PlayerP-PlayerE);
        player.setLocation(point); 
        player.setIcon(temp);
    }
       
    protected void Jump(){
        jumped=true;
        ImageIcon temp=new ImageIcon();
        if (jumpT<=3&&!failed){
        switch (CAnim) {
            case "j0":  CAnim="j1";
                        temp=j[1].getIcon();
                        PlayerP+=height/15;
                        break;
            case "j1":  CAnim="j2";
                        temp=j[2].getIcon();
                        PlayerP+=height/15;
                        break;
            case "j2":  CAnim="j3";
                        temp=j[3].getIcon();
                        PlayerP+=height/15;
                        break;
            case "j3":  CAnim="j4";
                        temp=j[4].getIcon();
                        PlayerP-=height/5;
                        jumped=false;
                        break; 
            default:    CAnim="j0";
                        temp=j[0].getIcon();
                        break;
        }
        Point point = new Point();
        point.setLocation(width*2/amount,height/3-PlayerP-PlayerE);
        player.setLocation(point);
        player.setIcon(temp);
        Timer t = new Timer(animT+15, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jumpT++;
                Jump();
                }
            });
        t.setRepeats( false );
        t.start();
        }
        else {
            PlayerP=0;
            tm1.start(); 
            jumpT=0;
            }
    }
    
/*  Checking if the Player failed to react to a trap or won  */    
    protected void IsFail(){
        Tile current=list.GetItem(2);
        Tile next=list.GetItem(3);
        if (!jumped){
            if(next.getType()==3){    //hole
                stopAll();
                failed=true;
                FailHole();
            }
            else if (current.getType()==4||next.getType()==4){    //spikes floor
                stopAll();
                failed=true;
                FailFloor();
            }
            else if (next.getType()==5){    //wall spikes
                stopAll();
                failed=true;
                FailWallS();
            }
            else if (next.getType()==8){    //wall
                stopAll();
                failed=true;
                FailWall();
            }
            else if (next.getType()==11){    //saw
                stopAll();
                failed=true;
                FailSaw();
            }
            else if (next.getType()==12||next.getType()==13){    //fire/water
                stopAll();
                failed=true;
                FailFW();
            }
        }
        else {  //jump
            if (next.getType()==1){    //ceiling
                stopAll();
                failed=true;
                FailCeiling();
            }
            else if (current.getType()==2||next.getType()==2){   //ceiling spikes
                stopAll();
                failed=true;
                FailCeilingS();
            }
        }
        if(!failed&&current.getTile()==19){    // won
            stopAll();
            finished=true;
            WinAnim();
        }
    }
    
 /*  End animation  */      
    protected void WinAnim(){
        boolean anim=true;
        ImageIcon temp=new ImageIcon();
        switch (CAnim) {
            case "a2":  CAnim="a1";
                        temp=a[1].getIcon();
                        PlayerP+=height/6;
                        break;
            case "a1":  CAnim="a0";
                        temp=a[0].getIcon();
                        PlayerP+=height/6;
                        break;   
            case "a0":  CAnim="null";
                        temp=null;
                        PlayerP=height/2;
                        anim=false;
                        break;  
            default:    CAnim="a2";
                        temp=a[2].getIcon();
                        PlayerP+=height/6;
                        Screen2.stopBG();
                        break;
        } 
        if (anim){
            Point point = new Point();
            point.setLocation(width*2/amount+height/30,height/3-PlayerP-PlayerE);
            player.setLocation(point);
            player.setIcon(temp);
            Timer t = new Timer(animT+50, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    WinAnim();
                    }
            });
            t.setRepeats( false );
            t.start();
        }
        else 
            Screen2.won();
    }
    
/*  Failing animations  */     
    private void FailCeiling(){
        boolean failanim=true;
        ImageIcon temp=new ImageIcon();
        switch (CAnim) {
            case "j1":  CAnim="ce0";
                        temp=ce[0].getIcon();
                        PlayerP=height/60;
                        break;
            case "ce0": CAnim="ce1";
                        temp=ce[1].getIcon();
                        break;
            case "ce1": CAnim="ce2";
                        temp=ce[2].getIcon();
                        break;   
            case "ce2": failanim=false;
                        break;   
            default:    CAnim="j1";
                        temp=j[1].getIcon();
                        break;
        }
        if (PlayerP>0){
            PlayerP-=height/27;
            if (PlayerP<height/27)
                PlayerP=0;
        }
        if (failanim){
            Point point = new Point();
            point.setLocation(width*2/amount,height/3-PlayerP-PlayerE);
            player.setLocation(point);
            player.setIcon(temp);
            Timer t = new Timer(animT, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    FailCeiling();
                    }
            });
            t.setRepeats( false );
            t.start();
        }
        else
            Screen2.fail();
    }
    
    private void FailCeilingS(){
        boolean failanim=true;
        ImageIcon temp=new ImageIcon();
        switch (CAnim) {
            case "j1":  CAnim="cs0";
                        temp=cs[0].getIcon();
                        PlayerP=height/60;
                        break;
            case "cs0": CAnim="cs1";
                        temp=cs[1].getIcon();
                        Screen2.stopBG();
                        break;
            case "cs1": CAnim="cs2";
                        temp=cs[2].getIcon();
                        break;
            case "cs2": failanim=false;
                        break;   
            default:    CAnim="j1";
                        temp=j[0].getIcon();
                        break;
        }
        if (failanim){
            Point point = new Point();
            point.setLocation(width*2/amount,height/3-PlayerP-PlayerE);
            player.setLocation(point);
            player.setIcon(temp);
            Timer t = new Timer(animT, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    FailCeilingS();
                    }
            });
            t.setRepeats( false );
            t.start();
        }
        else
            Screen2.fail();
    }
    
    private void FailHole(){
        boolean failanim=true;
        ImageIcon temp=new ImageIcon();
        switch (CAnim) {
            case "h0":  CAnim="h1";
                        temp=h[1].getIcon();
                        PlayerP-=height/6;
                        break;
            case "h1":  CAnim="h2";
                        temp=h[1].getIcon();
                        Screen2.stopBG();
                        PlayerP-=height/6;
                        break;   
            case "h2":  failanim=false;
                        PlayerP-=height/6;
                        break;   
            default:    CAnim="h0";
                        temp=h[0].getIcon();
                        break;
        }
        if (failanim){
            Point point = new Point();
            point.setLocation(width*2/amount,height/3-PlayerP-PlayerE);
            player.setLocation(point);
            player.setIcon(temp);
            Timer t = new Timer(animT, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    FailHole();
                    }
            });
            t.setRepeats( false );
            t.start();
        }
        else{
            Screen2.fail();
            player.setLocation(width*2/amount,height);
        }
    }
    
    private void FailFloor(){
        boolean failanim=true;
        ImageIcon temp=new ImageIcon();
        switch (CAnim) {
            case "f0":  CAnim="f1";
                        temp=f[1].getIcon();
                        Screen2.stopBG();
                        break;
            case "f1":  failanim=false;
                        break;   
            default:    CAnim="f0";
                        temp=f[0].getIcon();
                        PlayerP=0;
                        break;
        }
        if (failanim){
            Point point = new Point();
            point.setLocation(width*2/amount,height/3-PlayerP-PlayerE);
            player.setLocation(point);
            player.setIcon(temp);
            Timer t = new Timer(animT, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    FailFloor();
                    }
            });
            t.setRepeats( false );
            t.start();
        }
        else
            Screen2.fail();
    }
    
    private void FailWall(){
        boolean failanim=true;
        int shift=0;
        ImageIcon temp=new ImageIcon();
        switch (CAnim) {
            case "w0":  CAnim="w1";
                        temp=w[1].getIcon();
                        if (width<1300)
                            shift=width/54;
                        else
                            shift=width/35;
                        Screen2.stopBG();
                        break;
            case "w1":  CAnim="w2";
                        temp=w[2].getIcon();
                        if (width<1300)
                            shift=width/45;
                        else
                            shift=width/25;
                        break;   
            case "w2":  failanim=false;
                        break;   
            default:    CAnim="w0";
                        temp=w[0].getIcon();
                        if (width<1300)
                            shift=width/27;
                        else
                            shift=width/18;
                        PlayerP=0;
                        break;
        } 
        if (failanim){
            Point point = new Point();
            point.setLocation(width*2/amount+shift,height/3-PlayerP-PlayerE);
            player.setLocation(point);
            player.setIcon(temp);
            Timer t = new Timer(animT, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    FailWall();
                    }
            });
            t.setRepeats( false );
            t.start();
        }
        else
            Screen2.fail();
    }
    
    private void FailWallS(){
        boolean failanim=true;
        int shift=0;
        ImageIcon temp=new ImageIcon();
        switch (CAnim) {
            case "ws0": CAnim="ws1";
                        temp=ws[1].getIcon();
                        if (width<1300)
                            shift=width/48;
                        else
                           shift=width/18; 

                        break;
            case "ws1": failanim=false;

                        break;   
            default:    CAnim="ws0";
                        temp=ws[0].getIcon();
                        PlayerP=0;
                        if (width<1300)
                            shift=width/54;
                        else
                            shift=width/33;
                        Screen2.stopBG();
                        break;
        } 
        if (failanim){
            Point point = new Point();
            point.setLocation(width*2/amount+shift,height/3-PlayerP-PlayerE);
            player.setLocation(point);
            player.setIcon(temp);
            Timer t = new Timer(animT, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    FailWallS();
                    }
            });
            t.setRepeats( false );
            t.start();
        }
        else
            Screen2.fail();
    }
     
    private void FailSaw(){
        boolean failanim=true;
        int shift=0;
        ImageIcon temp=new ImageIcon();
        switch (CAnim) {
            case "s0":  CAnim="s1";
                        temp=s[1].getIcon();
                        if (width<1300)
                            shift=width/20;
                        else
                            shift=width/13;
                        break;
            case "s1":  failanim=false;
                        break;   
            default:    CAnim="s0";
                        temp=s[0].getIcon();
                        PlayerP=0;
                        if (width<1300)
                            shift=width/40;
                        else
                            shift=width/25;
                        Screen2.stopBG();
                        break;
        } 
        if (failanim){
            Point point = new Point();
            point.setLocation(width*2/amount+shift,height/3-PlayerP-PlayerE);
            player.setLocation(point);
            player.setIcon(temp);
            Timer t = new Timer(animT, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    FailSaw();
                    }
            });
            t.setRepeats( false );
            t.start();
        }
        else
            Screen2.fail();
    }
    
    private void FailFW(){
        boolean failanim=true;
        int shift=0;
        ImageIcon temp=new ImageIcon();
        switch (CAnim) {
            case "fw0": CAnim="fw1";
                        temp=fw[1].getIcon();
                        if (width<1300)
                            shift=width/17;
                        else
                            shift=width/15;
                        PlayerP-=height/18;
                        break;
            case "fw1": CAnim="fw2";
                        temp=fw[2].getIcon();
                        if (width<1300)
                        shift=width/14;
                        else
                            shift=width/10;
                        PlayerP-=height/18;
                        break;
            case "fw2": CAnim="fw3";
                        temp=fw[3].getIcon();
                        if (width<1300)
                            shift=width/14;
                        else
                            shift=width/10;
                        PlayerP-=height/30;
                        break;  
            case "fw3": CAnim="null";
                        temp=null;
                        if (width<1300)
                            shift=width/8;
                        else
                          shift=width/5;  
                        break; 
            case "null": failanim=false;
                        break;  
            default:    CAnim="fw0";
                        temp=fw[0].getIcon();
                        Screen2.stopBG();
                        break;
        }
        if (failanim){
            Point point = new Point();
            point.setLocation(width*2/amount+shift,height/3-PlayerP-PlayerE);
            player.setLocation(point);
            player.setIcon(temp);
            Timer t = new Timer(animT, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    FailFW();
                    }
            });
            t.setRepeats( false );
            t.start();
        }
        else
            Screen2.fail();
    }
}