public class Upgrade
{
	private int x;
	private int y;
	private String effect;

	public Upgrade(int x, int y, String effect)
	{
		this.x = x;
		this.y = y;
		this.effect = effect;
	}

	public int getX()
	{
		return x;
	}
	public int getY()
	{
		return y;
	}
	public String getEffect()
	{
		return effect;
	}
}