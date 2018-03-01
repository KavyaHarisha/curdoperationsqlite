package com.kavya.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText uName,uAge,uAddress,uSalry;
    Button bInsert;
    RecyclerView viewRecycle;
    RecyclerView.LayoutManager viewCycleManager;
    UserInfoAdapter userAdapter;
    List<UserInfo> userList =  new ArrayList<>();
    SQLiteHelper helper;
    UserInfo getInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uName = (EditText)findViewById(R.id.eidt_name);
        uAge = (EditText)findViewById(R.id.eidt_age);
        uAddress = (EditText)findViewById(R.id.eidt_address);
        uSalry = (EditText)findViewById(R.id.eidt_salary);
        bInsert = (Button) findViewById(R.id.button_insert);
        viewRecycle = (RecyclerView) findViewById(R.id.recycler_view);
        viewCycleManager = new LinearLayoutManager(getApplicationContext());
        viewRecycle.setLayoutManager(viewCycleManager);
        helper = new SQLiteHelper(MainActivity.this);
//        helper.deleteAllDataUserInfo();
//        helper.onUpgrade(helper.bdDatabase,1,2);
        userList = helper.getAllUserInfo();

        if(userList.size()>0){
            userAdapter =  new UserInfoAdapter(userList);
            viewRecycle.setAdapter(userAdapter);
        }


        bInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textType = bInsert.getText().toString();
                if(textType.equalsIgnoreCase("update")){
                    helper.updateUserInfo(new UserInfo(getInfo.getId(),uName.getText().toString(),Integer.parseInt(uAge.getText().toString())
                            ,uAddress.getText().toString(),Float.parseFloat(uSalry.getText().toString())));
                    cleardata();

                }else if(textType.equalsIgnoreCase("insert")) {
                    helper.addUserInfo(new UserInfo(uName.getText().toString(),Integer.parseInt(uAge.getText().toString())
                            ,uAddress.getText().toString(),Float.parseFloat(uSalry.getText().toString())));
                    cleardata();
                }

                userList = helper.getAllUserInfo();
                userAdapter =  new UserInfoAdapter(userList);
                viewRecycle.setAdapter(userAdapter);
                userAdapter.notifyItemInserted(userList.size() - 1);
            }
        });
    }

    private void cleardata() {
        uName.setText("");
        uAge.setText("");
        uAddress.setText("");
        uSalry.setText("");
        bInsert.setText(getResources().getString(R.string.insert));
    }

    public class UserInfoAdapter extends RecyclerView.Adapter<UserInfoAdapter.ViewHolder>{

        List<UserInfo> mListInfo;

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView itemText;
            Button itemUpdate,itemDelete;
            public ViewHolder(View itemView) {
                super(itemView);
                itemText = (TextView)itemView.findViewById(R.id.row_view_item);
                itemUpdate = (Button)itemView.findViewById(R.id.row_button_udpdate);
                itemDelete = (Button)itemView.findViewById(R.id.row_button_delete);
            }
        }

        UserInfoAdapter(List<UserInfo> listInfo){
            this.mListInfo = listInfo;
        }

        @Override
        public UserInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item,parent,false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(UserInfoAdapter.ViewHolder holder, final int position) {
            UserInfo dataInfo = mListInfo.get(position);
            holder.itemText.setText(dataInfo.getId()+"\n"+
                    dataInfo.getName()+"\n"+
                    dataInfo.getAge()+"\n"+
                    dataInfo.getAddress()+"\n"+
                    dataInfo.getSalary());
            holder.itemUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getInfo = mListInfo.get(position);
                    uName.setText(mListInfo.get(position).getName());
                    uAge.setText(String.valueOf(mListInfo.get(position).getAge()));
                    uAddress.setText(mListInfo.get(position).getAddress());
                    uSalry.setText(String.valueOf(mListInfo.get(position).getSalary()));
                    bInsert.setText(getResources().getString(R.string.update));

                }
            });
            holder.itemDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    helper.deleteOneUserInfo(mListInfo.get(position));
                    mListInfo.remove(position);
                    notifyItemRemoved(position);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mListInfo.size();
        }

    }
}
