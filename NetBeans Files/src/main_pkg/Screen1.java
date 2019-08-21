/*
 * @author Elena Chestnov  * 
 */
package main_pkg;
/*  Visuals  */
    import java.awt.*;
    import javax.swing.*;
/*  Other  */
    import java.io.*;
    import java.awt.event.*;
    import javax.sound.sampled.*;

public class Screen1 extends JFrame{
 /*  Variables  */  
    static public int width=1024;
    static public int height=768;
    static public int delay=300;
    static public String filename="";
    static protected int difficulty=3;
    static protected boolean random;
/*  Visuals&Decorating  */      
    static public JButton [] menu1 = new JButton[5];
    static public JButton optButton = new JButton();
    static public Font font = new Font("Arial", Font.BOLD, 25);
    static public Font font2 = new Font("Arial", Font.BOLD, 15);
    static public Color color = new Color(60, 35, 30);
    static public JLabel BgImg;
    static public Clip BGPlayer;
/*  Screens  */    
    static public int layer;
    static public JFrame frame=new JFrame("Neverending Platrofmer");
    static public JLayeredPane pane=new JLayeredPane();
    static public JPanel Scr1Panel=new JPanel();
    static public JPanel Scr2Panel=new JPanel();
    static public JPanel LevPanel=new JPanel();
    static public JPanel CreaPanel=new JPanel();
    static public JPanel OptPanel=new JPanel();
    static public JPanel LedPanel=new JPanel();
/*  Classes  */      
    static RunLevel rl=new RunLevel();
    static LoadLevel ll=new LoadLevel();
    static CreateLevel cl=new CreateLevel();
    static Screen3 s3=new Screen3();
    static Leaderboard lb=new Leaderboard();
    
/*  Error dialog  */       
    static protected void Error(JPanel panel, String msg){
        JOptionPane.showConfirmDialog (panel, msg, "Warning",JOptionPane.PLAIN_MESSAGE);
    }
    
/*  Set background image  */      
    static protected void setBg (JPanel panel, JLabel BgImg){ 
        BgImg=new JLabel();
        BgImg.setBounds(-1,-1,width-3,height-6);
        String path= System.getProperty( "user.dir" )+"/res/bg.png";
        ImageIcon imageIcon= new ImageIcon(new ImageIcon(path).getImage().getScaledInstance
                                    (width-4, height-5, Image.SCALE_DEFAULT));
        BgImg.setIcon(imageIcon); 
        panel.add(BgImg);
 }

/*  Update resolution&font according to Screen3  */    
    static protected void Update (){
        width=Screen2.width=s3.width;
        height=Screen2.height=s3.height; 
        switch (width){
            case 800:
                font = new Font("Arial", Font.BOLD, 19);
                font2 = new Font("Arial", Font.BOLD, 12);
                break;
            case 1024:
                font = new Font("Arial", Font.BOLD, 22);
                font2 = new Font("Arial", Font.BOLD, 15);
                break;
            case 1280:
                font = new Font("Arial", Font.BOLD, 25);
                font2 = new Font("Arial", Font.BOLD, 17);
                break;
            case 1366:
                font = new Font("Arial", Font.BOLD, 27);
                font2 = new Font("Arial", Font.BOLD, 19);
                break;
            case 1920:
                font = new Font("Arial", Font.BOLD, 28);
                font2 = new Font("Arial", Font.BOLD, 20);
                break;
            default:
                break;
        }
        if (layer==3)
            cl.setRes(width, height);
        else if (layer!=1){
            rl.setRes(width, height);
            Screen2.setRes(width, height);
        }
        setRes();
    }
    
/*  Setting difficulty  */      
    static protected void setDif(int dif){
        difficulty=dif;
        rl.difficulty=rl.bg.difficulty=dif;
    }
    
/*  Background music loop  */       
    public void startBGMusic(){
        BGPlayer=null;
        try {
            File file = new File(System.getProperty( "user.dir" )+"/res/bgmusic.wav");
            BGPlayer=AudioSystem.getClip();
            BGPlayer.open(AudioSystem.getAudioInputStream(file));
        }
        catch(NullPointerException e){
            Error(Scr1Panel,"Music not found.");
        }
        catch(UnsupportedAudioFileException e){
            Error(Scr1Panel,"Music not supported.");
        }
        catch(IOException e){
            Error(Scr1Panel,"Music not found.");
        }
        catch(LineUnavailableException e){
            Error(Scr1Panel,"Music not supported");
        }
        BGPlayer.start();  
    }
    
/*  Retry level  */  
    static protected void Retry (){ 
    rl.CloseLevel();
    if(rl.random){
        rl=new RunLevel(width, height, delay, LevPanel);
        random=rl.random=true;
    }
    else {
        rl=new RunLevel(width, height, delay, LevPanel);
        random=rl.random=false;
        rl.file=filename;
    }
    rl.initLevel();
    LevPanel.requestFocus();    
 }    
    
/*  Set screen resolution  */       
    static public void setRes (){
        frame.setSize(width,height);
        Scr1Panel.removeAll();
        Scr1Panel.setSize(width,height);
        String str= System.getProperty( "user.dir" )+"/res/buttonB.png";
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(str).getImage().getScaledInstance
                                        (width-100,height/8, Image.SCALE_DEFAULT));
        for (int i=0;i<5;i++){
            menu1[i].setBounds(40,height/8+height*i/7,width-100,height/8);
                menu1[i].setIcon(imageIcon);
                menu1[i].setFont(font);
            Scr1Panel.add(menu1[i]);
        }
        str= System.getProperty( "user.dir" )+"/res/buttonS.png";
        imageIcon = new ImageIcon(new ImageIcon(str).getImage().getScaledInstance
                                        (width/10,width/30, Image.SCALE_DEFAULT));
        optButton.setBounds(height/108,height/108,width/10,width/30); 
            optButton.setIcon(imageIcon);
            optButton.setFont(font2);
            
