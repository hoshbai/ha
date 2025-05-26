/*
package com.example.campus_life_assistant;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campus_life_assistant.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.campus_life_assistant.entry.Course;
public class ScheduleActivity extends AppCompatActivity {
    private List<Course> courses = new ArrayList<>();
    private int currentWeek = 9; // 默认当前周

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        // 初始化模拟数据
        initCourses();

        setupSpinner();
        setupScheduleTable(); // 动态生成表格
    }

    private void initCourses() {
        courses.add(new Course("数学", "张老师", 9, "周一", 1));
        courses.add(new Course("英语", "李老师", 9, "周二", 3));
        courses.add(new Course("物理", "王老师", 10, "周五", 5));
        courses.add(new Course("化学", "赵老师", 9, "周三", 7));
        courses.add(new Course("计算机", "刘老师", 9, "周四", 2));
    }

    private void setupSpinner() {
        Spinner spWeek = findViewById(R.id.spWeek);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.weeks, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWeek.setAdapter(adapter);

        int maxWeek = adapter.getCount(); // 获取实际周数
        currentWeek = Math.max(1, Math.min(currentWeek, maxWeek)); // 限制范围
        spWeek.setSelection(currentWeek - 1); // 假设数组从第1周开始

        spWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentWeek = position + 1;
                setupScheduleTable(); // 切换周时重新生成表格
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupScheduleTable() {
        TableLayout tlSchedule = findViewById(R.id.tlSchedule);

        // 时间段（Y 轴）
        String[] periods = {
                "第1节", "第2节",
                "第3节", "第4节",
                "第5节", "第6节",
                "第7节", "第8节"
        };

        // 星期（X 轴）
        String[] daysOfWeek = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

        // 清空之前的表格内容
        tlSchedule.removeAllViews();

        // 添加表头
        TableRow headerRow = new TableRow(this);
        headerRow.setBackgroundColor(Color.DKGRAY);
        TextView emptyHeader = new TextView(this);
        emptyHeader.setText("时间段");
        emptyHeader.setPadding(8, 8, 8, 8);
        emptyHeader.setTextColor(Color.WHITE);
        emptyHeader.setGravity(Gravity.CENTER);
        headerRow.addView(emptyHeader);

        for (String day : daysOfWeek) {
            TextView dayHeader = new TextView(this);
            dayHeader.setText(day);
            dayHeader.setPadding(8, 8, 8, 8);
            dayHeader.setTextColor(Color.WHITE);
            dayHeader.setGravity(Gravity.CENTER);
            headerRow.addView(dayHeader);
        }
        tlSchedule.addView(headerRow);

        // 动态生成表格内容
        for (int i = 0; i < periods.length; i++) {
            TableRow row = new TableRow(this);
            row.setBackgroundColor(i % 2 == 0 ? Color.LTGRAY : Color.TRANSPARENT); // 每两行交替背景色

            // 时间段列
            TextView periodCell = new TextView(this);
            periodCell.setText(periods[i]);
            periodCell.setPadding(8, 8, 8, 8);
            periodCell.setGravity(Gravity.CENTER);
            periodCell.setBackgroundResource(R.drawable.cell_border); // 边框样式
            row.addView(periodCell);

            // 遍历每一天
            for (String day : daysOfWeek) {
                Course course = findCourseByPeriodAndDay(currentWeek, day, i + 1); // 查找课程
                TextView cell = new TextView(this);
                cell.setPadding(8, 8, 8, 8);
                cell.setGravity(Gravity.CENTER);
                cell.setBackgroundResource(R.drawable.cell_border); // 边框样式

                if (course != null) {
                    cell.setText(course.getName() + "\n" + course.getTeacher());
                    cell.setBackgroundColor(getCourseColor(course.getName()));
                    cell.setTextColor(Color.WHITE);
                } else {
                    cell.setText("无课"); // 空课时
                    cell.setBackgroundColor(Color.TRANSPARENT);
                    cell.setTextColor(Color.GRAY);
                }

                row.addView(cell);
            }

            tlSchedule.addView(row);
        }
    }

    // 根据周数、星期几和课时查找课程
    private Course findCourseByPeriodAndDay(int week, String dayOfWeek, int period) {
        for (Course course : courses) {
            if (course.getWeek() == week && course.getDayOfWeek().equals(dayOfWeek) && course.getPeriod() == period) {
                return course;
            }
        }
        return null;
    }

    // 获取课程颜色（为每个课程生成唯一颜色）
    private Map<String, Integer> courseColors = new HashMap<>();

    private int getCourseColor(String courseName) {
        if (!courseColors.containsKey(courseName)) {
            // 生成随机颜色并缓存
            int randomColor = Color.rgb(
                    (int) (Math.random() * 256),
                    (int) (Math.random() * 256),
                    (int) (Math.random() * 256)
            );
            courseColors.put(courseName, randomColor);
        }
        return courseColors.get(courseName);
    }
}*/

