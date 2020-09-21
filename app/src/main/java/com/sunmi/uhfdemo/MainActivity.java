package com.sunmi.uhfdemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.sunmi.rfid.RFIDManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(R.string.app_name);
        }
        RFIDManager.getInstance().setPrintLog(true);
        RFIDManager.getInstance().connect(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RFIDManager.getInstance().disconnect();
    }

    public void on6bTagInventory(View view) {
        startActivity(new Intent(this, Tag6bInventoryActivity.class));
    }

    public void on6bTagOperate(View view) {
        if (Tag6bInventoryActivity.list.size() != 0) {
            startActivity(new Intent(this, Tag6bOperateActivity.class));
        } else {
            Toast.makeText(this, getString(R.string.rfid_x_inventory, "6B"), Toast.LENGTH_SHORT).show();
        }
    }

    public void on6cTagInventory(View view) {
        startActivity(new Intent(this, Tag6cInventoryActivity.class));
    }

    public void on6cTagOperate(View view) {
        if (Tag6cInventoryActivity.list.size() != 0) {
            startActivity(new Intent(this, Tag6cOperateActivity.class));
        } else {
            Toast.makeText(this, getString(R.string.rfid_x_inventory, "6C"), Toast.LENGTH_SHORT).show();
        }
    }
}