        setBg(Scr1Panel, BgImg);
        Scr1Panel.repaint();
    }
    
/*  Initiating main menu  */  
static protected void MainScreen (){
    Scr1Panel.setLayout(null);
    for (int i=0;i<5;i++){
        menu1[i]=new JButton();
            menu1[i].setVerticalTextPosition(JButton.CENTER);
            menu1[i].setHorizontalTextPosition(JButton.CENTER);
            menu1[i].setForeground(color);
    }
    optButton.setVisible(true);
        optButton.setVerticalTextPosition(JButton.CENTER);
        optButton.setHorizontalTextPosition(JButton.CENTER);
        optButton.setForeground(color);
        
    menu1[0].setText("Random Level");
        menu1[0].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rl=new RunLevel(width, height, delay, LevPanel);
                optButton.setVisible(false);
                random=rl.random=true;
                rl.initLevel();
                LevPanel.requestFocus();
            }
        });
    menu1[1].setText("Leaderboard");
        menu1[1].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                lb=new Leaderboard(width, height, LedPanel);
                lb.Display(layer);
            }
        });
    menu1[2].setText("Load Level");
        menu1[2].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame("");
                FileDialog fd = new FileDialog(frame, "Choose a file", FileDialog.LOAD);
                fd.setDirectory(System.getProperty( "user.dir" )+"\\levels");
                fd.setFile("1.txt");
                fd.setVisible(true);
                filename = fd.getFile();
                if (filename == null)
                    Error(Scr1Panel, "You canceled the choice");
                else {
                    ll.file=filename;
                    if(ll.IsValidLevel())
                    {
                        rl=new RunLevel(width, height, delay, LevPanel);
                        rl.file=filename;
                        random=rl.random=false;
                        optButton.setVisible(false);
                        rl.initLevel();
                        LevPanel.requestFocus();
                    }
                    else {
                        Scr1Panel.requestFocus();
                        Error(Scr1Panel,"File has been corrupted or changed.");
                    }
                }
            }
        });
    menu1[3].setText("Level Creation");
        menu1[3].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cl=new CreateLevel(width, height, CreaPanel);
                cl.initCreation();
                CreaPanel.requestFocus();
            }
        }); 
    menu1[4].setText("Exit to desktop");
        menu1[4].addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int dialogButton = JOptionPane.YES_NO_OPTION;
                int dialogResult = JOptionPane.showConfirmDialog 
                                (Scr1Panel, "Are you sure you want to exit?","Warning",dialogButton);
                if(dialogResult == JOptionPane.YES_OPTION){
                    System.exit(0);
                }
                else
                    frame.requestFocus();
            }
        });
    optButton.setText("Options");    
        optButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                s3=new Screen3(width, height, OptPanel);
                s3.OptionsScreen(layer);
            }
        });
    setRes();
    Scr1Panel.requestFocus();
    Scr1Panel.setVisible(true);
 }
 
/*  Creates new form  */   
     public Screen1() {
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setResizable(false);
        ImageIcon icon = new ImageIcon(System.getProperty( "user.dir" )+"/res/icon.png");
        frame.setIconImage(icon.getImage());
        
        pane=new JLayeredPane();
        pane.add(OptPanel, new Integer(300),1);
        pane.add(optButton, new Integer(300),0); 
        pane.add(LedPanel, new Integer(200),0);  
        pane.add(Scr2Panel, new Integer(200),1);  // layer=2
        pane.add(LevPanel, new Integer(100),0); 
        pane.add(CreaPanel, new Integer(100),0);  // layer=3
        pane.add(Scr1Panel, new Integer(0),0);  // layer=1
        
        frame.add(pane);
        frame.setVisible(true);
        frame.setAutoRequestFocus(true);
        layer=1;
        startBGMusic();
        MainScreen();
        
        frame.addWindowListener(new WindowAdapter() {
            @Override
        public void windowClosing(WindowEvent we) {
            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog
                           (Scr1Panel, "Are you sure you want to exit?","Warning",dialogButton);
            if(dialogResult == JOptionPane.YES_OPTION){
                System.exit(0);
            }
            else
                Scr1Panel.requestFocus();
            }
        });
     }
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Screen1();
            }
        });
    }
}