/*
 * @author Elena Chestnov
 */
package main_pkg;
/*  Visuals  */
    import java.awt.*; 
    import javax.swing.*;
/*  Other  */
    import java.io.*;
    import java.sql.*;
    import java.awt.event.*;

public class CreateLevel {
/*  Flags and variables  */    
    static private int SAmount=15;
    static private int width;    //of screen
    static private int height;   //of screen
    static protected int difficulty;
    
    static private int CAmount; // Current tiles amount
    static private int TAmount; // Amount of temp tiles
    static private Tile CTile;    // Curent tile
    static private int SLTrap; // Since Last Trap
    static protected int CHeight;  // Current height
/*  Pointers  */     
    static private JPanel CreaPanel;
    static private JLabel BgImg;
/*  Visuals  */    
    static private JLabel [] labels = new JLabel[SAmount];
    static private Stack <Tile> level;
    static private JButton [] length = new JButton[2];
    static private JButton [] nb = new JButton[2];    // next/back
    static private JButton finish;
    static private JComboBox box;
    static private JButton exit;
/*  Classes  */    
    static Background bg=new Background();
    
/*  Builders  */ 
    public CreateLevel(int width, int height, JPanel CreaPanel){
        this.width=width;
        this.height=height;
        this.CreaPanel=CreaPanel;
    }
    public CreateLevel() {}

/*  Remove all components  */  
    static public void CloseScreen (){
        Screen1.layer=1;
        CreaPanel.removeAll();
        CreaPanel.repaint();
        Screen1.Scr1Panel.setVisible(true);
        Screen1.Scr1Panel.requestFocus();
        CreaPanel.setVisible(false);
    }    
    
/*  Set screen resolution  */     
    static public void setRes (int w, int h){
        width=w;
        height=h;
        CreaPanel.removeAll();
        CreaPanel.setBounds(0,0,width,height);
        for (int i=0;i<SAmount;i++){
            labels[i].setBounds(width*i/SAmount,height/10,width/SAmount+1,height/2);
            CreaPanel.add(labels[i]);
        }
        String str= System.getProperty( "user.dir" )+"/res/buttonM.png";
        ImageIcon imageIcon = new ImageIcon(new ImageIcon(str).getImage().getScaledInstance(height/7,height/18, Image.SCALE_DEFAULT));
        for (int i=0;i<2;i++){
            nb[i].setBounds((-i)*height/4+i*width+height/20,height*2/3,height/7,height/18);
                nb[i].setIcon(imageIcon);
                nb[i].setFont(Screen1.font2);
            CreaPanel.add(nb[i]);
        }
        finish.setBounds(width-height/5,height-height/7,height/7,height/18);
            finish.setIcon(imageIcon);
            finish.setFont(Screen1.font2);
        CreaPanel.add(finish);
        str= System.getProperty( "user.dir" )+"/res/buttonT.png";
        imageIcon = new ImageIcon(new ImageIcon(str).getImage().getScaledInstance(height/15,height/15, Image.SCALE_DEFAULT));
        for (int i=0;i<2;i++){
            length[i].setBounds(width/2+height/5+i*height/10,height*2/3,height/15,height/15);
                length[i].setIcon(imageIcon);
                length[i].setFont(Screen1.font2);
            CreaPanel.add(length[i]);
        }
        box.setBounds(height/3-height/54,height*2/3,width/3,height/15);
            box.setFont(Screen1.font2);
        CreaPanel.add(box);

        str= System.getProperty( "user.dir" )+"/res/buttonM.png";
        imageIcon = new ImageIcon(new ImageIcon(str).getImage().getScaledInstance(width/10,width/30, Image.SCALE_DEFAULT));
        exit.setBounds(width/10+height/54,height/108,width/10,width/30);
            exit.setIcon(imageIcon);
            exit.setFont(Screen1.font2);
        CreaPanel.add(exit);   
        
        Screen1.setBg(CreaPanel, BgImg);
        Update();
        CreaPanel.repaint();
    }
    
/*  Setting difficulty  */  
    static public void initCreation (){
        String[] str={"Easy","Normal","Hard"};
        String input = (String) JOptionPane.showInputDialog(Screen1.Scr1Panel, "Select level difficulty:",
        "?", JOptionPane.QUESTION_MESSAGE, null, str, null); 
        if (input!=null){
            switch (input) {
                case "Easy":    difficulty=5;
                                initScreen ();
                                break;
                case "Normal":  difficulty=4;
                                initScreen ();
                                break;
                case "Hard":    difficulty=3;
                                initScreen ();
                                break;
                default:        break;
            }
        }
    }
    
/*  Initiating Screen  */  
    static public void initScreen (){
        Screen1.layer=3;
        Screen1.Scr1Panel.setVisible(false);
        CreaPanel.setLayout(null);
        
        level = new <Tile> Stack();
        for (int i=0;i<SAmount;i++){
            labels[i]=new JLabel();
        }
        for (int i=0;i<2;i++){
            length[i]=new JButton();
                length[i].setVerticalTextPosition(JButton.CENTER);
                length[i].setHorizontalTextPosition(JButton.CENTER);
                length[i].setForeground(Screen1.color);
            nb[i]=new JButton();
                nb[i].setVerticalTextPosition(JButton.CENTER);
                nb[i].setHorizontalTextPosition(JButton.CENTER);
                nb[i].setForeground(Screen1.color);
        }
        finish=new JButton();
            finish.setVerticalTextPosition(JButton.CENTER);
            finish.setHorizontalTextPosition(JButton.CENTER);
            finish.setForeground(Screen1.color);
        
        box=new JComboBox();
            box.setForeground(Screen1.color);
        
        exit=new JButton();
            exit.setVerticalTextPosition(JButton.CENTER);
            exit.setHorizontalTextPosition(JButton.CENTER);
            exit.setForeground(Screen1.color);
        
        CTile=new Tile(1,1,false,0,0,true);
        level.Push(CTile);
        level.Push(CTile);
        CTile=new Tile(CTile,3);
        level.Push(CTile);
        SLTrap=0;
        CHeight=1;
        CAmount=2;
        TAmount=0;
        length[0].setEnabled(false);
        length[1].setEnabled(false);
        nb[0].setEnabled(false);
        nb[1].setEnabled(false);
        NextTile();
        Update();
        
        box.addActionListener(new ActionListener() {   // Chosen from ComboBox
            @Override
            public void actionPerformed(ActionEvent e) {
                CreaPanel.requestFocus();
                ClearStack();
                ClearScreen();
                Update();
                Tile temp=(Tile)box.getSelectedItem();
                if (temp!=null){
                    TAmount=1;
                    CTile=temp;
                    int value = CTile.getTile();
                    if (!CTile.IsDynamic())
                        CHeight=CTile.getHeight();
                    else
                        CTile=bg.CreateTile(value,CHeight,true);
                    
                    if(CTile.IsTrap()) 
                        SLTrap=difficulty;
            
                    if (value==1){
                        TAmount+=difficulty-1;
                        SLTrap=0;
                        for (int i=1;i<difficulty;i++)
                            level.Push(CTile);
                        
                        CTile=new Tile(CTile,TAmount);   // Set end
                        level.Push(CTile);
                    }
                    else if (CTile.IsSet()){
                        TAmount+=2;
                        level.Push(CTile);
                        level.Push(bg.CreateTile(value+1,CHeight,true));
                        
                        CTile=bg.CreateTile(value+2,CHeight,true);
                        CTile=new Tile(CTile,TAmount);   // Set end
                        level.Push(CTile);
                    }
                    else {
                        CTile=new Tile(CTile,TAmount);   // Set end
                        level.Push(CTile);
                    }
                    nb[1].setEnabled(true);
                    Update();
                }
                else 
                    nb[1].setEnabled(false);
                
                length[0].setEnabled(false);
                if (TAmount<3)
                    length[1].setEnabled(false);
                
                else if (TAmount>=3)
                    length[1].setEnabled(true);
            }
        });
        length[0].setText("-");
            length[0].addActionListener(new ActionListener() {   //Minus Button
            @Override
            public void actionPerformed(ActionEvent e) {
                if (TAmount>3){
                    Tile end=level.Pop();
                    level.Pop();    // getting rid of middle tile
                    end=new Tile(end,TAmount-1);
                    level.Push(end);
                    ClearScreen();
                    TAmount--;
                    if (TAmount==3)
                        length[0].setEnabled(false);
                    Update();
                }
            }
        });
        length[1].setText("+");
            length[1].addActionListener(new ActionListener() {   //Plus Button
            @Override
            public void actionPerformed(ActionEvent e) {
                if (TAmount>=2){
                    TAmount++;
                    Tile end=level.Pop();
                    Tile middle=level.Pop();
                    level.Push(middle);
                    level.Push(middle);
                    end=new Tile(end,TAmount);
                    level.Push(end);
                    length[0].setEnabled(true);
                    Update();
                }
            }
        });
        nb[0].setText("Back");
            nb[0].addActionListener(new ActionListener() {   //Back Button
            @Override
            public void actionPerformed(ActionEvent e) {
                if (CAmount>=3){
                    ClearScreen();
                    if (TAmount!=0)
                        ClearStack();
                    CTile=level.Pop();
                    TAmount=CTile.getSetCount();
                    CAmount-=TAmount;
                    level.Push(CTile);
                    Tile temp=LastSetEnd();
                    if (temp!=null){
                        if(temp.IsTrap())
                            SLTrap=difficulty;
                        else
                            SLTrap=0;
                        
                        CHeight=temp.getHeight();
                        }
                    else
                    Screen1.Error(CreaPanel,"Error loading last set");
                    if (CAmount==2)
                        nb[0].setEnabled(false);
                    NextTile();
                    Update();
                }
            }
        });
        nb[1].setText("Next");
            nb[1].addActionListener(new ActionListener() {   //Next Button
            @Override
            public void actionPerformed(ActionEvent e) {
                CAmount+=TAmount;
                TAmount=0;
                nb[0].setEnabled(true);
                NextTile();
                Update();
            }
        });
        finish.setText("Finish");
            finish.addActionListener(new ActionListener() {   //Finish Button
            @Override
            public void actionPerformed(ActionEvent e) {
                CTile=new Tile(null,19,CHeight,false,0,0,true,false,0);
                level.Push(CTile);
                Update();
                int dialogResult = JOptionPane.showConfirmDialog (CreaPanel, "Export your level?","?",JOptionPane.YES_NO_OPTION);
                if(dialogResult == JOptionPane.YES_OPTION){
                    dialogNameFile();
                }
                else {
                    Screen1.Error(CreaPanel,"You cancelled the choice");
                    CTile=level.Pop();
                    Update();
                }   
            }      
        });
        exit.setText("Exit");        
            exit.addActionListener(new ActionListener() { 
            @Override
                public void actionPerformed(ActionEvent e) {
            int dialogButton = JOptionPane.YES_NO_OPTION;
            int dialogResult = JOptionPane.showConfirmDialog (CreaPanel, "Are you sure you want to exit?","Warning",dialogButton);
            if(dialogResult == JOptionPane.YES_OPTION)
                    CloseScreen();
            
                }
            }); 
        setRes(width,height);
        CreaPanel.setVisible(true);
    }
                
