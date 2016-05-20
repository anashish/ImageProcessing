package picazzy.picazzy.com.picazzy;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;


public class EffectsAdapter extends RecyclerView.Adapter<EffectsAdapter.MyViewHolder> {

    private List<EffectsModel> effectsModelList;
    private OnItemClickListener itemClickListener;
    public interface OnItemClickListener {
        void onItemClick(EffectsModel item);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView imageView;
        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.filter_name);
            imageView = (ImageView) view.findViewById(R.id.filterimage);
        }
        public void bind(final EffectsModel item, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }


    public EffectsAdapter(List<EffectsModel> effectsModelList,OnItemClickListener itemClickListener ) {
        this.effectsModelList = effectsModelList;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.effect_elements, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        EffectsModel EffectsModel = effectsModelList.get(position);
        holder.bind(effectsModelList.get(position), itemClickListener);
        holder.title.setText(EffectsModel.getTitle());
        holder.imageView.setImageResource(EffectsModel.getImageDrawable());
    }

    @Override
    public int getItemCount() {
        return effectsModelList.size();
    }
}
