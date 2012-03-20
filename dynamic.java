import java.awt.Point;
import java.util.ArrayList;

public class dynamic extends Algorithm{
	int [][]m;
	private ArrayList<String> _heavyAndValues=new ArrayList<String>();
	private ArrayList<Point> _drawList= new ArrayList<Point>();
	
	public dynamic(bagData bag,goodsData goods,solutionData solution){
		super(bag, goods, solution);
		_heavyAndValues.clear();
		_drawList.clear();
	}
	
	public void solve(){
		m=new int[_goods.getNumbers()+1][(int)_bag.getBagSize()+1];
		dynamicProgram();
		trackBack();
		_bag.setMaxValue(maxValue());
	}
	
	private void dynamicProgram(){
		int c=(int)_bag.getBagSize();
		int n=_goods.getNumbers();
		int jMax=Math.min((int)_goods.getOneWeight(n-1)-1,c);
		for(int j=0; j<=jMax; j++)
			m[n][j]=0;
		for(int j=(int)_goods.getOneWeight(n-1); j<=c; j++)
			m[n][j]=(int)_goods.getOneValue(n-1);
		for(int i=n-1; i>1; i--){
			jMax=Math.min((int)_goods.getOneWeight(i-1)-1, c);
			for(int j=0; j<=jMax; j++)
				m[i][j]=m[i+1][j];
			for(int j=(int)_goods.getOneWeight(i-1);j<=c;j++)
				m[i][j]=Math.max(m[i+1][j], m[i+1][j-(int)_goods.getOneWeight(i-1)]+
						(int)_goods.getOneValue(i-1));
		}
		m[1][c]=m[2][c];
		if(c >=(int)_goods.getOneWeight(0))
			m[1][c]=Math.max(m[1][c], 
					m[2][c-(int)_goods.getOneWeight(0)]+(int)_goods.getOneValue(0));
	}
	
	private void trackBack(){
		int n=_goods.getNumbers();
		int c= (int)_bag.getBagSize();
		for(int i=1; i<n; i++){
			if(m[i][c]==m[i+1][c])
				_solution.setBestSolution(i, 0);
			else{
				_solution.setBestSolution(i, 1);
				c-=_goods.getOneWeight(i-1);
			}
		}
		_solution.setBestSolution(n, (m[n][c]>0) ? 1:0);
	}
	
	public float maxValue(){
		float max=0;
		for(int i=1; i<_solution.getBestSolutionSize(); i++){
			max += _solution.getBestSolution(i)*_goods.getOneValue(i-1);
		}
		return max;
	}
	
	public ArrayList<String> levelPoint(){
		for(int i=0; i<_goods.getNumbers(); i++){
			String result="("+_goods.getOneWeight(i)+" , "+
				_goods.getOneValue(i)+")";
			_heavyAndValues.add(result);
		}
		return _heavyAndValues;
	}
	
	public ArrayList<Point> getDrawList(){
		_drawList.clear();
		int k=1;
		for(int i=1;i<_solution.getBestSolutionSize();i++){
			if(_solution.getBestSolution(i)==1)
				k=k*2;
			else
				k=k*2+1;
			_drawList.add(_drawPoint.get(k/2-1));
			_drawList.add(_drawPoint.get(k-1));
		}
		return _drawList;
	}
}
