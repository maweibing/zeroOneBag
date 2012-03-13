import javax.swing.*;
import java.util.ArrayList;

public class goodsData {
	private JTable _table;
	private final int _maxBagNumber=52;
	private ArrayList<Float> _values=new ArrayList<Float>();
	private ArrayList<Float> _weight=new ArrayList<Float>();
	
	public goodsData(JTable table){
		_table=table;
	}
	
	public void initGoods(){
		_values.clear();
		_weight.clear();
	}
	
	public ArrayList<Float> getValues(){
		_values.clear();
		for(int i=0; i<_maxBagNumber; i++){
			if(_table.getValueAt(i, 1)==null)
				break;
			_values.add(Float.parseFloat((String)_table.getValueAt(i,1)));
		}
		return _values;
	}
	
	public float getOneValue(int index){
		return _values.get(index);
	}
	
	public ArrayList<Float> getWeight(){
		_weight.clear();
		for(int i=0; i<_maxBagNumber; i++){
			if(_table.getValueAt(i, 0)==null)
				break;
			_weight.add(Float.parseFloat((String)_table.getValueAt(i, 0)));
		}
		return _weight;
	}
	
	public float getOneWeight(int index){
		return _weight.get(index);
	}
	
	public int getNumbers(){
		if(_values.size()==_weight.size())
			return _values.size();
		else
			return 0;
	}
}
