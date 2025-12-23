package com.app.task;

import com.app.lib.layout.VerticalFlowLayout;
import com.app.storage.DataStorage;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class TaskManager extends JPanel {
    private List<Task> tasks = new ArrayList<>();
    private int focusedTask_idx = 0;
    private VerticalFlowLayout layoutM = new VerticalFlowLayout(5, 5);

    public TaskManager () {
        this(new Dimension(100,100));
    }

    public TaskManager(Dimension preferredSize) {
        initUI(preferredSize);
        TaskData[] tasks_data = DataStorage.loadTasks_data();
        for (TaskData td: tasks_data) {
            addTask(td);
        }
        updatePanel();
    }

    public void setTempFocused(Task taskToFocusTemporary) {
        forEach((task, idx)->{
            task.setTempFocused(false);
        });
        taskToFocusTemporary.setTempFocused(true);
    }

    public interface Consumer<T> {
        void exec(T obj, int index);
    }

    public void forEach (Consumer<Task> cons) {
        for (int i=0; i<tasks.size(); i++) {
            Optional<Task> task_opt=getTask(i);
            if (task_opt.isPresent()) {
                cons.exec(task_opt.get(), i);
            }
        }
    }

    public void swapFocusedTaskDesc() {
        forEach(new Consumer<Task>() {
            @Override
            public void exec(Task task, int index) {
                if (index!=focusedTask_idx&&!task.isOnDefaultState()) {
                    task.setDefaultState(true);
                }
            }
        });
        Optional<Task> taskToChange_opt = getTask(focusedTask_idx);
        if (taskToChange_opt.isPresent()) {
            Task taskToChange = taskToChange_opt.get();
            taskToChange.setDefaultState(!taskToChange.isOnDefaultState());
        }
    }

    public void saveTasks () {
        TaskData[] data = getTasks_data();
        DataStorage.writeTasks_data(data);
    }

    public TaskData[] getTasks_data() {
        TaskData[] out = new TaskData[tasks.size()];
        for (int i=0; i<out.length; i++) {
            out[i] = tasks.get(i).getData();
        }
        return out;
    }

    private void updatePanel () {
        this.removeAll();
        forEach((t, i) -> add(t));

        this.revalidate();
        this.repaint();
        focusTask(focusedTask_idx);
    }

    public void clickFocusedTask() {
        clickTask(focusedTask_idx);
    }

    public void focusTask (Task taskToFocus) {
        if (taskToFocus!=null)
            focusTask(tasks.indexOf(taskToFocus));
    }

    public void clickTask(int taskToComplete_idx) {
        Optional<Task> taskToComplete_opt = getTask(taskToComplete_idx);
        taskToComplete_opt.ifPresent(this::clickTask);
    }

    public Optional<Task> getTask (int idx) {
        try {
            return Optional.of(tasks.get(idx));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void clickTask(Task taskToComplete) {
        if (taskToComplete.isDescHiddenOrHide()) {
            tasks.remove(taskToComplete);
            focusedTask_idx--;
            updatePanel();
        }
    }

    public void addTask (TaskData tData) {
        Task tui = new Task(tData, this, getBackground());
        tasks.add(tui);
        focusedTask_idx = tasks.indexOf(tui);
        updatePanel();
    }

    private void initUI (Dimension ps) {
        setLayout(layoutM);
        setPreferredSize(ps);
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

    }

    @Override
    public void setBackground (Color c) {
        super.setBackground(c);
        if (tasks!=null)
            forEach((t,i)->t.setBackground(getBackground()));
    }

    public void moveFocus (int dir) {
        switch (dir) {
            case +1:
                focusedTask_idx++;
                break;
            case -1:
                focusedTask_idx--;
                break;
            default:
                throw new IllegalArgumentException();
        }
        focusTask(focusedTask_idx);
    }

    public void moveFocusOneCol(int direction) {
        if (tasks.isEmpty())
            return;

        int panelHeight = getPreferredSize().height;
        int taskHeight = tasks.getFirst().getPreferredSize().height+layoutM.vGap;
        final int TASKS_PER_COL = panelHeight/taskHeight;

        switch (direction) {
            case 1:
                focusedTask_idx += TASKS_PER_COL;
                break;
            case -1:
                focusedTask_idx -= TASKS_PER_COL;
                break;
            default:
                throw new IllegalArgumentException(direction + " is invalid");
        }

        focusTask(focusedTask_idx);
    }

    public void focusTask (int task_idx) {
        forEach((t,i)->t.setFocused(false));

        focusedTask_idx = task_idx;

        if (focusedTask_idx < 0)
            focusedTask_idx = tasks.size()-1;
        if (focusedTask_idx >= tasks.size())
            focusedTask_idx = 0;

        Optional<Task> task_opt = getTask(focusedTask_idx);
        task_opt.ifPresent(task -> task.setFocused(true));
    }

}