/*package com.example.campus_life_assistant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campus_life_assistant.entry.Course;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {
    private List<Course> courses = new ArrayList<>();
    private int currentWeek = 9; // 默认当前周

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        // 初始化模拟数据
        initCourses();

        setupSpinner();
        setupScheduleTable(); // 动态生成表格

        // 添加课程按钮
        Button btnAddCourse = findViewById(R.id.btnAddCourse);
        btnAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCourseDialog();
            }
        });
    }

    private void showAddCourseDialog() {
        // 创建输入框和选择器
        final EditText inputName = new EditText(this);
        final EditText inputTeacher = new EditText(this);
        final EditText inputWeek = new EditText(this);
        final Spinner spDayOfWeek = new Spinner(this);
        final Spinner spPeriod = new Spinner(this);
        final EditText inputClassroom = new EditText(this);

        // 设置星期几的适配器
        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(
                this, R.array.days_of_week, android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDayOfWeek.setAdapter(dayAdapter);

        // 设置节次的适配器
        ArrayAdapter<CharSequence> periodAdapter = ArrayAdapter.createFromResource(
                this, R.array.periods, android.R.layout.simple_spinner_item);
        periodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPeriod.setAdapter(periodAdapter);

        // 将视图放入对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("添加新课程");

        // 创建一个包含所有输入控件的线性布局
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(inputName);
        layout.addView(inputTeacher);
        layout.addView(inputWeek);
        layout.addView(spDayOfWeek);
        layout.addView(spPeriod);
        layout.addView(inputClassroom);

        inputName.setHint("课程名称");
        inputTeacher.setHint("授课教师");
        inputWeek.setHint("所属周");
        inputClassroom.setHint("教室");

        builder.setView(layout);

        // 添加确认和取消按钮
        builder.setPositiveButton("添加", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = inputName.getText().toString();
                String teacher = inputTeacher.getText().toString();
                int week = Integer.parseInt(inputWeek.getText().toString());
                String dayOfWeek = spDayOfWeek.getSelectedItem().toString();
                int period = Integer.parseInt(spPeriod.getSelectedItem().toString());
                String classroom = inputClassroom.getText().toString();

                if (!name.isEmpty() && !teacher.isEmpty() && !inputWeek.getText().toString().isEmpty() && !classroom.isEmpty()) {
                    Course newCourse = new Course(name, teacher, week, dayOfWeek, period, classroom);
                    courses.add(newCourse);
                    setupScheduleTable(); // 更新表格
                } else {
                    Toast.makeText(ScheduleActivity.this, "请填写所有字段", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("取消", null);

        builder.show();
    }

    private void initCourses() {
        courses.add(new Course("网络与信息安全", "张老师", 9, "周一", 1, "文端2512"));
        courses.add(new Course("软件设计与体系结构", "李老师", 9, "周三", 3, "文端2506"));
        courses.add(new Course("软件项目管理", "王老师", 10, "周五", 5, "2410"));
        courses.add(new Course("移动开发技术", "赵老师", 9, "周一", 3, "文端2609"));
        courses.add(new Course("移动开发实验", "刘老师", 9, "周二", 3, "6206"));
    }

    private void setupSpinner() {
        Spinner spWeek = findViewById(R.id.spWeek);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.weeks, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWeek.setAdapter(adapter);

        int maxWeek = adapter.getCount(); // 获取实际周数
        currentWeek = Math.max(1, Math.min(currentWeek, maxWeek)); // 限制范围
        spWeek.setSelection(currentWeek - 1); // 假设数组从第1周开始

        spWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentWeek = position + 1;
                setupScheduleTable(); // 切换周时重新生成表格
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupScheduleTable() {
        TableLayout tlSchedule = findViewById(R.id.tlSchedule);

        // 时间段（Y 轴）
        String[] periods = {
                "第1节", "第2节",
                "第3节", "第4节",
                "第5节", "第6节",
                "第7节", "第8节"
        };

        // 星期（X 轴）
        String[] daysOfWeek = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

        // 清空之前的表格内容
        tlSchedule.removeAllViews();

        // 添加表头
        TableRow headerRow = new TableRow(this);
        headerRow.setBackgroundColor(Color.DKGRAY);
        TextView emptyHeader = new TextView(this);
        emptyHeader.setText("时间段");
        emptyHeader.setPadding(8, 8, 8, 8);
        emptyHeader.setTextColor(Color.WHITE);
        emptyHeader.setGravity(Gravity.CENTER);
        headerRow.addView(emptyHeader);

        for (String day : daysOfWeek) {
            TextView dayHeader = new TextView(this);
            dayHeader.setText(day);
            dayHeader.setPadding(8, 8, 8, 8);
            dayHeader.setTextColor(Color.WHITE);
            dayHeader.setGravity(Gravity.CENTER);
            headerRow.addView(dayHeader);
        }
        tlSchedule.addView(headerRow);

        // 动态生成表格内容
        for (int i = 0; i < periods.length; i++) {
            TableRow row = new TableRow(this);
            row.setBackgroundColor(i % 2 == 0 ? Color.LTGRAY : Color.TRANSPARENT); // 每两行交替背景色

            // 时间段列
            TextView periodCell = new TextView(this);
            periodCell.setText(periods[i]);
            periodCell.setPadding(8, 8, 8, 8);
            periodCell.setGravity(Gravity.CENTER);
            periodCell.setBackgroundResource(R.drawable.cell_border); // 边框样式
            row.addView(periodCell);

            // 遍历每一天
            for (String day : daysOfWeek) {
                Course course = findCourseByPeriodAndDay(currentWeek, day, i + 1); // 查找课程
                TextView cell = new TextView(this);
                cell.setPadding(8, 8, 8, 8);
                cell.setGravity(Gravity.CENTER);
                cell.setBackgroundResource(R.drawable.cell_border); // 边框样式

                if (course != null) {
                    cell.setText(course.getName() + "\n" + course.getTeacher() + "\n@" + course.getClassroom());
                    cell.setBackgroundColor(getCourseColor(course.getName()));
                    cell.setTextColor(Color.WHITE);
                } else {
                    cell.setText("无课"); // 空课时
                    cell.setBackgroundColor(Color.TRANSPARENT);
                    cell.setTextColor(Color.GRAY);
                }

                row.addView(cell);
            }

            tlSchedule.addView(row);
        }
    }

    private Course findCourseByPeriodAndDay(int week, String dayOfWeek, int period) {
        for (Course course : courses) {
            if (course.getWeek() == week && course.getDayOfWeek().equals(dayOfWeek) && course.getPeriod() == period) {
                return course;
            }
        }
        return null;
    }

    private int getCourseColor(String courseName) {
        // 根据课程名称返回不同的颜色
        switch (courseName) {
            case "网络与信息安全":
                return Color.BLUE;
            case "软件设计与体系结构":
                return Color.GREEN;
            case "软件项目管理":
                return Color.RED;
            case "移动开发技术":
                return Color.YELLOW;
            case "移动开发实验":
                return Color.MAGENTA;
            default:
                return Color.CYAN;
        }
    }
}*/

