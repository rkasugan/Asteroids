public class Laser
{
	private int x;
	private int y;
	private int slope = 4;
	private int slopeX = 0;
	private int slopeY = 0;
	int dir;    //1 = up     2 = up-right    3 = right    4 = down-right     5 = down    6 = down-left    7 = left    8 = up-left
	private int size = 15;

	public Laser(int x, int y, int dir)
	{
		switch (dir)
		{
			case 1:
				slopeY = -1 * slope;
				break;
			case 2:
				slopeY = -1 * slope;
				slopeX = slope;
				break;
			case 3:
				slopeX = slope;
				break;
			case 4:
				slopeY = slope;
				slopeX = slope;
				break;
			case 5:
				slopeY = slope;
				break;
			case 6:
				slopeY = slope;
				slopeX = -1 * slope;
				break;
			case 7:
				slopeX = -1 * slope;
				break;
			case 8:
				slopeY = -1 * slope;
				slopeX = -1 * slope;
				break;
			default:
				break;
		}
		this.x = x;
		this.y = y;
	}

	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	public int getSlopeX()
	{
		return slopeX;
	}
	public int getSlopeY()
	{
		return slopeY;
	}
	public void move()
	{
		x += slopeX;
		y += slopeY;
	}
	public int getSize()
	{
		return size;
	}
}