package org.royalmc.Framework;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.royalmc.uhc.UHCMain;
import static java.lang.Math.sin;

public class ParticleFX {
	
	static ArrayList<Location> locs = new ArrayList<>();

	
	public static void heartEffect(Player p, UHCMain plugin)
	{
		double x = p.getLocation().getX();
		double y = p.getLocation().getY()+3;
		double z = p.getLocation().getZ();
		
		Location a = new Location(p.getWorld(), x, y, z);
		Location a1 = new Location(p.getWorld(), x+1, y+1, z);
		Location a2 = new Location(p.getWorld(), x+2, y+2, z);
		Location a3 = new Location(p.getWorld(), x+3, y+3, z);
		Location a4 = new Location(p.getWorld(), x+3.5, y+4, z);
		Location a5 = new Location(p.getWorld(), x+3.5, y+5, z);
		Location a6 = new Location(p.getWorld(), x+3.5, y+6, z);
		Location a7 = new Location(p.getWorld(), x+3.25, y+7, z);
		Location a8 = new Location(p.getWorld(), x+3, y+7.5, z);
		Location a9 = new Location(p.getWorld(), x+2, y+8, z);
		Location a10 = new Location(p.getWorld(), x+1, y+7.5, z);
		Location a11 = new Location(p.getWorld(), x, y+7, z);
		Location a12 = new Location(p.getWorld(), x-1, y+1, z);
		Location a13 = new Location(p.getWorld(), x-2, y+2, z);
		Location a14 = new Location(p.getWorld(), x-3, y+3, z);
		Location a15 = new Location(p.getWorld(), x-3.5, y+4, z);
		Location a16 = new Location(p.getWorld(), x-3.5, y+5, z);
		Location a17 = new Location(p.getWorld(), x-3.5, y+6, z);
		Location a18 = new Location(p.getWorld(), x-3.25, y+7, z);
		Location a19 = new Location(p.getWorld(), x-3, y+7.5, z);
		Location a20 = new Location(p.getWorld(), x-2, y+8, z);
		Location a21 = new Location(p.getWorld(), x-1, y+7.5, z);
		
		locs.add(a);
		locs.add(a1);
		locs.add(a2);
		locs.add(a3);
		locs.add(a4);
		locs.add(a5);
		locs.add(a6);
		locs.add(a7);
		locs.add(a8);
		locs.add(a9);
		locs.add(a10);
		locs.add(a11);
		locs.add(a12);
		locs.add(a13);
		locs.add(a14);
		locs.add(a15);
		locs.add(a16);
		locs.add(a17);
		locs.add(a18);
		locs.add(a19);
		locs.add(a20);
		locs.add(a21);
		
		new BukkitRunnable() {
			double timesThrough = 0;
			@Override
            public void run() {
				timesThrough++;
			if(timesThrough >= 15){
				locs.clear();
				this.cancel();
			}
		for(int i = 0; i < locs.size(); i++)
		{
			p.getWorld().playEffect(locs.get(i), Effect.HEART, 0);
		}

		p.getWorld().playSound(p.getLocation(), Sound.NOTE_BASS_DRUM, 2, 0);
		p.getWorld().playSound(p.getLocation(), Sound.DIG_WOOD, 2, .2F);



		}
        
    }.runTaskTimer(plugin, 0, 10);	
	}
	
