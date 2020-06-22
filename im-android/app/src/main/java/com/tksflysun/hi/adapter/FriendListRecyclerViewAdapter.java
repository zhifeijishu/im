package com.tksflysun.hi.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.tksflysun.hi.R;
import com.tksflysun.hi.bean.Friend;

import java.util.List;

/**
 * 好友列表 RecycleListView Adapter
 *
 */
public abstract class FriendListRecyclerViewAdapter  extends RecyclerView.Adapter<FriendListRecyclerViewAdapter.ViewHolder> {
    private int mLayoutRes;
    private List<Friend> mData;

    //定义接口 OnItemClickListener
    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public FriendListRecyclerViewAdapter(List<Friend> mData, int mLayoutRes) {
        this.mLayoutRes = mLayoutRes;
        this.mData = mData;
    }

    /**
     * inflate操作
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = ViewHolder.get(parent.getContext(), parent, mLayoutRes);
        return viewHolder;
    }

    /**
     * 暴露出来的接口
     *
     * @param holder
     * @param obj
     */
    public abstract void bindView(ViewHolder holder, Friend obj);

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        bindView(holder, mData.get(position));

        //绑定事件
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, pos);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    /**
     * 获取高度
     *
     * @return
     */
    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    /**
     * ViewHolder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private SparseArray<View> mViews;
        private View mConvertView;

        public ViewHolder(Context context, View itemView, ViewGroup parent) {
            super(itemView);
            mConvertView = itemView;
            mViews = new SparseArray<View>();
        }

        public static ViewHolder get(Context context, ViewGroup parent, int layoutid) {
            View itemView = LayoutInflater.from(context).inflate(layoutid, parent, false);
            ViewHolder holder = new ViewHolder(context, itemView, parent);
            return holder;
        }

        public <T extends View> T getView(int viewid) {
            View view = mViews.get(viewid);
            if (view == null) {
                view = mConvertView.findViewById(viewid);
                mViews.put(viewid, view);
            }
            return (T) view;
        }

        /**
         * 设置TextView文本
         */
        public ViewHolder setText(int viewId, CharSequence text) {
            View view = getView(viewId);
            if (view instanceof TextView) {
                ((TextView) view).setText(text);
            }
            return this;
        }

        /**
         * 设置ImageView的资源
         */
        public ViewHolder setImageResource(int viewId, Bitmap bitmap) {
            View view = getView(viewId);
            if (view instanceof ImageView) {
                ((ImageView) view).setImageBitmap(bitmap);
            } else {
                view.setBackgroundResource(R.drawable.ic_user);
            }
            return this;
        }
    }

}
