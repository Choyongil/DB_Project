package com.example.test.db_project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class ListAdapter extends BaseAdapter
{
    LayoutInflater inflater = null;
    private ArrayList<ItemData> m_oData = null;
    private int nListCnt = 0;

    public ListAdapter(ArrayList<ItemData> _oData)
    {
        m_oData = _oData;
        nListCnt = m_oData.size();
    }

    @Override
    public int getCount()
    {
        Log.i("TAG", "getCount");
        return nListCnt;
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            final Context context = parent.getContext();
            if (inflater == null)
            {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        TextView oTextNum = (TextView) convertView.findViewById(R.id.roomNum);
        TextView oTextClaim = (TextView) convertView.findViewById(R.id.textClaim);
        TextView oTextName = (TextView) convertView.findViewById(R.id.textName);
        Button oBtn = (Button) convertView.findViewById(R.id.btnSelector);

        oTextNum.setText(m_oData.get(position).strNum);
        oTextClaim.setText(m_oData.get(position).strClaim);
        oTextName.setText(m_oData.get(position).strName);
        oBtn.setOnClickListener(m_oData.get(position).onClickListener);

        convertView.setTag(""+position);
        return convertView;
    }
}