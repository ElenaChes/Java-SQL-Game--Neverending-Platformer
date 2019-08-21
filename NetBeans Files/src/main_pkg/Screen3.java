/*
 * @author Elena Chestnov  * 
 */
package main_pkg;
    import javax.swing.*;
    import java.awt.Image;
    import java.awt.event.*;
    import java.awt.ComponentOrientation;

public class Screen3 {
/*  Flags and variables  */      
    static protected int width;    //of screen
    static protected int height;   //of screen
    static private int before;
/*  Visuals&Decorating  */       
    static private JLabel reslab;
    static private JComboBox res;
    static private JButton save;
    static private JLabel diflab;
    static private JComboBox dif;
    static private JButton savedif;
    static private JButton music;
    static private JButton lang;
    static private JLabel help;
    static private JLabel dev;
    static private String streng="<html>"
                + "<p><h1>Playing:"
                + "</h1><ul><li>Press SPACE to jump.</li>"
                + "<br/><li>Avoid traps to get best score.</ul></li>"
                
                + "</p><br/><p><h1>Loading levels:"
                + "</h1><ul><li>The file must be in folder 'levels' to work correctly.</li>"
                + "<br/><li>Avoid all traps to get the lowest time.</li>"
                + "<br/><li>Compare your score to other players via the level's leaderboard.</ul></li>"
                
                + "</p><br/><p><h1>Creating levels:"
                + "</h1><ul><li>Choose a tile from the list to create your level.</li>"
                + "<br/><li>Use the '+' and '-' buttons to change length of a tile set.</li>"
                + "<br/><li>Use 'Next' to move to next tile, or 'Back' to delete.</li>"
                + "<br/><li>Press 'Finish' to export.</li>"
                + "<br/><li>The exported file can be found in the 'levels' folder.</ul></li>"
                + "</p></html>";
        
    static private String strheb="<html><div align=right width=%100>"
                + "<p><h1>הוראות משחק:"
                + "</h1><ul><li>לחץ על רווח כדי לקפוץ.</li>"
                + "<br/><li>המנע ממלכודות כדי לצבור זמן מירבי.</ul></li>"
                
                + "</p><br/><p><h1>טעינת שלבים:"
                + "</h1><ul><li>על הקובץ להיות בתקיית levels כדי לעבוד כראוי.</li>"
                + "<br/><li>המנע מהמלכודות כדי להגיע לזמן הכי קצר.</li>"
                + "<br/><li>השווה את התוצאות שלך מול שחקנים אחרים בטבלת leaderboard של השלב.</ul></li>"
                
                + "</p><br/><p><h1>יצירת שלבים:"
                + "</h1><ul><li>בחר משטח מהרשימה כדי ליצור את השלב שלך.</li>"
                + "<br/><li>השתמש בכפתורי ה+ וה- כדי לשנות את אורך הסטים של המשטחים.</li>"
                + "<br/><li>השתמש בכפתור Next כדי לעבור לבחירה הבאה וכפתור Back כדי למחוק ולחזור חזרה.</li>"
                + "<br/><li>לחץ על Finish כדי לייצא את השלב.</li>"
                + "<br/><li>השלב שנוצר נמצא בתקיית levels.</ul></li>"
                + "</p></div></html>";
    
    static private String str2eng="<html>"
                + "<p><h1>'Neverending Platformer' was made by</h1>"
                + "<h2>Elena Chestnov,"
                + "<br/>The Technological college, Be'er Sheva."
                + "</h2></p></html>";
    
