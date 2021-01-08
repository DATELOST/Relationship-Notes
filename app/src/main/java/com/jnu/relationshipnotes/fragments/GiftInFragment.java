package com.jnu.relationshipnotes.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jnu.relationshipnotes.GiftUpdateActivity;
import com.jnu.relationshipnotes.R;
import com.jnu.relationshipnotes.dataprocesser.DataBank;
import com.jnu.relationshipnotes.dataprocesser.Gift;

import java.util.List;
import static android.app.Activity.RESULT_OK;

public class GiftInFragment extends Fragment {
    DataBank dataBank;
    private GiftAdapter adapter;
    private static final int CONTEXT_MENU_ITEM_UPDATE = 1;
    private static final int CONTEXT_MENU_ITEM_DELETE = CONTEXT_MENU_ITEM_UPDATE+1;
    private static final int REQUEST_CODE_UPDATE_GIFT = 101;
    public GiftInFragment(){}
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v==this.getActivity().findViewById(R.id.listview_gifts)) {
            menu.setHeaderTitle("操作");
            menu.add(0, CONTEXT_MENU_ITEM_UPDATE, 1, "修改");
            menu.add(0, CONTEXT_MENU_ITEM_DELETE, 1, "删除");
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CODE_UPDATE_GIFT:
                if (resultCode == RESULT_OK) {
                    String name  = data.getStringExtra("name");
                    String date  = data.getStringExtra("date");
                    String reason= data.getStringExtra("reason");
                    String money = data.getStringExtra("money");
                    int position =data.getIntExtra("position",0);
                    dataBank.getGifts().get(position).setName(name);
                    dataBank.getGifts().get(position).setDate(date);
                    dataBank.getGifts().get(position).setReason(reason);
                    try{ dataBank.getGifts().get(position).setMoney(Integer.parseInt(money)); }
                    catch(NumberFormatException e) { e.printStackTrace(); }
                    dataBank.Save(0);
                    adapter.notifyDataSetChanged();
                }
                break;
            default:break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo=(AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        Intent intent;
        final int position=menuInfo.position;
        switch(item.getItemId()) {
            case CONTEXT_MENU_ITEM_UPDATE:
                intent = new Intent(this.getContext(), GiftUpdateActivity.class);
                intent.putExtra("position",position);
                intent.putExtra("name",dataBank.getGifts().get(position).getName() );
                intent.putExtra("date",dataBank.getGifts().get(position).getDate() );
                intent.putExtra("reason",dataBank.getGifts().get(position).getReason() );
                intent.putExtra("money",dataBank.getGifts().get(position).getMoney()+"");
                startActivityForResult(intent, REQUEST_CODE_UPDATE_GIFT);
                break;
            case CONTEXT_MENU_ITEM_DELETE:
                AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
                builder.setTitle("询问");
                builder.setMessage("你确定要删除\""+dataBank.getGifts().get(position).getName() + "\"？");
                builder.setCancelable(true);
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dataBank.getGifts().remove(position);
                        dataBank.Save(0);
                        adapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                });
                builder.create().show();
                break;
            default:break;
        }
        return super.onContextItemSelected(item);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_gift_in,container,false);
        dataBank=new DataBank(getActivity());
        dataBank.Load(0);
        adapter = new GiftAdapter(this.getContext(),R.layout.item_gift,dataBank.getGifts());
        ListView listViewGifts=view.findViewById(R.id.listview_gifts);
        listViewGifts.setAdapter(adapter);
        this.registerForContextMenu(listViewGifts);
        return view;
    }
    private class GiftAdapter extends ArrayAdapter<Gift> {
        private int resourceId;
        public GiftAdapter(Context context, int resource, List<Gift> objects) {
            super(context,resource,objects);
            this.resourceId=resource;
        }
        @Override
        public View getView(int position,View convertView,ViewGroup parent) {
            Gift gift = getItem(position);
            View view;
            if(null==convertView)
                view=LayoutInflater.from(getContext()).inflate(this.resourceId,parent,false);
            else view=convertView;
            String str=String.format("%s%10s\n%s%15s",gift.getDate(),gift.getName(),
                    "¥ "+gift.getMoney(),gift.getReason());
            ((TextView) view.findViewById(R.id.text_view_gift)).setText(str);
            return view;
        }
    }
}