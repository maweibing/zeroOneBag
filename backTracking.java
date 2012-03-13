import java.util.ArrayList;

public class backTracking extends Algorithm{
	private ArrayList<String> _heavyAndValues=new ArrayList<String>();
	
	public backTracking(bagData bag, goodsData goods, solutionData solution){
		super(bag, goods, solution);
		_heavyAndValues.clear();
	}
	
	public void backTrack(int level){
		if(level>_goods.getNumbers()){
			if(_bag.getNowValue() > _bag.getMaxValue()){
				_bag.setMaxValue(_bag.getNowValue());
				for(int i=1; i<_solution.getNowSolutionSize(); i++)
					_solution.setBestSolution(i, _solution.getNowSolution(i));
			}
			return;
		}
		if(_bag.getNowWeight()+_goods.getOneWeight(level-1) <=_bag.getBagSize()){
			_bag.setNowWeight(_bag.getNowWeight()+_goods.getOneWeight(level-1));
			_bag.setNowValue(_bag.getNowValue()+_goods.getOneValue(level-1));			
			_solution.setNowSolution(level, 1);			
			_solution.addSolutionPath(level);			
			_solution.addSolutionPath(_solution.getNowSolution(level));			
			backTrack(level+1);			
			_bag.setNowWeight(_bag.getNowWeight()-_goods.getOneWeight(level-1));
			_bag.setNowValue(_bag.getNowValue()-_goods.getOneValue(level-1));
		}
		_solution.setNowSolution(level, 0);
		_solution.addSolutionPath(level);
		_solution.addSolutionPath(_solution.getNowSolution(level));
		backTrack(level+1);
	}
	
	public ArrayList<String> levelPoint(){
		for(int i=0; i<_goods.getNumbers(); i++){
			String result="("+_goods.getOneWeight(i)+" , "+
				_goods.getOneValue(i)+")";
			_heavyAndValues.add(result);
		}
		return _heavyAndValues;
	}
	
}