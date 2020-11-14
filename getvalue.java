package groupexercise;
import genius.core.issue.Value;
import java.util.Comparator;


public class getvalue implements Comparator<getvalue>{

	public Value value_name;
	public int option_count; //记录这个值一共出现了多少次
	public int all_bidder_number;//对手一共出了多少次报价。
	public int total_option;//当前一共有多少个选项
	public int total_bid_number;//一共报了多少次价格
	public int rank;//在issue当中的排名
	public double weight;//当前这个值的权重
	public double calculatedValue=0.0f; 
	
	public getvalue(Value value) {
        this.value_name = value;
    }
	
	
	public void single_weight_comput(){
		double weight_term;
		weight_term = option_count / this.all_bidder_number;
		this.weight = weight_term * weight_term;
		this.calculatedValue=((this.total_option-(double)this.rank+1)/this.total_option);
		
	}
	
	
	@Override
	public int compare(getvalue o1, getvalue o2) {
		if(o1.option_count < o2.option_count)
			return 1;
		else{
			return -1;
		}
		
		
		
	}

	
}
