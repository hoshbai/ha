package com.example.campus_life_assistant;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.campus_life_assistant.entry.LostFoundItem;
import com.example.campus_life_assistant.fragment.LostFoundListFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddLostFoundActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RadioGroup typeRadioGroup;
    private EditText titleEditText;
    private EditText descriptionEditText;
    private EditText locationEditText;
    private TextView dateTimeTextView;
    private Button dateTimeButton;
    private Spinner categorySpinner;
    private EditText contactEditText;
    private Spinner contactTypeSpinner;
    private Button submitButton;

    private Calendar calendar = Calendar.getInstance();
    private Date selectedDateTime = calendar.getTime();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

    // 物品类别选项
    private String[] categories = {"证件", "电子", "钱包", "其他"};
    // 联系方式类型选项
    private String[] contactTypes = {"电话", "微信", "QQ"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lost_found);

        initViews();
        setupToolbar();
        setupSpinners();
        setupListeners();
        updateDateTimeText();
    }

    private void initViews() {
        toolbar = findViewById(R.id.add_lost_found_toolbar);
        typeRadioGroup = findViewById(R.id.add_lost_found_type_radio_group);
        titleEditText = findViewById(R.id.add_lost_found_title);
        descriptionEditText = findViewById(R.id.add_lost_found_description);
        locationEditText = findViewById(R.id.add_lost_found_location);
        dateTimeTextView = findViewById(R.id.add_lost_found_time_text);
        dateTimeButton = findViewById(R.id.add_lost_found_time_button);
        categorySpinner = findViewById(R.id.add_lost_found_category_spinner);
        contactEditText = findViewById(R.id.add_lost_found_contact);
        contactTypeSpinner = findViewById(R.id.add_lost_found_contact_type_spinner);
        submitButton = findViewById(R.id.add_lost_found_submit_button);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("发布失物招领信息");
    }

    private void setupSpinners() {
        // 设置物品类别选择器
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, categories);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        // 设置联系方式类型选择器
        ArrayAdapter<String> contactTypeAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, contactTypes);
        contactTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        contactTypeSpinner.setAdapter(contactTypeAdapter);
    }

    private void setupListeners() {
        // 设置日期时间选择按钮点击事件
        dateTimeButton.setOnClickListener(v -> showDateTimePicker());

        // 设置提交按钮点击事件
        submitButton.setOnClickListener(v -> {
            if (validateInput()) {
                saveItem();
            }
        });
    }

    private void showDateTimePicker() {
        // 显示日期选择器
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            // 显示时间选择器
            new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                selectedDateTime = calendar.getTime();
                updateDateTimeText();
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateDateTimeText() {
        dateTimeTextView.setText(dateFormat.format(selectedDateTime));
    }

    private boolean validateInput() {
        // 验证标题
        if (titleEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "请输入标题", Toast.LENGTH_SHORT).show();
            return false;
        }

        // 验证描述
        if (descriptionEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "请输入描述", Toast.LENGTH_SHORT).show();
            return false;
        }

        // 验证地点
        if (locationEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "请输入地点", Toast.LENGTH_SHORT).show();
            return false;
        }

        // 验证联系方式
        if (contactEditText.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "请输入联系方式", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void saveItem() {
        // 获取输入内容
        int itemType = typeRadioGroup.getCheckedRadioButtonId() == R.id.add_lost_found_type_lost ?
                LostFoundItem.TYPE_LOST : LostFoundItem.TYPE_FOUND;
        String title = titleEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();
        Date time = selectedDateTime;
        String category = categorySpinner.getSelectedItem().toString();
        String contact = contactEditText.getText().toString().trim();
        String contactType = contactTypeSpinner.getSelectedItem().toString();

        // 模拟用户信息
        String publisherName = "当前用户";
        String publisherId = "10001";

        // 创建物品对象
        LostFoundItem item = new LostFoundItem(
                0, // 临时ID，实际应该由数据库生成
                title,
                description,
                location,
                time,
                category,
                contact,
                contactType,
                null, // 暂无图片
                itemType,
                publisherName,
                publisherId
        );

        // 保存物品到列表中
        LostFoundListFragment.addItem(item);

        // 显示成功提示
        Toast.makeText(this, "信息发布成功", Toast.LENGTH_SHORT).show();
        
        // 设置结果并关闭页面
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
} 