/*
package com.example.campus_life_assistant.entry;

public class Course {
    private String name; // 课程名称
    private String teacher; // 授课教师
    private int week; // 所属周
    private String dayOfWeek; // 星期几（周一到周日）
    private int period; // 节次（1-8）

    public Course(String name, String teacher, int week, String dayOfWeek, int period) {
        this.name = name;
        this.teacher = teacher;
        this.week = week;
        this.dayOfWeek = dayOfWeek;
        this.period = period;
    }

    public String getName() {
        return name;
    }

    public String getTeacher() {
        return teacher;
    }

    public int getWeek() {
        return week;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public int getPeriod() {
        return period;
    }
}*/
/*package com.example.campus_life_assistant.entry;

public class Course {
    private String name; // 课程名称
    private String teacher; // 授课教师
    private int week; // 所属周
    private String dayOfWeek; // 星期几（周一到周日）
    private int period; // 节次（1-8）

    // 构造函数，包含所有参数
    public Course(String name, String teacher, int week, String dayOfWeek, int period) {
        this.name = name;
        this.teacher = teacher;
        this.week = week;
        this.dayOfWeek = dayOfWeek;
        this.period = period;
    }

    // 构造函数，不包含教师参数
    public Course(String name, int week, String dayOfWeek, int period) {
        this(name, null, week, dayOfWeek, period);
    }

    // Getter 和 Setter 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    // toString 方法，用于调试和日志记录
    @Override
    public String toString() {
        return "Course{" +
                "name='" + name + '\'' +
                ", teacher='" + teacher + '\'' +
                ", week=" + week +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", period=" + period +
                '}';
    }
}*/

package com.example.campus_life_assistant.entry;

public class Course {
    private String name;        // 课程名称
    private String teacher;     // 授课教师
    private int week;           // 所属周
    private String dayOfWeek;   // 星期几（周一到周日）
    private int period;         // 节次（1-8）
    private String classroom;   // 教室

    // 完整构造函数：包含所有参数
    public Course(String name, String teacher, int week, String dayOfWeek, int period, String classroom) {
        this.name = name;
        this.teacher = teacher;
        this.week = week;
        this.dayOfWeek = dayOfWeek;
        this.period = period;
        this.classroom = classroom;
    }

    // 简化构造函数：不包含教师和教室
    public Course(String name, int week, String dayOfWeek, int period) {
        this(name, null, week, dayOfWeek, period, null);
    }

    // Getter 和 Setter 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    // toString 方法，用于调试和日志记录
    @Override
    public String toString() {
        return "Course{" +
                "name='" + name + '\'' +
                ", teacher='" + teacher + '\'' +
                ", week=" + week +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", period=" + period +
                ", classroom='" + classroom + '\'' +
                '}';
    }
}



