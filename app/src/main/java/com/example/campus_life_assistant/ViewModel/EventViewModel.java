package com.example.campus_life_assistant.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.campus_life_assistant.entry.CampusEvent;

import java.util.ArrayList;
import java.util.List;

public class EventViewModel extends ViewModel {
    
    private MutableLiveData<List<CampusEvent>> allEvents = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<List<CampusEvent>> filteredEvents = new MutableLiveData<>(new ArrayList<>());
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    
    // 获取所有活动
    public LiveData<List<CampusEvent>> getAllEvents() {
        return allEvents;
    }
    
    // 获取按类别过滤的活动
    public LiveData<List<CampusEvent>> getFilteredEvents() {
        return filteredEvents;
    }
    
    // 获取加载状态
    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }
    
    // 获取错误信息
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }
    
    // 加载活动数据
    public void loadEvents() {
        isLoading.setValue(true);
        
        // 这里应当从数据源加载数据，如API或数据库
        // 现在使用模拟数据代替
        // fetchEventsFromDataSource();
        
        // 模拟加载完成
        isLoading.setValue(false);
    }
    
    // 按类别过滤活动
    public void filterEventsByCategory(String category) {
        if (allEvents.getValue() == null) {
            return;
        }
        
        List<CampusEvent> events = allEvents.getValue();
        List<CampusEvent> filtered = new ArrayList<>();
        
        if ("全部".equals(category)) {
            filtered = events;
        } else {
            for (CampusEvent event : events) {
                if (category.equals(event.getCategory())) {
                    filtered.add(event);
                }
            }
        }
        
        filteredEvents.setValue(filtered);
    }
    
    // 报名参加活动
    public void registerForEvent(int eventId) {
        isLoading.setValue(true);
        
        // 这里应当调用数据源执行报名操作
        // registerEventToDataSource(eventId);
        
        // 模拟报名成功，更新本地数据
        if (allEvents.getValue() != null) {
            for (CampusEvent event : allEvents.getValue()) {
                if (event.getId() == eventId) {
                    event.setRegistered(true);
                    break;
                }
            }
            // 触发LiveData更新
            allEvents.setValue(allEvents.getValue());
            
            // 如果有过滤数据，也需要更新
            if (filteredEvents.getValue() != null) {
                for (CampusEvent event : filteredEvents.getValue()) {
                    if (event.getId() == eventId) {
                        event.setRegistered(true);
                        break;
                    }
                }
                filteredEvents.setValue(filteredEvents.getValue());
            }
        }
        
        isLoading.setValue(false);
    }
    
    // 取消报名
    public void unregisterFromEvent(int eventId) {
        isLoading.setValue(true);
        
        // 这里应当调用数据源执行取消报名操作
        // unregisterEventFromDataSource(eventId);
        
        // 模拟取消报名成功，更新本地数据
        if (allEvents.getValue() != null) {
            for (CampusEvent event : allEvents.getValue()) {
                if (event.getId() == eventId) {
                    event.setRegistered(false);
                    break;
                }
            }
            // 触发LiveData更新
            allEvents.setValue(allEvents.getValue());
            
            // 如果有过滤数据，也需要更新
            if (filteredEvents.getValue() != null) {
                for (CampusEvent event : filteredEvents.getValue()) {
                    if (event.getId() == eventId) {
                        event.setRegistered(false);
                        break;
                    }
                }
                filteredEvents.setValue(filteredEvents.getValue());
            }
        }
        
        isLoading.setValue(false);
    }
} 