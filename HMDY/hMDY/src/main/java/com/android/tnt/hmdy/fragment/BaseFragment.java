package com.android.tnt.hmdy.fragment;

import java.lang.reflect.Field;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

/***
 * 
 * @author TNT
 *
 */
public class BaseFragment extends Fragment {

	/***
	 * fragment请求监听类
	 */
	public OnFragmentListener mFragmentlistener = null;

	/****
	 * 注册fragment请求监听
	 * 
	 * @param listener
	 */
	public void setOnFragmentListener(OnFragmentListener listener) {
		this.mFragmentlistener = listener;
	}

	/***
	 * 碎片事件监听
	 * 
	 * @author TNT
	 * 
	 */
	public interface OnFragmentListener {
		/***
		 * 碎片界面交互操作监听
		 * @param fragment		碎片类
		 * @param optCode		碎片请求操作类型
		 * @param data			碎片请求操作附带数据，无数据则为空
		 */
		public void onFragmentRequest(Fragment fragment, int optCode, Bundle data);
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	//解决childfragemanager初始化问题 java.lang.IllegalStateException: No activity
	//http://blog.csdn.net/leewenjin/article/details/19410949
	@Override
    public void onDetach() {
    	super.onDetach();
    	try {
    	    Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
    	    childFragmentManager.setAccessible(true);
    	    childFragmentManager.set(this, null);

    	} catch (NoSuchFieldException e) {
    	    throw new RuntimeException(e);
    	} catch (IllegalAccessException e) {
    	    throw new RuntimeException(e);
    	}
    }
	
	
}