/*package com.example.campus_life_assistant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campus_life_assistant.entry.Course;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {
    private List<Course> courses = new ArrayList<>();
    private int currentWeek = 9; // 默认当前周

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        // 初始化模拟数据
        initCourses();

        setupSpinner();
        setupScheduleTable(); // 动态生成表格

        // 添加课程按钮
        Button btnAddCourse = findViewById(R.id.btnAddCourse);
        btnAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCourseDialog();
            }
        });
    }

    private void showAddCourseDialog() {
        final EditText inputName = new EditText(this);
        final EditText inputTeacher = new EditText(this);
        final EditText inputWeek = new EditText(this);
        final Spinner spDayOfWeek = new Spinner(this);
        final Spinner spPeriod = new Spinner(this);
        final EditText inputClassroom = new EditText(this);

        // 设置星期几的适配器
        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(
                this, R.array.days_of_week, android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDayOfWeek.setAdapter(dayAdapter);

        // 设置节次的适配器
        ArrayAdapter<CharSequence> periodAdapter = ArrayAdapter.createFromResource(
                this, R.array.periods, android.R.layout.simple_spinner_item);
        periodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPeriod.setAdapter(periodAdapter);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                100
        );
        params.setMargins(0, 0, 0, 16);

        inputName.setLayoutParams(params);
        inputTeacher.setLayoutParams(params);
        inputWeek.setLayoutParams(params);
        inputClassroom.setLayoutParams(params);

        inputName.setHint("课程名称");
        inputTeacher.setHint("授课教师");
        inputWeek.setHint("所属周");
        inputClassroom.setHint("教室");

        layout.addView(inputName);
        layout.addView(inputTeacher);
        layout.addView(inputWeek);
        layout.addView(spDayOfWeek);
        layout.addView(spPeriod);
        layout.addView(inputClassroom);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("添加新课程")
                .setView(layout)
                .setPositiveButton("添加", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = inputName.getText().toString();
                        String teacher = inputTeacher.getText().toString();
                        String weekStr = inputWeek.getText().toString();
                        String classroom = inputClassroom.getText().toString();

                        if (name.isEmpty() || teacher.isEmpty() || weekStr.isEmpty() || classroom.isEmpty()) {
                            Toast.makeText(ScheduleActivity.this, "请填写所有字段", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        try {
                            int week = Integer.parseInt(weekStr);
                            String dayOfWeek = spDayOfWeek.getSelectedItem().toString();
                            int period = Integer.parseInt(spPeriod.getSelectedItem().toString());

                            Course newCourse = new Course(name, teacher, week, dayOfWeek, period, classroom);
                            courses.add(newCourse);
                            setupScheduleTable(); // 更新表格
                        } catch (NumberFormatException e) {
                            Toast.makeText(ScheduleActivity.this, "请输入有效的数字格式", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void showEditCourseDialog(Course course) {
        final EditText inputName = new EditText(this);
        final EditText inputTeacher = new EditText(this);
        final EditText inputWeek = new EditText(this);
        final Spinner spDayOfWeek = new Spinner(this);
        final Spinner spPeriod = new Spinner(this);
        final EditText inputClassroom = new EditText(this);

        inputName.setText(course.getName());
        inputTeacher.setText(course.getTeacher());
        inputWeek.setText(String.valueOf(course.getWeek()));
        inputClassroom.setText(course.getClassroom());

        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(
                this, R.array.days_of_week, android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDayOfWeek.setAdapter(dayAdapter);
        spDayOfWeek.setSelection(getIndex(spDayOfWeek, course.getDayOfWeek()));

        ArrayAdapter<CharSequence> periodAdapter = ArrayAdapter.createFromResource(
                this, R.array.periods, android.R.layout.simple_spinner_item);
        periodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPeriod.setAdapter(periodAdapter);
        spPeriod.setSelection(getIndex(spPeriod, String.valueOf(course.getPeriod())));

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                100
        );
        params.setMargins(0, 0, 0, 16);

        inputName.setLayoutParams(params);
        inputTeacher.setLayoutParams(params);
        inputWeek.setLayoutParams(params);
        inputClassroom.setLayoutParams(params);

        layout.addView(inputName);
        layout.addView(inputTeacher);
        layout.addView(inputWeek);
        layout.addView(spDayOfWeek);
        layout.addView(spPeriod);
        layout.addView(inputClassroom);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("编辑课程")
                .setView(layout)
                .setPositiveButton("保存", (dialog, which) -> {
                    try {
                        course.setName(inputName.getText().toString());
                        course.setTeacher(inputTeacher.getText().toString());
                        course.setWeek(Integer.parseInt(inputWeek.getText().toString()));
                        course.setDayOfWeek(spDayOfWeek.getSelectedItem().toString());
                        course.setPeriod(Integer.parseInt(spPeriod.getSelectedItem().toString()));
                        course.setClassroom(inputClassroom.getText().toString());

                        setupScheduleTable(); // 更新表格
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "请输入有效的数字格式", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNeutralButton("删除", (dialog, which) -> {
                    courses.remove(course);
                    setupScheduleTable(); // 更新表格
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private int getIndex(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                return i;
            }
        }
        return 0;
    }

    private void initCourses() {
        courses.add(new Course("网络与信息安全", "张老师", 9, "周一", 1, "文端2512"));
        courses.add(new Course("软件设计与体系结构", "李老师", 9, "周三", 3, "文端2506"));
        courses.add(new Course("软件项目管理", "王老师", 10, "周五", 5, "2410"));
        courses.add(new Course("移动开发技术", "赵老师", 9, "周一", 3, "文端2609"));
        courses.add(new Course("移动开发实验", "刘老师", 9, "周二", 3, "6206"));
    }

    private void setupSpinner() {
        Spinner spWeek = findViewById(R.id.spWeek);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.weeks, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWeek.setAdapter(adapter);

        int maxWeek = adapter.getCount(); // 获取实际周数
        currentWeek = Math.max(1, Math.min(currentWeek, maxWeek)); // 限制范围
        spWeek.setSelection(currentWeek - 1); // 假设数组从第1周开始

        spWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentWeek = position + 1;
                setupScheduleTable(); // 切换周时重新生成表格
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupScheduleTable() {
        TableLayout tlSchedule = findViewById(R.id.tlSchedule);

        // 时间段（Y 轴）
        String[] periods = {
                "第1节", "第2节",
                "第3节", "第4节",
                "第5节", "第6节",
                "第7节", "第8节"
        };

        // 星期（X 轴）
        String[] daysOfWeek = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

        // 清空之前的表格内容
        tlSchedule.removeAllViews();

        // 添加表头
        TableRow headerRow = new TableRow(this);
        headerRow.setBackgroundColor(Color.DKGRAY);
        TextView emptyHeader = new TextView(this);
        emptyHeader.setText("时间段");
        emptyHeader.setPadding(8, 8, 8, 8);
        emptyHeader.setTextColor(Color.WHITE);
        emptyHeader.setGravity(Gravity.CENTER);
        headerRow.addView(emptyHeader);

        for (String day : daysOfWeek) {
            TextView dayHeader = new TextView(this);
            dayHeader.setText(day);
            dayHeader.setPadding(8, 8, 8, 8);
            dayHeader.setTextColor(Color.WHITE);
            dayHeader.setGravity(Gravity.CENTER);
            headerRow.addView(dayHeader);
        }
        tlSchedule.addView(headerRow);

        // 动态生成表格内容
        for (int i = 0; i < periods.length; i++) {
            TableRow row = new TableRow(this);
            row.setBackgroundColor(i % 2 == 0 ? Color.LTGRAY : Color.TRANSPARENT); // 每两行交替背景色

            // 时间段列
            TextView periodCell = new TextView(this);
            periodCell.setText(periods[i]);
            periodCell.setPadding(8, 8, 8, 8);
            periodCell.setGravity(Gravity.CENTER);
            periodCell.setBackgroundResource(R.drawable.cell_border); // 边框样式
            row.addView(periodCell);

            // 遍历每一天
            for (String day : daysOfWeek) {
                Course course = findCourseByPeriodAndDay(currentWeek, day, i + 1); // 查找课程
                TextView cell = new TextView(this);
                cell.setPadding(8, 8, 8, 8);
                cell.setGravity(Gravity.CENTER);
                cell.setBackgroundResource(R.drawable.cell_border); // 边框样式

                if (course != null) {
                    cell.setText(course.getName() + "\n" + course.getTeacher() + "\n@" + course.getClassroom());
                    cell.setBackgroundColor(getCourseColor(course.getName()));
                    cell.setTextColor(Color.WHITE);
                    cell.setOnClickListener(v -> showEditCourseDialog(course));
                } else {
                    cell.setText("无课"); // 空课时
                    cell.setBackgroundColor(Color.TRANSPARENT);
                    cell.setTextColor(Color.GRAY);
                }

                row.addView(cell);
            }

            tlSchedule.addView(row);
        }
    }

    private Course findCourseByPeriodAndDay(int week, String dayOfWeek, int period) {
        for (Course course : courses) {
            if (course.getWeek() == week && course.getDayOfWeek().equals(dayOfWeek) && course.getPeriod() == period) {
                return course;
            }
        }
        return null;
    }

    private int getCourseColor(String courseName) {
        // 根据课程名称返回不同的颜色
        switch (courseName) {
            case "网络与信息安全":
                return Color.BLUE;
            case "软件设计与体系结构":
                return Color.GREEN;
            case "软件项目管理":
                return Color.RED;
            case "移动开发技术":
                return Color.YELLOW;
            case "移动开发实验":
                return Color.MAGENTA;
            default:
                return Color.CYAN;
        }
    }
}*/

