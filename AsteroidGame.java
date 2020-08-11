import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.Ellipse2D;
import java.lang.ArrayIndexOutOfBoundsException;
import java.applet.Applet;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.net.URL;

public class AsteroidGame extends JPanel implements KeyListener,Runnable
{
	Image ship;
	AffineTransform identity = new AffineTransform();
	private float angle;
	private int x, y;
	private int dir = 4;      //1 = up     2 = up-right        3 = right        4 = down-right     5 = down    6 = down-left    7 = left    8 = up-left
	private int dirlaser = 4;
	private boolean right, left, up, down;
	private boolean rightlaser, leftLaser, upLaser, downLaser;
	private int health = 10;
	private int gunLevel = 1;
	private int deaths = 0;
	private final int invFrames = 70;
	private int tempFrames = 0;
	private int pointTime = 0;
	private JFrame frame;
	private Thread t;
	private boolean gameOn = true;
	private Font f;
	private Polygon poly;
	private GradientPaint gp;
	private Color colorpurple, colorgreen, colorgray;
	private int runcount;
	private ArrayList<Asteroid> asteroidList;
	private ArrayList<Laser> laserList;
	private ArrayList<Upgrade> upgradeList;
	private int points = 0;


	public AsteroidGame()
	{
		asteroidList = new ArrayList<Asteroid>();
		laserList = new ArrayList<Laser>();
		upgradeList = new ArrayList<Upgrade>();

		ship = new ImageIcon(this.getClass().getResource("ship.png")).getImage();
		MediaTracker tracker = new MediaTracker(this);
		tracker.addImage(ship, 0);

		frame=new JFrame();
		frame.setResizable(true);
		x=450;
		y=400;
		gameOn=true;
		colorpurple=new Color(180,181,254);
		colorgreen = new Color(181, 254, 180);
		colorgray = new Color(230,230,255);

		//Sets a gradient painted section that goes along with a polygon
		//gradient painting is fading from one color to the next
		gp = new GradientPaint((float)0.0, (float)0.0, Color.BLUE, (float)0.0, (float)30.0, Color.WHITE, true);
		f=new Font("Arial",Font.BOLD,27);
		frame.addKeyListener(this);
		frame.add(this);
		frame.setSize(916,839);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		t=new Thread(this);
		t.start();
	}
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;

		//all painting (AND ONLY PAINTING) happens here!
		//Don't use this method to deal with mathematics
		//The painting imps aren't fond of math.

		g2d.setStroke(new BasicStroke(4));
		g2d.setColor(colorpurple);
		g2d.fillRect(0,0,900,800);   //background

		g2d.setFont(f);

		int pos = 20;
		for (int i = 0; i < health; i ++)   //draw health bar
		{
			g2d.setColor(Color.RED);
			g2d.fillRect(pos, 50, 50, 3);
			pos += 52;
		}

		g2d.setColor(Color.WHITE);
		g2d.drawString("Points: " + points, 700, 750);

		for (int i = 0; i < asteroidList.size(); i ++)    //draw the asteroids
		{
			//g2d.setColor(Color.BLACK);
			//g2d.drawOval(asteroidList.get(i).getX(), asteroidList.get(i).getY(), asteroidList.get(i).getSize(), asteroidList.get(i).getSize());
			//g2d.setColor(Color.BLUE);
			//g2d.fillOval(asteroidList.get(i).getX()+2, asteroidList.get(i).getY()+2, asteroidList.get(i).getSize()-4, asteroidList.get(i).getSize()-4);
			g2d.drawImage(asteroidList.get(i).getImage(), asteroidList.get(i).getX(), asteroidList.get(i).getY(), this);
		}

		//drawing all lasers
		for (int i = 0; i < laserList.size(); i ++)
		{
			g2d.setStroke(new BasicStroke(4));

			g2d.setColor(Color.BLACK);
			g2d.drawOval(laserList.get(i).getX(), laserList.get(i).getY(), laserList.get(i).getSize(), laserList.get(i).getSize());
			g2d.setColor(Color.YELLOW);
			g2d.fillOval(laserList.get(i).getX()+2, laserList.get(i).getY()+2, laserList.get(i).getSize()-4, laserList.get(i).getSize()-4);
		}

