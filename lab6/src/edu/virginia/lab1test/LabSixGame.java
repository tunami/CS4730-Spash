package edu.virginia.lab1test;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import edu.virginia.engine.display.AnimatedSprite;
import edu.virginia.engine.display.DisplayObjectContainer;
import edu.virginia.engine.display.Game;
import edu.virginia.engine.display.PhysicsSprite;
import edu.virginia.engine.display.Sprite;
import edu.virginia.engine.events.Event;
import edu.virginia.engine.tweens.Tween;
import edu.virginia.engine.tweens.TweenJuggler;
import edu.virginia.engine.tweens.TweenableParam;

/**
 * Example game that utilizes our engine. We can create a simple prototype game with just a couple lines of code
 * although, for now, it won't be a very fun game :)
 * */
public class LabSixGame extends Game{
    
    public static final String COIN_PICKED_UP = "picked_up";
    
    PhysicsSprite red = new PhysicsSprite("dog", "dog_0.png", 6);
    
    PhysicsSprite gold = new PhysicsSprite("gold", "gold.png");
    PhysicsSprite platform = new PhysicsSprite("platform", "platform.png");
    PhysicsSprite platform2 = new PhysicsSprite("platform2", "platform.png");
    SoundManager sounds = new SoundManager();
    QuestManager myQuestManager = new QuestManager();
    Event picked_up =  new Event();

    boolean comp = false;
    boolean fail = false;
    boolean played = false;
    boolean isleft = true;
    int count = 1;
    int go = 0;
    
    /**
	 * Constructor. See constructor in Game.java for details on the parameters given
	 * */
	public LabSixGame() {
		super("Lab Five Test Game", 600, 600);
		this.addChild(red);
		this.addChild(gold);
        this.addChild(platform);
        this.addChild(platform2);
        
        red.setAnimation("run", 0, 5);
        red.setAnimationSpeed(10);
        red.setAlpha(0);
        
        sounds.loadSoundEffect("coin", "coin.wav");
        sounds.loadSoundEffect("jump", "jump.wav");
		gold.setPosx(350);
		gold.setPosy(60);
		
		
		red.setPosx(350);
		red.setPosy(400);
		
		platform2.setPosx(100);
		platform2.setPosy(350);
		
		
		platform.setPosx(300);
		platform.setPosy(450);
		
		
		
		
        picked_up.setSource(gold);
        picked_up.setEventType(COIN_PICKED_UP);
		gold.addEventListener(myQuestManager, COIN_PICKED_UP);
	}
	
	/**
	 * Engine will automatically call this update method once per frame and pass to us
	 * the set of keys (as strings) that are currently being pressed down
	 * */
	@Override
	public void update(ArrayList<Integer> pressedKeys){
		super.update(pressedKeys);
		red.setJumping(false);
		
		
		Tween dog = new Tween(red);
        TweenJuggler.getSource().add(dog); 

		if(count == 1) {
		    dog.animate(TweenableParam.ALPHA, 0, 1, 1000);
		}
		
		Tween coin = new Tween(gold);
        TweenJuggler.getSource().add(coin); 
		
		
		
		if(pressedKeys.contains(32)) { //space {
		    if(!played) {
		        sounds.playSoundEffect("jump");
		        played = true;
		    }
		    red.setVelocity(20);
		    
		    red.setPosy(red.getPosy() - (int)red.getVelocity());
		    //red.setVelocity( red.getVelocity() + red.getGravity());
		    
		    
		    
		    
		}
		if(!red.isJumping()) {
		    //positive y acceleration
		    //yvelocity to like 10
		    
		    //red.collideswith(platform);
		    
		    //platforms y, and then make it the player's plus like 2
		    if(!red.collidesWith(platform) && !red.collidesWith(platform2)) {
		    //if(red.getPosy() > 400)    { 
		       
		        red.setAcceleration(red.getAcceleration() - 1);
		        red.setVelocity(red.getAcceleration());
		        red.setPosy(red.getPosy() - (int)red.getVelocity());
		        
		        if(red.collidesWith(platform)) {
		            red.setPosy(platform.getPosy() - red.getUnscaledHeight());
		            red.setAcceleration(0);
	                red.setVelocity(0);
	                played=false;
		        }
		        if(red.collidesWith(platform2)) {
                    red.setPosy(platform2.getPosy() - red.getUnscaledHeight());
                    red.setAcceleration(0);
                    red.setVelocity(0);
                    played=false;
                }
		    }
		    else {
		        played=false;
		        red.setAcceleration(0);
		        red.setVelocity(0);
		    }
		}
		
        if(pressedKeys.contains(37)) { //left
                
            
            red.setPosx(red.getPosx() - 4); 
            red.callAnimation("run");
            
        }
        if(pressedKeys.contains(39)) { //right
         
           
            red.setPosx(red.getPosx() + 4); 
            red.callAnimation("run");
        }
        if(!pressedKeys.contains(39) && !pressedKeys.contains(37)) {
            red.setPlaying(false);
        }
        if(red.collidesWith(gold) && comp !=true) {
            gold.dispatchEvent(picked_up);
            sounds.playSoundEffect("coin");
            
            coin.animate(TweenableParam.YPOS, 0, 20, 200);
            coin.animate(TweenableParam.YSCALE, 0, .3, 100);
            coin.animate(TweenableParam.XSCALE, 0, .3, 100);
            coin.animate(TweenableParam.XPOS, 0, -15, 300);
            

            comp = true;
            
         }
        
         if(gold.getPosx() <= 340) {
             go++;
             //System.out.println(go);
             if(go>20) {
                 if(gold.getAlpha() >= (float) .02) {
                 gold.setAlpha(gold.getAlpha() - (float).02);
             
                 }
             }
         }
        
		if(red.getPosy() > 600) {
		    fail = true;
		    this.stop();
		}
		count++;
		if(red != null)red.update(pressedKeys);
	    if(gold != null)gold.update(pressedKeys);
	}
	
	/**
	 * Engine automatically invokes draw() every frame as well. If we want to make sure mario gets drawn to
	 * the screen, we need to make sure to override this method and call mario's draw method.
	 * */
	@Override
	public void draw(Graphics g){
		super.draw(g);
		if(!isleft) {
		   
		}
		if(comp) {
            g.drawString("Quest Complete", 250, 250);
        }
		if(fail) {
            g.drawString("You have died", 250, 250);
        }
	}

	/**
	 * Quick main class that simply creates an instance of our game and starts the timer
	 * that calls update() and draw() every frame
	 * */
	public static void main(String[] args) {
		LabSixGame game = new LabSixGame();
		game.start();

	}
}
