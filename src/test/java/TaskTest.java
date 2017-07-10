import org.sql2o.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.time.LocalDateTime;

public class TaskTest {

  @Before
  public void setUp() {
    DB.sql2o = new Sql2o("jdbc:postgresql://localhost:5432/to_do_test", null, null);
  }

  @After
  public void tearDown() {
    try(Connection con = DB.sql2o.open()) {
      String deleteTasksQuery = "DELETE FROM tasks *;";
      String deleteCategoriesQuery = "DELETE FROM categories *;";
      con.createQuery(deleteTasksQuery).executeUpdate();
      con.createQuery(deleteCategoriesQuery).executeUpdate();
    }
  }

    @Test
  public void getId_tasksInstantiateWithAnID_1() {
    Task myTask = new Task("Mow the lawn", 1);
    myTask.save();
    assertTrue(myTask.getId() > 0);
  }

  @Test
  public void find_returnsTaskWithSameId_secondTask() {
    Task firstTask = new Task("Mow the lawn", 1);
    firstTask.save();
    Task secondTask = new Task("Buy groceries", 2);
    secondTask.save();
    assertEquals(Task.find(secondTask.getId()), secondTask);
  }

  @Test
    public void equals_returnsTrueIfDescriptionsAretheSame() {
      Task firstTask = new Task("Mow the lawn", 1);
      firstTask.save();
      String firstName = firstTask.getDescription();
      Task secondTask = new Task("Mow the lawn", 2);
      secondTask.save();
      String secondName = secondTask.getDescription();
      assertTrue(firstName.equals(secondName));
    }

    @Test
    public void save_returnsTrueIfDescriptionsAretheSame() {
      Task myTask = new Task("Mow the lawn", 1);
      myTask.save();
      assertTrue(Task.all().get(0).equals(myTask));
    }

    @Test
    public void all_returnsAllInstancesOfTask_true() {
      Task firstTask = new Task("Mow the lawn", 1);
      firstTask.save();
      Task secondTask = new Task("Buy groceries", 2);
      secondTask.save();
      assertEquals(true, Task.all().get(0).equals(firstTask));
      assertEquals(true, Task.all().get(1).equals(secondTask));
    }

    @Test
    public void save_assignsIdToObject() {
      Task myTask = new Task("Mow the lawn", 1);
      myTask.save();
      Task savedTask = Task.all().get(0);
      assertEquals(myTask.getId(), savedTask.getId());
    }

    @Test
    public void save_savesCategoryIdIntoDB_true() {
      Category myCategory = new Category("Household chores");
      myCategory.save();
      Task myTask = new Task("Mow the lawn", myCategory.getId());
      myTask.save();
      Task savedTask = Task.find(myTask.getId());
      assertEquals(savedTask.getCategoryId(), myCategory.getId());
    }

    @Test
    public void update_updatesTaskDescription_true() {
      Task myTask = new Task("Mow the lawn", 1);
      myTask.save();
      myTask.update("Take a nap");
      assertEquals("Take a nap", Task.find(myTask.getId()).getDescription());
    }

}
