package cn.sxw.android.base.ui;

/**
 * Created by Alex.Tang on 2017-03-22.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseListAdapter<Holder, Item> extends BaseAdapter {
    protected List<Item> mItemList = new ArrayList<Item>();
    protected LayoutInflater mLayoutInflater;
    protected int mItemLayout;
    protected Context mContext;
    public BaseListAdapter(Context context, int itemLayout, List<Item> items) {
        if(context == null) {
            return;
        }
        mLayoutInflater = LayoutInflater.from(context);
        if (items == null) {
            mItemList = new ArrayList<Item>();
        } else {
            mItemList.addAll(items);
        }
        mItemLayout = itemLayout;
        mContext = context;
    }

    public BaseListAdapter(Context context, int itemLayout) {
        this(context, itemLayout, null);
    }

    public void clear() {
        mItemList.clear();
        notifyDataSetChanged();
    }
    public void addItem(Item item) {
        mItemList.add(item);
        notifyDataSetChanged();
    }

    public void addItem(Item item, int index, boolean isDel, boolean isNotify) {
        if(index >= mItemList.size()){
            return;
        }
        if(isDel){
            mItemList.remove(index);
        }
        mItemList.add(index, item);
        if(isNotify){
            notifyDataSetChanged();
        }
    }

    public void addList(List<Item> items) {
        if(items == null){
            return;
        }
        mItemList.addAll(items);
        notifyDataSetChanged();
    }

    public void addList(List<Item> items, boolean clear) {
        if(clear){
            mItemList.clear();
        }

        if(items == null){
            notifyDataSetChanged();
            return;
        }
        mItemList.addAll(items);
        notifyDataSetChanged();
    }

    public void delItem(int index){
        if(index >= mItemList.size()){
            return;
        }
        mItemList.remove(index);
        notifyDataSetChanged();;
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }

    @Override
    public Item getItem(int position) {
        if(position >= mItemList.size()){
            return null;
        }
        return mItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    abstract public Holder initViewHodler(View convertView);

    abstract public void setViewHodler(Holder viewHolder, int position);

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder viewHolder = null;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(mItemLayout, null);
            viewHolder = initViewHodler(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (Holder) convertView.getTag();
        }
        setViewHodler(viewHolder, position);
        return convertView;
    }

    abstract class BaseHolder {
        abstract void initByView(View convertView);
    }
}
