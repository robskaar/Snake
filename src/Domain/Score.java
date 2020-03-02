package Domain;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;

import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Score {

    private String pathCSV = "src/Resources/CSV/HighScores.csv";

    private String name;
    private String difficulty;
    private int score;

    private static TableView tableView = new TableView();
    private static TableColumn<String, Score> columnName;
    private static TableColumn<Integer, Score> columnScore;
    private static TableColumn<String, Score> columnDifficulty;

    private ObservableList<Score> highScores = FXCollections.observableArrayList();

    public Score() {
    }

    public Score(String name, Integer score, String difficulty) {
        this.name = name;
        this.score = score;
        this.difficulty = difficulty;
    }

    public void writeCSV() {

        FileWriter fileWriter = null;

        try {
            fileWriter = new FileWriter(pathCSV, true);
            fileWriter.append(this.name + "," + this.getScore() + "," + this.difficulty);
            fileWriter.append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void readCSV() {

        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new FileReader(pathCSV));
            String newLine = "";

            while ((newLine = bufferedReader.readLine()) != null) {

                String[] fields = newLine.split(",");

                if (fields.length > 0) {
                    Score s = new Score();
                    s.setName(fields[0]);
                    s.setScore(Integer.parseInt(fields[1]));
                    s.setDifficulty(fields[2]);
                    highScores.add(s);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addToObservableList(){
        highScores.add(this);
    }

    public void showHighScores() {

        readCSV();

        tableView.setItems(highScores);
        tableView.getSortOrder().addAll(columnScore, columnDifficulty);
        tableView.getSelectionModel().select(highScores.indexOf(this));
        tableView.scrollTo(highScores.indexOf(this));

    }

    public static void initHighScores(Pane pane){

        tableView.getStyleClass().addAll("HighScores");
        tableView.setLayoutX(50);
        tableView.setLayoutY(150);

        columnName = new TableColumn<>("Name");
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        columnName.setPrefWidth(165);

        columnScore = new TableColumn<>("Score");
        columnScore.setCellValueFactory(new PropertyValueFactory<>("score"));
        columnScore.setSortType(TableColumn.SortType.DESCENDING);
        columnScore.setPrefWidth(165);

        columnDifficulty = new TableColumn<>("Level");
        columnDifficulty.setCellValueFactory(new PropertyValueFactory<>("difficulty"));
        columnDifficulty.setPrefWidth(165);

        tableView.getColumns().addAll(columnName, columnScore, columnDifficulty);

        pane.getChildren().addAll(tableView);
    }

    public int getScore() {
        return this.score;
    }

    public String getName() {
        return name;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

}
