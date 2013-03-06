package edu.bit.dlde.analysis.rank.commons;

/**
 * 机器学习的公共参数
 * 
 * @author ChenJie
 *
 */
public class Parameters {
	
	/** 训练的轮数. */
	private static int EpochNum = 4000;
	
	/** 权重初始值. */
	private static final double WeightInit = 0.0;
	
	/** 梯度下降法的学习步长. */
	private static final double Step = 0.0025;
	
	/** 训练中每多少轮保存一次. */
	private static int save = -1;
	/**
	 * Instantiates a new parameters.
	 */
	private Parameters(){}
	
	public static int getEpochNum() {
		return EpochNum;
	}
	public static void setEpochNum(int epochNum) {
		EpochNum = epochNum;
	}
	public static int getSave() {
		return save;
	}
	public static void setSave(int save) {
		Parameters.save = save;
	}
	public static double getWeightinit() {
		return WeightInit;
	}
	public static double getStep() {
		return Step;
	}
	
	

}
