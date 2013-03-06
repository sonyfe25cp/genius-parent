package edu.bit.dlde.analysis.plsa;
import Jama.Matrix;
import java.util.*;

/**
 *	@author zhangchangmin
 **/
public class Plsa_EM {

	/**
	 * EM算法
	 * @param tf			输入矩阵(term*document)
	 * @param K				类别数目
	 * @param Par			EM算法参数
	 * @param pzdw			实体类对象(Pz+Pw_z+Pd_z)
	 * @param folding_in	是否为新的文档
	 * @return				EM算法的输出(Pz+Pw_z+Pd_z)
	 */
	public P_z_d_w pLSA_EM(Matrix tf, int K, Para Par,P_z_d_w pzdw,boolean folding_in) {		
		int m = tf.getRowDimension();// vocabulary size
		int nd = tf.getColumnDimension();// # of documents
		// allocate memory for the posterior
		List<Matrix> Pz_dw = new ArrayList<Matrix>();
		Matrix pdw;
		for (int i = 0; i < K; i++) {
			pdw = new Matrix(m, nd,0);
			Pz_dw.add(pdw);
		}
		//最大迭代次数
		int maxit = Par.getMaxit();
		double[] Li = new double[2];
		P_z_d_w pzdw_t=new P_z_d_w();
		int it;
		// EM算法
		for (it = 0; it < maxit; it++) {
			System.err.print("Iteration" + it);
			// E-step
			Pz_dw = pLSA_Estep(pzdw.getPw_z(), pzdw.getPd_z(), pzdw.getPz());

			pzdw_t.setPw_z(pzdw.getPw_z());
            pzdw_t.setPz(pzdw.getPz());
            pzdw_t.setPd_z(pzdw.getPd_z());
			// M-step
			pzdw = pLSA_Mstep(tf, Pz_dw,pzdw_t,folding_in);
			// 计算似然函数
			Li[1] = pLSA_logL(tf, pzdw.getPw_z(), pzdw.getPz(), pzdw.getPd_z());
			System.err.print(" Li["+it+"]="+Li[1]+"\t");
			// 判断算法的迭代是否需要结束
			double dLi = 0;
			if(it==0){				
				dLi=Li[1];
				Li[0]=Li[1];
				System.err.println(" dLi=" + dLi);
			}else{
				if(Li[1]==-1){
					System.err.println(" ");
					break;
				}					
				dLi = Li[1] - Li[0];
				Li[0]=Li[1];
				System.err.println(" dLi=" + dLi);
				if (dLi < Par.getLeps()) {
					break;
				}
			}
		}
		return pzdw_t;

	}
	/**
	 * initialize conditional probabilities for EM
	 * @param m		vocabulary size
	 * @param nd	number of documents
	 * @param K		number of topics
	 * @return		Pz ... P(z) Pd_z ... P(d|z) Pw_z ... P(w|z)
	 */
	public P_z_d_w pLSA_init(int m, int nd, int K) {
		P_z_d_w pzdw = new P_z_d_w();
		//初始化并规约Pz
		Matrix Pz =Matrix.random(1, K);
		double sum=0;
		for(int k=0;k<K;k++){
			sum+=Pz.get(0, k);
		}
		sum=1/sum;
		Pz=Pz.times(sum);
		// 初始化并规约Pd_z
		Matrix Pd_z = Matrix.random(nd, K);  
		Matrix C = new Matrix(K, K,0);
		double[] c_a = new double[K];
		for (int i = 0; i < K; i++) {
			c_a[i] = 1 / Pd_z.getMatrix(0, nd - 1, i, i).norm1();
			C.set(i, i, c_a[i]);
		}
		Pd_z = Pd_z.times(C); 
		//初始化并规约Pw_z
		Matrix Pw_z = Matrix.random(m, K); 
		for (int j = 0; j < K; j++) {
			c_a[j] = 1 / Pw_z.getMatrix(0, m - 1, j, j).norm1();
			C.set(j, j, c_a[j]);
		}
		Pw_z = Pw_z.times(C); 

		pzdw.setPd_z(Pd_z);
		pzdw.setPw_z(Pw_z);
		pzdw.setPz(Pz);
		return pzdw;
	}
	/**
	 * (1) E step
	 * @param Pw_z	P(w|z)
	 * @param Pd_z	P(d|z)
	 * @param Pz	P(z)
	 * @return Pz_dw	P(z|d,w)
	 */
	public List<Matrix> pLSA_Estep(Matrix Pw_z, Matrix Pd_z, Matrix Pz) {
		int K = Pw_z.getColumnDimension();//topic number
		int m = Pw_z.getRowDimension();//word number
		int n = Pd_z.getRowDimension();//document number
		Matrix P_dw = new Matrix(m, n,0);
		List<Matrix> Pz_dw = new ArrayList<Matrix>();
		for (int k = 0; k < K; k++) {
			// Pz_dw(:,:,k) = Pw_z(:,k) * Pd_z(:,k)' * Pz(k);
			P_dw = Pw_z.getMatrix(0, m - 1, k, k)
					.times(Pd_z.getMatrix(0, n - 1, k, k).transpose())
					.times(Pz.get(0, k));
			Pz_dw.add(P_dw);

		}
        // sum(Pz_dw,3);
		Matrix C = new Matrix(m, n,0);
		double[][] c = C.getArray();
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				for (int l = 0; l < Pz_dw.size(); l++)
					c[i][j] += Pz_dw.get(l).get(i, j);
			}
		}
		// normalize posterior		
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				c[i][j] = 1 / c[i][j];
			}
		}	
		for (int k1 = 0; k1 < K; k1++)
			 Pz_dw.set(k1,Pz_dw.get(k1).arrayTimes(C)) ;// (:,:,k) .* (1./C);

		return Pz_dw;
	}
	/**
	 * (2) M step
	 * @param X			(term*document)矩阵
	 * @param Pz_dw		P(z|d,w)
	 * @return 			Pd_z+Pw_z+Pz
	 */
	public P_z_d_w pLSA_Mstep(Matrix X, List<Matrix> Pz_dw,P_z_d_w pzdw_t,boolean folding_in) {
		P_z_d_w pzdw = new P_z_d_w();
		int K = Pz_dw.size();
		int m = X.getRowDimension();
		int n = X.getColumnDimension();
		int k;
		double R=0;
		for(int m1=0;m1<m;m1++){
			for(int n1=0;n1<n;n1++){
				R+=X.get(m1, n1);//sum of words
			}
		}
		
		Matrix Pd_z = new Matrix(n, K,0);
		Matrix xp_n = new Matrix(n, 1,0);
		double[][] xpn_a = xp_n.getArray();
		for (k = 0; k < K; k++) {
			for (int j = 0; j < n; j++) {
				xpn_a[j][0] = X.arrayTimes(Pz_dw.get(k))
						.getMatrix(0, m - 1, j, j).norm1();
			}
			Pd_z.setMatrix(0, n - 1, k, k, xp_n);
		}
		// Pd_z(:,k) = sum(X.* Pz_dw(:,:,k),1)';//w的和，每列
		
		if(!folding_in){
			Matrix Pw_z = new Matrix(m, K,0);	
			Matrix xp = new Matrix(m, 1,0);
			double[][] xp_a = xp.getArray();			
			for (k = 0; k < K; k++) {
				for (int i = 0; i < m; i++) {
					xp_a[i][0] = X.arrayTimes(Pz_dw.get(k))
							.getMatrix(i, i, 0, n - 1).normInf();//sum of per row,total 12
				}
				Pw_z.setMatrix(0, m - 1, k, k, xp);
			}
			// Pw_z(:,k) = sum(X .* Pz_dw(:,:,k),2);//d的和，每行
			
			Matrix Pz = new Matrix(1, K,0);
			double[][] pz_a = Pz.getArray();
			for (int l = 0; l < K; l++) {
				pz_a[0][l] = Pd_z.getMatrix(0, n - 1, l, l).norm1();
			}
			// Pz = sum(Pd_z,1);每列的和	

			// normalize to sum to 1		
			Pd_z = norm21(Pd_z, n, K);
			Pw_z = norm21(Pw_z, m, K);
			Pz = Pz.times(1/R); 
			
			pzdw.setPw_z(Pw_z);
			pzdw.setPz(Pz);
		}else{
			double dz=0;
			for(int d=0;d<K;d++){
				dz+=Pd_z.get(0, d);
			}
			dz=1/dz;
			Pd_z=Pd_z.times(dz);
			pzdw.setPw_z(pzdw_t.getPw_z());
			pzdw.setPz(pzdw_t.getPz());			
		}							
		pzdw.setPd_z(Pd_z);
		
		return pzdw;
	}
	/**
	 * 计算似然函数
	 * @param X		(term*document)矩阵
	 * @param Pw_z	P(w|z)
	 * @param Pz	P(z)
	 * @param Pd_z	P(d|z)
	 * @return		似然函数的值
	 */
	public double pLSA_logL(Matrix X, Matrix Pw_z, Matrix Pz, Matrix Pd_z) {
		double L = 0;
		int m = X.getRowDimension();
		int n = X.getColumnDimension();
		int k = Pz.getColumnDimension();

		Matrix pz = new Matrix(k, k,0);
		for (int i = 0; i < k; i++) {
			pz.set(i, i, Pz.get(0, i));
		}

		Matrix p = new Matrix(m, n,0);
		p = (Pw_z.times(pz)).times(Pd_z.transpose());
		pz=null;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				if(p.get(i, j)>0){
					double mL=Math.log(p.get(i, j)+0.01);//修正
					if(Double.isNaN(mL))
						return -1;//mL=0;
				    p.set(i, j,mL );
				}
			}
		}
		Matrix X1=X.arrayTimes(p);
		p=null;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
