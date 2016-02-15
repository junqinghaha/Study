package com.android.tnt.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.android.tnt.hmdy.R;
import com.android.uitils.Util;

public class NumberSelectedView extends LinearLayout {

	private EditText editText = null;
	private ImageButton btnUp = null;
	private ImageButton btnDown = null;
	private int tipValue = 1;
	private int defValue = 0;
	private int minValue = Integer.MIN_VALUE;
	private int maxValue = Integer.MAX_VALUE;

	public NumberSelectedView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	public NumberSelectedView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	public NumberSelectedView(Context context) {
		super(context);
		initView(context);
	}

	private void initView(final Context context) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.layout_view_number_select, this);
		btnUp = (ImageButton) findViewById(R.id.button01);
		btnDown = (ImageButton) findViewById(R.id.button02);
		editText = (EditText) findViewById(R.id.editText01);
		editText.setText(defValue + "");
		btnUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (getTextValue() < maxValue) {
					editText.setText((getTextValue() + tipValue) + "");
					if (getTextValue() == maxValue) {
						btnUp.setEnabled(false);
					}
					if (!btnDown.isEnabled()) {
						btnDown.setEnabled(true);
					}
				} else {
					Util.MsgBox(context, "当前值为最大值");
				}
			}
		});
		btnDown.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (getTextValue() > minValue) {
					editText.setText((getTextValue() - tipValue) + "");
					if (getTextValue() == minValue) {
						btnDown.setEnabled(false);
					}
					if (!btnUp.isEnabled()) {
						btnUp.setEnabled(true);
					}
				} else {
					Util.MsgBox(context, "当前值为最小值");
				}
			}
		});
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		btnUp.setEnabled(enabled);
		btnDown.setEnabled(enabled);
		editText.setEnabled(enabled);
	}

	public void init(int defValue, int minValue, int maxValue, int tipValue) {
		setDefValue(defValue);
		setMinValue(minValue);
		setMaxValue(maxValue);
		setTipValue(tipValue);
		
		if(editText != null){
			editText.setText(defValue + "");
		}
	}

	public String getText() {
		return editText.getEditableText().toString();
	}

	public int getTextValue() {
		int value = 0;
		try {
			String data = editText.getEditableText().toString();
			value = Integer.parseInt(data);
		} catch (Exception e) {
		}
		return value;
	}
	
	public void setDefText(){
		editText.setText(defValue+"");
	}

	/**
	 * 设置文本
	 * 
	 * @param text
	 */
	public void setText(String text) {
		editText.setText(text);
	}

	public EditText getEditText() {
		return editText;
	}

	public void setEditText(EditText editText) {
		this.editText = editText;
	}

	public ImageButton getBtnUp() {
		return btnUp;
	}

	public void setBtnUp(ImageButton btnUp) {
		this.btnUp = btnUp;
	}

	public ImageButton getBtnDown() {
		return btnDown;
	}

	public void setBtnDown(ImageButton btnDown) {
		this.btnDown = btnDown;
	}

	public int getTipValue() {
		return tipValue;
	}

	public void setTipValue(int tipValue) {
		this.tipValue = tipValue;
	}

	public int getDefValue() {
		return defValue;
	}

	public void setDefValue(int defValue) {
		this.defValue = defValue;
	}

	public int getMinValue() {
		return minValue;
	}

	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}

	public int getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(int maxValue) {
		if(maxValue < 0){
			this.maxValue = Integer.MAX_VALUE;
		}else{
			this.maxValue = maxValue;
		}
	}

}