		//drawing all upgrades
		for (int i = 0; i < upgradeList.size(); i ++)
		{
			g2d.setColor(Color.BLACK);
			g2d.drawRect(upgradeList.get(i).getX(), upgradeList.get(i).getY(), 20, 20);

			if (upgradeList.get(i).getEffect().equals("gunLevel")) //gunLevel upgrades are yellow
			{
				g2d.setColor(Color.YELLOW);
				g2d.fillRect(upgradeList.get(i).getX()+1, upgradeList.get(i).getY()+1, 18, 18);
			}
			else   //health upgrades are red
			{
				g2d.setColor(Color.RED);
				g2d.fillRect(upgradeList.get(i).getX()+1, upgradeList.get(i).getY()+1, 18, 18);
			}
		}

		g2d.drawImage(ship, x, y, this);

		if (gameOn == false)
		{
			g2d.drawString("Game Over! You scored " + points + " points", 250, 400);
		}
	}

	public void run()
	{
		while(true)
		{
			if(gameOn)
			{
				int movespeed = 3;

				if (right && new Rectangle(0,0,900,800).contains(new Rectangle(x+movespeed,y,30,30)))   //MOVEMENT
				{
					x+= movespeed;
				}
				if (left && new Rectangle(0,0,900,800).contains(new Rectangle(x-movespeed,y,30,30)))
				{
					x-= movespeed;
				}
				if (up && new Rectangle(0,0,900,800).contains(new Rectangle(x,y-movespeed,30,30)))
				{
					y-= movespeed;
				}
				if (down && new Rectangle(0,0,900,800).contains(new Rectangle(x,y+movespeed,30,30)))
				{
					y+= movespeed;
				}

				if (up && !down && !left && !right)        //decides direction of player
					dir = 1;
				else if (up && right && !down && !left)
					dir = 2;
				else if (right && !up && !down && !left)
					dir = 3;
				else if (down && right && !up && !left)
					dir = 4;
				else if (down && !up && !left && !right)
					dir = 5;
				else if (down && left && !up && !right)
					dir = 6;
				else if(left && !right && !up && !down)
					dir = 7;
				else if(up && left && !down && !right)
					dir = 8;



				//MATH HAPPENS HERE!!!!

				while (asteroidList.size() < 15)    //SPAWNING NEW ASTEROIDS FROM OUTSIDE SCREEN
				{
					int spawnside = (int)(Math.random()*4)+1;   //1 = top      2 = left       3 = bottom        4 = right
					int spawnX = 0;
					int spawnY = 0;
					int type = (int)(Math.random()*3)+1;
					int size = 0;

					if (type == 1)
					{
						size = 15;
					}
					else if (type == 2)
					{
						size = 25;
					}
					else if (type == 3)
					{
						size = 35;
					}


					switch (spawnside)
					{
						case 1:
							spawnY = 0;
							spawnX = (int)(Math.random()*900);
							break;

						case 3:
							spawnY = 800-size;
							spawnX = (int)(Math.random()*900);
							break;

						case 2:
							spawnX = 0;
							spawnY = (int)(Math.random()*800);
							break;

						case 4:
							spawnX = 900-size;
							spawnY = (int)(Math.random()*800);
							break;
					}


					int slopeX = (int)(Math.random()*4);  //slopeX of 0-3
					if (spawnside == 4)
					{
						slopeX *= -1;
					}
					int slopeY = (int)(Math.random()*4);
					if (spawnside == 3)
					{
						slopeY *= -1;
					}
					while (slopeX == 0 && slopeY == 0)    //MAKE SURE THAT AN ASTEROID DOESN'T HAVE A SLOPEx AND SLOPEy OF 0, CAUSE IT WON'T MOVE
					{
						slopeX = (int)(Math.random()*4);  //slopeX of 0-3
						if (spawnside == 4)
						{                                 //checking for which positions require initial negative slopes so they actually go onscreen
							slopeX *= -1;
						}
						slopeY = (int)(Math.random()*4);
						if (spawnside == 3)
						{
							slopeY *= -1;
						}
					}

					asteroidList.add(new Asteroid(spawnX, spawnY, type, slopeX, slopeY));
				}


				//SPAWNING NEW UPGRADES
				if (gunLevel < 3 || health < 17) //when upgrades can spawn
				{
					int chance = (int)(Math.random()*800); //small chance of spawning new upgrade, happens around every 10s or so?
					int upgradeType = (int)(Math.random()*100)+1;   //60 % of upgrades will be health, 40% will be gunlevel
					if (gunLevel == 3)   //if one upgrade is maxed, get the other one
					{
						upgradeType = 40;
					}
					if (health == 17)
					{
						upgradeType = 80;
					}
					String upgradeEffect = "";
					if (chance == 5)
					{
						if (upgradeType > 60)
						{
							upgradeEffect = "gunLevel";
						}
						else
						{
							upgradeEffect = "health";
						}

						int upgradeX = (int)(Math.random()*900);
						int upgradeY = (int)(Math.random()*800);
						upgradeList.add(new Upgrade(upgradeX, upgradeY, upgradeEffect));
					}
				}

				ArrayList<Ellipse2D.Double> ellipses = new ArrayList<Ellipse2D.Double>();
				ArrayList<Ellipse2D.Double> laserellipses = new ArrayList<Ellipse2D.Double>();

				for (int i = 0; i < asteroidList.size(); i ++)    //DECLARE ELLIPSES FOR MATH
				{
					ellipses.add(new Ellipse2D.Double(asteroidList.get(i).getX(), asteroidList.get(i).getY(), asteroidList.get(i).getSize(), asteroidList.get(i).getSize()));
				}
				for (int i = 0; i < laserList.size(); i ++)
				{
					laserellipses.add(new Ellipse2D.Double(laserList.get(i).getX(), laserList.get(i).getY(), laserList.get(i).getSize(), laserList.get(i).getSize()));
				}


				//STUFF FOR WHEN ASTEROIDS HIT EACH OTHER
				runcount = 1;
				int locX;
				int locY;
				boolean exitLoop = false;

				while (runcount > 0)
				{
					exitLoop = false;
					for (int i = 0; i < asteroidList.size()-1; i ++)
					{
						for (int j = i+1; j < asteroidList.size();  j ++)
						{
							if (new Rectangle(asteroidList.get(i).getX(), asteroidList.get(i).getY(), asteroidList.get(i).getSize(), asteroidList.get(i).getSize()).intersects(new Rectangle(asteroidList.get(j).getX(), asteroidList.get(j).getY(), asteroidList.get(j).getSize(), asteroidList.get(j).getSize())))
							{
								//System.out.println("Removed by collision");
								locX = asteroidList.get(i).getX();
								locY = asteroidList.get(i).getY();
								ellipses.remove(j);
								ellipses.remove(i);
								asteroidList.remove(j);
								asteroidList.remove(i);
								runcount ++;
								exitLoop = true;

								//SPAWN NEW ASTEROIDS AFTER COLLISION

								asteroidList.add(newAsteroid(locX-21, locY, 3, 2));
								ellipses.add(new Ellipse2D.Double(locX-21, locY, asteroidList.get(asteroidList.size()-1).getSize(), asteroidList.get(asteroidList.size()-1).getSize()));
								asteroidList.add(newAsteroid(locX+21, locY, 3, 2));
								ellipses.add(new Ellipse2D.Double(locX+21, locY, asteroidList.get(asteroidList.size()-1).getSize(), asteroidList.get(asteroidList.size()-1).getSize()));

								break;
							}
						}
						if (exitLoop == true)
						{
							break;
						}
					}
					runcount --;
				}

				//STUFF FOR WHEN LASER HITS ASTEROID
					runcount = 1;
					exitLoop = false;

					while (runcount > 0)
					{
						exitLoop = false;
						for (int i = laserList.size()-1; i >= 0; i --)
						{
							for (int j = asteroidList.size()-1; j >= 0;  j --)
							{
								if (new Rectangle(laserList.get(i).getX(), laserList.get(i).getY(), laserList.get(i).getSize(), laserList.get(i).getSize()).intersects(new Rectangle(asteroidList.get(j).getX(), asteroidList.get(j).getY(), asteroidList.get(j).getSize(), asteroidList.get(j).getSize())))
								{
									//System.out.println("Removed by laser");
									points += 2;  //earn 2 points for shooting asteroid
									ellipses.remove(j);
									laserellipses.remove(i);
									asteroidList.remove(j);
									laserList.remove(i);
									runcount ++;
									exitLoop = true;

									break;
								}
							}
							if (exitLoop == true)
							{
								break;
							}
						}
						runcount --;
					}


				//STUFF FOR WHEN PLAYER HITS ASTEROID
				if (tempFrames == 0)
				{
					for (int i = 0; i < ellipses.size(); i ++)
					{
						if (ellipses.get(i).intersects(new Rectangle(x,y,30,30)))
						{
							tempFrames = invFrames;
							deaths ++;
							x = 450;
							y = 400;
							health --;

						}
					}
				}

				//STUFF FOR WHEN PLAYER GETS UPGRADE

				for (int i = upgradeList.size()-1; i >= 0; i --)
				{
					if(new Rectangle(x, y, 30, 30).intersects(new Rectangle(upgradeList.get(i).getX(), upgradeList.get(i).getY(), 20, 20)))
					{
						if (upgradeList.get(i).getEffect().equals("gunLevel"))
						{
							gunLevel ++;
						}
						if (upgradeList.get(i).getEffect().equals("health"))
						{
							health ++;
						}
						upgradeList.remove(i);
					}
				}

				/*for (int i = 0; i < ellipses.size(); i ++)    //bit of code that makes the asteroids bounce off the walls, just for fun
				{
					if (ellipses.get(i).getX()+asteroidList.get(i).getSlopeX() < 0 || ellipses.get(i).getX()+asteroidList.get(i).getSlopeX()+asteroidList.get(i).getSize() > 900)
					{
						asteroidList.get(i).setSlopeX(-1 * asteroidList.get(i).getSlopeX());
					}
					if (ellipses.get(i).getY()+asteroidList.get(i).getSlopeY() < 0 || ellipses.get(i).getY()+asteroidList.get(i).getSlopeY()+asteroidList.get(i).getSize() > 800)
					{
						asteroidList.get(i).setSlopeY(-1 * asteroidList.get(i).getSlopeY());
					}
				}*/

				/*for (int i = 1; i <= 8; i ++) //this is super fun
				{
					laserList.add(new Laser(x, y, i));
				}*/


				for (int i = 0; i < asteroidList.size(); i ++)  //move all the asteroids
				{
					asteroidList.get(i).move();
				}
				for (int i = 0; i < laserList.size(); i ++)     //move all the lasers
				{
					laserList.get(i).move();
				}


				for (int i = ellipses.size()-1; i >= 0; i --)  //if an asteroid leaves the screen, remove it from the arrays
				{
					if (!ellipses.get(i).intersects(new Rectangle(0,0,900,800)))
					{
						//System.out.println("Removed by leaving screen");
						ellipses.remove(i);
						asteroidList.remove(i);
					}
				}
				for (int i = laserellipses.size()-1; i >= 0; i --)  //if an laser leaves the screen, remove it from the arrays
				{
					if (!laserellipses.get(i).intersects(new Rectangle(0,0,900,800)))
					{
						//System.out.println("Removed by leaving screen");
						laserellipses.remove(i);
						laserList.remove(i);
					}
				}

				if (tempFrames > 0)   //reduce tempFrams in case just died
				{
					tempFrames--;
				}

				pointTime ++;
				if (pointTime == 30)  //makes points increase at a certain rate
				{
					pointTime = 0;
					points ++;
				}

				if (health <= 0)
				{
					gameOn = false;
				}

				repaint();
			}

			try
			{
				t.sleep(5);
			}
			catch(InterruptedException e)
			{

			}

		}
	}

	public void keyPressed(KeyEvent ke)
	{
		//System.out.println(ke.getKeyCode());

		if(ke.getKeyCode()==KeyEvent.VK_RIGHT)
		{
			//System.out.println("RIGHT");
			right = true;
		}
		if (ke.getKeyCode() == KeyEvent.VK_LEFT)
		{
			//System.out.println("LEFT");
			left = true;
		}
		if (ke.getKeyCode() == KeyEvent.VK_UP)
		{
			//System.out.println("UP");
			up = true;
		}
		if (ke.getKeyCode() == KeyEvent.VK_DOWN)
		{
			//System.out.println("DOWN");
			down = true;
		}
		//87 = W
		//65 = A
		//83 = S
		//68 = D
		//32 = space

		if (laserList.size() == 0) //limit number of bullets
		{
			if (ke.getKeyCode() == 87) //w
			{
				if (gunLevel == 1)
				{
					laserList.add(new Laser(x+7, y+10, 1));
				}
				else if (gunLevel == 2)
				{
					laserList.add(new Laser(x+7, y+10, 8));
					laserList.add(new Laser(x+7, y+10, 2));
				}
				else
				{
					laserList.add(new Laser(x+7, y+10, 1));
					laserList.add(new Laser(x+7, y+10, 2));
					laserList.add(new Laser(x+7, y+10, 8));
				}
			}
			if (ke.getKeyCode() == 68) //d
			{
				if (gunLevel == 1)
				{
					laserList.add(new Laser(x+7, y+10, 3));
				}
				else if (gunLevel == 2)
				{
					laserList.add(new Laser(x+7, y+10, 4));
					laserList.add(new Laser(x+7, y+10, 2));
				}
				else
				{
					laserList.add(new Laser(x+7, y+10, 3));
					laserList.add(new Laser(x+7, y+10, 2));
					laserList.add(new Laser(x+7, y+10, 4));
				}
			}
			if (ke.getKeyCode() == 83) //s
			{
				if (gunLevel == 1)
				{
					laserList.add(new Laser(x+7, y+10, 5));
				}
				else if (gunLevel == 2)
				{
					laserList.add(new Laser(x+7, y+10, 6));
					laserList.add(new Laser(x+7, y+10, 4));
				}
				else
				{
					laserList.add(new Laser(x+7, y+10, 5));
					laserList.add(new Laser(x+7, y+10, 4));
					laserList.add(new Laser(x+7, y+10, 6));
				}
			}
			if (ke.getKeyCode() == 65) //a
			{
				if (gunLevel == 1)
				{
					laserList.add(new Laser(x+7, y+10, 7));
				}
				else if (gunLevel == 2)
				{
					laserList.add(new Laser(x+7, y+10, 8));
					laserList.add(new Laser(x+7, y+10, 6));
				}
				else
				{
					laserList.add(new Laser(x+7, y+10, 7));
					laserList.add(new Laser(x+7, y+10, 6));
					laserList.add(new Laser(x+7, y+10, 8));
				}
			}
		}
	}
	public void keyReleased(KeyEvent ke)
	{
		if(ke.getKeyCode()==KeyEvent.VK_RIGHT)
		{
			right = false;
		}
		if (ke.getKeyCode() == KeyEvent.VK_LEFT)
		{
			left = false;
		}
		if (ke.getKeyCode() == KeyEvent.VK_UP)
		{
			up = false;
		}
		if (ke.getKeyCode() == KeyEvent.VK_DOWN)
		{
			down = false;
		}
	}
	public void keyTyped(KeyEvent ke)
	{

	}
	public Asteroid newAsteroid(int x, int y, int sizeRange, int slopeRange)   //makes coding easier
	{
		int dir = (int)(Math.random()*4)+1;    //1 = up-right     2 = up-left     3 = down-right    4 = down-left
		int size = (int)(Math.random()*sizeRange)+1;
		int slopeX = (int)(Math.random()*(slopeRange+1));
		int slopeY = (int)(Math.random()*(slopeRange+1));

		switch (dir)
		{
			case 1:
				slopeY *= -1;
				break;
			case 2:
				slopeY *= -1;
				slopeX *= -1;
				break;
			case 3:
				break;
			case 4:
				slopeX *= -1;
				break;
			default:
				break;
		}

		while (slopeY == 0 && slopeX == 0)
		{
			dir = (int)(Math.random()*4)+1;    //1 = up-right     2 = up-left     3 = down-right    4 = down-left
			slopeX = (int)(Math.random()*(slopeRange+1));
			slopeY = (int)(Math.random()*(slopeRange+1));

			switch (dir)
			{
				case 1:
					slopeY *= -1;
					break;
				case 2:
					slopeY *= -1;
					slopeX *= -1;
					break;
				case 3:
					break;
				case 4:
					slopeX *= -1;
					break;
				default:
					break;
			}
		}

		return new Asteroid(x, y, size, slopeX, slopeY);
	}
	public static void main(String args[])
	{
		AsteroidGame app=new AsteroidGame();
	}
}