	public static void spiritEffect(Player deadPlayer, UHCMain plugin){
		deadPlayer.getWorld().playSound(deadPlayer.getLocation(), Sound.AMBIENCE_CAVE, 1, 1);
		 new BukkitRunnable() {
			 double timesThrough = 0;
	            @Override
	            public void run() {
	            	double radians = Math.toRadians(timesThrough);
	        		double x = Math.cos(radians);
	        		double z = Math.sin(radians);
	        		
	   			 Location loc = deadPlayer.getLocation();		
	        		
	        		loc.setX(loc.getX() + x);
	        		loc.setZ(loc.getZ() + z);
	        		loc.setY(loc.getY() + timesThrough/300);//particles float up
	        		
	        		Location loc2 = deadPlayer.getLocation();	
	        		
	        		loc2.setX(loc2.getX() + x*-.5);
	        		loc2.setZ(loc2.getZ() + z*-.5);
	        		loc2.setY(loc2.getY() + timesThrough/400);//particles float up
	        		
	        		Location loc3 = deadPlayer.getLocation();	
	            	double radians2 = Math.toRadians(timesThrough/2);
	        		double x2 = Math.cos(radians2);
	        		double z2 = Math.sin(radians2);
	        		loc3.setX(loc3.getX() + x2*.5);
	        		loc3.setZ(loc3.getZ() + z2*.5);
	        		loc3.setY(loc3.getY() + timesThrough/350);//particles float up
	        		
	        		if(!(loc2.getY() > (deadPlayer.getLocation().getY() + 6))){//particles are still floatin'
	    				loc2.getWorld().spigot().playEffect(loc2, Effect.FIREWORKS_SPARK, 1, 0, 0, 0, 0, 0, 1, 50);
	        		}
	        		if(!(loc3.getY() > (deadPlayer.getLocation().getY() + 6))){
	        			loc3.getWorld().spigot().playEffect(loc3, Effect.FIREWORKS_SPARK, 1, 0, 0, 0, 0, 0, 1, 50);
	        		}
	        		if(!(loc.getY() > (deadPlayer.getLocation().getY() + 6))){
	        			loc.getWorld().spigot().playEffect(loc, Effect.FIREWORKS_SPARK, 1, 0, 0, 0, 0, 0, 1, 50);
	        		}
	        		
	        		if(loc2.getY() > (deadPlayer.getLocation().getY() + 6) &&
	        				loc.getY() > (deadPlayer.getLocation().getY() + 6) &&
	        				loc3.getY() > (deadPlayer.getLocation().getY() + 6)){//make sure they have all reached 6 blocks before canceling
	        			this.cancel();
	        		}
	        		timesThrough += 15;
	            }
	            
	        }.runTaskTimer(plugin, 0, 2);	
	}
	
