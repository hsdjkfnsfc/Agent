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
		for (Issue issue : userModel.getDomain().getIssues()) { // ��ǰΪһ�������һ��

			IssueDiscrete option = (IssueDiscrete) issue; // �������е�ѡ��ȫ�ó���
			List<getvalue> list = new ArrayList<>();// list���潫���ÿ��ѡ���ֵ���������Ķ���
			for (int i = 0; i < option.getNumberOfValues(); i++) { // ��Ϊ�����Value���Ͳ���ֱ��for
																	// each��ֻ����getNumberOfValues
				getvalue value = new getvalue(option.getValue(i)); // ����ÿһ��value���ͣ����Ƕ�ת��ΪValueNew����
				list.add(value); // ����ÿһ��value�����Ƕ��ὫvalueNew�Ž��б���

			}
			this.put(issue, list);
		}

	}

	public void JnBlack(Bid lastoffer) {

		this.bid_number += 1; // ÿ�γ�һ��offer���Ǿ���Ҫ����һ��bid

		for (Issue issue : lastoffer.getIssues()) {
			int num = issue.getNumber();// Ϊÿ��issueŪһ�����
			for (getvalue value : this.get(issue)) {// ������ǰ�����µ�ÿ��value��ֵ��������ϳ�������ͬ�����֣��Ϳ���ֱ�Ӽ�һ
				if (value.value_name.toString().equals(lastoffer.getValue(num).toString()))
					value.option_count += 1;

				IssueDiscrete issueDiscrete = (IssueDiscrete) issue;
				value.total_option = issueDiscrete.getNumberOfValues();// ��ÿ����ǰ��ѡ��֪������һ���һ���ж��ٸ�ѡ��
				value.total_bid_number = this.bid_number;

			}
			Collections.sort(this.get(issue), this.get(issue).get(0));
			for (getvalue value : this.get(issue)) {
				value.rank = this.get(issue).indexOf(value) + 1;
			}

		}
		// ����ÿ��issue����ÿ��choice��Ȩ��
		for (Issue issue : lastoffer.getIssues()) {
			for (getvalue value : this.get(issue))
				value.single_weight_comput();

		}
		// ��ʼ����ÿ�������Ȩ��
		// ���ȹ�һ��,�������е�ȨֵҲ�������㷨�еķ�ĸ
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

		// ����Ч��
		double utility = 0.0f;
		for (Issue issue : lastoffer.getIssues()) {
			int num = issue.getNumber();
			for (getvalue value : this.get(issue)) {
				if (value.value_name.toString().equals(lastoffer.getValue(num).toString())) { // ע�⣬ÿ��bid������ͨ��getValue(num)֪�����issue(issue��Ӧ��num)�µ�����ʲôvalue
					utility += list_weight.get(issue) * value.calculatedValue;
					break;
				}

			}

		}
		System.out.println(bid_number + "����Ч���ǣ�����������" + utility);
	}
	
	 public double JBpredict(Bid lastOffer){
	        //��������֪����ÿ��issue��Ȩ�أ�������Ҫ������Ȩ�غ�ÿ��value��evaluation������Ч�á�
	        //����Ч��
	        double utility=0.0f;   //�Ƚ��г�ʼ��

	        for(Issue issue:lastOffer.getIssues()){
	            int num=issue.getNumber();   //ÿһ��issue���Ƕ�Ҫ����ת��Ϊһ�����
	            for(getvalue value:this.get(issue)){
	                if(value.value_name.toString().equals(lastOffer.getValue(num).toString())){   //ע�⣬ÿ��bid������ͨ��getValue(num)֪�����issue(issue��Ӧ��num)�µ�����ʲôvalue
	                    utility+=list_weight.get(issue)*value.calculatedValue;
	                    break;  //����ҵ��ˣ������valueNew�Ͳ���Ҫ���ˡ�
	                }
	            }
	        }
	        return utility;
	    }
}
