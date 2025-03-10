package jp.gihyo.projava.tasklist.model;

import jp.gihyo.projava.tasklist.controller.HomeController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TaskListDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    TaskListDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(HomeController.TaskItem taskItem) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(taskItem);

        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate).withTableName("tasklist");
        insert.execute(param);
    }

    public List<HomeController.TaskItem> findAll() {
        String query = "SELECT * FROM tasklist";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);

        return result.stream().map((Map<String, Object> row) -> new HomeController.TaskItem(
            row.get("id").toString(),
            row.get("task").toString(),
            row.get("deadline").toString(),
            (Boolean)row.get("done")
        )).toList();
    }

    public int delete(String id) {
        return jdbcTemplate.update("DELETE FROM tasklist WHERE id = ?", id);
    }

    public int update(HomeController.TaskItem taskItem) {
        return jdbcTemplate.update("UPDATE tasklist SET task = ?, deadline = ?, done = ? WHERE id = ?",
                taskItem.task(),
                taskItem.deadline(),
                taskItem.done(),
                taskItem.id());
    }
}