	public static void rainEffect(Player deadPlayer, UHCMain plugin){
		
		int i = 0;
		deadPlayer.getWorld().playSound(deadPlayer.getLocation(), Sound.SWIM, 1, 1);
		while(i < 360){
			i++;
			
			double radians = Math.toRadians(i);
    		double x = Math.cos(radians);
    		double z = Math.sin(radians);
    		
			 Location loc = deadPlayer.getLocation();	
			 loc.setY(loc.getY()+2);
    		
    		loc.setX(loc.getX() + x);
    		loc.setZ(loc.getZ() + z);
    		
    		loc.getWorld().playEffect(loc, Effect.WATERDRIP, 1);
		}
		new BukkitRunnable() {
			int timesThrough = 0;
			double times = 0;
			Location loc = deadPlayer.getLocation();
			@Override
			public void run() {
				if(timesThrough < 1)loc.getWorld().playSound(loc, Sound.SPLASH, 1, 1);
				timesThrough++;
				times++;
				if(timesThrough > 7){
					this.cancel();
				}
				int i = 0;
				while(i < 360){
					i++;
					
					double radians = Math.toRadians(i);
		    		double x = Math.cos(radians);
		    		double z = Math.sin(radians);
		    		
					 Location loc = deadPlayer.getLocation();	
					 
		    		loc.setX(loc.getX() + (x*(times/3)));
		    		loc.setZ(loc.getZ() + (z*(times/3)));
		    		
		    		loc.getWorld().playEffect(loc, Effect.SPLASH, 1);
				}
				
			}
		 }.runTaskTimer(plugin, 50, 2);//Wait for drip to be done, then begin splash animation	
		new BukkitRunnable() {
			Location loc = deadPlayer.getLocation();
			int i = 0;
			@Override
			public void run() {
				loc.getWorld().playEffect(loc, Effect.SPLASH, 20);
				loc.setY(loc.getY()+(i/15));
				loc.setZ(loc.getZ()+i/15);
				loc.getWorld().playEffect(loc, Effect.SPLASH, 20);
				loc.setZ(loc.getZ()-i/15);
				loc.setZ(loc.getZ()-i/15);
				loc.getWorld().playEffect(loc, Effect.SPLASH, 20);
				loc.setX(loc.getX()+i/15);
				loc.getWorld().playEffect(loc, Effect.SPLASH, 20);
				loc.setX(loc.getX()+i/15);
				loc.setX(loc.getX()+i/15);
				loc.getWorld().playEffect(loc, Effect.SPLASH, 20);
				
				loc.getWorld().playEffect(loc, Effect.SPLASH, 20);
				loc.setY(loc.getY()+(i/15));
				loc.setZ(loc.getZ()-i/15);
				loc.getWorld().playEffect(loc, Effect.SPLASH, 20);
				loc.setZ(loc.getZ()+i/15);
				loc.setZ(loc.getZ()+i/15);
				loc.getWorld().playEffect(loc, Effect.SPLASH, 20);
				loc.setX(loc.getX()+i/15);
				loc.getWorld().playEffect(loc, Effect.SPLASH, 20);
				loc.setX(loc.getX()-i/15);
				loc.setX(loc.getX()-i/15);
				loc.getWorld().playEffect(loc, Effect.SPLASH, 20);
				 loc = deadPlayer.getLocation();
				i++;
				if(i > 40){
					this.cancel();
				}
				
			}
		
	 }.runTaskTimer(plugin, 50, 1);//Wait for drip to be done, then begin splash animation	
	}
	
	
	public static void flameEffect(Player deadPlayer, UHCMain plugin){
		deadPlayer.getWorld().playSound(deadPlayer.getLocation(), Sound.GHAST_FIREBALL, 2, 0);
		new BukkitRunnable() {
			double descent = 10;
			@Override
			public void run() {
				
			for(int i = 0; i < 10; i++){
				Location loc = deadPlayer.getLocation();
				loc.setX(loc.getX() + (Math.random()*1.5 - .75));
				loc.setZ(loc.getZ() + (Math.random()*1.5 - .75));
				loc.setY(loc.getY() + descent);
				loc.getWorld().playEffect(loc, Effect.LAVADRIP, 1);
			}
			descent -= .5;
			if(descent <= 0){
				this.cancel();
			}
			//deadPlayer.getWorld().playSound(deadPlayer.getLocation(), Sound.FIRE, 2, 0);
			
			}
		}.runTaskTimer(plugin, 0, 2);
		
		
		new BukkitRunnable() {
			int timesThrough = 0;
			double times = 0;
			Location loc = deadPlayer.getLocation();
			@Override
			public void run() {
				if(timesThrough < 1)loc.getWorld().playSound(loc, Sound.EXPLODE, 1, 1);
				timesThrough++;
				times++;
				if(timesThrough > 7){
					this.cancel();
				}
				int i = 0;
				while(i < 360){
					i += 50;
					
					double radians = Math.toRadians(i);
		    		double x = Math.cos(radians);
		    		double z = Math.sin(radians);
		    		
					 Location loc = deadPlayer.getLocation();	
					 
		    		loc.setX(loc.getX() + (x*(times/3)));
		    		loc.setZ(loc.getZ() + (z*(times/3)));
		    		
		    		loc.getWorld().playEffect(loc, Effect.MOBSPAWNER_FLAMES, 1);
				}
				
			}
		 }.runTaskTimer(plugin, 40, 2);
		 
		}
	
