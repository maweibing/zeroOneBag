import java.awt.Point;
import java.util.ArrayList;

public class Algorithm {
	protected bagData _bag;
	protected goodsData _goods;
	protected solutionData _solution;
	protected ArrayList<Point> _drawPoint=new ArrayList<Point>();
	
	public Algorithm(bagData bag, goodsData goods, solutionData solution){
		_bag=bag;
		_goods=goods;
		_solution=solution;
	}
	
	public ArrayList<Point> getDrawPoint(int pointNum){
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
}
