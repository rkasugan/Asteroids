import javax.swing.*;
import java.awt.*;

public class Asteroid
{
	private int x;
	private int y;
	private int type;
	private int size;
	private int slopeX;
	private int slopeY;
	private Image rock;

	public Asteroid(int x, int y, int type, int slopeX, int slopeY)
	{
		if (type == 1)
		{
			size = 15;
			rock = new ImageIcon(this.getClass().getResource("rock15.png")).getImage();		}
		else if (type == 2)
		{
			size = 25;
			rock = new ImageIcon(this.getClass().getResource("rock25.png")).getImage();
		}
		else if (type == 3)
		{
			size = 35;
			rock = new ImageIcon(this.getClass().getResource("rock35.png")).getImage();
		}
		this.x = x;
		this.y = y;
		this.type = type;
		this.slopeX = slopeX;
		this.slopeY = slopeY;
	}

	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	public int getSize()
	{
		return size;
	}
	public void changeX(int val)
	{
		x += val;
	}
	public void changeY(int val)
	{
		y += val;
	}
	public void setX(int val)
	{
		x = val;
	}
	public void setY(int val)
	{
		y = val;
	}
	public void setType(int val)
	{
		type = val;
		updateSize();
	}
	public void updateSize()
	{
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
	}
	public int getType()
	{
		return type;
	}
	public int getSlopeX()
	{
		return slopeX;
	}
	public int getSlopeY()
	{
		return slopeY;
	}
	public void setSlopeX(int val)
	{
		slopeX = val;
	}
	public void setSlopeY(int val)
	{
		slopeY = val;
	}
	public void move()
	{
		x += slopeX;
		y += slopeY;
	}
	public Image getImage()
	{
		return rock;
	}
}