    static private void CpStack(Stack <Tile> s2){   // Pasting a temporary Stack back into "level"
        while(s2.IsEmpty()!=true)
            level.Push(s2.Pop());
    }
    
    static private void dialogNameFile (){   
        File folder = new File(System.getProperty( "user.dir" )+"\\levels");
        File file;
            if (folder.exists() && folder.isDirectory()) {
                String fileName = (String)JOptionPane.showInputDialog(CreaPanel,"Name your file:","?",JOptionPane.QUESTION_MESSAGE);
                if ((fileName != null) && (fileName.length()>40))
                    Screen1.Error(CreaPanel,"File name must be shorter than 40 characters.");
                else if ((fileName != null) && (fileName.length() > 0)) {
                    file = new File(System.getProperty( "user.dir" )+"\\levels\\"+fileName+".bin");
                    if (file.exists()) {
                        int dialogResult2 = JOptionPane.showConfirmDialog (CreaPanel, "Rewrite file?","File already exists",JOptionPane.YES_NO_OPTION);
                        if(dialogResult2 == JOptionPane.YES_OPTION) {
                            file.setWritable(true);
                            writeToFile(file);
                        }
                        else
                            dialogNameFile();
                    }
                    else
                        try {
                            file.getParentFile().mkdirs(); 
                            file.createNewFile();
                            writeToFile(file);
                        }
                    catch (IOException ex){
                        Screen1.Error(CreaPanel,"Invalid file name.");
                        dialogNameFile();
                    }
                }
                else{
                    Screen1.Error(CreaPanel,"You didn't provide a name.");
                    CTile=level.Pop();
                    Update();
                }
            }
            else{
                Screen1.Error(CreaPanel,"Folder 'levels' doesn't exist.");
                CTile=level.Pop();
                Update();
            }
    }
    
