/*
 * @author Elena Chestnov
 */
package main_pkg;
/*  Visuals  */
    import java.awt.*;
    import javax.swing.*;
/*  Other  */
    import java.awt.event.*;
    import java.time.LocalTime;
    import java.time.format.DateTimeFormatter;

public class Screen2 extends Screen1 {
/*  Flags and variables  */      
    static protected int width=Screen1.width;    // of screen
    static protected int height=Screen1.height;   // of screen
    static protected String creator="";
    static protected Results [] table;
    static protected String dif;
    static protected LocalTime resultTime=LocalTime.of(0,0,0);
/*  Visuals&Decorating  */  
    static protected JButton [] menu2=new JButton[2];
    static protected JButton [] menu3=new JButton[3];
    static protected JLabel time;
    static protected JLabel text;
    static protected JLabel crlab;
/*  Pointers  */      
    static private JPanel Scr2Panel=Screen1.Scr2Panel;
    static private JLabel BgImg;
    
/*  Timers  */     
    static public Timer delayTimer = new Timer(delay*2, new ActionListener() {  // Delay in order to close everything neatly
            @Override
            public void actionPerformed(ActionEvent e) { 
                FinMenu(rl.resultTime);
            }
        });;    
    
/*  Remove menus  */      
    static protected void CloseScreen (){  
        layer=1;
        Scr2Panel.removeAll();
        Scr2Panel.repaint();
        Scr2Panel.setVisible(false);
 }
    
/*  For loaded level  */    
    static public void update(String c, Results [] t, int difficulty){
        creator=c;
        table=t;
        switch (difficulty){
            case 5:     dif="Easy";
                        break;
            case 4:     dif="Normal";
                        break;
            case 3:     dif="Hard";
                        break;
            default:    break;
            }
        }  
    
/*  Save result dialog  */    
    static private void SaverT (LocalTime rT){
        int dialogResult = JOptionPane.showConfirmDialog (Scr2Panel, "Would you like to save your result?","?",JOptionPane.YES_NO_OPTION);
        if(dialogResult == JOptionPane.YES_OPTION){
            String s = (String)JOptionPane.showInputDialog(Scr2Panel,"Enter your name:","?",JOptionPane.QUESTION_MESSAGE);
            if ((s != null) && (s.length() > 0)&&(s.length()<60)){
                if (rl.random)
                    lb.add(s, rT);
                else
                    ll.add(s, rT);
            }
            else if ((s != null) &&(s.length()>60))
                Error(Scr2Panel,"Name must be shorter than 60 characters.");
            else
                    Error(Scr2Panel,"You didn't provide a name.");
        }
    }
    
/*  Stop game in case of fail  */    
    static public void fail(){   
        rl.failed=true;
        if(rl.random)
            rl.bg.stop();
        else
            rl.ll.stop();
            rl.resultTimer.stop();
            delayTimer.setRepeats( false );
            delayTimer.start();
        }

/*  Stop game in case of win  */       
    static public void won(){   
        rl.finished=true;
        delayTimer.setRepeats( false );
        delayTimer.start();
        }

/*  Stop background movement  */       
    static public void stopBG(){   
        if(rl.random)
            rl.bg.stop();
        else
            rl.ll.stop();
        rl.resultTimer.stop();
}

/*  Start all timers  */       
    static public void startAll(){   
        if(rl.random)
            rl.bg.start();
        else
            rl.ll.start();
        rl.pl.startAll();
        rl.resultTimer.start();
        }
    
 /*  Set screen resolution  */        
    static public void setRes (int width, int height){
        Scr2Panel.removeAll();
        Scr2Panel.setSize(width,height);
        text.setFont(font);
        time.setFont(font2);
        String str= System.getProperty( "user.dir" )+"/res/buttonB.png";
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(str).getImage().getScaledInstance(width-100,height/8, Image.SCALE_DEFAULT));
        if (!rl.random){
            crlab.setBounds(width/2-150,height-150,300,100);
            crlab.setHorizontalAlignment(JLabel.CENTER);
            crlab.setFont(font);
            Scr2Panel.add(crlab);
        }
        if (rl.failed||rl.finished){ 
            for (int i=0;i<3;i++){
                menu3[i].setBounds(40,height/7+height*i/7,width-100,height/8);
                    menu3[i].setIcon(imageIcon);
                    menu3[i].setFont(font);
                Scr2Panel.add(menu3[i]);
            }
            text.setBounds(width/2-150,-10,300,100);
            time.setBounds(width/2-150,20,300,100);
        }
        else if (rl.paused){ 
            for (int i=0;i<2;i++){
                menu2[i].setBounds(40,height/4+height*i/6,width-100,height/8);
                    menu2[i].setIcon(imageIcon);
                    menu2[i].setFont(font);
                Scr2Panel.add(menu2[i]);
            }
            text.setBounds(width/2-150,height/20,300,100);
            time.setBounds(width/2-150,height/10,300,100); 
        }
        Scr2Panel.add(text);
        Scr2Panel.add(time);
        setBg(Scr2Panel, BgImg);  
        Scr2Panel.repaint();
    }
    
