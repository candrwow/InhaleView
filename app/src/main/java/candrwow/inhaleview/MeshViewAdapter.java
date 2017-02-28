package candrwow.inhaleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Candrwow on 2017/2/28.
 */

public class MeshViewAdapter extends RecyclerView.Adapter<MeshViewAdapter.MeshViewHolder> {
    List<String> list;
    Context mContext;

    public MeshViewAdapter(Context mContext) {
        list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add("http://upload-images.jianshu.io/upload_images/1176696-685e173e0c7835b4.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240");
        }
        this.mContext = mContext;
    }

    @Override
    public MeshViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mesh_view, parent, false);
        return new MeshViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MeshViewHolder holder, int position) {
        Glide.with(mContext).load(list.get(position)).diskCacheStrategy(DiskCacheStrategy.SOURCE).into(holder.ivItem);
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }


    static class MeshViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_item)
        ImageView ivItem;

        public MeshViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