    static private void writeToFile (File file){  
        BufferedWriter bw = null;
        String creator = (String)JOptionPane.showInputDialog(CreaPanel,"Enter your name:","?",JOptionPane.QUESTION_MESSAGE);
        if ((creator != null) && (creator.length()>60))
            Screen1.Error(CreaPanel,"Name must be shorter than 60 characters.");
        else if ((creator != null) && (creator.length() > 0)) {
            try {
                String export="%"+difficulty+"\n"+levelToString()+BlankLeaderboard(creator);
                export=export+"\n&\n"+stringToHash(export);
                bw = new BufferedWriter( new FileWriter(file));
                bw.write(export);
                file.setWritable(false);
                bw.close();
                CloseScreen();
            }
            catch (IOException ex){
                ex.printStackTrace();
            }
        }
        else{
            Screen1.Error(CreaPanel,"You didn't provide a name.");
            file.delete();
            CTile=level.Pop();
            Update();
        }
    }
    
/*  Turn 'level' stack into string */      
     static protected String levelToString(){
        Tile temp;
        String str="";
        Stack s2=new Stack();
        while(!level.IsEmpty()) // to flip tiles order
            s2.Push(level.Pop());
    
        while(!s2.IsEmpty()){
            temp=(Tile)s2.Pop();
            str+=temp.getTile();
            str+="\n";
            level.Push(temp);
        }
        return str;
    }
     
/*  Create blank leaderboard */      
    static protected String BlankLeaderboard(String creator){
        String str="@"+creator;
        for (int i=0;i<5;i++){
            str+="\n0#0@ ";
        }
        return str;
    }
    
/*  Make hash out of string */     
    static protected int stringToHash(String export){
        int hash = 7;
        for (int i = 0; i < export.length(); i++) 
            hash = hash*31 + export.charAt(i);
        return hash;
    } 
    
/*  Find last set end for 'back' button */     
    static private Tile LastSetEnd(){ 
        Tile temp;
        Stack s2=new Stack();
        s2.Push(level.Pop());
        temp=level.Pop();
        s2.Push(temp);
        while(temp.getSetCount()==0){
            temp=(Tile)level.Pop();
            s2.Push(temp);
            }
    if (temp.getSetCount()!=0){
            CpStack(s2);
            return temp;
        } 
        return null;
    }
    
/*  Clear screen  */  
    static protected void ClearScreen(){
        for (int i=0;i<SAmount;i++)
            labels[i].setVisible(false);
    }    
    
/*  Clear temp tiles  */  
    static protected void ClearStack(){
        for (;TAmount>0;TAmount--)
            level.Pop();
    }  
    
/*  Update screen according to "level"  */  
    static protected void Update(){
        Tile temp;
        String str;
        int i;
        if ((CAmount+TAmount)>SAmount-2)
            i=SAmount-2;
        else
            i=CAmount+TAmount;
        Stack s2=new Stack();
        ImageIcon imageIcon=new ImageIcon();
        for (;i>=0;i--){
            temp=(Tile)level.Pop();
            s2.Push(temp);
            int pic=temp.getTile();
            str= System.getProperty( "user.dir" )+"/res/"+pic+".png";
            imageIcon = new ImageIcon(new ImageIcon(str).getImage().getScaledInstance(width/SAmount+1,height/2,Image.SCALE_DEFAULT));
            labels[i].setIcon(imageIcon);
            labels[i].setVisible(true);
            if (temp.IsDynamic()){
                int elevation=temp.getHeight();
                Point point = new Point();
                point.setLocation(width*i/SAmount,-(elevation-1)*height*16/216+height/10);
                labels[i].setLocation(point);
            }
            else
                labels[i].setLocation(width*i/SAmount,height/10);  //will change
        }
        CpStack(s2);
    }
    
/*  Showing all tiles from the options avalible  */    
    static private void NextTile(){
        try{
            box.removeAllItems();
            box.addItem(null);
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            String msAccDB = System.getProperty( "user.dir" )+"/res/Database.accdb";
            String dbURL = "jdbc:ucanaccess://" + msAccDB; 
            Connection conn = DriverManager.getConnection(dbURL); 
            Statement s = conn.createStatement();
            String query="";
            if (SLTrap>0)   //can't choose a trap
                query="SELECT * FROM Background WHERE Category="+CTile.getAllowed()+" AND Trap=False AND (Start_Height="+CHeight+" OR Dynamic=TRUE)";    // command 
            else    //can choose a trap
                query="SELECT * FROM Background WHERE Category="+CTile.getAllowed()+" AND (Start_Height="+CHeight+" OR Dynamic=TRUE)";   // command
            ResultSet rs = s.executeQuery(query);
            while (rs.next()){    
                String key=rs.getString("Tile_Name");
                if (key!=""){
                    int value=rs.getInt("ID");
                    boolean trap=rs.getBoolean("Trap");
                    int type=rs.getInt("Type");
                    int allowed=rs.getInt("Allowed_After");
                    boolean dynamic=rs.getBoolean("Dynamic");
                    boolean set=rs.getBoolean("Set");
                    int elevation=rs.getInt("End_Height");
                    Tile temp=new Tile(key, value, elevation, trap, allowed, type, dynamic, set ,0);
                    box.addItem(temp);
                }
            }
            s.close();  // Close the statement
            conn.close();   // Close the database.
            box.setSelectedItem(null);
            CreaPanel.requestFocus();
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        catch(ClassNotFoundException ex) {
            Screen1.Error(CreaPanel,"Problem in loading or "
                + "registering MS Access JDBC driver");
        }
    }
}