/*
 * @author Elena Chestnov
 */
package main_pkg;
/*  Visuals  */
    import javax.swing.*;
/*  Other  */
    import java.awt.event.*;
    import javax.swing.Timer;
    import java.time.LocalTime;
    import java.time.format.DateTimeFormatter;

public class RunLevel{
/*  Flags and variables  */    
    protected int amount=10;
    protected int width;    //of screen
    protected int height;   //of screen
    protected int difficulty=Screen1.difficulty;
    protected boolean failed;
    protected boolean finished;
    protected boolean paused;
    protected boolean random;
    protected String file="";
    protected LocalTime resultTime;
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("mm:ss");
/*  Visuals&Decorating  */  
    private JLabel [] labels = new JLabel[amount+1];  // for background
    private Queue <Tile> list=new Queue <Tile>();  // for background
    protected JLabel player = new JLabel();
    protected JLabel text = new JLabel();
    private JLabel time;
/*  Timers  */      
    protected int delay;
    protected Timer delayTimer;
    public Timer resultTimer;
/*  Pointers  */     
    private JPanel LevPanel;
    static protected JLabel BgImg;
/*  Classes  */       
    Background bg=new Background();
    LoadLevel ll=new LoadLevel();
    Player pl=new Player();
    
/*  Builders  */       
    public RunLevel(int width, int height, int delay, JPanel LevPanel){
        this.width=width;
        this.height=height;
        this.LevPanel=LevPanel;
        this.delay=delay;
}
    public RunLevel(){}
    
/*  Functions for button events  */  
    public void SpacePrsd (){   //  jump when space is pressed
        if (!pl.jumped&&!paused&&!failed&&!pl.failed){
                pl.stop1();
                pl.Jump();
          }
    }
    public void EscPrsd (){ // pause/unpause
        if(!failed){
            if(!paused){
                if(random)
                    bg.stop();
                else
                    ll.stop();
                pl.stopAll();
                resultTimer.stop();
                paused=true;
                Screen2.PausedMenu(resultTime);
              }
            else {
                Screen2.CloseScreen();
                LevPanel.repaint();
                LevPanel.requestFocus();
                  if(random)
                    bg.start();
                  else
                    ll.start();
                pl.startAll();
                paused=false;  
              }
          }
    }
    
/*  Remove all components  */  
    public void CloseLevel (){
        LevPanel.removeAll();
        LevPanel.repaint();
        Screen1.Scr1Panel.setVisible(true);
        Screen1.Scr1Panel.requestFocus();
        LevPanel.setVisible(false);
    }
    
/*  Set screen resolution  */     
    public void setRes (int width, int height){
        LevPanel.removeAll();
        this.width=width;
        this.height=height;
        LevPanel.setBounds(0,0,width,height);
        player.setBounds(width*2/amount,height/3,height/5,height/5);
        LevPanel.add(player);
        for (int i=0;i<amount+1;i++){
            labels[i].setBounds(width*i/amount,0,width/amount+1,height);
            LevPanel.add(labels[i]);
        }
        time.setBounds(width-width/20-height/108,10,width/10,width/30);
            time.setFont(Screen1.font2);
        LevPanel.add(text);
        text.setBounds(height/108,height/108,width/10,width/30);
            text.setFont(Screen1.font2);
        LevPanel.add(time);
        if (random)
            bg.LoadImg(width, height);
        else
            ll.LoadImg(width, height);
        pl.LoadImg(width, height);
        
        if(random)
            bg.Update();
        else
            ll.Update();
        Screen1.setBg(LevPanel, BgImg);
        LevPanel.repaint();
    }
    
/*  Initiating components  */  
    public void initLevel (){
        Screen1.Scr1Panel.setVisible(false);
        LevPanel.addKeyListener(new java.awt.event.KeyAdapter() {   // listener for keyboard
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });
        LevPanel.setLayout(null);
        for (int i=0;i<amount+1;i++){
            labels[i]=new JLabel();
        }
        time=new JLabel();
        if (random)
            resultTime=LocalTime.of(0,0,0);
        else
            resultTime=Screen2.resultTime;
        time.setText(resultTime.format(dtf));
            time.setForeground(Screen1.color);
        text.setText("Esc to pause");
            text.setForeground(Screen1.color);
        pl.failed=failed=false;
        pl.finished=finished=false;
        paused=false;
        pl.jumped=false;
        pl=new Player(amount, player, list);
        if (random){
            bg=new Background(amount, labels, list);
            bg.difficulty=difficulty;
        }
        else {
            ll=new LoadLevel(amount, labels, list);
            ll.file=file;
            ll.LoadFile();
        }
            
/*  Timer  */  
    resultTimer = new Timer(1000, new ActionListener() {
        @Override
            public void actionPerformed(ActionEvent e) {    // show time on screen
                resultTime=resultTime.plusSeconds(1);
                time.setText(resultTime.format(dtf)); 
            }
        });
        
/*  Level start  */   
        setRes(width, height);
        pl.Start();
        LevPanel.setVisible(true);
  }
    private void formKeyPressed(java.awt.event.KeyEvent evt) {  
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){    // pressed space
                SpacePrsd();
          }
        if(evt.getKeyCode()==KeyEvent.VK_ESCAPE){    // pressed escape
                EscPrsd();
            }
        }
    }   
