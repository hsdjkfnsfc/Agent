package groupexercise;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import genius.core.Bid;
import genius.core.issue.Issue;
import genius.core.issue.IssueDiscrete;
import genius.core.issue.Value;
import genius.core.uncertainty.UserModel;

public class Table extends HashMap<Issue, List<getvalue>> {
	public int bid_number = 0;
	HashMap<Issue, Double> list_weight = new HashMap<>();

	public Table(UserModel userModel) {
		super();
		for (Issue issue : userModel.getDomain().getIssues()) { // 当前为一个问题的一行

			IssueDiscrete option = (IssueDiscrete) issue; // 将问题中的选项全拿出来
			List<getvalue> list = new ArrayList<>();// list里面将会包每个选项的值所画出来的对象。
			for (int i = 0; i < option.getNumberOfValues(); i++) { // 因为这里的Value类型不能直接for
																	// each，只能用getNumberOfValues
				getvalue value = new getvalue(option.getValue(i)); // 对于每一个value类型，我们都转化为ValueNew类型
				list.add(value); // 对于每一个value，我们都会将valueNew放进列表里

			}
			this.put(issue, list);
		}

	}

	public void JnBlack(Bid lastoffer) {

		this.bid_number += 1; // 每次出一次offer我们就需要增加一个bid

		for (Issue issue : lastoffer.getIssues()) {
			int num = issue.getNumber();// 为每个issue弄一个编号
			for (getvalue value : this.get(issue)) {// 便利当前问题下的每个value的值，如果碰上出价里相同的名字，就可以直接加一
				if (value.value_name.toString().equals(lastoffer.getValue(num).toString()))
					value.option_count += 1;

				IssueDiscrete issueDiscrete = (IssueDiscrete) issue;
				value.total_option = issueDiscrete.getNumberOfValues();// 让每个当前的选择知道和他一起的一共有多少个选择
				value.total_bid_number = this.bid_number;

			}
			Collections.sort(this.get(issue), this.get(issue).get(0));
			for (getvalue value : this.get(issue)) {
				value.rank = this.get(issue).indexOf(value) + 1;
			}

		}
		// 计算每个issue当中每个choice的权重
		for (Issue issue : lastoffer.getIssues()) {
			for (getvalue value : this.get(issue))
				value.single_weight_comput();

		}
		// 开始计算每个问题的权重
		// 首先归一化,加上所有的权值也就是求算法中的分母
		double total_Weight = 0.0f;
		for (Issue issue : lastoffer.getIssues()) {
			for (getvalue valueNew : this.get(issue)) {
				total_Weight += valueNew.weight;
			}

		}
		for (Issue issue : lastoffer.getIssues()) {
			double distinct_weight = 0.0f;
			for (getvalue valueNew : this.get(issue)) {
				distinct_weight += valueNew.weight;
			}
			double issue_weight = distinct_weight / total_Weight;
			this.list_weight.put(issue, issue_weight);

		}

		// 计算效用
		double utility = 0.0f;
		for (Issue issue : lastoffer.getIssues()) {
			int num = issue.getNumber();
			for (getvalue value : this.get(issue)) {
				if (value.value_name.toString().equals(lastoffer.getValue(num).toString())) { // 注意，每个bid都可以通过getValue(num)知道这个issue(issue对应的num)下到底是什么value
					utility += list_weight.get(issue) * value.calculatedValue;
					break;
				}

			}

		}
		System.out.println(bid_number + "对手效用是！！！！！！" + utility);
	}
	
	 public double JBpredict(Bid lastOffer){
	        //我们现在知道了每个issue的权重，现在需要来根据权重和每个value的evaluation来计算效用。
	        //计算效用
	        double utility=0.0f;   //先进行初始化

	        for(Issue issue:lastOffer.getIssues()){
	            int num=issue.getNumber();   //每一个issue我们都要将其转换为一个编号
	            for(getvalue value:this.get(issue)){
	                if(value.value_name.toString().equals(lastOffer.getValue(num).toString())){   //注意，每个bid都可以通过getValue(num)知道这个issue(issue对应的num)下到底是什么value
	                    utility+=list_weight.get(issue)*value.calculatedValue;
	                    break;  //如果找到了，后面的valueNew就不需要找了。
	                }
	            }
	        }
	        return utility;
	    }
}
