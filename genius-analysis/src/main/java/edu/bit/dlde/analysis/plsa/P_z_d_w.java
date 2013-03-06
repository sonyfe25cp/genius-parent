package edu.bit.dlde.analysis.plsa;
import Jama.Matrix;

/**
 *	@author zhangchangmin
 **/
public class P_z_d_w {
	private Matrix Pz;
	private Matrix Pd_z;
	private Matrix Pw_z;
	public Matrix getPz() {
		return Pz;
	}
	public void setPz(Matrix pz) {
		Pz = pz;
	}
	public Matrix getPd_z() {
		return Pd_z;
	}
	public void setPd_z(Matrix pd_z) {
		Pd_z = pd_z;
	}
	public Matrix getPw_z() {
		return Pw_z;
	}
	public void setPw_z(Matrix pw_z) {
		Pw_z = pw_z;
	}		
}
