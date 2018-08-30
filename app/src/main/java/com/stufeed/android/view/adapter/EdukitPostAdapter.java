package com.stufeed.android.view.adapter;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidquery.AQuery;
import com.github.jksiezni.permissive.PermissionsGrantedListener;
import com.github.jksiezni.permissive.PermissionsRefusedListener;
import com.github.jksiezni.permissive.Permissive;
import com.stufeed.android.R;
import com.stufeed.android.api.APIClient;
import com.stufeed.android.api.Api;
import com.stufeed.android.api.response.GetEdukitResponse;
import com.stufeed.android.api.response.Response;
import com.stufeed.android.databinding.RowEdukitItemBinding;
import com.stufeed.android.listener.DialogListener;
import com.stufeed.android.util.ProgressDialog;
import com.stufeed.android.util.Utility;
import com.stufeed.android.view.activity.EdukitPostActivity;

import org.apache.commons.io.FilenameUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;

public class EdukitPostAdapter extends RecyclerView.Adapter<EdukitPostAdapter.ViewHolder> {

    private Context context;
    private ArrayList<GetEdukitResponse.EdukitPost> postArrayList;
    private AQuery aQuery;
    private String collegeId = "";
    private String userType = "";

    public EdukitPostAdapter(Context context, ArrayList<GetEdukitResponse.EdukitPost> postArrayList, String collegeId) {
        this.context = context;
        this.postArrayList = postArrayList;
        aQuery = new AQuery(context);
        this.collegeId = collegeId;
        userType = Utility.getLoginUserDetail(context).getUserType();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RowEdukitItemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(context), R.layout.row_edukit_item, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GetEdukitResponse.EdukitPost model = postArrayList.get(position);
        holder.binding.setModel(model);
        holder.binding.setAdapter(this);

        if (!userType.equals("4")) {
            holder.binding.imgDelete.setVisibility(View.GONE);
        } else {
            holder.binding.imgDelete.setVisibility(View.VISIBLE);
        }

        String url = "";
        if (TextUtils.isEmpty(model.getImage())) {
            url = Api.IMAGE_URL + model.getFile();
        } else {
            url = Api.IMAGE_URL + model.getImage();
        }

        if (model.getPostType().equals("1")) {
            holder.binding.documentLayout.setVisibility(View.VISIBLE);
            holder.binding.imageLayout.setVisibility(View.GONE);
            String ext = FilenameUtils.getExtension(model.getFile());
            setFileAsExtension(holder, ext);
            holder.binding.doctextAVUrl.setText(model.getFile());
        } else {
            holder.binding.imageLayout.setVisibility(View.VISIBLE);
            holder.binding.documentLayout.setVisibility(View.GONE);
            aQuery.id(holder.binding.imageView).image(url, true, true, 200, R.drawable.user_default);
        }

        holder.binding.documentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Api.IMAGE_URL + model.getFile();
                /*CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ContextCompat.getColor(context, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(context, Uri.parse(url));*/
                downloadFile(url, model.getFile());
            }
        });
    }

    private void setFileAsExtension(ViewHolder holder, String extension) {
        switch (extension) {
            case "pdf":
                holder.binding.documentImg.setImageResource(R.drawable.icon_file_pdf);
                break;
            case "doc":
            case "docx":
                holder.binding.documentImg.setImageResource(R.drawable.icon_file_doc);
                break;
            case "xlsx":
            case "xls":
                holder.binding.documentImg.setImageResource(R.drawable.icon_file_xls);
                break;
            case "txt":
                holder.binding.documentImg.setImageResource(R.drawable.icon_file_unknown);
                break;
            case "ppt":
                holder.binding.documentImg.setImageResource(R.drawable.icon_file_ppt);
                break;
            default:
                holder.binding.documentImg.setImageResource(R.drawable.icon_file_unknown);
        }
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private RowEdukitItemBinding binding;

        public ViewHolder(final RowEdukitItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.textViewTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int visibility = binding.layoutFullView.getVisibility();
                    binding.layoutFullView.setVisibility(visibility == View.GONE ? View.VISIBLE : View.GONE);
                }
            });
        }
    }

    public void onClickDeletePost(final GetEdukitResponse.EdukitPost post) {
        Utility.setDialog(context, context.getString(R.string.alert), "Do you want to delete this post?",
                "No", "Yes", new DialogListener() {
                    @Override
                    public void onNegative(DialogInterface dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onPositive(DialogInterface dialog) {
                        dialog.dismiss();
                        callDeletePostApi(post);
                    }
                });
    }

    private void callDeletePostApi(final GetEdukitResponse.EdukitPost post) {
        Api api = APIClient.getClient().create(Api.class);
        ProgressDialog.getInstance().showProgressDialog(context);
        Call<Response> call = api.deleteInstitutePost(collegeId, post.getId());
        call.enqueue(new Callback<Response>() {
            @Override
            public void onResponse(Call<Response> call, retrofit2.Response<Response> response) {
                ProgressDialog.getInstance().dismissDialog();
                Response response1 = response.body();
                if (response1 != null && response1.getResponseCode().equals(Api.SUCCESS)) {
                    int index = postArrayList.indexOf(post);
                    postArrayList.remove(index);
                    notifyItemRemoved(index);
                    Utility.showToast(context, "delete successfully.");
                } else {
                    Utility.showToast(context, context.getString(R.string.wrong));
                }
            }

            @Override
            public void onFailure(Call<Response> call, Throwable t) {
                ProgressDialog.getInstance().dismissDialog();
                Utility.showToast(context, context.getString(R.string.wrong));
            }
        });
    }

    private void downloadFile(final String url, final String name) {
        new Permissive.Request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .whenPermissionsGranted(new PermissionsGrantedListener() {
                    @Override
                    public void onPermissionsGranted(String[] permissions) throws SecurityException {
                        if (permissions.length == 2) {
                            Uri Download_Uri = Uri.parse(url);
                            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                            DownloadManager.Request request = new DownloadManager.Request(Download_Uri);
                            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                            request.setAllowedOverRoaming(false);
                            request.setTitle("Stufeed");
                            request.setDescription("Downloading...");
                            request.setVisibleInDownloadsUi(true);
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "/Stufeed/" + "/" + name);

                            downloadManager.enqueue(request);
                            Utility.showToast(context, "downloading...");

                        }
                    }
                }).whenPermissionsRefused(new PermissionsRefusedListener() {
            @Override
            public void onPermissionsRefused(String[] permissions) {
                Utility.showToast(context, "Need permission to open camera.");
            }
        }).execute((EdukitPostActivity) context);
    }
}