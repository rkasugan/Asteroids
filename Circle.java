public class Circle
{
	private int x;
	private int y;
	private int size;

	public Circle(int x, int y, int size)
	{
		this.x = x;
		this.y = y;
		this.size = size;
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
}