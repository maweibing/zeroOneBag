import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.event.*;

public class zeroOneBag extends JFrame {

	public zeroOneBag() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("0-1 bag  Author:Weibing Ma");
		showPanel panel = new showPanel();
		JScrollPane spane = new JScrollPane(panel);
		setPreferredSize(new Dimension(1000, 610));
		getContentPane().add(spane);
		setLocation(200, 100);
		pack();
		setVisible(true);
	}

	public static void main(String[] args) {
		zeroOneBag zeroOnePanel = new zeroOneBag();
	}

	private class showPanel extends JPanel {
		private JLabel label1 = new JLabel("BagSize：");
		private JTextField text = new JTextField(10);
		private String[] mode = { "BackTrackint", "BranchAndBound",
				"exhausivity", "dynamic"};		
		private JComboBox algorithm = new JComboBox(mode);
		private JButton button = new JButton("Start");
		private JButton stopButton = new JButton("stop");
				
		private String[] column = { "Weight", "Values" };				
		private String[][] content=new String[52][2];
		private DefaultTableModel model = new DefaultTableModel(content, column);
		private JTable table = new JTable(model);
		private JScrollPane data = new JScrollPane(table);
		
		private String modestr = mode[0], string = "";				
		private JPanel panel1 = new JPanel();
		private Timer timer;
		private long runTime;
		private ArrayList<Point> point = new ArrayList<Point>();
		private ArrayList<Integer> path = new ArrayList<Integer>();
		private ArrayList<Point> drawList = new ArrayList<Point>();
		private ArrayList<String> level = new ArrayList<String>();
		private Point point1;
		private Point point2=new Point();
		private Point drawPoint =new Point();		
		private float bestp;
		private int move, k, i, stepNum;						
		private boolean mark=false;
		
		goodsData goods = new goodsData(table);
		bagData bag = new bagData(text);
		solutionData solution = new solutionData(goods);
		backTracking back = new backTracking(bag, goods,solution);
		BranchAndBound _branch = new BranchAndBound(bag, goods,solution);
		exhausivity _exhausivity= new exhausivity(bag, goods, solution);
		dynamic _dynamic=new dynamic(bag, goods, solution);
		
		public showPanel() {			
			text.setBounds(100, 10, 20, 10);			
			panel1.setLayout(new BoxLayout(panel1, BoxLayout.X_AXIS));
			panel1.add(Box.createRigidArea(new Dimension(230, 0)));
			panel1.add(label1);
			panel1.add(text);
			panel1.add(Box.createRigidArea(new Dimension(10, 0)));
			panel1.add(algorithm);
			panel1.add(Box.createRigidArea(new Dimension(10, 0)));
			panel1.add(button);
			panel1.add(Box.createRigidArea(new Dimension(10, 0)));
			panel1.add(stopButton);
			panel1.add(Box.createRigidArea(new Dimension(1400, 0)));
			
			data.setPreferredSize(new Dimension(200, 510));						
			setLayout(new BorderLayout());
			add(panel1, BorderLayout.NORTH);
			add(data, BorderLayout.WEST);
			setPreferredSize(new Dimension(2100, 900));
			setBackground(Color.black);			
			timer = new Timer(40, new ReboundListener());

			// 选择框监听事件
			algorithm.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JComboBox box = (JComboBox) e.getSource();
					modestr = (String) box.getSelectedItem();
				}
			});

			// 按钮监听事件
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					initDatas();
					if (modestr.equals(mode[0])) {
						backTrackSolve();
					} else if(modestr.equals(mode[1])){
						branchAndBoundSolve();
					}else if(modestr.equals(mode[2])){
						exhausivitySolve();
					}else if(modestr.equals(mode[3])){
						dynamicSolve();
					}
					timer.start();
				}
				
				public void dynamicSolve(){
					point = _dynamic.getDrawPoint(goods.getNumbers()+1);
					long t1 = System.currentTimeMillis();
					_dynamic.solve();
					long t2 = System.currentTimeMillis();
					runTime = t2 - t1;
					bestp=bag.getMaxValue();
					string= _dynamic.showResult();
					level = _dynamic.levelPoint();
					drawList = _dynamic.getDrawList();
					getBranchAndBoundPoint();
				}
				
				public void exhausivitySolve(){
					point = _exhausivity.getDrawPoint(goods.getNumbers()+1);
					long t1 = System.currentTimeMillis();
					_exhausivity.solve();
					long t2 = System.currentTimeMillis();
					runTime = t2 - t1;
					bestp=bag.getMaxValue();
					string=_exhausivity.showResult();
					level = _exhausivity.levelPoint();
					drawList=_exhausivity.getDrawList();
					getBranchAndBoundPoint();
				}
				
				public void backTrackSolve(){
					point = back.getDrawPoint(goods.getNumbers() + 1);
					long t1 = System.currentTimeMillis();
					back.backTrack(1);												
					long t2 = System.currentTimeMillis();
					runTime = t2 - t1;					
					path = solution.getSolutionPath();
					bestp = bag.getMaxValue();
					string = back.showResult();
					level = back.levelPoint();											
					getBacktrackPoint();
				}
				
				public void branchAndBoundSolve(){
					point = _branch.getDrawPoint(goods.getNumbers() + 1);
					long t1 = System.currentTimeMillis();
					_branch.knapsack();
					long t2 = System.currentTimeMillis();
					runTime = t2 - t1;					
					bestp=bag.getMaxValue();
					string = _branch.showResult();
					drawList=_branch.getOffSet();
					level = _branch.levelPoint();
					getBranchAndBoundPoint();
				}
				
				public void initDatas(){
					goods.initGoods();
					goods.getValues();
					goods.getWeight();
					solution.initSolution();
					bag.initBag();					
					i = 0;
					stepNum=0;
					mark = true;
					point.clear();
					level.clear();
					path.clear();
					drawList.clear();
				}
			});
			
			stopButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent event){
					if(mark && timer.isRunning())
						timer.stop();
					else
						timer.start();
				}
			});
		}

		// 时间相应事件
		private class ReboundListener implements ActionListener {
			public void actionPerformed(ActionEvent event) {
				if (rightBottomSearch() || leftBottomSearch()) {
					drawPoint.x += move;
					drawPoint.y = (point2.y - point1.y)
							* (drawPoint.x - point1.x) / (point2.x - point1.x)
							+ point1.y;
					repaint();
				} else {
					if (modestr.equals(mode[0]))
						getBacktrackPoint();
					else
						getBranchAndBoundPoint();
					stepNum=stepNum+2;
				}
			}
			
			public boolean rightBottomSearch(){
				return (point1.x < point2.x && (drawPoint.x + move) < point2.x);
			}
			
			public boolean leftBottomSearch(){
				return (point1.x > point2.x && (drawPoint.x + move) > point2.x);
			}
		}

		public void paintComponent(Graphics page) {
			super.paintComponent(page);
			drawBinaryTree(page);			
			keepPathRecord(page);
			showAnimation(page);
			displayResult(page);
		}
		
		public void showAnimation(Graphics page){
			if (drawPoint != null && point1 != null)
				page.drawLine(point1.x, point1.y, drawPoint.x, drawPoint.y);
		}
		
		public void keepPathRecord(Graphics page){
			page.setColor(Color.yellow);
			for (int i = 0; i < stepNum; i = i + 2) {
				page.drawLine(drawList.get(i).x, drawList.get(i).y,
						drawList.get(i + 1).x, drawList.get(i + 1).y);
			}	
		}

		public void drawBinaryTree(Graphics page) {
			page.setColor(Color.blue);
			for (int i = 0; i < point.size(); i++)
				page.fillOval(point.get(i).x - 4, point.get(i).y - 4, 8, 8);
			for (int i = 1; i <= point.size() / 2; i++) {
				page.drawLine(point.get(i - 1).x, point.get(i - 1).y,
						point.get(2 * i - 1).x, point.get(2 * i - 1).y);
				page.drawLine(point.get(i - 1).x, point.get(i - 1).y,
						point.get(2 * i).x, point.get(2 * i).y);
			}
		}

		public void displayResult(Graphics page) {
			page.setColor(Color.cyan);
			page.drawString("Optimum Solution：" + string, 210, 55);
			page.drawString("MaxValue：" + bestp, 210, 75);
			page.drawString("Run Time：" + runTime + " ms", 210, 95);
			for (int j = 0; j < level.size(); j++)
				page.drawString(level.get(j), 210, 150 + j * 50);
		}

		public void getBacktrackPoint() {
			if (i < path.size()) {
				getPoint1();
				drawPoint.x = point1.x;
				drawPoint.y = point1.y;
				getPoint2();
				move=getMove();
				i = i + 2;		
			} else {
				timer.stop();
				mark = false;
			}
		}
		
		public int getMove(){
			if(point1.x<point2.x)
				return 3;
			else
				return -3;
		}
		
		public void getPoint2(){
			if (path.get(i + 1) == 1)
				point2 = point.get(2 * k - 1);
			else
				point2 = point.get(2 * k);
			drawList.add(point2);
		}
		
		public Point getPoint1(){
			if (i == 0 || path.get(i) == 1)
				k = 1;
			else {
				if (path.get(i) > path.get(i - 2) && path.get(i - 1) == 1)
					k = 2 * k;					
				else if (path.get(i) > path.get(i - 2)&& path.get(i - 1) == 0) 
					k = 2 * k + 1;					
				else if (path.get(i) < path.get(i - 2)) {
					int n3 = path.get(i - 2) - path.get(i);
					while (n3 > 0) {
						k = k / 2;
						n3--;
					}
				}
			}
			point1=point.get(k-1);
			drawList.add(point1);
			return point1;
		}

		public void getBranchAndBoundPoint() {
			if(i<drawList.size()){
				point1=drawList.get(i);
				drawPoint.x = point1.x;
				drawPoint.y = point1.y;
				point2=drawList.get(i+1);
				move=getMove();
				i = i + 2;
			} else {
				timer.stop();
				mark = false;
			}
		}

	}
}