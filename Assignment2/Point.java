//Romanos Kotsis 4714
public class Point{
	private int lineNumber;
	private double x;
	private double y;

	public Point(int lineNumber,double x,double y){
		this.lineNumber = lineNumber;
		this.x = x;
		this.y = y;
	}

	public int getLineNumber(){
		return this.lineNumber;
	}

	public double getX(){
		return this.x;
	}

	public double getY(){
		return this.y;
	}

	public void setLineNumber(int lineNumber){
		this.lineNumber = lineNumber;
	}

	public void setX(double x){
		this.x = x;
	}

	public void setY(double y){
		this.y = y;
	}
}