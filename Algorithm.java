import java.awt.Point;
import java.util.ArrayList;

public class Algorithm {
	protected bagData _bag;
	protected goodsData _goods;
	protected solutionData _solution;
	protected ArrayList<Point> _drawPoint=new ArrayList<Point>();
	protected ArrayList<String> _heavyAndValues=new ArrayList<String>();
	protected ArrayList<Point> _drawList= new ArrayList<Point>();
	protected ArrayList<Integer> _path = new ArrayList<Integer>();
	
	public Algorithm(bagData bag, goodsData goods, solutionData solution){
		_bag=bag;
		_goods=goods;
		_solution=solution;
		_heavyAndValues.clear();
		_drawList.clear();
		_path.clear();
	}
	
	public ArrayList<Point> getDrawPoint(int pointNum){
		_drawPoint.clear();
		final int Row=50;
		int length, higth, col;
		for (int j = 1; j <= pointNum; j++) {
			int k = (int) Math.pow(2, j - 1);
			col=(int)(20*Math.pow(2, pointNum-1)/(Math.pow(2, j-1)+1));
			higth = j * Row + 100;
			length = col + 270;
			while (k >= 1) {
				_drawPoint.add(new Point(length, higth));
				length += col;
				k--;
			}
		}
		return _drawPoint;
	}
	
	public String showResult(){
		String result="[ ";
		for(int i=1; i<_solution.getBestSolutionSize(); i++)
			result+=_solution.getBestSolution(i)+" ";
		result+=" ]";
		return result;
	}
	
	public ArrayList<Integer> getPath(){return _path;}
	public void solve(){}
	public ArrayList<String> levelPoint(){return _heavyAndValues;}
	public ArrayList<Point> getDrawList(){return _drawList;}
}
