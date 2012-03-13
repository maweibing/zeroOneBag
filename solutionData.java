import java.util.ArrayList;

public class solutionData {
	private ArrayList<Integer> _solutionPath=new ArrayList<Integer>();
	private int[] _bestSolution;
	private int[] _nowSolution;
	private goodsData _goods;
	
	public solutionData(goodsData goods){
		_solutionPath.clear();
		_goods=goods;	
	}
	
	public void initSolution(){
		_solutionPath.clear();
		_bestSolution=new int[_goods.getNumbers()+1];
		_nowSolution=new int[_goods.getNumbers()+1];
	}
	
	public void addSolutionPath(int pathNumber){
		_solutionPath.add(pathNumber);
	}
	
	public ArrayList<Integer> getSolutionPath(){
		return _solutionPath;
	}
	
	public void setBestSolution(int index, int element){
		_bestSolution[index]=element;
	}
	
	public int[] getBestSolution(){
		return _bestSolution;
	}
	
	public int getBestSolution(int index){
		return _bestSolution[index];
	}
	
	public int getBestSolutionSize(){
		return _bestSolution.length;
	}
	
	public int getNowSolutionSize(){
		return _nowSolution.length;
	}
	
	public int[] getNowSolution(){
		return _nowSolution;
	}
	
	public int getNowSolution(int index){
		return _nowSolution[index];
	}
	
	public void setNowSolution(int index, int element){
		_nowSolution[index]=element;
	}
}
