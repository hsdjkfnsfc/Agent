package groupexercise;
import genius.core.issue.Value;
import java.util.Comparator;


public class getvalue implements Comparator<getvalue>{

	public Value value_name;
	public int option_count; //��¼���ֵһ�������˶��ٴ�
	public int all_bidder_number;//����һ�����˶��ٴα��ۡ�
	public int total_option;//��ǰһ���ж��ٸ�ѡ��
	public int total_bid_number;//һ�����˶��ٴμ۸�
	public int rank;//��issue���е�����
	public double weight;//��ǰ���ֵ��Ȩ��
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
