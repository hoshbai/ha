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
}