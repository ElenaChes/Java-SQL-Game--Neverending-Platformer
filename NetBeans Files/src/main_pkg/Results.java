/*
 * @author Elena Chestnov  * 
 */
package main_pkg;
    import java.util.*;
    import java.time.LocalTime;

public class Results {
    private String player;
    private LocalTime time;
    
    public Results (String player, LocalTime time){
        this.player=player;
        this.time=time;
    }
    
    protected LocalTime getValue(){
        return time;
    }
    
    protected String getPlayer(){
        return player;
    }
    
    protected LocalTime getTime(){
        return time;
    }   
    
    protected void PrintLine(){
        System.out.println("************************");
        System.out.println((this.getTime()).getMinute()+"#"+(this.getTime()).getSecond()+"@"+this.getPlayer());
    }
}

