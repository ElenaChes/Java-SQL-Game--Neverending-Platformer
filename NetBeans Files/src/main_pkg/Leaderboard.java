/*
 * @author Elena Chestnov  * 
 */
package main_pkg;
/*  Visuals  */
    import java.awt.*;
    import javax.swing.*;
/*  Other  */
    import java.sql.*;
    import java.awt.event.*;
    import java.time.LocalTime;
    import java.time.format.DateTimeFormatter;

public class Leaderboard {
/*  Flags and variables  */      
    static private int width;    //of screen
    static private int height;   //of screen
    static private int before;
/*  Visuals&Decorating  */      
    static private JLabel [] line1 = new JLabel[10];
    static private JLabel [] line2 = new JLabel[10];
    static private JLabel [] title = new JLabel[2];
    static private JLabel ScrName = new JLabel();
    static private JButton back;
/*  Pointers  */         
    static private JPanel LedPanel;
    static private JLabel BgImg;
     
/*  Builders  */     
    public Leaderboard(int width, int height, JPanel LedPanel){
        this.width=width;
        this.height=height;
        this.LedPanel=LedPanel;
    }
    public Leaderboard() {}
    
/*  Remove all components  */  
    static public void CloseScreen (){
        LedPanel.removeAll();
        LedPanel.setBackground(null);
        LedPanel.repaint();
        switch (before) {
            case 1: Screen1.Scr1Panel.setVisible(true);
                break;
            case 2: Screen1.Scr2Panel.setVisible(true);
                break;
            default: break;
        }
        Screen1.optButton.setVisible(true);
        LedPanel.setVisible(false);
    } 
    
/*  Initiate screen  */    
    static public void LB (){
        Screen1.Scr1Panel.setVisible(false);
        Screen1.Scr2Panel.setVisible(false);
        Screen1.optButton.setVisible(false);
        LedPanel.setBounds(0,0,width,height);
        LedPanel.setLayout(null);
        back=new JButton();
        back.setBounds(height/108,height/108,width/10,width/30);
        String str= System.getProperty( "user.dir" )+"/res/buttonS.png";
            ImageIcon imageIcon = new ImageIcon(new ImageIcon(str).getImage().getScaledInstance(width/10,width/30, Image.SCALE_DEFAULT));
            back.setIcon(imageIcon);
            back.setVerticalTextPosition(JButton.CENTER);
            back.setHorizontalTextPosition(JButton.CENTER);
            back.setForeground(Screen1.color);
        back.setFont(Screen1.font2);
        LedPanel.add(back);
        ScrName=new JLabel();
            ScrName.setBounds(width/2-width/4,height/54,width/2,30);
            ScrName.setText("Leaderboard");
            ScrName.setHorizontalAlignment(JLabel.CENTER);
            ScrName.setForeground(Screen1.color);
            ScrName.setFont(Screen1.font);
        LedPanel.add(ScrName);
            for (int i=0;i<2;i++){
                title [i]= new JLabel();
                    title[i].setBounds(width/8+i*(width/4),height/10,width/2,30);
                    title[i].setHorizontalAlignment(JLabel.CENTER);
                    title[i].setForeground(Screen1.color);
                    title[i].setFont(Screen1.font);
                LedPanel.add(title[i]);
            }
                title[0].setText("Player Name");
                title[1].setText("Result");
    }
    
/*  Screen for loaded level  */    
    static public void Display(Results [] table){
        before=2;
        LB();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("mm:ss");
        for (int i=0;i<5;i++){    
            line1 [i]= new JLabel();
            line2 [i]= new JLabel();
            line1[i].setText(table[i].getPlayer());
            if(table[i].getTime()!=LocalTime.of(0,0,0))
                line2[i].setText((table[i].getTime()).format(dtf));
            line1[i].setBounds(width/8,height/6+i*height/27,width/2,30);
            line2[i].setBounds(width/8+width/4,height/6+i*height/27,width/2,30);
            line1[i].setHorizontalAlignment(JLabel.CENTER);
            line2[i].setHorizontalAlignment(JLabel.CENTER);
            line1[i].setForeground(Screen1.color);
            line2[i].setForeground(Screen1.color);
            line1[i].setFont(Screen1.font2);
            line2[i].setFont(Screen1.font2);
            LedPanel.add(line1[i]);
            LedPanel.add(line2[i]);
        }
        back.setText("Back");        
            back.addActionListener(new ActionListener() { 
                @Override
                    public void actionPerformed(ActionEvent e) {
                    CloseScreen();
                }
            }); 
        LedPanel.repaint();
        LedPanel.requestFocus();
        Screen1.setBg(LedPanel, BgImg);
        LedPanel.setVisible(true);
    }
       
/*  Screen for random level  */    
    static public void Display(int b){  // For random levels
        before=b;
        LB();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("mm:ss");
        try{
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            String msAccDB = System.getProperty( "user.dir" )+"/res/Database.accdb";
            String dbURL = "jdbc:ucanaccess://" + msAccDB; 
            Connection conn = DriverManager.getConnection(dbURL); 
            Statement s = conn.createStatement();
            String query="";
            query="SELECT * FROM Leaderboard ORDER by Time DESC";    // command 
            ResultSet rs = s.executeQuery(query);
            int i=0;
            while (rs.next()){    
                String temp1=rs.getString("Player");
                Time temp2=rs.getTime("Time");
                LocalTime temp3=LocalTime.of(temp2.getHours(),temp2.getMinutes(),temp2.getSeconds());
                line1 [i]= new JLabel();
                line2 [i]= new JLabel();
                line1[i].setText(temp1);
                line2[i].setText(temp3.format(dtf));
                line1[i].setBounds(width/8,height/6+i*height/27,width/2,30);
                line2[i].setBounds(width/8+width/4,height/6+i*height/27,width/2,30);
                line1[i].setHorizontalAlignment(JLabel.CENTER);
                line2[i].setHorizontalAlignment(JLabel.CENTER);
                line1[i].setForeground(Screen1.color);
                line2[i].setForeground(Screen1.color);
                line1[i].setFont(Screen1.font2);
                line2[i].setFont(Screen1.font2);
                LedPanel.add(line1[i]);
                LedPanel.add(line2[i]);
                i++;
            }
            s.close();  // Close the statement
            conn.close();   // Close the database.
            back.setText("Back");        
                back.addActionListener(new ActionListener() { 
                    @Override
                    public void actionPerformed(ActionEvent e) {
                    CloseScreen();
                }
            }); 
            LedPanel.repaint();
            LedPanel.requestFocus();
            Screen1.setBg(LedPanel, BgImg);
            LedPanel.setVisible(true);
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        catch(ClassNotFoundException ex) {
            Screen1.Error(Screen1.LevPanel,"Problem in loading or "
                + "registering MS Access JDBC driver");
            }
    }
    
/*  Get smallest time in table&return if it's worse than new time */     
    static public int getWorst(LocalTime resultTime){
        try{
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            String msAccDB = System.getProperty( "user.dir" )+"/res/Database.accdb";
            String dbURL = "jdbc:ucanaccess://" + msAccDB; 
            Connection conn = DriverManager.getConnection(dbURL); 
            Statement s = conn.createStatement();
            String query="";
            query="SELECT * FROM Leaderboard ORDER by Time ASC";    // command 
            ResultSet rs = s.executeQuery(query);
            rs.next();
            int worst=rs.getInt("ID");
            Time temp=rs.getTime("Time");
            LocalTime old=LocalTime.of(temp.getHours(),temp.getMinutes(),temp.getSeconds());
            s.close();  // Close the statement
            conn.close();   // Close the database.
            if (old.isBefore(resultTime))
                return worst;
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        catch(ClassNotFoundException ex) {
            Screen1.Error(Screen1.LevPanel,"Problem in loading or "
                + "registering MS Access JDBC driver");
        }
        return -1;
    }

/*  Add new result to board  */     
    static public void add(String player, LocalTime resultTime){
        try{
            int worst=getWorst(resultTime);
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            String msAccDB = System.getProperty( "user.dir" )+"/res/Database.accdb";
            String dbURL = "jdbc:ucanaccess://" + msAccDB; 
            Connection conn = DriverManager.getConnection(dbURL); 
            PreparedStatement s = conn.prepareStatement
                    ("UPDATE Leaderboard SET Player=?,Time=? where ID=? ;");
            s.setString(1, player);
            s.setTime(2, java.sql.Time.valueOf(resultTime));
            s.setInt(3,worst);
            s.executeUpdate();
            s.close();  // Close the statement
            conn.close();   // Close the database.
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        catch(ClassNotFoundException ex) {
            Screen1.Error(Screen1.LevPanel,"Problem in loading or "
                + "registering MS Access JDBC driver");
        }
    } 
}