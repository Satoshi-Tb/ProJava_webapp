package jp.gihyo.projava.tasklist.rest.controller;

import jp.gihyo.projava.tasklist.controller.HomeController;
import jp.gihyo.projava.tasklist.model.TaskListDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class HomeRestController {

    private final TaskListDao dao;

    @Autowired
    HomeRestController(TaskListDao dao) {
        this.dao = dao;
    }

    @GetMapping("/add")
    ResponseEntity<String> addItem(@RequestParam("task") String task, @RequestParam("deadline") String deadline) {
        String id = UUID.randomUUID().toString().substring(0, 8);
        var item = new HomeController.TaskItem(id, task, deadline, false);
        dao.add(item);

        return new ResponseEntity<String>("", HttpStatus.OK);
    }

    @GetMapping("/list")
    ResponseEntity<List<HomeController.TaskItem>> listItems() {
        return new ResponseEntity<List<HomeController.TaskItem>>(dao.findAll(), HttpStatus.OK) ;
    }


    @RequestMapping("/hello")
    ResponseEntity<String>  hello() {
        String response =  """
                Hello.
                It works!
                    現在時刻は%sです。
                """.formatted(LocalDateTime.now());
        return new ResponseEntity<String>(response, HttpStatus.OK);
    }

    @GetMapping("/delete")
    ResponseEntity<Integer> deleteItem(@RequestParam("id") String id) {
        int count = dao.delete(id);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }

    @GetMapping("/update")
    ResponseEntity<Integer>  updateItem(@RequestParam("id") String id,
                      @RequestParam("task") String task,
                      @RequestParam("deadline") String deadline,
                      @RequestParam("done") boolean done) {
        var taskItem = new HomeController.TaskItem(id, task, deadline, done);
        int count = dao.update(taskItem);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }
}
