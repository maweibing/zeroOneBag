import javax.swing.*;

public class bagData{
	private JTextField _textField;
	private float _nowValue;
	private float _maxValue;
	private float _nowWeight;
	
	public bagData(JTextField textField){
		_textField=textField;
		_nowValue=0;
		_maxValue=0;
		_nowWeight=0;
	}
	
	public void initBag(){
		_nowValue=0;
		_maxValue=0;
		_nowWeight=0;
	}
	
	public float getBagSize(){
		return Float.parseFloat(_textField.getText());
	}
	
	public void setNowValue(float nowValue){
		_nowValue=nowValue;
	}
	
	public float getNowValue(){
		return _nowValue;
	}
	
	public void setMaxValue(float maxValue){
		_maxValue=maxValue;
	}
	
	public float getMaxValue(){
		return _maxValue;
	}
	
	public void setNowWeight(float nowWeight){
		_nowWeight=nowWeight;
	}
	
	public float getNowWeight(){
		return _nowWeight;
	}
}