/*  Screen 2  */      
    static protected void initScreen2 (){
        layer=2;
        Scr2Panel.setLayout(null);
        Scr2Panel.setVisible(true);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("mm:ss");
        time=new JLabel();
            time.setText("Your Result is "+resultTime.format(dtf)); 
            time.setHorizontalAlignment(JLabel.CENTER);
            time.setForeground(color);
        text=new JLabel();
            text.setHorizontalAlignment(JLabel.CENTER);
        if (!rl.random){
            crlab=new JLabel();
            crlab.setText   ("<html><p style='text-align:center;'>"
                            + "<br/>Level by: "+creator
                            + "<br/>Difficulty: "+dif
                            + "</html>");
            crlab.setForeground(color);
        }
        if (rl.failed)
            text.setText("You lost!"); 
        else if (rl.finished)
                text.setText("You've won!"); 
        else if (rl.paused)
            text.setText("Paused"); 
        text.setForeground(color);
                optButton.setVisible(true);
        LevPanel.requestFocus();
    } 

/*  Menu for paused game  */      
    static protected void PausedMenu (LocalTime rT){
        resultTime=rT;
        initScreen2();
        for (int i=0;i<2;i++){
            menu2[i]=new JButton();
                menu2[i].setVerticalTextPosition(JButton.CENTER);
                menu2[i].setHorizontalTextPosition(JButton.CENTER);
                menu2[i].setForeground(color);
        }
        menu2[0].setText("Resume");
            menu2[0].addActionListener(new ActionListener() { 
                @Override
                public void actionPerformed(ActionEvent e) {
                    CloseScreen();
                    frame.requestFocus();
                    rl.EscPrsd();
                    optButton.setVisible(false);
                }
            });
        menu2[1].setText("Exit Level");
            menu2[1].addActionListener(new ActionListener() { 
                @Override
                public void actionPerformed(ActionEvent e) {
                    int dialogButton = JOptionPane.YES_NO_OPTION;
                    int dialogResult = JOptionPane.showConfirmDialog (Scr2Panel, "Are you sure you want to exit?","?",dialogButton);
                    if(dialogResult == JOptionPane.YES_OPTION){
                        rl.failed=true;
                        CloseScreen();
                        FinMenu(rT);
                    }
                    else
                        Scr2Panel.requestFocus();  
                }
            });
        setRes(width,height);
    }
 
/*  Menu for finished game  */     
    static protected void FinMenu (LocalTime rT){
        resultTime=rT;
        initScreen2();
        if (rl.finished)
            resultTime=LocalTime.of(0,0,0);
        for (int i=0;i<3;i++){
            menu3[i]=new JButton();
                menu3[i].setVerticalTextPosition(JButton.CENTER);
                menu3[i].setHorizontalTextPosition(JButton.CENTER);
                menu3[i].setForeground(color);
        }
        menu3[0].setText("Leader Board");
            menu3[0].addActionListener(new ActionListener() { 
                @Override
                public void actionPerformed(ActionEvent e) {
                    lb=new Leaderboard(width, height, LedPanel);
                    if (rl.random)
                        lb.Display(layer);
                    else 
                        lb.Display(table);
                }
            });
        menu3[1].setText("Retry");        
            menu3[1].addActionListener(new ActionListener() { 
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (rl.random||rl.finished)
                        SaverT(rT);
                    else
                        rl.resultTime=rT;
                    optButton.setVisible(false);
                    LevPanel.requestFocus();
                    Retry();
                    CloseScreen();
                }
            });
        menu3[2].setText("Back to Main Menu");        
            menu3[2].addActionListener(new ActionListener() { 
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (rl.random||rl.finished)
                        SaverT(rT);
                    creator="";
                    resultTime=LocalTime.of(0,0,0);
                    rl.CloseLevel();
                    CloseScreen();
                }
            });
        setRes(width,height);
    } 
}