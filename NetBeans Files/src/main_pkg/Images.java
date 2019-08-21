/*
 * @author Elena Chestnov  * 
 */
package main_pkg;
    import javax.swing.*;
    import java.awt.Image;

public class Images {
    ImageIcon icon;
    ImageIcon icon2;
    ImageIcon icon3;
    String name;
    int ID;
    
    public Images (String url, int width, int height, String name){ // used by 'player'
        this.icon=new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
        this.name=name;
        this.icon2=null;
        this.icon3=null;
        this.ID=0;
    }
    
    public Images (String url, int width, int height, int ID){  // used by 'background'
        this.icon=new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
        if (ID>10&&ID<19){
            url=System.getProperty( "user.dir" )+"/res/"+ID+"_2.png";
            this.icon2=new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
            url=System.getProperty( "user.dir" )+"/res/"+ID+"_3.png";
            this.icon3=new ImageIcon(new ImageIcon(url).getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
        }
        else{
            this.icon2=null;
            this.icon3=null;
        }
        this.name="";
    }
    protected String getName(){
        return name;
    }
    
    protected int getID(){
        return ID;
    }
    
    protected ImageIcon getIcon(){
        return icon;
    }
    
    protected ImageIcon getIcon2(){
        return icon2;
    }
    
    protected ImageIcon getIcon3(){
        return icon2;
    }
}