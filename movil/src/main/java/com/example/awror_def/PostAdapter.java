package com.example.awror_def;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    ArrayList<Post> posts;
    PostListener listener;

    public interface PostListener {
        void onDelete(Post post);
        void onEdit(Post post);
    }

    public void setListener(PostListener listener) {
        this.listener = listener;
    }

    public PostAdapter(ArrayList<Post> posts){
        this.posts = posts;
    }

    public void setPosts(ArrayList<Post> newPosts){
        this.posts.clear();
        this.posts.addAll(newPosts);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_publicacion, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.tvUsuario.setText(post.getUsername());
        holder.tvContenido.setText(post.getContenido());
        if (post.getImagen() != null && !post.getImagen().isEmpty()){
            byte[] decoded = Base64.decode(post.getImagen(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decoded, 0, decoded.length);

            holder.ivImagen.setImageBitmap(bitmap);
            holder.ivImagen.setVisibility(View.VISIBLE);
        } else {
            holder.ivImagen.setVisibility(View.GONE);
        }

        if (post.getUser_id() == ApiRest.userId) {
            holder.layoutAcciones.setVisibility(View.VISIBLE);
        } else {
            holder.layoutAcciones.setVisibility(View.GONE);
        }

        holder.btnBorrar.setOnClickListener(v -> {
            if (listener != null){
                listener.onDelete(post);
            };
        });

        holder.btnEditar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEdit(post);
            }
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsuario;
        TextView tvContenido;
        ImageView ivImagen;
        ImageView btnEditar;
        ImageView btnBorrar;
        LinearLayout layoutAcciones;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsuario = itemView.findViewById(R.id.tvUsuario);
            tvContenido = itemView.findViewById(R.id.tvContenido);
            ivImagen = itemView.findViewById(R.id.ivImagen);
            layoutAcciones = itemView.findViewById(R.id.layoutAcciones);
            btnEditar = itemView.findViewById(R.id.btnEditar);
            btnBorrar = itemView.findViewById(R.id.btnBorrar);
        }
    }
}