package com.example.campus_life_assistant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campus_life_assistant.entry.Course;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {
    private List<Course> courses = new ArrayList<>();
    private int currentWeek = 9; // 默认当前周

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        // 初始化模拟数据
        initCourses();

        setupSpinner();
        setupScheduleTable(); // 动态生成表格

        // 添加课程按钮
        Button btnAddCourse = findViewById(R.id.btnAddCourse);
        btnAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCourseDialog();
            }
        });
    }

    private void showAddCourseDialog() {
        final EditText inputName = new EditText(this);
        final EditText inputTeacher = new EditText(this);
        final EditText inputWeek = new EditText(this);
        final Spinner spDayOfWeek = new Spinner(this);
        final Spinner spPeriod = new Spinner(this);
        final EditText inputClassroom = new EditText(this);

        // 设置星期几的适配器
        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(
                this, R.array.days_of_week, android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDayOfWeek.setAdapter(dayAdapter);

        // 设置节次的适配器
        ArrayAdapter<CharSequence> periodAdapter = ArrayAdapter.createFromResource(
                this, R.array.periods, android.R.layout.simple_spinner_item);
        periodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPeriod.setAdapter(periodAdapter);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                100
        );
        params.setMargins(0, 0, 0, 16);

        inputName.setLayoutParams(params);
        inputTeacher.setLayoutParams(params);
        inputWeek.setLayoutParams(params);
        inputClassroom.setLayoutParams(params);

        inputName.setHint("课程名称");
        inputTeacher.setHint("授课教师");
        inputWeek.setHint("所属周");
        inputClassroom.setHint("教室");

        layout.addView(inputName);
        layout.addView(inputTeacher);
        layout.addView(inputWeek);
        layout.addView(spDayOfWeek);
        layout.addView(spPeriod);
        layout.addView(inputClassroom);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("添加新课程")
                .setView(layout)
                .setPositiveButton("添加", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String name = inputName.getText().toString();
                        String teacher = inputTeacher.getText().toString();
                        String weekStr = inputWeek.getText().toString();
                        String classroom = inputClassroom.getText().toString();

                        if (name.isEmpty() || teacher.isEmpty() || weekStr.isEmpty() || classroom.isEmpty()) {
                            Toast.makeText(ScheduleActivity.this, "请填写所有字段", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        try {
                            int week = Integer.parseInt(weekStr);
                            String dayOfWeek = spDayOfWeek.getSelectedItem().toString();
                            int period = Integer.parseInt(spPeriod.getSelectedItem().toString());

                            Course newCourse = new Course(name, teacher, week, dayOfWeek, period, classroom);
                            courses.add(newCourse);
                            setupScheduleTable(); // 更新表格
                        } catch (NumberFormatException e) {
                            Toast.makeText(ScheduleActivity.this, "请输入有效的数字格式", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private void showEditCourseDialog(Course course) {
        final EditText inputName = new EditText(this);
        final EditText inputTeacher = new EditText(this);
        final EditText inputWeek = new EditText(this);
        final Spinner spDayOfWeek = new Spinner(this);
        final Spinner spPeriod = new Spinner(this);
        final EditText inputClassroom = new EditText(this);

        inputName.setText(course.getName());
        inputTeacher.setText(course.getTeacher());
        inputWeek.setText(String.valueOf(course.getWeek()));
        inputClassroom.setText(course.getClassroom());

        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(
                this, R.array.days_of_week, android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spDayOfWeek.setAdapter(dayAdapter);
        spDayOfWeek.setSelection(getIndex(spDayOfWeek, course.getDayOfWeek()));

        ArrayAdapter<CharSequence> periodAdapter = ArrayAdapter.createFromResource(
                this, R.array.periods, android.R.layout.simple_spinner_item);
        periodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPeriod.setAdapter(periodAdapter);
        spPeriod.setSelection(getIndex(spPeriod, String.valueOf(course.getPeriod())));

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                100
        );
        params.setMargins(0, 0, 0, 16);

        inputName.setLayoutParams(params);
        inputTeacher.setLayoutParams(params);
        inputWeek.setLayoutParams(params);
        inputClassroom.setLayoutParams(params);

        layout.addView(inputName);
        layout.addView(inputTeacher);
        layout.addView(inputWeek);
        layout.addView(spDayOfWeek);
        layout.addView(spPeriod);
        layout.addView(inputClassroom);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("编辑课程")
                .setView(layout)
                .setPositiveButton("保存", (dialog, which) -> {
                    try {
                        course.setName(inputName.getText().toString());
                        course.setTeacher(inputTeacher.getText().toString());
                        course.setWeek(Integer.parseInt(inputWeek.getText().toString()));
                        course.setDayOfWeek(spDayOfWeek.getSelectedItem().toString());
                        course.setPeriod(Integer.parseInt(spPeriod.getSelectedItem().toString()));
                        course.setClassroom(inputClassroom.getText().toString());

                        setupScheduleTable(); // 更新表格
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "请输入有效的数字格式", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNeutralButton("删除", (dialog, which) -> {
                    courses.remove(course);
                    setupScheduleTable(); // 更新表格
                })
                .setNegativeButton("取消", null)
                .show();
    }

    private int getIndex(Spinner spinner, String value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
                return i;
            }
        }
        return 0;
    }

    private void initCourses() {
        courses.add(new Course("网络与信息安全", "张老师", 9, "周一", 1, "文端2512"));
        courses.add(new Course("软件设计与体系结构", "李老师", 9, "周三", 3, "文端2506"));
        courses.add(new Course("软件项目管理", "王老师", 10, "周五", 5, "2410"));
        courses.add(new Course("移动开发技术", "赵老师", 9, "周一", 3, "文端2609"));
        courses.add(new Course("移动开发实验", "刘老师", 9, "周二", 3, "6206"));
    }

    private void setupSpinner() {
        Spinner spWeek = findViewById(R.id.spWeek);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.weeks, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWeek.setAdapter(adapter);

        int maxWeek = adapter.getCount(); // 获取实际周数
        currentWeek = Math.max(1, Math.min(currentWeek, maxWeek)); // 限制范围
        spWeek.setSelection(currentWeek - 1); // 假设数组从第1周开始

        spWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentWeek = position + 1;
                setupScheduleTable(); // 切换周时重新生成表格
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupScheduleTable() {
        TableLayout tlSchedule = findViewById(R.id.tlSchedule);

        // 时间段（Y 轴）
        String[] periods = {
                "第1节", "第2节",
                "第3节", "第4节",
                "第5节", "第6节",
                "第7节", "第8节"
        };

        // 星期（X 轴）
        String[] daysOfWeek = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

        // 清空之前的表格内容
        tlSchedule.removeAllViews();

        // 添加表头
        TableRow headerRow = new TableRow(this);
        headerRow.setBackgroundColor(Color.DKGRAY);
        TextView emptyHeader = new TextView(this);
        emptyHeader.setText("时间段");
        emptyHeader.setPadding(8, 8, 8, 8);
        emptyHeader.setTextColor(Color.WHITE);
        emptyHeader.setGravity(Gravity.CENTER);
        headerRow.addView(emptyHeader);

        for (String day : daysOfWeek) {
            TextView dayHeader = new TextView(this);
            dayHeader.setText(day);
            dayHeader.setPadding(8, 8, 8, 8);
            dayHeader.setTextColor(Color.WHITE);
            dayHeader.setGravity(Gravity.CENTER);
            headerRow.addView(dayHeader);
        }
        tlSchedule.addView(headerRow);

        // 动态生成表格内容
        for (int i = 0; i < periods.length; i++) {
            TableRow row = new TableRow(this);
            row.setBackgroundColor(i % 2 == 0 ? Color.LTGRAY : Color.TRANSPARENT); // 每两行交替背景色

            // 时间段列
            TextView periodCell = new TextView(this);
            periodCell.setText(periods[i]);
            periodCell.setPadding(8, 8, 8, 8);
            periodCell.setGravity(Gravity.CENTER);
            periodCell.setBackgroundResource(R.drawable.cell_border); // 边框样式
            row.addView(periodCell);

            // 遍历每一天
            for (String day : daysOfWeek) {
                Course course = findCourseByPeriodAndDay(currentWeek, day, i + 1); // 查找课程
                TextView cell = new TextView(this);
                cell.setPadding(8, 8, 8, 8);
                cell.setGravity(Gravity.CENTER);
                cell.setBackgroundResource(R.drawable.cell_border); // 边框样式

                if (course != null) {
                    cell.setText(course.getName() + "\n" + course.getTeacher() + "\n@" + course.getClassroom());
                    cell.setBackgroundColor(getCourseColor(course.getName()));
                    cell.setTextColor(Color.WHITE);
                    cell.setOnClickListener(v -> showEditCourseDialog(course));
                } else {
                    cell.setText("无课"); // 空课时
                    cell.setBackgroundColor(Color.GRAY);
                    cell.setTextColor(Color.BLACK);
                }

                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);
                cell.setLayoutParams(layoutParams);
                row.addView(cell);
            }

            tlSchedule.addView(row);
        }
    }

    private Course findCourseByPeriodAndDay(int week, String dayOfWeek, int period) {
        for (Course course : courses) {
            if (course.getWeek() == week && course.getDayOfWeek().equals(dayOfWeek) && course.getPeriod() == period) {
                return course;
            }
        }
        return null;
    }

    private int getCourseColor(String courseName) {
        // 根据课程名称返回不同的颜色
        switch (courseName) {
            case "网络与信息安全":
                return Color.BLUE;
            case "软件设计与体系结构":
                return Color.GREEN;
            case "软件项目管理":
                return Color.RED;
            case "移动开发技术":
                return Color.YELLOW;
            case "移动开发实验":
                return Color.MAGENTA;
            default:
                return Color.CYAN;
        }
    }
}

/*package com.example.campus_life_assistant;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.campus_life_assistant.entry.Course;

import java.util.ArrayList;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity {
    private List<Course> courses = new ArrayList<>();
    private int currentWeek = 9; // 默认当前周

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        // 初始化模拟数据
        initCourses();

        setupSpinner();
        setupScheduleTable(); // 动态生成表格
    }

    private void initCourses() {
        courses.add(new Course("数学", "张老师", 9, "周一", 1));
        courses.add(new Course("英语", "李老师", 9, "周二", 3));
        courses.add(new Course("物理", "王老师", 10, "周五", 5));
        courses.add(new Course("化学", "赵老师", 9, "周三", 7));
        courses.add(new Course("计算机", "刘老师", 9, "周四", 2));
    }

    private void setupSpinner() {
        Spinner spWeek = findViewById(R.id.spWeek);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.weeks, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spWeek.setAdapter(adapter);

        int maxWeek = adapter.getCount(); // 获取实际周数
        currentWeek = Math.max(1, Math.min(currentWeek, maxWeek)); // 限制范围
        spWeek.setSelection(currentWeek - 1); // 假设数组从第1周开始

        spWeek.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentWeek = position + 1;
                setupScheduleTable(); // 切换周时重新生成表格
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setupScheduleTable() {
        TableLayout tlSchedule = findViewById(R.id.tlSchedule);

        // 时间段（Y 轴）
        String[] periods = {
                "第1节", "第2节",
                "第3节", "第4节",
                "第5节", "第6节",
                "第7节", "第8节"
        };

        // 星期（X 轴）
        String[] daysOfWeek = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};

        // 清空之前的表格内容
        tlSchedule.removeAllViews();

        // 添加表头
        TableRow headerRow = new TableRow(this);
        headerRow.setBackgroundColor(Color.DKGRAY);
        TextView emptyHeader = new TextView(this);
        emptyHeader.setText("时间段");
        emptyHeader.setPadding(8, 8, 8, 8);
        emptyHeader.setTextColor(Color.WHITE);
        emptyHeader.setGravity(Gravity.CENTER);
        headerRow.addView(emptyHeader);

        for (String day : daysOfWeek) {
            TextView dayHeader = new TextView(this);
            dayHeader.setText(day);
            dayHeader.setPadding(8, 8, 8, 8);
            dayHeader.setTextColor(Color.WHITE);
            dayHeader.setGravity(Gravity.CENTER);
            headerRow.addView(dayHeader);
        }
        tlSchedule.addView(headerRow);

        // 动态生成表格内容
        for (int i = 0; i < periods.length; i++) {
            TableRow row = new TableRow(this);
            row.setBackgroundColor(i % 2 == 0 ? Color.LTGRAY : Color.TRANSPARENT); // 每两行交替背景色

            // 时间段列
            TextView periodCell = new TextView(this);
            periodCell.setText(periods[i]);
            periodCell.setPadding(8, 8, 8, 8);
            periodCell.setGravity(Gravity.CENTER);
            periodCell.setBackgroundResource(R.drawable.cell_border); // 边框样式
            row.addView(periodCell);

            // 遍历每一天
            for (String day : daysOfWeek) {
                Course course = findCourseByPeriodAndDay(currentWeek, day, i + 1); // 查找课程
                TextView cell = new TextView(this);
                cell.setPadding(8, 8, 8, 8);
                cell.setGravity(Gravity.CENTER);
                cell.setBackgroundResource(R.drawable.cell_border); // 边框样式

                if (course != null) {
                    cell.setText(course.getName() + "\n" + course.getTeacher());
                    cell.setBackgroundColor(getCourseColor(course.getName()));
                    cell.setTextColor(Color.WHITE);
                } else {
                    cell.setText("无课"); // 空课时
                    cell.setBackgroundColor(Color.TRANSPARENT);
                    cell.setTextColor(Color.GRAY);
                }

                row.addView(cell);
            }

            tlSchedule.addView(row);
        }
    }

    private Course findCourseByPeriodAndDay(int week, String dayOfWeek, int period) {
        for (Course course : courses) {
            if (course.getWeek() == week && course.getDayOfWeek().equals(dayOfWeek) && course.getPeriod() == period) {
                return course;
            }
        }
        return null;
    }

    private int getCourseColor(String courseName) {
        // 根据课程名称返回不同的颜色
        switch (courseName) {
            case "数学":
                return Color.BLUE;
            case "英语":
                return Color.GREEN;
            case "物理":
                return Color.RED;
            case "化学":
                return Color.YELLOW;
            case "计算机":
                return Color.MAGENTA;
            default:
                return Color.CYAN;
        }
    }
}*/