//				if(Double.isNaN(X1.get(i, j))){
//					X1.set(i, j, 0);
//				}
				L += X1.get(i, j);
			}
		}
		X1=null;
		if(Double.isNaN(L))
			return -1;
		// L = sum(sum(X .* log(Pw_z * diag(Pz) * Pd_z' + eps)));
		return L;
	}
	/**
	 * 按每列规约
	 * @param X		需要规约的矩阵
	 * @param m		矩阵的行数
	 * @param n		矩阵的列数
	 * @return		规约后的矩阵
	 */
	public Matrix norm21(Matrix X, int m, int n) {
		Matrix C = new Matrix(n, n,0);
		double[] c_a = new double[n];
		for (int i = 0; i < n; i++) {
			c_a[i] = 1 / X.getMatrix(0, m - 1, i, i).norm1();//sum of per column
			C.set(i, i, c_a[i]);
		}
		Matrix D = X.times(C);
		return D;
	}
	/**
	 * 为新来的文档分类
	 * @param X			新来文档的(term*document)矩阵
	 * @param Pz_t		训练得到的Pz
	 * @param Pw_z_t	训练得到的Pw_z
	 * @return			新来文档所属分类的编号
	 */
	public int folding_in(Matrix X,Matrix Pz_t,Matrix Pw_z_t){
		int K=Pz_t.getColumnDimension();
		Matrix Pz=Pz_t;
		Matrix Pw_z=Pw_z_t;
		Matrix Pd_z = Matrix.random(1, K);// rand(nd,K); 
		double sum=0;
		for(int i=0;i<K;i++){
			sum+=Pd_z.get(0, i);
		}
		sum=1/sum;
		Pd_z=Pd_z.times(sum);
		
		P_z_d_w pzdw=new P_z_d_w();
		pzdw.setPd_z(Pd_z);
		pzdw.setPw_z(Pw_z);
		pzdw.setPz(Pz);
		
		Para par=new Para();
		par.setMaxit(10);
		par.setLeps(0.01);		
		
//		PEM pem=new PEM();		
		pzdw=pLSA_EM(X, K,par, pzdw,true);
//		pem.getPzdw().getPd_z().print(3,4);
		
		Matrix Pz_d=new Matrix(1,K,0);
		Pz_d=pzdw.getPd_z().arrayTimes(Pz_t);
		double[] pz_d_a=Pz_d.getRowPackedCopy();
		int z_d=max(pz_d_a);
		return z_d;
//		double z=Pz_d.norm1();
//		return z;
	}
	/**
	 * 数组最大数所在的位置
	 * @param a		数组
	 * @return		最大数所在的位置
	 */
	public int max(double[] a){
		int m=0;
		for(int i=1;i<a.length;i++){
			if(a[m]<a[i])
				m=i;			
		}
		return m;
	}
}