    static private String str2heb="<html><div align=right width=%100>"
                + "<p><h1>'פלטפורמר אינסופי' נכתב על ידי</h1>"
                + "<h2>ילנה צ'סטנוב,"
                + "<br/>המכללה הטכנולוגית, באר שבע."
                + "</h2></p></div></html>";
/*  Pointers  */      
    static private JPanel OptPanel;
    static private JLabel BgImg;
    
/*  Builders  */ 
    public Screen3 (int width, int height, JPanel OptPanel){
    this.width=width;
    this.height=height;
    this.OptPanel=OptPanel;
}
    public Screen3 (){}
  
/*  Remove menus  */       
    static protected void CloseScreen (){  
        OptPanel.removeAll();
        OptPanel.repaint();
        switch (before) {
            case 1: Screen1.Scr1Panel.setVisible(true);
                break;
            case 2: Screen1.Scr2Panel.setVisible(true);
                break;
            case 3: Screen1.CreaPanel.setVisible(true);
                break;
            default: break;
        }
        Screen1.optButton.setVisible(true);
        OptPanel.setVisible(false);
 }     
    
/*  Hide components  */       
    static protected void hideAll (){  
        reslab.setVisible(false);
        res.setVisible(false);
        save.setVisible(false);
        if (before==1||(before==2&&Screen1.random)){
            diflab.setVisible(false);
            dif.setVisible(false);
            savedif.setVisible(false);
        }
        music.setVisible(false);
        lang.setVisible(false);
        help.setVisible(false);
        dev.setVisible(false);
 }     
    
/*  Options Screen  */      
    static protected void OptionsScreen (int b){
        before=b;
        Screen1.Scr1Panel.setVisible(false);
        Screen1.Scr2Panel.setVisible(false);
        Screen1.CreaPanel.setVisible(false);
        Screen1.optButton.setVisible(false);
        OptPanel.removeAll();
        OptPanel.setLayout(null);
        OptPanel.setSize(width,height);
        OptPanel.requestFocus();
        
        JButton [] menu4=new JButton[4];
        String str= System.getProperty( "user.dir" )+"/res/buttonM.png";
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(str).getImage().getScaledInstance(width/5,width/30, Image.SCALE_DEFAULT));
        for (int i=0;i<4;i++){
            menu4[i]=new JButton();
                menu4[i].setBounds(height/54,height/54+width/30*i,width/5,width/30);
                menu4[i].setIcon(imageIcon);
                menu4[i].setVerticalTextPosition(JButton.CENTER);
                menu4[i].setHorizontalTextPosition(JButton.CENTER);
                menu4[i].setForeground(Screen1.color);
                menu4[i].setFont(Screen1.font2);
            OptPanel.add(menu4[i]);
        }
        reslab=new JLabel();
            reslab.setBounds(width/4,height/54,width,height/27);
            reslab.setText("Select resolution");
            reslab.setForeground(Screen1.color);
            reslab.setFont(Screen1.font2);
        OptPanel.add(reslab);
            
        res=new JComboBox();
            res.setBounds(width/4,height/18,width/5,width/30);
            res.addItem("800x600");
            res.addItem("1024x768");
            res.addItem("1280x720");
            res.addItem("1366x768");
            res.addItem("1920x1080");
            res.setForeground(Screen1.color);
            res.setFont(Screen1.font2);
        switch (width){
            case 800:
                res.setSelectedItem("800x600");
                break;
            case 1024:
                res.setSelectedItem("1024x768");
                break;
            case 1280:
                res.setSelectedItem("1280x720");
                break;
            case 1366:
                res.setSelectedItem("1366x768");
                break;
            case 1920:
                res.setSelectedItem("1920x1080");
                break;
            default:
                break;
        }
        OptPanel.add(res);

        save=new JButton();
            save.setBounds(width/4,height/8,width/5,width/30);
            save.setIcon(imageIcon);
            save.setVerticalTextPosition(JButton.CENTER);
            save.setHorizontalTextPosition(JButton.CENTER);
            save.setForeground(Screen1.color);
            save.setFont(Screen1.font2);
        OptPanel.add(save);
        
