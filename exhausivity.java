import java.util.ArrayList;
import java.awt.*;

public class exhausivity extends Algorithm{
	private int[] _list;
	
	public exhausivity(bagData bag,goodsData goods,solutionData solution){
		super(bag, goods, solution);
	}
	
	public void solve(){
		_list=new int[_goods.getNumbers()];
		perm(0, _list.length-1);
	}
	
	private void perm(int k, int m){
		if(k==m){
			_list[m]=0;
			calculate();
			_list[m]=1;
			calculate();
		}
		else{
			_list[k]=0;
			perm(k+1, m);
			_list[k]=1;
			perm(k+1, m);
		}
	}
	
	private void calculate(){
		for(int i=0; i<_list.length; i++){
			float oneValue=_list[i]*_goods.getOneValue(i);
			float oneWeight= _list[i]*_goods.getOneWeight(i);
			_bag.setNowValue(_bag.getNowValue()+oneValue);
			_bag.setNowWeight(_bag.getNowWeight()+oneWeight);
			_solution.setNowSolution(i+1, _list[i]);
		}
		if(_bag.getNowWeight()<=_bag.getBagSize() && _bag.getNowValue()>_bag.getMaxValue()){
			_bag.setMaxValue(_bag.getNowValue());
			_solution.refreshBestSolution();
		}
		_bag.setNowValue(0);
		_bag.setNowWeight(0);
	}
	
	public ArrayList<String> levelPoint(){
		_heavyAndValues.clear();
		for(int i=0; i<_goods.getNumbers(); i++){
			String result="("+_goods.getOneWeight(i)+" , "+
				_goods.getOneValue(i)+")";
			_heavyAndValues.add(result);
		}
		return _heavyAndValues;
	}
	
	public ArrayList<Point> getDrawList(){
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
