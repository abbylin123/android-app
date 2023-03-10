package com.example.test_lognin.group;

import android.content.Context;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.example.test_lognin.cc.BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

//分組Adapter
public class GroupRecyclerAdapter  <Parent, Child> extends BaseRecyclerAdapter<Child> {

    private LinkedHashMap<Parent, List<Child>> mGroups;

    private List<Parent> mGroupTitles;

    public GroupRecyclerAdapter(Context context) {
        super(context);
        mGroups = new LinkedHashMap<>();
        mGroupTitles = new ArrayList<>();
    }

    @Override
    protected RecyclerView.ViewHolder onCreateDefaultViewHolder(ViewGroup parent, int type) {
        return null;
    }

    @Override
    protected void onBindViewHolder(RecyclerView.ViewHolder holder, Child item, int position) {

    }

    //返回特定標題

    Parent getGroup(int groupPosition) {
        return mGroupTitles.get(groupPosition);
    }

    //取得分组的數量

    int getGroupCount() {
        return mGroupTitles.size();
    }

    //取得某组的數量

    int getChildCount(int groupPosition) {
        if (mGroupTitles == null || mGroups.size() == 0)
            return 0;
        if (mGroups.get(mGroupTitles.get(groupPosition)) == null)
            return 0;
        return mGroups.get(mGroupTitles.get(groupPosition)).size();
    }

    //重置分组數據

    protected void resetGroups(LinkedHashMap<Parent, List<Child>> groups, List<Parent> titles) {
        if (groups == null || titles == null) {
            return;
        }
        mGroups.clear();
        mGroupTitles.clear();
        mGroups.putAll(groups);
        mGroupTitles.addAll(titles);
        mItems.clear();
        for (Parent key : mGroups.keySet()) {
            mItems.addAll(mGroups.get(key));
        }
        notifyDataSetChanged();
    }

    //清除分组數據
    /*public final void clearGroup() {
        mGroupTitles.clear();
        mGroups.clear();
        clear();
    }*/

    //從分组移除數據

    /*public boolean removeGroupItem(int position) {
        int group = getGroupIndex(position);
        removeGroupChildren(group);
        int count = getChildCount(group);
        removeItem(position);
        if (count <= 0) {
            mGroupTitles.remove(group);
            return true;
        }
        return false;
    }*/

    //取得所在分组

    private int getGroupIndex(int position) {
        int count = 0;
        if (position <= count)
            return 0;
        int i = 0;
        for (Parent parent : mGroups.keySet()) {
            count += mGroups.get(parent).size();
            if (position < count) {
                return i;
            }
            i++;
        }
        return 0;
    }

    private void removeGroupChildren(int groupPosition) {
        if (groupPosition >= mGroupTitles.size())
            return;
        List<Child> childList = mGroups.get(mGroupTitles.get(groupPosition));
        if (childList != null && childList.size() != 0) {
            childList.remove(childList.size() - 1);
        }
    }
}

