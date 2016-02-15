package com.android.uitils;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

/**
 * 控件移动监听 
 * @author TNT
 *
 */
public class MoveListener implements OnTouchListener {

	private int screenWidth = 0;
	private int screenHeight = 0;
	int lastX, lastY;

	public MoveListener(int width, int height) {
		screenWidth = width;
		screenHeight = height;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		int ea = event.getAction();
		Log.i("TAG", "Touch:" + ea);

		// Toast.makeText(DraftTest.this, "位置："+x+","+y,
		// Toast.LENGTH_SHORT).show();

		switch (ea) {
		case MotionEvent.ACTION_DOWN:
			lastX = (int) event.getRawX();
			lastY = (int) event.getRawY();
			break;
		/**
		 * layout(l,t,r,b) l Left position, relative to parent t Top position,
		 * relative to parent r Right position, relative to parent b Bottom
		 * position, relative to parent
		 * */
		case MotionEvent.ACTION_MOVE:
			int dx = (int) event.getRawX() - lastX;
			int dy = (int) event.getRawY() - lastY;

			int left = v.getLeft() + dx;
			int top = v.getTop() + dy;
			int right = v.getRight() + dx;
			int bottom = v.getBottom() + dy;

			if (left < 0) {
				left = 0;
				right = left + v.getWidth();
			}

			if (right > screenWidth) {
				right = screenWidth;
				left = right - v.getWidth();
			}

			if (top < 0) {
				top = 0;
				bottom = top + v.getHeight();
			}

			if (bottom > screenHeight) {
				bottom = screenHeight;
				top = bottom - v.getHeight();
			}

			v.layout(left, top, right, bottom);

			Log.i("", "position：" + left + ", " + top + ", " + right + ", "
					+ bottom);

			lastX = (int) event.getRawX();
			lastY = (int) event.getRawY();

			break;
		case MotionEvent.ACTION_UP:
			break;
		}
		return true;
	}

}