	 static Location particleLoc = null;
	 static int count = 0;
	 public static void drawSinWave(String effect, Player p, UHCMain plugin) {

	    for (int i = 0; i < 14 * 3; i += 14) {

	      Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
	        public void run() {

	          new BukkitRunnable() {

	            boolean finishedPeriod = false;

	            Location loc = p.getLocation().subtract(1.75, 0, 0);
	            Location loc2 = p.getLocation().subtract(0, 0, 1.75);
	            double x = 0;
	            double y = 0;
	            double θ = 0;

	            double angle = 0;
	            double x2 = 0;
	            double y2 = 0;

	            public void run() {

	              if (finishedPeriod == false) {
	                θ += Math.PI / 8;
	                x += Math.toRadians(Math.PI * 8);
	                y = (2 * sin(θ)) + 1;

	                loc.add(x, y, 0);
	                loc.getWorld().playEffect(loc, Effect.valueOf(effect), 1);
	                loc.subtract(x, y, 0);

	                loc2.add(0, y, x);
	                loc2.getWorld().playEffect(loc2, Effect.valueOf(effect), 1);
	                loc2.subtract(0, y, x);
	              }


	              if (θ >= Math.PI - Math.PI / 8) {
	                finishedPeriod = true;
	              }

	              if (finishedPeriod) {
	                angle += Math.PI / 8;
	                x2 += -Math.toRadians(Math.PI * 8);
	                y2 = (2 * sin(angle)) + 1;

	                loc.add(x2 + 3.525, y2, 0);
	                loc.getWorld().playEffect(loc, Effect.valueOf(effect), 1);
	                loc.subtract(x2 + 3.525, y2, 0);

	                loc2.add(0, y2, x2 + 3.525);
	                loc2.getWorld().playEffect(loc2, Effect.valueOf(effect), 1);
	                loc2.subtract(0, y2, x2 + 3.525);

	                if (angle >= Math.PI - Math.PI / 8) this.cancel();
	              }

	            }
	          }.runTaskTimer(plugin, 0, 1);
	        }
	      }, i);
	    }
	  } 
	 
	 public static void fireballEffect(Player deadPlayer, UHCMain plugin){
		 deadPlayer.getWorld().playSound(deadPlayer.getLocation(), Sound.GHAST_FIREBALL, 2, 0);
		 deadPlayer.getWorld().playSound(deadPlayer.getLocation(), Sound.FIRE, 2, 0);
		 new BukkitRunnable() {
			 double times = 0;
			@Override
			public void run() {

				times += .7;
				//double radians = Math.toRadians(times);
	    		//double x = Math.cos(radians);
	    		//double z = Math.sin(radians);
	    		
	    		Location fireballLoc = deadPlayer.getLocation();
				 
	    		//fireballLoc.setX(fireballLoc.getX() + x);
	    		//fireballLoc.setZ(fireballLoc.getZ() + z);
	    		fireballLoc.setY(fireballLoc.getY() + times);
	    		
	    		fireballLoc.getWorld().spigot().playEffect(fireballLoc, Effect.MOBSPAWNER_FLAMES, 1, 0, 0, 0, 0, 0, 4, 50);
				if(times > 20){
		    		fireballLoc.getWorld().spigot().playEffect(fireballLoc, Effect.FLAME, 1, 0, 0, 0, 0, 1, 100, 50);
		    		fireballLoc.getWorld().playSound(fireballLoc, Sound.EXPLODE, 2, 0);
		    		fireballLoc.getWorld().playSound(fireballLoc, Sound.FIREWORK_TWINKLE, 2, 0);
		    		fireballLoc.getWorld().playSound(fireballLoc, Sound.FIREWORK_LARGE_BLAST2, 2, 0);
					this.cancel();
				}
	    		
	    		fireballLoc.setY(fireballLoc.getY() - .5);
	    		fireballLoc.getWorld().spigot().playEffect(fireballLoc.getBlock().getLocation(), Effect.LAVA_POP, 1, 0, 1, 0, 1, 0, 5, 50);
			}
		 }.runTaskTimer(plugin, 0, 2);
		 }
	 
	 
	 public static void rektEffect(Player deadPlayer, UHCMain plugin){
			double x = deadPlayer.getLocation().getX() - 5.5;
			double y = deadPlayer.getLocation().getY()+3;
			double z = deadPlayer.getLocation().getZ();
			
			 ArrayList<Location> rektLocs = new ArrayList<>();
			 deadPlayer.getWorld().playSound(deadPlayer.getLocation(), Sound.FIRE, 1, 0);
			
			
			//R
			Location a = new Location(deadPlayer.getWorld(), x, y, z);
			Location a1 = new Location(deadPlayer.getWorld(), x, y+.5, z);
			Location a2 = new Location(deadPlayer.getWorld(), x, y+1, z);
			Location a3 = new Location(deadPlayer.getWorld(), x, y+1.5, z);
			Location a4 = new Location(deadPlayer.getWorld(), x, y+2, z);
			Location a5 = new Location(deadPlayer.getWorld(), x, y+2.5, z);
			Location a6 = new Location(deadPlayer.getWorld(), x, y+3, z);
			Location a7 = new Location(deadPlayer.getWorld(), x, y+3.5, z);
			Location a8 = new Location(deadPlayer.getWorld(), x, y+4, z);
			Location a9 = new Location(deadPlayer.getWorld(), x+.5, y+4, z);
			Location a10 = new Location(deadPlayer.getWorld(), x+1, y+4, z);
			Location a11 = new Location(deadPlayer.getWorld(), x+1.5, y+4, z);
			Location a12 = new Location(deadPlayer.getWorld(), x+2, y+4, z);
			Location a13 = new Location(deadPlayer.getWorld(), x+2, y+3.5, z);
			Location a14 = new Location(deadPlayer.getWorld(), x+2, y+3, z);
			Location a15 = new Location(deadPlayer.getWorld(), x+1.5, y+2.5, z);
			Location a16 = new Location(deadPlayer.getWorld(), x+1, y+2, z);
			//Location a17 = new Location(deadPlayer.getWorld(), x+1, y+1.5, z);
			Location a18 = new Location(deadPlayer.getWorld(), x+1, y+1, z);
			Location a19 = new Location(deadPlayer.getWorld(), x+1.5, y+.5, z);
			Location a20 = new Location(deadPlayer.getWorld(), x+2, y, z);
			Location a21 = new Location(deadPlayer.getWorld(), x+.5, y+1.5, z);


			
			//E
			Location a22 = new Location(deadPlayer.getWorld(), x+5, y+4, z);
			Location a23 = new Location(deadPlayer.getWorld(), x+4.5, y+4, z);
			Location a24 = new Location(deadPlayer.getWorld(), x+4, y+4, z);
			Location a25 = new Location(deadPlayer.getWorld(), x+3.5, y+4, z);
			Location a26 = new Location(deadPlayer.getWorld(), x+3, y+4, z);
			Location a27 = new Location(deadPlayer.getWorld(), x+3, y+3.5, z);
			Location a28 = new Location(deadPlayer.getWorld(), x+3, y+3, z);
			Location a29 = new Location(deadPlayer.getWorld(), x+3, y+2.5, z);
			Location a30 = new Location(deadPlayer.getWorld(), x+3, y+2, z);
			Location a31 = new Location(deadPlayer.getWorld(), x+3, y+1.5, z);
			Location a32 = new Location(deadPlayer.getWorld(), x+3, y+1, z);
			Location a33 = new Location(deadPlayer.getWorld(), x+3, y+.5, z);
			Location a34 = new Location(deadPlayer.getWorld(), x+3, y, z);
			Location a35 = new Location(deadPlayer.getWorld(), x+3.5, y, z);
			Location a36 = new Location(deadPlayer.getWorld(), x+4, y, z);
			Location a37 = new Location(deadPlayer.getWorld(), x+4.5, y, z);
			Location a38 = new Location(deadPlayer.getWorld(), x+5, y, z);
			Location a39 = new Location(deadPlayer.getWorld(), x+3.5, y+2, z);
			Location a40 = new Location(deadPlayer.getWorld(), x+4, y+2, z);
			Location a41 = new Location(deadPlayer.getWorld(), x+4.5, y+2, z);
			Location a42 = new Location(deadPlayer.getWorld(), x+5, y+2, z);
			
			//K
			Location a43 = new Location(deadPlayer.getWorld(), x+6, y+4, z);
			Location a44 = new Location(deadPlayer.getWorld(), x+6, y+3.5, z);
			Location a45 = new Location(deadPlayer.getWorld(), x+6, y+3, z);
			Location a46 = new Location(deadPlayer.getWorld(), x+6, y+2.5, z);
			Location a47 = new Location(deadPlayer.getWorld(), x+6, y+2, z);
			Location a48 = new Location(deadPlayer.getWorld(), x+6, y+1.5, z);
			Location a49 = new Location(deadPlayer.getWorld(), x+6, y+1, z);
			Location a50 = new Location(deadPlayer.getWorld(), x+6, y+.5, z);
			Location a51 = new Location(deadPlayer.getWorld(), x+6, y, z);
			Location a52 = new Location(deadPlayer.getWorld(), x+6.5, y+2.5, z);
			Location a53 = new Location(deadPlayer.getWorld(), x+7, y+3, z);
			Location a54 = new Location(deadPlayer.getWorld(), x+7.5, y+3.5, z);
			Location a55 = new Location(deadPlayer.getWorld(), x+8, y+4, z);
			Location a56 = new Location(deadPlayer.getWorld(), x+6.5, y+1.5, z);
			Location a57 = new Location(deadPlayer.getWorld(), x+7, y+1, z);
			Location a58 = new Location(deadPlayer.getWorld(), x+7.5, y+.5, z);
			Location a59 = new Location(deadPlayer.getWorld(), x+8, y, z);
			
			
			
			//T
			Location a60 = new Location(deadPlayer.getWorld(), x+10, y, z);
			Location a62 = new Location(deadPlayer.getWorld(), x+10, y+.5, z);
			Location a61 = new Location(deadPlayer.getWorld(), x+10, y+1, z);
			Location a63 = new Location(deadPlayer.getWorld(), x+10, y+1.5, z);
			Location a64 = new Location(deadPlayer.getWorld(), x+10, y+2, z);
			Location a65 = new Location(deadPlayer.getWorld(), x+10, y+2.5, z);
			Location a66 = new Location(deadPlayer.getWorld(), x+10, y+3, z);
			Location a67 = new Location(deadPlayer.getWorld(), x+10, y+3.5, z);
			Location a68 = new Location(deadPlayer.getWorld(), x+10, y+4, z);
			
			Location a69 = new Location(deadPlayer.getWorld(), x+9, y+4, z);
			Location a70 = new Location(deadPlayer.getWorld(), x+9.5, y+4, z);
			Location a71 = new Location(deadPlayer.getWorld(), x+10, y+4, z);
			Location a72 = new Location(deadPlayer.getWorld(), x+10.5, y+4, z);
			Location a73 = new Location(deadPlayer.getWorld(), x+11, y+4, z);
			
			

			
			
			rektLocs.add(a);
			rektLocs.add(a1);
			rektLocs.add(a2);
			rektLocs.add(a3);
			rektLocs.add(a4);
			rektLocs.add(a5);
			rektLocs.add(a6);
			rektLocs.add(a7);
			rektLocs.add(a8);
			rektLocs.add(a9);
			rektLocs.add(a10);
			rektLocs.add(a11);
			rektLocs.add(a12);
			rektLocs.add(a13);
			rektLocs.add(a14);
			rektLocs.add(a15);
			rektLocs.add(a16);
			//rektLocs.add(a17);
			rektLocs.add(a18);
			rektLocs.add(a19);
			rektLocs.add(a20);
			rektLocs.add(a21);
			rektLocs.add(a22);
			rektLocs.add(a23);
			rektLocs.add(a24);
			rektLocs.add(a25);
			rektLocs.add(a26);
			rektLocs.add(a27);
			rektLocs.add(a28);
			rektLocs.add(a29);
			rektLocs.add(a30);
			rektLocs.add(a31);
			rektLocs.add(a32);
			rektLocs.add(a33);
			rektLocs.add(a34);
			rektLocs.add(a35);
			rektLocs.add(a36);
			rektLocs.add(a37);
			rektLocs.add(a38);
			rektLocs.add(a39);
			rektLocs.add(a40);
			rektLocs.add(a41);
			rektLocs.add(a42);
			rektLocs.add(a43);
			rektLocs.add(a44);
			rektLocs.add(a45);
			rektLocs.add(a46);
			rektLocs.add(a47);
			rektLocs.add(a48);
			rektLocs.add(a49);
			rektLocs.add(a50);
			rektLocs.add(a51);
			rektLocs.add(a52);
			rektLocs.add(a53);
			rektLocs.add(a54);
			rektLocs.add(a55);
			rektLocs.add(a56);
			rektLocs.add(a57);
			rektLocs.add(a58);
			rektLocs.add(a59);
			rektLocs.add(a60);
			rektLocs.add(a61);
			rektLocs.add(a62);
			rektLocs.add(a63);
			rektLocs.add(a64);
			rektLocs.add(a65);
			rektLocs.add(a66);
			rektLocs.add(a67);
			rektLocs.add(a68);
			rektLocs.add(a69);
			rektLocs.add(a70);
			rektLocs.add(a71);
			rektLocs.add(a72);
			rektLocs.add(a73);

			
			
			
			new BukkitRunnable() {
				double timesThrough = 0;
				@Override
	            public void run() {
					timesThrough++;
					
				if(timesThrough >= rektLocs.size()+20){
					this.cancel();
				}
				
				if(timesThrough == (rektLocs.size()+20)/3 || timesThrough == (((rektLocs.size()+20)/3)*2)){
				deadPlayer.getWorld().playSound(deadPlayer.getLocation(), Sound.FIRE, 1, 0);
				}
				
			for(int i = 0; i < timesThrough; i++)
			{
				rektLocs.get(i).getWorld().spigot().playEffect(rektLocs.get(i), Effect.FLAME, 1, 0, 0, 0, 0, 0, 1, 50);
			}

			}
	        
	    }.runTaskTimer(plugin, 0, 2);
	 }
/**
(1) Effect: Heart, beats and has a beating sound effect, it is in the shape of a heart not the real heart particles but multiple particles in the shape of a heart look on epic cube for a good example of this in there uhc lobby 5s

(2) Effect: Rain, have rain make a big splash onto the player that was just killed and make the "splah" or water particles go up in the air and come down (big) and splash sound effect 8s

(3) Effect: Fire Ball, have a particle version of a fireball in minecraft rise up from the player (a small fire ball size of chest) and have a sound effect for this, it will go up in the air and spin for like 5s than go away

(4) Effect: Spirt, have tons of black dust surround your recent victum and have it circle the player going up like spiroling upwords (make sure there is not too much of this or to big and spammy) 7s

(5) Effect: JumpMan, have a particle "jump" around and over the player with a sound effect (good example of this is on epic cube) 6s

(6) Effect: Flame, have cool fire coming like 10 blocks from above the player coming down into the players chest making a noise, (good example epic cube) also all of these effects is not in hub but on there UHC lobbys,

(7) Effect: Recked, have particles above the player like 2 blocks above death spot, saying Recked in particles smoke letters (well you choose what you want for particles) on this one. 9s
*/
}
