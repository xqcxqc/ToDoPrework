package com.example.todoprework;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//responsible for displaying data from the model into a row in the recycler view
public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.ViewHolder> {

    //to pass longClick information from main activity into adapater
    public interface OnLongClickListener {
        //the main class will implement this method, and it
        //need to know the position where the long-press happen
        void  onItemLongClicked(int position);
    }

    //we want to define a similar interface which will communicate back to
    //main activity and inform us what position was tapped
    public interface OnClickListener{
        void onItemClicked(int position);
    }

    List<String> items;
    OnLongClickListener longClickListener;
    OnClickListener clickListener;

    //constructor
    public ItemsAdapter(List<String> items, OnLongClickListener longClickListener, OnClickListener clickListener){
        this.items = items;
        this.longClickListener = longClickListener;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //use layout inflator to inflate a view
        View todoView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1,parent,false);
        //wrap it inside a view holder and return it
        return new ViewHolder(todoView);
    }

    @Override
    //responsible for binding data to particular view holder
    public void onBindViewHolder(ViewHolder holder, int position){
        //grab the item at the position
        String item = items.get(position);
        //bind the items into the specified view holder
        holder.bind(item);
    }

    @Override
    //tells the RV how many items are in the list
    public int getItemCount(){
        return items.size();
    }

    //Container to provide easy access to views that represent each row of the list
    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvItem;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            tvItem = itemView.findViewById(android.R.id.text1);
        }

        public void bind(String item){
            tvItem.setText(item);
            tvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v){
                    //when android notifies us that the textview has been tapped
                    //we can invoke the method on out interface, and pass the position
                    clickListener.onItemClicked(getAdapterPosition());
                }
            });
            tvItem.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v){
                    //Notify the listener which position was long pressed.
                    longClickListener.onItemLongClicked(getAdapterPosition());
                    return true;
                }
            });
        }

    }

}