        if (before==1||(before==2&&Screen1.random)){
            diflab=new JLabel();
            diflab.setBounds(width/2,height/54,width,height/27);
            diflab.setText("Select difficulty");
            diflab.setForeground(Screen1.color);
            diflab.setFont(Screen1.font2);
            OptPanel.add(diflab);
        
            dif=new JComboBox();
            dif.setBounds(width/2,height/18,width/5,width/30);
            dif.addItem("Easy");
            dif.addItem("Normal");
            dif.addItem("Hard");
            dif.setForeground(Screen1.color);
            dif.setFont(Screen1.font2);
            switch (Screen1.difficulty) {
                case 5: dif.setSelectedItem("Easy");
                        break;
                case 4: dif.setSelectedItem("Normal");
                        break;
                case 3: dif.setSelectedItem("Hard");
                        break;
                default:break;   
            }
            OptPanel.add(dif);

            savedif=new JButton();
            savedif.setBounds(width/2,height/8,width/5,width/30);
            savedif.setIcon(imageIcon);
            savedif.setVerticalTextPosition(JButton.CENTER);
            savedif.setHorizontalTextPosition(JButton.CENTER);
            savedif.setForeground(Screen1.color);
            savedif.setFont(Screen1.font2);
            OptPanel.add(savedif);
        }
            
        music=new JButton();
            music.setBounds(width/4,height/5,width/5,width/30);
            music.setIcon(imageIcon);
            music.setVerticalTextPosition(JButton.CENTER);
            music.setHorizontalTextPosition(JButton.CENTER);
            music.setForeground(Screen1.color);
            music.setFont(Screen1.font2);
        OptPanel.add(music);   
         
        lang=new JButton();
            lang.setBounds(width/4,height/54,width/5,width/30);
            lang.setIcon(imageIcon);
            lang.setVerticalTextPosition(JButton.CENTER);
            lang.setHorizontalTextPosition(JButton.CENTER);
            lang.setForeground(Screen1.color);
            lang.setFont(Screen1.font2);
        OptPanel.add(lang);
            
        help=new JLabel();
            help.setBounds(width/4,height/27,width-width/3,height);
            help.setText(streng);
            help.setVerticalAlignment(JLabel.TOP);
            help.setForeground(Screen1.color);
            help.setFont(Screen1.font2);
        OptPanel.add(help);
            
        dev=new JLabel();
            dev.setBounds(width/4,height/27,width-width/3,height);
            dev.setText(str2eng);
            dev.setVerticalAlignment(JLabel.TOP);
            dev.setForeground(Screen1.color);
            dev.setFont(Screen1.font2);
        OptPanel.add(dev);
        
