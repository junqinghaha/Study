package com.android.tnt.hmdy.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.tnt.hmdy.R;

public class LeftFragment extends Fragment {
	View _RootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			_RootView = inflater.inflate(R.layout.left_fragment_menu, null);
		return _RootView;
	}

}
