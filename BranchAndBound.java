import java.awt.Point;
import java.util.ArrayList;

public class BranchAndBound extends Algorithm{
	private MaxHeap heap;
	private int nodeNum;
	private float[] p,w;
	private ArrayList<Point> offSet = new ArrayList<Point>();
	private ArrayList<String> _heavyAndValues=new ArrayList<String>();
	
	public BranchAndBound(bagData bag,goodsData goods,solutionData solution){
		super(bag, goods, solution);
		offSet.clear();
		_drawPoint.clear();
		_heavyAndValues.clear();
	}
	
	public float MaxKnapsack() {
		BBnode enode = null;	
		float up = bound(1);
		float max = 0;
		int j = 1;
		while (j != _goods.getNumbers() + 1) {
			float wt = _bag.getNowWeight() + w[j];
			if (wt <= _bag.getBagSize()) {
				if (_bag.getNowValue() + p[j] > max)
					max = _bag.getNowValue()+ p[j];
				addLiveNode(up, _bag.getNowValue() + p[j], 
						_bag.getNowWeight()+ w[j], j + 1, enode, true);
			}
			up = bound(j + 1);
			if (up >= max)
				addLiveNode(up, _bag.getNowValue(), _bag.getNowWeight(), 
						j + 1, enode, false);
			HeapNode node = (HeapNode) heap.removeMax();
			enode = node.liveNode;
			offSet.add(_drawPoint.get(enode.nodeNumber() / 2 - 1));
			offSet.add(_drawPoint.get(enode.nodeNumber() - 1));
			_bag.setNowWeight(node.weight);
			_bag.setNowValue(node.profit);
			up = node.upperProfit;
			j = node.level;

		}
		for (int j1 = _goods.getNumbers(); j1 > 0; j1--) {
			_solution.setNowSolution(j1, (enode.leftChild) ? 1 : 0);
			enode = enode.parent;
		}
		return _bag.getNowValue();
	}

	public void knapsack() {
		int n = _goods.getNumbers();
		Element q[] = new Element[n];
		float ws = 0, ps = 0;
		for (int j = 1; j <= n; j++) {
			q[j - 1] = new Element(j,_goods.getOneValue(j-1)/_goods.getOneWeight(j-1));
			ps+=_goods.getOneValue(j-1);
			ws+=_goods.getOneWeight(j-1);
		}
		if (ws <= _bag.getBagSize()) {
			for (int j = 1; j <= n; j++)
				_solution.setBestSolution(j, 1);
			_bag.setMaxValue(ps);
			return;
		}

		p = new float[n + 1];
		w = new float[n + 1];
		qSort(q, 0, n - 1);
		for (int j = 1; j <= n; j++) {
			p[j] = _goods.getOneValue(q[j - 1].id - 1);
			w[j] = _goods.getOneWeight(q[j - 1].id - 1);
		}
		heap = new MaxHeap(n);
		_bag.setMaxValue(MaxKnapsack());
		for (int j = 1; j <= n; j++)
			_solution.setBestSolution(q[j - 1].id, _solution.getNowSolution(j));
	}

	public void qSort(Element[] q, int p, int r) {
		if (p < r) {
			int k = partition(q, p, r);
			qSort(q, p, k - 1);
			qSort(q, k + 1, r);
		}
	}

	public int partition(Element[] q, int p, int r) {
		int m = p, n = r + 1;
		double k = q[p].d;
		int k1 = q[p].id;
		while (true) {
			while (q[++m].d >= k && m < r)
				;
			while (q[--n].d < k)
				;
			if (m >= n)
				break;
			double mid = q[m].d;
			q[m].d = q[n].d;
			q[n].d = mid;
			int midi = q[m].id;
			q[m].id = q[n].id;
			q[n].id = midi;
		}
		q[p].d = q[n].d;
		q[n].d = k;
		q[p].id = q[n].id;
		q[n].id = k1;
		return n;
	}

	private class Element {
		int id;
		double d;

		public Element(int id, double d) {
			this.id = id;
			this.d = d;
		}
	}

	private class BBnode {
		BBnode parent;
		boolean leftChild;

		BBnode(BBnode parent, boolean leftChild) {
			this.parent = parent;
			this.leftChild = leftChild;
		}

		public int nodeNumber() {
			if (parent == null && leftChild)
				return 2;
			else if (parent == null && !leftChild)
				return 3;
			else if (parent != null && leftChild)
				nodeNum = 2 * parent.nodeNumber();
			else
				nodeNum = 2 * parent.nodeNumber() + 1;
			return nodeNum;
		}
	}

	private class HeapNode {
		BBnode liveNode;
		float upperProfit;
		float profit;
		float weight;
		int level;

		HeapNode(BBnode liveNode, float upperProfit, float profit,
				float weight, int level) {
			this.liveNode = liveNode;
			this.upperProfit = upperProfit;
			this.profit = profit;
			this.weight = weight;
			this.level = level;
		}
	}

	private class MaxHeap {
		HeapNode[] nodes;
		int nextPlace;
		int maxNumber;

		public MaxHeap(int n) {
			maxNumber = (int) Math.pow(2, n);
			nextPlace = 1;
			nodes = new HeapNode[maxNumber];
		}

		// 添加一个元素
		public void put(HeapNode node) {
			nodes[nextPlace] = node;
			nextPlace++;
			heapSort(nodes);
		}

		// 输出最堆的
		public HeapNode removeMax() {
			HeapNode tempNode = nodes[1];
			nextPlace--;
			nodes[1] = nodes[nextPlace];
			heapSort(nodes);
			return tempNode;
		}

		public void heapAdjust(HeapNode[] nodes, int s, int m) {
			HeapNode rc = nodes[s];
			for (int j = 2 * s; j <= m; j *= 2) {
				if (j < m
						&& nodes[j].upperProfit < nodes[j + 1].upperProfit)
					++j;
				if (!(rc.upperProfit < nodes[j].upperProfit))
					break;
				nodes[s] = nodes[j];
				s = j;
			}
			nodes[s] = rc;
		}

		public void heapSort(HeapNode[] nodes) {
			for (int j = (nextPlace - 1) / 2; j > 0; --j)
				heapAdjust(nodes, j, nextPlace - 1);
		}

	}

	public void addLiveNode(float up, float pp, float ww, int lev,
			BBnode par, boolean ch) {
		BBnode b = new BBnode(par, ch);
		HeapNode node = new HeapNode(b, up, pp, ww, lev);
		heap.put(node);
	}

	public float bound(int j) {
		float left = _bag.getBagSize() - _bag.getNowWeight();
		float bound = _bag.getNowValue();
		while (j <= _goods.getNumbers() && w[j] <= left) {
			left -= w[j];
			bound += p[j];
			j++;
		}
		if (j <= _goods.getNumbers())
			bound += p[j] * left / w[j];
		return bound;
	}
		
	public ArrayList<String> levelPoint(){
		for (int j = 1; j < p.length; j++) {
			String str = "(" + w[j] + " , " + p[j] + ")";
			_heavyAndValues.add(str);
		}
		return _heavyAndValues;
	}
		
	public ArrayList<Point> getOffSet(){
		return offSet;
	}
	
}