        menu4[0].setText("Gameplay");
            menu4[0].addActionListener(new ActionListener() { 
                @Override
                public void actionPerformed(ActionEvent e) {
                    hideAll();
                    reslab.setVisible(true);
                    res.setVisible(true);
                    save.setVisible(true);
                    if (before==1||(before==2&&Screen1.random)){
                        diflab.setVisible(true);
                        dif.setVisible(true);
                        savedif.setVisible(true);
                    }
                    music.setVisible(true);
                    OptPanel.repaint();
                }
            });
        menu4[1].setText("Help");        
            menu4[1].addActionListener(new ActionListener() { 
                @Override
                public void actionPerformed(ActionEvent e) {
                    hideAll();
                    lang.setVisible(true);
                    help.setVisible(true);
                    OptPanel.repaint();
                }
            });
        menu4[2].setText("About");        
            menu4[2].addActionListener(new ActionListener() { 
                @Override
                public void actionPerformed(ActionEvent e) {
                    hideAll();
                    lang.setVisible(true);
                    dev.setVisible(true);
                    OptPanel.repaint();
                }
            });   
        menu4[3].setText("Back");        
            menu4[3].addActionListener(new ActionListener() { 
                @Override
                public void actionPerformed(ActionEvent e) {
                    CloseScreen();
                }
            }); 
        save.setText("Change resolution");        
            save.addActionListener(new ActionListener() { 
                @Override
                public void actionPerformed(ActionEvent e) {
                    String temp=(String)res.getSelectedItem();
                    if (temp!=null){
                        switch (temp) {
                            case "800x600":     width=800;
                                                height=600;
                                                break;
                            case "1024x768":    width=1024;
                                                height=768;
                                                break;
                            case "1280x720":    width=1280;
                                                height=720;
                                                break;
                            case "1366x768":    width=1366;
                                                height=768;
                                                break;
                            case "1920x1080":   width=1920;
                                                height=1080;
                                                break;
                            default: break;
                        }
                        CloseScreen();
                        Screen1.Update();
                        OptionsScreen(before);
                        reslab.setVisible(true);
                        res.setVisible(true);
                        save.setVisible(true);
                        music.setVisible(true);
                        if (before==1||(before==2&&Screen1.random)){
                            diflab.setVisible(true);
                            dif.setVisible(true);
                            savedif.setVisible(true);
                        }
                        switch (width){
                            case 800:
                                res.setSelectedItem("800x600");
                                break;
                            case 1024:
                                res.setSelectedItem("1024x768");
                                break;
                            case 1280:
                                res.setSelectedItem("1280x720");
                                break;
                            case 1366:
                                res.setSelectedItem("1366x768");
                                break;
                            case 1920:
                                res.setSelectedItem("1920x1080");
                                break;
                            default:
                                break;
                        }
                    }
                }
            }); 
            if (before==1||(before==2&&Screen1.random)){
                savedif.setText("Change difficulty");        
                savedif.addActionListener(new ActionListener() { 
                @Override
                public void actionPerformed(ActionEvent e) {
                    String temp=(String)dif.getSelectedItem();
                    if (temp!=null){
                        switch (temp) {
                            case "Easy":    Screen1.setDif(5);
                            JOptionPane.showConfirmDialog (
                                    OptPanel, "Difficulty set to easy.", " ",JOptionPane.PLAIN_MESSAGE);
                                            break;
                            case "Normal":  Screen1.setDif(4);
                            JOptionPane.showConfirmDialog (
                                    OptPanel, "Difficulty set to normal.", " ",JOptionPane.PLAIN_MESSAGE);
                                            break;
                            case "Hard":    Screen1.setDif(3);
                            JOptionPane.showConfirmDialog (
                                    OptPanel, "Difficulty set to hard.", " ",JOptionPane.PLAIN_MESSAGE);
                                            break;
                            default:        break;
                        }
                    }
                }
            }); 
            }
        if(Screen1.BGPlayer.isRunning())
            music.setText("Mute music");    
        else
            music.setText("Play music"); 
            music.addActionListener(new ActionListener() { 
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(music.getText()=="Mute music"){
                        Screen1.BGPlayer.stop();
                        music.setText("Play music"); 
                    }
                    else {
                        Screen1.BGPlayer.start();
                        music.setText("Mute music"); 
                    }
                }
            }); 
        lang.setText("עברית"); 
            lang.addActionListener(new ActionListener() { 
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(lang.getText()=="English"){
                        help.setComponentOrientation( ComponentOrientation.LEFT_TO_RIGHT);
                        help.setText(streng);
                        dev.setComponentOrientation( ComponentOrientation.LEFT_TO_RIGHT);
                        dev.setText(str2eng);
                        lang.setText("עברית"); 
                    }
                    else {
                        help.setComponentOrientation( ComponentOrientation.RIGHT_TO_LEFT );
                        help.setText(strheb);
                        dev.setComponentOrientation( ComponentOrientation.RIGHT_TO_LEFT );
                        dev.setText(str2heb);
                        lang.setText("English"); 
                    }
                }
            }); 
        Screen1.setBg(OptPanel, BgImg);
        hideAll();
        OptPanel.setVisible(true);
        OptPanel.repaint();
    }
}
