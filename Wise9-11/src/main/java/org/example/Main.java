package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Main {
    static Scanner sc = new Scanner(System.in);
    static ArrayList<Wise> wiseArray = new ArrayList<>();
    private static final Path DB_PATH = Paths.get("db", "wiseSaying");


    public static void main(String[] args) throws IOException {

        System.out.println("== 명언 앱 ==");

        while (true) {
            System.out.print("명령) ");
            String command = sc.nextLine();
            int id = -1;

            if (command.startsWith("삭제") || command.startsWith("수정")) {
                String[] parts = command.split("[?=]");
                command = parts[0];
                id = Integer.parseInt(parts[2]);
            }

            switch (command) {
                case "종료":
                    return;
                case "등록":
                    add();
                    break;
                case "목록":
                    getWiseList();
                    break;
                case "삭제":
                    delete(id);
                    break;
                case "수정":
                    update(id);
                    break;
            }
        }
    }

    public static void add() throws IOException {
        System.out.print("명언 : ");
        String content = sc.nextLine();

        System.out.print("작가 : ");
        String author = sc.nextLine();

        // 새로운 명언 생성
        Wise wise = new Wise(content, author);
        wiseArray.add(wise);

        // json 파일로 명언 저장
        Files.createDirectories(DB_PATH);
        ObjectMapper mapper = new ObjectMapper();
        Path filePath = DB_PATH.resolve(wise.getId() + ".json");
        mapper.writeValue(filePath.toFile(), wise);

        System.out.println(Wise.count + "번 명언이 등록되었습니다.");

        // 가장 마지막 id 저장
        Path filePathId = DB_PATH.resolve("lastId.txt");
        FileWriter fw = new FileWriter(filePathId.toFile(), false);
        fw.write(wise.getId() + "\n");
        fw.close();

    }

    public static void getWiseList() {
        System.out.println("번호 / 작가 / 명언");
        System.out.println("----------------------");
        for (Wise wise : wiseArray) {
            wise.getWise();
        }
    }

    public static Wise findById(int id) {
        for (Wise wise: wiseArray) {
            if (wise.getId() == id) return wise;
        }
        return null;
    }

    public static void delete(int id) {
        Wise wise = findById(id);
        if (wise != null) {
            wiseArray.remove(wise);
            System.out.println(id + "번 명언이 삭제되었습니다.");
        }
        else {
            System.out.println(id + "번 명언은 존재하지 않습니다.");
        }
    }

    public static void update(int id) {
        Wise wise = findById(id);
        if (wise != null) {
            System.out.println("명언(기존) : " + wise.getContent());
            System.out.print("명언 : ");
            String newContent = sc.nextLine();

            System.out.println("작가(기존) : " + wise.getAuthor());
            System.out.print("작가 : ");
            String newAuthor = sc.nextLine();

            wise.updateWise(newContent, newAuthor);
        }
        else {
            System.out.println(id + "번 명언은 존재하지 않습니다.");
        }

    }
}

